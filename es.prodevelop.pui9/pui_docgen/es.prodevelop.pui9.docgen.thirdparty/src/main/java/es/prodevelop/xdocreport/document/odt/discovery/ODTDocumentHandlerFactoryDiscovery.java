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
package es.prodevelop.xdocreport.document.odt.discovery;

import es.prodevelop.xdocreport.core.document.DocumentKind;
import es.prodevelop.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import es.prodevelop.xdocreport.document.odt.textstyling.ODTDocumentHandler;
import es.prodevelop.xdocreport.document.preprocessor.sax.BufferedElement;
import es.prodevelop.xdocreport.document.textstyling.IDocumentHandler;
import es.prodevelop.xdocreport.template.IContext;

public class ODTDocumentHandlerFactoryDiscovery implements ITextStylingDocumentHandlerFactoryDiscovery {

	@Override
	public String getId() {
		return DocumentKind.ODT.name();
	}

	@Override
	public IDocumentHandler createDocumentHandler(BufferedElement parent, IContext context, String entryName) {
		return new ODTDocumentHandler(parent, context, entryName);
	}

	@Override
	public String getDescription() {
		return "ODT document handler factory.";
	}

}
