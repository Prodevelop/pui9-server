package es.prodevelop.pui9.json.adapters;

import com.google.gson.TypeAdapterFactory;

/**
 * This interface is intended to be used to group all the TypeAdapters,
 * Serializers and Deserializers that are created in the context of your
 * application
 * 
 * @param <T> The type for this adapter
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiGsonTypeAdapter<T> {

	/**
	 * The type for this adapter. You should implement it if you are defining a Type
	 * Adapter without a Factory
	 * 
	 * @return The Class of the Type
	 */
	Class<T> getType();

	/**
	 * The unboxed type adapter. You should implement it when you are defining a
	 * Type Adapter for primitive types that also has Object representation
	 * (int/Integer, double/Double...)
	 * 
	 * @return The Class of the Unboxed Class
	 */
	Class<T> getUnboxedType();

	/**
	 * The factory for this adapter. You should implement it if you are definig a
	 * Type Adapter Factory
	 * 
	 * @return The type adapter factory
	 */
	TypeAdapterFactory getFactory();

}
