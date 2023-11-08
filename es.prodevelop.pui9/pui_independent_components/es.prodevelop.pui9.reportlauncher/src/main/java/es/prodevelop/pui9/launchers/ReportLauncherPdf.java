package es.prodevelop.pui9.launchers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.utils.pdf.PdfReportData;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ReportLauncherPdf extends AbstractReportLauncher {

	private PdfReportData data;

	public ReportLauncherPdf(PdfReportData data) {
		super();
		this.data = data;
	}

	public PdfReportData getData() {
		return data;
	}

	@Override
	public void launch(int reportElementSize) throws Exception {
		JasperPrint jasperPrint = getCompiledReport(reportElementSize);
		innerLaunch(jasperPrint);
	}

	@Override
	public void launch(Connection connection) throws Exception {
		JasperPrint jasperPrint = getCompiledReport(connection);
		innerLaunch(jasperPrint);
	}

	@Override
	public void launch(Collection<?> collection) throws Exception {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);
		JasperPrint jasperPrint = getCompiledReport(dataSource);
		innerLaunch(jasperPrint);
	}

	private void innerLaunch(JasperPrint jasperPrint) throws Exception {
		JRPdfExporter exp = new JRPdfExporter();
		exp.setExporterInput(new SimpleExporterInput(jasperPrint));
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		exp.exportReport();
	}

	private JasperPrint getCompiledReport(JRDataSource dataSource) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

		InputStream is = PuiClassLoaderUtils.getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, dataSource);
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, dataSource);
		}
	}

	private JasperPrint getCompiledReport(int reportElementSize) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

		InputStream is = PuiClassLoaderUtils.getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, new JREmptyDataSource(reportElementSize));
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, new JREmptyDataSource(reportElementSize));
		}
	}

	private JasperPrint getCompiledReport(Connection connection) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.computeIfAbsent(JRParameter.REPORT_LOCALE, k -> new Locale("es", "ES"));

		InputStream is = PuiClassLoaderUtils.getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, connection);
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, connection);
		}
	}

	/**
	 * File with '.jasper' extension is a compiled report. Else, file has '.jrxml'
	 * extension
	 */
	private boolean isCompiledFile(String path) {
		return path.endsWith(".jasper");
	}

	@Override
	public File getResultAsFile(String folder, boolean includeTimeMillis) throws Exception {
		String fileName = getFileName(folder, data.getReportName(), includeTimeMillis);

		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);

		ByteArrayOutputStream mybaos = mergePDFs(Collections.singletonList(baos));
		mybaos.writeTo(fos);
		mybaos.close();
		fos.close();

		return file;
	}

	/**
	 * Merges a list of pdfs output stream into a single output stream
	 */
	public static ByteArrayOutputStream mergePDFs(List<ByteArrayOutputStream> pdfs) throws Exception {
		Document document = new Document();
		List<PdfReader> readers = new ArrayList<>();
		Iterator<ByteArrayInputStream> pdfIterator = pdfs.stream()
				.map(baosPdf -> new ByteArrayInputStream(baosPdf.toByteArray())).iterator();
		while (pdfIterator.hasNext()) {
			InputStream pdf = pdfIterator.next();
			PdfReader pdfReader = new PdfReader(pdf);
			readers.add(pdfReader);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);

		document.open();

		PdfContentByte pageContentByte = writer.getDirectContent();

		PdfImportedPage pdfImportedPage;
		int currentPdfReaderPage = 1;
		Iterator<PdfReader> iteratorPDFReader = readers.iterator();

		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = iteratorPDFReader.next();
			// Create page and add content.
			while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
				document.newPage();
				pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
				pageContentByte.addTemplate(pdfImportedPage, 0, 0);
				currentPdfReaderPage++;
			}
			currentPdfReaderPage = 1;
		}

		// Close document.
		outputStream.flush();
		document.close();

		return outputStream;
	}

}
