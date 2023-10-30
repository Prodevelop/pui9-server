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

import es.prodevelop.xdocreport.converter.MimeMapping;
import es.prodevelop.xdocreport.core.io.XDocArchive;
import es.prodevelop.xdocreport.document.IXDocReport;
import es.prodevelop.xdocreport.document.discovery.AbstractXDocReportFactoryDiscovery;
import es.prodevelop.xdocreport.document.discovery.IXDocReportFactoryDiscovery;
import es.prodevelop.xdocreport.document.odt.ODTConstants;
import es.prodevelop.xdocreport.document.odt.ODTReport;
import es.prodevelop.xdocreport.document.odt.ODTUtils;
import es.prodevelop.xdocreport.document.registry.XDocReportRegistry;

/**
 * ODT discovery used by the
 * {@link XDocReportRegistry#loadReport(java.io.InputStream)} to create an
 * instance of {@link ODTReport} if input stream to load is an ODT.
 */
public class ODTReportFactoryDiscovery extends AbstractXDocReportFactoryDiscovery
		implements IXDocReportFactoryDiscovery {

	@Override
	public boolean isAdaptFor(XDocArchive archive) {
		return ODTUtils.isODT(archive);
	}

	@Override
	public IXDocReport createReport() {
		return new ODTReport();
	}

	@Override
	public MimeMapping getMimeMapping() {
		return ODTConstants.MIME_MAPPING;
	}

	@Override
	public String getDescription() {
		return ODTConstants.DESCRIPTION_DISCOVERY;
	}

	@Override
	public String getId() {
		return ODTConstants.ID_DISCOVERY;
	}

	@Override
	public Class<?> getReportClass() {
		return ODTReport.class;
	}

}
