/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package es.prodevelop.xdocreport.converter.odt.odfdom.xhtml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.odftoolkit.odfdom.doc.OdfTextDocument;

import es.prodevelop.odfdom.converter.core.ODFConverterException;
import es.prodevelop.odfdom.converter.xhtml.XHTMLOptions;
import es.prodevelop.xdocreport.converter.IURIResolver;
import es.prodevelop.xdocreport.converter.MimeMapping;
import es.prodevelop.xdocreport.converter.MimeMappingConstants;
import es.prodevelop.xdocreport.converter.Options;
import es.prodevelop.xdocreport.converter.OptionsHelper;
import es.prodevelop.xdocreport.converter.XDocConverterException;
import es.prodevelop.xdocreport.converter.internal.AbstractConverterNoEntriesSupport;

public class ODF2XHTMLConverter extends AbstractConverterNoEntriesSupport implements MimeMappingConstants {

	private static final ODF2XHTMLConverter INSTANCE = new ODF2XHTMLConverter();

	public static ODF2XHTMLConverter getInstance() {
		return INSTANCE;
	}

	@Override
	public void convert(InputStream in, OutputStream out, Options options) throws XDocConverterException {
		try {
			OdfTextDocument odfDocument = OdfTextDocument.loadDocument(in);
			es.prodevelop.odfdom.converter.xhtml.XHTMLConverter.getInstance().convert(odfDocument, out,
					toXHTMLOptions(options));
		} catch (ODFConverterException e) {
			throw new XDocConverterException(e);
		} catch (IOException e) {
			throw new XDocConverterException(e);
		} catch (Exception e) {
			throw new XDocConverterException(e);
		}
	}

	public XHTMLOptions toXHTMLOptions(Options options) {
		if (options == null) {
			return null;
		}
		Object value = options.getSubOptions(XHTMLOptions.class);
		if (value instanceof XHTMLOptions) {
			return (XHTMLOptions) value;
		}
		XHTMLOptions xhtmlOptions = XHTMLOptions.create();
		final IURIResolver resolver = OptionsHelper.getURIResolver(options);
		if (resolver != null) {
			xhtmlOptions.URIResolver(new es.prodevelop.odfdom.converter.core.IURIResolver() {
				@Override
				public String resolve(String uri) {
					return resolver.resolve(uri);
				}
			});
		}
		return xhtmlOptions;
	}

	@Override
	public MimeMapping getMimeMapping() {
		return XHTML_MIME_MAPPING;
	}

	@Override
	public boolean isDefault() {
		return true;
	}
}