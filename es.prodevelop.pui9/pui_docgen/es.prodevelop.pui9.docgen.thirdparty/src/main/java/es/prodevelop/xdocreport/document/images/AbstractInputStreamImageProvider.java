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
package es.prodevelop.xdocreport.document.images;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import es.prodevelop.xdocreport.core.io.IOUtils;
import es.prodevelop.xdocreport.core.logging.LogUtils;

public abstract class AbstractInputStreamImageProvider extends AbstractImageProvider {

	private static final Logger LOGGER = LogUtils.getLogger(AbstractInputStreamImageProvider.class);

	public AbstractInputStreamImageProvider(boolean keepTemplateImageSize) {
		super(keepTemplateImageSize);
	}

	@Override
	public void write(OutputStream outputStream) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = getInputStream();
			IOUtils.copy(inputStream, outputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	protected abstract InputStream getInputStream() throws IOException;

	@Override
	protected boolean doIsValid() {
		try {
			return getInputStream() != null;
		} catch (IOException e) {
			if (LOGGER.isEnabled(Level.ERROR)) {
				LOGGER.error("Error while getting the input stream of the image", e);
			}
			return false;
		}
	}

	@Override
	protected IImageInfo loadImageInfo() throws IOException {
		SimpleImageInfo imageInfo = new SimpleImageInfo();
		imageInfo.setInput(getInputStream());
		if (!imageInfo.check()) {
			throw new IOException("Unable to read image info.");
		}
		return imageInfo;
	}
}
