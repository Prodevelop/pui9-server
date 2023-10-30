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
package es.prodevelop.xdocreport.document.json;

import java.io.IOException;
import java.io.OutputStream;

import es.prodevelop.xdocreport.core.document.ImageFormat;
import es.prodevelop.xdocreport.document.images.IImageProvider;
import es.prodevelop.xdocreport.template.formatter.NullImageBehaviour;

public class JSONImage extends JSONObject implements IImageProvider {

	private static final long serialVersionUID = 1L;

	@Override
	public void write(OutputStream outputStream) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ImageFormat getImageFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Float getWidth(Float defaultWidth) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidth(Float width) {
		// TODO Auto-generated method stub

	}

	@Override
	public Float getHeight(Float defaultHeight) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHeight(Float height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSize(Float width, Float height) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUseImageSize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUseImageSize(boolean useImageSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResize(boolean resize) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isResize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NullImageBehaviour getBehaviour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBehaviour(NullImageBehaviour behaviour) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
