package es.prodevelop.pui9.data.converters;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.springframework.http.converter.json.AbstractJsonHttpMessageConverter;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.services.exceptions.PuiServiceFromJsonException;
import es.prodevelop.pui9.services.exceptions.PuiServiceToJsonException;

/**
 * Own implementation of a Gson Converter, using the {@link GsonSingleton} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiGsonHttpMessageConverter extends AbstractJsonHttpMessageConverter {

	@Override
	protected boolean supports(Class<?> clazz) {
		return !String.class.isAssignableFrom(clazz) && !byte[].class.isAssignableFrom(clazz);
	}

	@Override
	protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
		try {
			String json = IOUtils.toString(reader);
			json = json.replace("\\u0000", "");
			return GsonSingleton.getSingleton().getGson().fromJson(json, resolvedType);
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new PuiServiceFromJsonException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}

	@Override
	protected void writeInternal(Object o, Type type, Writer writer) throws Exception {
		try {
			if (type instanceof ParameterizedType) {
				GsonSingleton.getSingleton().getGson().toJson(o, type, writer);
			} else {
				GsonSingleton.getSingleton().getGson().toJson(o, writer);
			}
		} catch (JsonIOException e) {
			if (!(e.getCause() instanceof IOException)) {
				throw new PuiServiceToJsonException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
			}
		}
	}

}