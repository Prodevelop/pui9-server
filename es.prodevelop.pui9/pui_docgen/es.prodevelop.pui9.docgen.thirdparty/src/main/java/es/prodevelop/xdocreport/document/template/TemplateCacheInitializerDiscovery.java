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
package es.prodevelop.xdocreport.document.template;

import es.prodevelop.xdocreport.document.discovery.ITemplateEngineInitializerDiscovery;
import es.prodevelop.xdocreport.document.registry.XDocReportRegistry;
import es.prodevelop.xdocreport.template.ITemplateEngine;
import es.prodevelop.xdocreport.template.cache.ITemplateCacheInfoProvider;

/**
 * Initializer template engine to set XDocReportRegistry.getRegistry() as
 * template cache {@link ITemplateCacheInfoProvider}.
 */
public class TemplateCacheInitializerDiscovery implements ITemplateEngineInitializerDiscovery {

	private static final String ID = TemplateCacheInitializerDiscovery.class.getSimpleName();

	private static final String DESCRIPTION = "Initializer template engine to set XDocReportRegistry.getRegistry() as template cache";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getDocumentKind() {
		// returns null to initialize the whole template engine.
		return null;
	}

	@Override
	public void initialize(ITemplateEngine templateEngine) {
		templateEngine.setTemplateCacheInfoProvider(XDocReportRegistry.getRegistry());
	}
}
