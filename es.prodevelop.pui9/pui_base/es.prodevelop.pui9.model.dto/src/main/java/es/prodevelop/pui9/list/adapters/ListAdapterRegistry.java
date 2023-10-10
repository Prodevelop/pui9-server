package es.prodevelop.pui9.list.adapters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * List Adapter registry, for having all the ListAdapters cached in a unique
 * registry
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ListAdapterRegistry {

	private static ListAdapterRegistry singleton;

	public static ListAdapterRegistry getSingleton() {
		if (singleton == null) {
			singleton = new ListAdapterRegistry();
		}
		return singleton;
	}

	private Map<Class<? extends IDto>, IListAdapter<? extends IDto>> listAdapters;

	private ListAdapterRegistry() {
		listAdapters = new LinkedHashMap<>();
	}

	@SuppressWarnings("unchecked")
	public void registerListAdapter(IListAdapter<? extends IDto> listAdapter) {
		List<Type> superTypes = new ArrayList<>();
		superTypes.addAll(Arrays.asList(listAdapter.getClass().getGenericInterfaces()));
		superTypes.add(listAdapter.getClass().getGenericSuperclass());

		ParameterizedType pt = null;
		for (Type genInt : superTypes) {
			if (genInt instanceof ParameterizedType) {
				pt = (ParameterizedType) genInt;
				break;
			}
		}
		if (pt == null) {
			throw new IllegalArgumentException(
					"No Generic Supertype found. Bad definition of List Adapter: " + listAdapter.getClass().getName());
		}

		Class<? extends IDto> dtoClass = (Class<? extends IDto>) pt.getActualTypeArguments()[0];
		synchronized (listAdapters) {
			if (listAdapters.containsKey(dtoClass)) {
				throw new IllegalArgumentException("Duplicated List Adapter for class " + dtoClass);
			}

			listAdapters.put(dtoClass, listAdapter);
		}
	}

	public IListAdapter<? extends IDto> getAdapter(Class<? extends IDto> dtoClass) {
		synchronized (listAdapters) {
			if (!listAdapters.containsKey(dtoClass) && !dtoClass.isInterface()) {
				for (Iterator<Entry<Class<? extends IDto>, IListAdapter<? extends IDto>>> it = listAdapters.entrySet()
						.iterator(); it.hasNext();) {
					Entry<Class<? extends IDto>, IListAdapter<? extends IDto>> entry = it.next();
					if (entry.getKey().isAssignableFrom(dtoClass)) {
						listAdapters.put(dtoClass, entry.getValue());
						break;
					}
				}
			}
			return listAdapters.get(dtoClass);
		}
	}

}
