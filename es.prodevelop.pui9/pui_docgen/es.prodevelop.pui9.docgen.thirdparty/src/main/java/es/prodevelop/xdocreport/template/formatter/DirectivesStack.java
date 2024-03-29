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
package es.prodevelop.xdocreport.template.formatter;

import java.util.Stack;

import es.prodevelop.xdocreport.template.formatter.Directive.DirectiveType;

public class DirectivesStack extends Stack<Directive> {

	private static final long serialVersionUID = -11427919871179717L;

	public Directive peekDirective(DirectiveType type) {
		Directive directive = peekOrNull();
		if (directive == null) {
			return null;
		}
		if (directive.getType().equals(type)) {
			return directive;
		}
		Directive d = null;
		Object[] elementData = super.toArray();
		for (int i = elementData.length - 1; i >= 0; i--) {
			d = (Directive) elementData[i];
			if (d.getType().equals(type)) {
				return d;
			}
		}
		return null;
	}

	public Directive peekOrNull() {
		if (isEmpty()) {
			return null;
		}
		return super.peek();
	}

}
