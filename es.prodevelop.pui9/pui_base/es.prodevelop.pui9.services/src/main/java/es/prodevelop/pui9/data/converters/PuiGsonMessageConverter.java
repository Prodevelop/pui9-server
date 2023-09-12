package es.prodevelop.pui9.data.converters;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import org.springframework.messaging.converter.AbstractJsonMessageConverter;

import es.prodevelop.pui9.json.GsonSingleton;

public class PuiGsonMessageConverter extends AbstractJsonMessageConverter {

	@Override
	protected Object fromJson(Reader reader, Type resolvedType) {
		return GsonSingleton.getSingleton().getGson().fromJson(reader, resolvedType);
	}

	@Override
	protected Object fromJson(String payload, Type resolvedType) {
		return GsonSingleton.getSingleton().getGson().fromJson(payload, resolvedType);
	}

	@Override
	protected void toJson(Object payload, Type resolvedType, Writer writer) {
		GsonSingleton.getSingleton().getGson().toJson(payload, resolvedType, writer);
	}

	@Override
	protected String toJson(Object payload, Type resolvedType) {
		return GsonSingleton.getSingleton().getGson().toJson(payload, resolvedType);
	}

}
