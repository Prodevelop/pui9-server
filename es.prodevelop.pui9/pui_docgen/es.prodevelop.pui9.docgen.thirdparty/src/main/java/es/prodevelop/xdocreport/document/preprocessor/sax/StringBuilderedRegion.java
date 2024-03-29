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
package es.prodevelop.xdocreport.document.preprocessor.sax;

import java.io.IOException;
import java.io.Writer;

/**
 * String buildered region.
 */
public class StringBuilderedRegion extends BufferedRegionAdpater {

	private final StringBuilder buffer = new StringBuilder();

	public StringBuilderedRegion(BufferedElement ownerElement, IBufferedRegion parent) {
		super(ownerElement, parent);
	}

	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public void save(Writer writer) throws IOException {
		writer.write(buffer.toString());
	}

	@Override
	public void append(String content) {
		buffer.append(content);
	}

	@Override
	public void append(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}

	@Override
	public void append(char c) {
		buffer.append(c);
	}

	@Override
	public String toString() {
		if (buffer == null) {
			return null;
		}
		return buffer.toString();
	}

	public void clear() {
		buffer.setLength(0);
	}

}
