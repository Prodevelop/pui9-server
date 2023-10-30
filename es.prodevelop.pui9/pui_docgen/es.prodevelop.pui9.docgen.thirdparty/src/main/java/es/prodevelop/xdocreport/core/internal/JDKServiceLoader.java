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
package es.prodevelop.xdocreport.core.internal;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * JDK ServiceLoader is used to load services declared in the
 * META-INF/services/MyClass. Switch JDK using, it uses:
 * <ul>
 * <li><b>java.util.ServiceLoader</b> if XDocReport works on Java6. For example
 * :
 * <p>
 * <code>Iterator<Discovery> discoveries =
                ServiceLoader.load( registryType, getClass().getClassLoader() ).iterator();</code>
 * </p>
 * </li>
 * <li><b>javax.imageio.spi.ServiceRegistry</b> if XDocReport works on Java5.
 * For example :
 * <p>
 * <code>Iterator<Discovery> discoveries =
                ServiceRegistry.lookupProviders( registryType, getClass().getClassLoader() );</code>
 * </p>
 * </li>
 * </ul>
 */
public abstract class JDKServiceLoader {

	public static <T> Iterator<T> lookupProviders(Class<T> providerClass, ClassLoader loader) {
		return ServiceLoader.load(providerClass, loader).iterator();
	}

}
