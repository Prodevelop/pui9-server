package es.prodevelop.pui9.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.csv.CsvWriter;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;

/**
 * This components is a utility class to export the data of an entity into a CSV
 * file
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DataExporterToCsv extends AbstractDataExporter {

	@Override
	public ExportType getExportType() {
		return ExportType.csv;
	}

	@Override
	protected InputStream doExport(ExportRequest req) throws PuiServiceExportException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CsvWriter writer = new CsvWriter(baos, ';', StandardCharsets.UTF_8);

		ExecutionData executionData = new ExecutionData();
		req.getExportColumns().forEach(ecd -> executionData.mapDateFormat.put(ecd.getName(),
				!ObjectUtils.isEmpty(ecd.getDateformat()) ? ecd.getDateformat() : req.getDateformat()));

		generateTableHeader(writer, req);
		generateDetail(req, data -> generateTableContent(writer, req, executionData, data));

		writer.close();

		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		try {
			baos.close();
		} catch (IOException e) {
			// do nothing
		}

		return is;
	}

	private void generateTableHeader(CsvWriter writer, ExportRequest req) {
		req.getExportColumns().forEach(ec -> {
			try {
				writer.write(ec.getTitle());
			} catch (IOException e) {
				// do nothing
			}
		});
		try {
			writer.endRecord();
		} catch (IOException e) {
			// do nothing
		}
	}

	private void generateTableContent(CsvWriter writer, ExportRequest req, ExecutionData executionData,
			List<List<Pair<String, Object>>> allData) {
		allData.forEach(rec -> {
			rec.forEach(pair -> {
				String value = null;
				if (DtoRegistry.getDateTimeFields(req.getDtoClass()).contains(pair.getKey())) {
					value = getInstantAsString(pair.getValue(), executionData.mapDateFormat.get(pair.getKey()));
				} else if (DtoRegistry.getFloatingFields(req.getDtoClass()).contains(pair.getKey())) {
					value = convertBigDecimalToString(getBigDecimal(pair.getValue()), req.getDecimalChar());
				} else {
					value = getString(pair.getValue());
				}
				try {
					writer.write(value);
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}
		});
	}

	private class ExecutionData {
		Map<String, String> mapDateFormat;

		public ExecutionData() {
			mapDateFormat = new LinkedHashMap<>();
		}
	}

}
