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
package es.prodevelop.xdocreport.converter.odt.odfdom.xhtml.discovery;

import es.prodevelop.xdocreport.converter.ConverterTypeTo;
import es.prodevelop.xdocreport.converter.ConverterTypeVia;
import es.prodevelop.xdocreport.converter.IConverter;
import es.prodevelop.xdocreport.converter.discovery.IConverterDiscovery;
import es.prodevelop.xdocreport.converter.odt.odfdom.xhtml.ODF2XHTMLConverter;
import es.prodevelop.xdocreport.core.document.DocumentKind;

public class ODF2XHTMLConverterDiscovery implements IConverterDiscovery {

	@Override
	public String getId() {
		return "ODT2XHTMLViaODFDOM";
	}

	@Override
	public String getDescription() {
		return "Convert ODT 2 XHTML via ODFDOM";
	}

	@Override
	public String getFrom() {
		return DocumentKind.ODT.name();
	}

	@Override
	public String getTo() {
		return ConverterTypeTo.XHTML.name();
	}

	@Override
	public String getVia() {
		return ConverterTypeVia.ODFDOM.name();
	}

	@Override
	public IConverter getConverter() {
		return ODF2XHTMLConverter.getInstance();
	}

}
