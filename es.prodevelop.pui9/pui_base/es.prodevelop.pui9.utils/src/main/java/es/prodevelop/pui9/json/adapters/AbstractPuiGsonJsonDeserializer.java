package es.prodevelop.pui9.json.adapters;

import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapterFactory;

/**
 * Use this class to inherit from her when you create a new JsonDeserializer for
 * GSON. This way, it will be registered automatically during the application
 * load
 * 
 * @param <T> The type for this deserializer
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiGsonJsonDeserializer<T> implements IPuiGsonTypeAdapter<T>, JsonDeserializer<T> {

	@Override
	public final Class<T> getUnboxedType() {
		return null;
	}

	@Override
	public final TypeAdapterFactory getFactory() {
		return null;
	}

}
