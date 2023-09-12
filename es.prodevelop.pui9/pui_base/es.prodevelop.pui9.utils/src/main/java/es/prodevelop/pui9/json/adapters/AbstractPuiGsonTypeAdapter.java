package es.prodevelop.pui9.json.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

/**
 * Use this class to inherit from her when you create a new Type Adapter for
 * GSON. This way, it will be registered automatically during the application
 * load
 * 
 * @param <T> The type for this adapter
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiGsonTypeAdapter<T> extends TypeAdapter<T> implements IPuiGsonTypeAdapter<T> {

	@Override
	public Class<T> getType() {
		return null;
	}

	@Override
	public Class<T> getUnboxedType() {
		return null;
	}

	@Override
	public TypeAdapterFactory getFactory() {
		if (getUnboxedType() != null) {
			return TypeAdapters.newFactory(getUnboxedType(), getType(), this);
		} else {
			return TypeAdapters.newFactory(getType(), this);
		}
	}

}
