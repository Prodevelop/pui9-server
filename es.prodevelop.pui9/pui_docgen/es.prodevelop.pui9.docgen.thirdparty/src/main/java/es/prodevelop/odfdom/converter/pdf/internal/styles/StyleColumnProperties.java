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
package es.prodevelop.odfdom.converter.pdf.internal.styles;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StyleColumnProperties {
	private Integer relWidth;

	private Float startIndent;

	private Float endIndent;

	public StyleColumnProperties() {
	}

	public Integer getRelWidth() {
		return relWidth;
	}

	public void setRelWidth(Integer relWidth) {
		this.relWidth = relWidth;
	}

	public Float getStartIndent() {
		return startIndent;
	}

	public void setStartIndent(Float startIndent) {
		this.startIndent = startIndent;
	}

	public Float getEndIndent() {
		return endIndent;
	}

	public void setEndIndent(Float endIndent) {
		this.endIndent = endIndent;
	}
}
