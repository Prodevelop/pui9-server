package es.prodevelop.pui9.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;

/**
 * A utility class to load properties files from its file name
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiPropertiesManager {

	private PuiPropertiesManager() {
	}

	/**
	 * Tries to load the properties file with the given name. File name should
	 * contain the whole path from the root of the project
	 * 
	 * @param filename The filename to be loaded
	 * @return The Properties object
	 * @throws IOException Exception if the file doesn't exist
	 */
	public static Properties loadPropertiesFile(String filename) throws IOException {
		Properties props = new Properties();
		InputStream is = PuiClassLoaderUtils.getClassLoader().getResourceAsStream(filename);
		if (is != null) {
			props.load(is);
		} else {
			File file = new File(filename);
			if (file.exists()) {
				try (InputStream is2 = new FileInputStream(file)) {
					props.load(is2);
				}
			}
		}

		return props;
	}

}
