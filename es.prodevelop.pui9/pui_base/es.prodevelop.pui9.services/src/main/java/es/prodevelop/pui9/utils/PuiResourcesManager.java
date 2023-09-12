package es.prodevelop.pui9.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

/**
 * A utility class to get the content of a resource in the classpath. It's
 * allowed to have this resource translated into multiples files, with the
 * suffix "_XX", where XX is the language of the resource<br>
 * <br>
 * A way to load the resource is to put this code in your component class:
 * 
 * <pre>
 * &#64;Value("&#42;&#42;/my_resource_name&#42;.html")
 * private Resource[] resources;
 * </pre>
 * 
 * Above code load my_resource_name.html, my_resource_name_es.html, etc... from
 * any folder
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiResourcesManager {

	private PuiResourcesManager() {
	}

	/**
	 * Return the content of the given resource. The resource parameter is an array
	 * of the resource. It is returned the default content (those wihout language)
	 * 
	 * @param resources The resource array to get its content
	 * @return The content in UTF-8 of the resource
	 */
	public static String getResourceContent(Resource[] resources) {
		return getResourceContent(resources, null);
	}

	/**
	 * Return the content of the given resource. The resource parameter is an array
	 * of the resource. The length of this array will be 0 if the resource was not
	 * found; 1 if the resource is not translated; N if the resource is translated
	 * 
	 * @param resources The resource array to get its content
	 * @param language  The desired language of the resource
	 * @return The content in UTF-8 of the resource
	 */
	public static String getResourceContent(Resource[] resources, String language) {
		if (ObjectUtils.isEmpty(resources)) {
			return null;
		}

		Resource resource = loadWithoutTranslation(resources);
		if (resource == null) {
			resource = loadWithTranslation(resources, language);
		}

		if (resource == null) {
			return null;
		}

		try {
			return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}

	private static Resource loadWithoutTranslation(Resource[] resources) {
		return resources.length == 1 ? resources[0] : null;
	}

	private static Resource loadWithTranslation(Resource[] resources, String language) {
		Resource defaultResource = null;
		for (Resource resource : resources) {
			if (resource.getFilename() == null) {
				continue;
			}

			String name = FilenameUtils.getBaseName(resource.getFilename());

			if (!ObjectUtils.isEmpty(language) && name.endsWith("_" + language)) {
				return resource;
			} else {
				if (defaultResource == null) {
					defaultResource = resource;
				} else {
					String defaultName = FilenameUtils.getBaseName(defaultResource.getFilename());
					if (defaultName.contains(name)) {
						defaultResource = resource;
					}
				}
			}
		}

		return defaultResource;
	}

}
