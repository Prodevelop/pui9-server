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

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import es.prodevelop.odfdom.converter.pdf.internal.styles.StyleBorder;

public class StyleUtils {

	public static void applyStyles(StyleBorder border, PdfPCell cell) {
		if (border == null) {
			return;
		}
		switch (border.getBorderType()) {
		case ALL:
			// border
			if (border.isNoBorder()) {
				cell.disableBorderSide(Rectangle.TOP);
				cell.disableBorderSide(Rectangle.BOTTOM);
				cell.disableBorderSide(Rectangle.RIGHT);
				cell.disableBorderSide(Rectangle.LEFT);
			} else {
				cell.enableBorderSide(Rectangle.TOP);
				cell.enableBorderSide(Rectangle.BOTTOM);
				cell.enableBorderSide(Rectangle.RIGHT);
				cell.enableBorderSide(Rectangle.LEFT);
				// border-color
				Color color = border.getColor();
				if (color != null) {
					cell.setBorderColor(
							new BaseColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
				}
				// border-width
				Float width = border.getWidth();
				if (width != null) {
					cell.setBorderWidth(width);
				}
			}
			break;
		case BOTTOM:
			// border-bottom
			if (border.isNoBorder()) {
				cell.disableBorderSide(Rectangle.BOTTOM);
			} else {
				cell.enableBorderSide(Rectangle.BOTTOM);
				// border-bottom-color
				Color color = border.getColor();
				if (color != null) {
					cell.setBorderColorBottom(
							new BaseColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
				}
				// border-bottom-width
				Float width = border.getWidth();
				if (width != null) {
					cell.setBorderWidthBottom(width);
				}
			}
			break;
		case LEFT:
			// border-left
			if (border.isNoBorder()) {
				cell.disableBorderSide(Rectangle.LEFT);
			} else {
				cell.enableBorderSide(Rectangle.LEFT);
				// border-left-color
				Color color = border.getColor();
				if (color != null) {
					cell.setBorderColorLeft(
							new BaseColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
				}
				// border-left-width
				Float width = border.getWidth();
				if (width != null) {
					cell.setBorderWidthLeft(width);
				}
			}
			break;
		case RIGHT:
			// border-right
			if (border.isNoBorder()) {
				cell.disableBorderSide(Rectangle.RIGHT);
			} else {
				cell.enableBorderSide(Rectangle.RIGHT);
				// border-right-color
				Color color = border.getColor();
				if (color != null) {
					cell.setBorderColorRight(
							new BaseColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
				}
				// border-right-width
				Float width = border.getWidth();
				if (width != null) {
					cell.setBorderWidthRight(width);
				}
			}
			break;
		case TOP:
			// border-top
			if (border.isNoBorder()) {
				cell.disableBorderSide(Rectangle.TOP);
			} else {
				cell.enableBorderSide(Rectangle.TOP);
				// border-top-color
				Color color = border.getColor();
				if (color != null) {
					cell.setBorderColorTop(
							new BaseColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
				}
				// border-top-width
				Float width = border.getWidth();
				if (width != null) {
					cell.setBorderWidthTop(width);
				}
			}
			break;
		}

	}
}
