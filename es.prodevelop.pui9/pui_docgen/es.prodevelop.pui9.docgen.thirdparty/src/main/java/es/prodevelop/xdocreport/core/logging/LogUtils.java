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
package es.prodevelop.xdocreport.core.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A container for static utility methods related to logging.
 * <p>
 * Inspired from org.apache.cxf.common.logging.LogUtils
 * </p>
 *
 * @author pascalleclercq
 */
public final class LogUtils {

	/**
	 * Prevents instantiation.
	 */
	private LogUtils() {
	}

	/**
	 * Get a Logger with the associated default resource bundle for the class.
	 *
	 * @param cls the Class to contain the Logger
	 * @return an appropriate Logger
	 */
	public static Logger getLogger(Class<?> cls) {
		return createLogger(cls.getName());
	}

	/**
	 * Create a logger
	 */
	protected static Logger createLogger(String loggerName) {
		return LogManager.getLogger(loggerName);
	}

	public static Logger getLogger(String name) {
		return createLogger(name);
	}

}
