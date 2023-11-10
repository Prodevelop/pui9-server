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
package es.prodevelop.xdocreport.core.registry;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.xdocreport.core.discovery.IBaseDiscovery;
import es.prodevelop.xdocreport.core.logging.LogUtils;

public abstract class AbstractRegistry<Discovery extends IBaseDiscovery> {

	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogUtils.getLogger(AbstractRegistry.class.getName());

	private boolean initialized;

	private final Class<Discovery> registryType;

	public AbstractRegistry(Class<Discovery> registryType) {
		this.registryType = registryType;
	}

	public void initialize() {
		initializeIfNeeded();
	}

	protected void initializeIfNeeded() {
		if (!initialized) {
			onStartInitialization();

			Reflections reflections = new Reflections(ConfigurationBuilder.build()
					.addClassLoaders(PuiClassLoaderUtils.getClassLoader()).forPackages("es.prodevelop.xdocreport"));
			List<Class<? extends Discovery>> classes = reflections.getSubTypesOf(registryType).stream()
					.filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
					.collect(Collectors.toList());

			classes.forEach(cl -> {
				Discovery instance;
				try {
					instance = cl.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					return;
				}

				try {
					boolean result = registerInstance(instance);
					if (LOGGER.isEnabled(Level.TRACE)) {
						LOGGER.trace("Registered Discovery instance  " + instance + " " + result);
					}
				} catch (Throwable e) {
					LOGGER.warn("Error while registration of Discovery instance  " + instance, e);
				}
			});

			onEndInitialization();
			initialized = true;
		}
	}

	/**
	 * Method called when registry start initialization.
	 */
	protected void onStartInitialization() {

	}

	/**
	 * Method called when registry end initialization.
	 */
	protected void onEndInitialization() {

	}

	/**
	 * Register the instance in the registry.
	 *
	 * @param instance
	 * @return true if instance cannot be registered and false otherwise.
	 */
	protected abstract boolean registerInstance(Discovery instance);

	/**
	 * Dispose the registry.
	 */
	public final void dispose() {
		doDispose();
		this.initialized = false;
	}

	protected abstract void doDispose();
}