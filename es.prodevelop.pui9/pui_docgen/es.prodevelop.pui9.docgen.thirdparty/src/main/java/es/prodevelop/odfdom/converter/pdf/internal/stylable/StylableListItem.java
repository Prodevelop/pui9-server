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
package es.prodevelop.odfdom.converter.pdf.internal.stylable;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;

import es.prodevelop.odfdom.converter.core.ODFConverterException;
import es.prodevelop.odfdom.converter.pdf.internal.styles.Style;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableListItem extends ListItem implements IStylableContainer {
	private static final long serialVersionUID = 664309269352903329L;

	private IStylableContainer parent;

	private List<Element> elements = new ArrayList<>();

	public StylableListItem(IStylableFactory ownerDocument, IStylableContainer parent) {
		this.parent = parent;
	}

	public StylableListItem(Phrase phrase) {
		super(phrase);
	}

	public List<Element> getElements() {
		return elements;
	}

	@Override
	public void addElement(Element element) {
		elements.add(element);
	}

	@Override
	public void setListSymbol(Chunk chunk) {
		throw new ODFConverterException("internal error - do not call setListSymbol(Chunk chunk)");
	}

	public void setListSymbol(String content, Font font, float lineHeight, boolean lineHeightProportional) {
		// adjust chunk attributes like text rise
		// use StylableParagraph mechanism
		Chunk symbol = StylableParagraph.createAdjustedChunk(content, font, lineHeight, lineHeightProportional);
		super.setListSymbol(symbol);
	}

	@Override
	public void applyStyles(Style style) {
	}

	@Override
	public Style getLastStyleApplied() {
		return null;
	}

	@Override
	public IStylableContainer getParent() {
		return parent;
	}

	@Override
	public Element getElement() {
		return this;
	}
}
