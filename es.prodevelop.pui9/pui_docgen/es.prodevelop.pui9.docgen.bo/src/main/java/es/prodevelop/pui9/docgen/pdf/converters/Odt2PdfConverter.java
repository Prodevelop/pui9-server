package es.prodevelop.pui9.docgen.pdf.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import es.prodevelop.xdocreport.converter.ConverterTypeTo;
import es.prodevelop.xdocreport.converter.ConverterTypeVia;
import es.prodevelop.xdocreport.converter.Options;
import es.prodevelop.xdocreport.core.XDocReportException;
import es.prodevelop.xdocreport.core.document.DocumentKind;
import es.prodevelop.xdocreport.document.IXDocReport;
import es.prodevelop.xdocreport.document.registry.XDocReportRegistry;
import es.prodevelop.xdocreport.template.TemplateEngineKind;

@Component
public class Odt2PdfConverter extends AbstractPdfConverter {

	@Override
	public String getFileExtension() {
		return "odt";
	}

	@Override
	public InputStream convert(byte[] content) throws Exception {
		IXDocReport report;
		try {
			report = XDocReportRegistry.getRegistry().loadReport(new ByteArrayInputStream(content),
					TemplateEngineKind.Freemarker);
			Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			report.convert(report.createContext(), options, os);

			InputStream is = new ByteArrayInputStream(os.toByteArray());
			os.close();

			return is;
		} catch (XDocReportException e) {
			throw new IOException(e.getMessage());
		}
	}

}
