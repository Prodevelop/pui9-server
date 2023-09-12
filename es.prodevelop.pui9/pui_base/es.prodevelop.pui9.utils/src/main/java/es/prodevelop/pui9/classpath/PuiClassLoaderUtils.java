package es.prodevelop.pui9.classpath;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class loader is a wrapper of the class loader used in the application.
 * This is of type {@link URLClassLoader}, and it's used to wrap class loaders
 * that are not of this type (for instance, when using JBoss)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiClassLoaderUtils {

	private static final Logger logger = LogManager.getLogger(PuiClassLoaderUtils.class);
	private static ClassLoader classLoader;

	/**
	 * Get application main class loader
	 * 
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		return classLoader;
	}

	public static void addURL(URL url) {
		ClassLoader cl = getClassLoader();
		if (cl instanceof URLClassLoader) {
			addUrl((URLClassLoader) cl, url);
		}
	}

	/**
	 * For Java 8 and Tomcat. Add a new url to the class loader
	 * 
	 * @param urlClassLoader
	 */
	private static void addUrl(URLClassLoader urlClassLoader, URL url) {
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(urlClassLoader, url);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			logger.error("Could not find 'addURL' method on ClassLoader", e);
		}
	}

	private PuiClassLoaderUtils() {
	}

}