package es.prodevelop.pui9.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;

/**
 * This components is a utility class to export the data of an entity into an
 * Excel file
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DataExporterToExcel extends AbstractDataExporter {

	@Override
	public ExportType getExportType() {
		return ExportType.excel;
	}

	@Override
	protected InputStream doExport(ExportRequest req) throws PuiServiceExportException {
		LocaleUtil.setUserTimeZone(TimeZone
				.getTimeZone(PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
						: ZoneId.systemDefault()));

		Workbook workbook = new XSSFWorkbook();
		workbook.createSheet(req.getExportTitle());

		ExecutionData executionData = new ExecutionData(workbook, req.getDecimalChar());
		req.getExportColumns().forEach(ecd -> executionData.mapDateFormat.put(ecd.getName(),
				!ObjectUtils.isEmpty(ecd.getDateformat()) ? ecd.getDateformat() : req.getDateformat()));

		generateGeneralHeader(workbook, req);
		generateTableHeader(workbook, req);
		generateDetail(req, data -> generateTableContent(workbook, req, executionData, data));
		adjustColumnsWidth(workbook, req);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			workbook.write(baos);
			workbook.close();
			is = new ByteArrayInputStream(baos.toByteArray());
			baos.close();
		} catch (IOException e) {
			// do nothing
		}

		LocaleUtil.resetUserTimeZone();

		return is;
	}

	/**
	 * Generates the header of the Excel, including information like:<br>
	 * <ul>
	 * <li>The name of the model (the exported entity)</li>
	 * <li>The export date</li>
	 * </ul>
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void generateGeneralHeader(Workbook workbook, ExportRequest req) {
		CellStyle valueCellStyle = workbook.createCellStyle();
		valueCellStyle.setAlignment(HorizontalAlignment.LEFT);
		valueCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font valueFont = workbook.createFont();
		valueFont.setBold(true);
		valueFont.setFontHeightInPoints((short) 14);
		valueCellStyle.setFont(valueFont);

		// model name
		Row row = workbook.getSheetAt(0).createRow(0);
		row.setHeightInPoints(15);

		Cell valueCell = row.createCell(0);
		valueCell.setCellStyle(valueCellStyle);
		valueCell.setCellValue(req.getExportTitle());

		// date
		row = workbook.getSheetAt(0).createRow(1);
		row.setHeightInPoints(15);

		valueCell = row.createCell(0);
		valueCell.setCellValue(getInstantAsString(Instant.now(), null));
	}

	/**
	 * Generates the header of the table, including a column for each exportable
	 * column
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void generateTableHeader(Workbook workbook, ExportRequest req) {
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerCellStyle.setFont(headerFont);

		int nextRow = 3;

		Row row = workbook.getSheetAt(0).createRow(nextRow);
		for (int i = 0; i < req.getExportColumns().size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue(req.getExportColumns().get(i).getTitle());
		}
	}

	/**
	 * Generates the content of the table with the data
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 * @param data     The list of data to be exported
	 */
	private void generateTableContent(Workbook workbook, ExportRequest req, ExecutionData executionData,
			List<List<Pair<String, Object>>> data) {
		data.forEach(rec -> {
			Row row = workbook.getSheetAt(0).createRow(executionData.rownum++);
			AtomicInteger cellnum = new AtomicInteger(0);
			rec.forEach(pair -> {
				Cell cell = row.createCell(cellnum.getAndIncrement());
				if (DtoRegistry.getDateTimeFields(req.getDtoClass()).contains(pair.getKey())) {
					CellStyle dateStyle;
					if (executionData.mapDateCellStyles.containsKey(executionData.mapDateFormat.get(pair.getKey()))) {
						dateStyle = executionData.mapDateCellStyles.get(executionData.mapDateFormat.get(pair.getKey()));
					} else {
						dateStyle = workbook.createCellStyle();
						dateStyle.setDataFormat(
								workbook.createDataFormat().getFormat(executionData.mapDateFormat.get(pair.getKey())));
						executionData.mapDateCellStyles.put(executionData.mapDateFormat.get(pair.getKey()), dateStyle);
					}
					cell.setCellStyle(dateStyle);
					Date value = getDate(pair.getValue());
					if (value != null) {
						cell.setCellValue(value);
					}
				} else if (DtoRegistry.getNumericFields(req.getDtoClass()).contains(pair.getKey())) {
					cell.setCellStyle(executionData.numberStyle);
					String value = getString(pair.getValue());
					if (value != null) {
						cell.setCellValue(Double.valueOf(value));
					}
				} else if (DtoRegistry.getFloatingFields(req.getDtoClass()).contains(pair.getKey())) {
					cell.setCellStyle(executionData.doubleStyle);
					BigDecimal value = getBigDecimal(pair.getValue());
					if (value != null) {
						cell.setCellValue(value.doubleValue());
					}
				} else {
					cell.setCellStyle(executionData.textStyle);
					String value = getString(pair.getValue());
					if (value != null) {
						cell.setCellValue(value);
					}
				}
			});
		});
	}

	/**
	 * Adjust the width of the columns to auto
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void adjustColumnsWidth(Workbook workbook, ExportRequest req) {
		for (int i = 0; i < req.getExportColumns().size(); i++) {
			workbook.getSheetAt(0).autoSizeColumn(i);
		}
	}

	private Date getDate(Object value) {
		if (value instanceof Instant) {
			return Date.from((Instant) value);
		} else {
			return null;
		}
	}

	private class ExecutionData {
		Map<String, String> mapDateFormat;
		Map<String, CellStyle> mapDateCellStyles;
		CellStyle numberStyle;
		CellStyle doubleStyle;
		CellStyle textStyle;
		Integer rownum;

		public ExecutionData(Workbook workbook, char decimalChar) {
			mapDateFormat = new LinkedHashMap<>();
			mapDateCellStyles = new LinkedHashMap<>();

			numberStyle = workbook.createCellStyle();
			numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

			doubleStyle = workbook.createCellStyle();
			doubleStyle.setDataFormat(workbook.createDataFormat().getFormat("0#" + decimalChar + "##"));

			textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

			rownum = 4;
		}
	}

}
