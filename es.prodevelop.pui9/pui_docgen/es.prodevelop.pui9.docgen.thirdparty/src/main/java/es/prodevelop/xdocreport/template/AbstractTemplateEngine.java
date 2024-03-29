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
package es.prodevelop.xdocreport.template;

import static es.prodevelop.xdocreport.core.utils.StringUtils.isNotEmpty;
import static es.prodevelop.xdocreport.core.utils.XMLUtils.prettyPrint;
import static es.prodevelop.xdocreport.template.utils.TemplateUtils.getCachedTemplateName;
import static java.lang.String.format;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import es.prodevelop.xdocreport.core.XDocReportException;
import es.prodevelop.xdocreport.core.io.IEntryReaderProvider;
import es.prodevelop.xdocreport.core.io.IEntryWriterProvider;
import es.prodevelop.xdocreport.core.io.IOUtils;
import es.prodevelop.xdocreport.core.io.MultiWriter;
import es.prodevelop.xdocreport.core.logging.LogUtils;
import es.prodevelop.xdocreport.template.cache.ITemplateCacheInfoProvider;
import es.prodevelop.xdocreport.template.config.ITemplateEngineConfiguration;

public abstract class AbstractTemplateEngine implements ITemplateEngine {

	private ITemplateCacheInfoProvider templateCacheInfoProvider;

	private ITemplateEngineConfiguration configuration;

	@Override
	public ITemplateCacheInfoProvider getTemplateCacheInfoProvider() {
		return templateCacheInfoProvider;
	}

	@Override
	public void setTemplateCacheInfoProvider(ITemplateCacheInfoProvider templateCacheInfoProvider) {
		this.templateCacheInfoProvider = templateCacheInfoProvider;
	}

	@Override
	public ITemplateEngineConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ITemplateEngineConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void process(String reportId, String entryName, IEntryReaderProvider readerProvider,
			IEntryWriterProvider writerProvider, IContext context) throws XDocReportException, IOException {

		// Get writer of the entry to merge Java model with template engine
		Writer writer = writerProvider.getEntryWriter(entryName);
		process(reportId, entryName, readerProvider, writer, context);
	}

	private static final Logger LOGGER = LogUtils.getLogger(AbstractTemplateEngine.class.getName());

	@Override
	public void process(String reportId, String entryName, IEntryReaderProvider readerProvider, Writer writer,
			IContext context) throws XDocReportException, IOException {
		boolean useTemplateCache = isUseTemplateCache(reportId);
		// 1) Start process template engine
		long startTime = -1;
		if (LOGGER.isEnabled(Level.TRACE)) {

			startTime = System.currentTimeMillis();
			LOGGER.trace(format("Start template engine id=%s for the entry=%s with template cache=%s", getId(),
					entryName, useTemplateCache));

		}

		Reader reader = null;
		try {
			writer = getWriter(writer);
			String templateName = getCachedTemplateName(reportId, entryName);
			if (useTemplateCache) {
				// cache template is used, process it
				processWithCache(templateName, context, writer);
			} else {
				// No cache template is used, get the reader from the entry
				reader = readerProvider.getEntryReader(entryName);
				processNoCache(templateName, context, reader, writer);
			}
			if (LOGGER.isEnabled(Level.TRACE)) {
				// Debug start preprocess
				startTime = System.currentTimeMillis();

				LOGGER.trace(format("Result template engine id=" + getId() + "  for the entry=" + entryName + ": "));
				LOGGER.trace(prettyPrint(((MultiWriter) writer).getWriter(1).toString()));

				LOGGER.trace("End template engine id=" + getId() + " for the entry=" + entryName + " done with "
						+ (System.currentTimeMillis() - startTime) + "(ms).");
			}

		} catch (Throwable e) {
			if (LOGGER.isEnabled(Level.TRACE)) {
				LOGGER.trace(("End template engine id=" + getId() + " for the entry=" + entryName + " done with "
						+ (System.currentTimeMillis() - startTime) + "(ms)."));
			}
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			if (e instanceof XDocReportException) {
				throw (XDocReportException) e;
			}
			throw new XDocReportException(e);
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
			if (writer != null) {
				IOUtils.closeQuietly(writer);
			}
		}
	}

	@Override
	public void process(String entryName, IContext context, Reader reader, Writer writer)
			throws XDocReportException, IOException {
		try {
			processNoCache(entryName, context, reader, writer);
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
			if (writer != null) {
				IOUtils.closeQuietly(writer);
			}
		}

	}

	@Override
	public void extractFields(IEntryReaderProvider readerProvider, String entryName, FieldsExtractor extractor)
			throws XDocReportException {
		Reader reader = readerProvider.getEntryReader(entryName);
		extractFields(reader, entryName, extractor);
	}

	private Writer getWriter(Writer writer) {
		if (LOGGER.isEnabled(Level.TRACE)) {
			return new MultiWriter(writer, new StringWriter());
		}
		return writer;
	}

	protected boolean isUseTemplateCache(String reportId) {
		return isNotEmpty(reportId) && getTemplateCacheInfoProvider() != null
				&& getTemplateCacheInfoProvider().existsReport(reportId);

	}

	/**
	 * Merge the given template with the given context and writes the result in the
	 * given writer. Cache is used here to avoid parsing the template name each
	 * time.
	 *
	 * @param templateName the template name.
	 * @param context      the context.
	 * @param writer       the result of merge.
	 * @throws XDocReportException
	 * @throws IOException
	 */
	protected abstract void processWithCache(String templateName, IContext context, Writer writer)
			throws XDocReportException, IOException;

	/**
	 * Merge the given template with the given context and writes the result in the
	 * given writer.
	 *
	 * @param templateName the template name.
	 * @param context      the context.
	 * @param writer       the result of merge.
	 * @throws XDocReportException
	 * @throws IOException
	 */
	protected abstract void processNoCache(String templateName, IContext context, Reader reader, Writer writer)
			throws XDocReportException, IOException;
}
