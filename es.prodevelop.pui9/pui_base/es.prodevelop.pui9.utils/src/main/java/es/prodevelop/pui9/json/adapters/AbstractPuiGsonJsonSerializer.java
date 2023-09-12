package es.prodevelop.pui9.json.adapters;

import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;

/**
 * Use this class to inherit from her when you create a new JsonSerializer for
 * GSON. This way, it will be registered automatically during the application
 * load
 * 
 * @param <T> The type for this serializer
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiGsonJsonSerializer<T> implements IPuiGsonTypeAdapter<T>, JsonSerializer<T> {

	@Override
	public final Class<T> getUnboxedType() {
		return null;
	}

	@Override
	public final TypeAdapterFactory getFactory() {
		return null;
	}

}
