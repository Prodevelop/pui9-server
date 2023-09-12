package es.prodevelop.pui9.model.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * This Factory class is a utility class to manage DTOs, allowing to retrieve
 * the concret class of an interface of type {@link IDto}. See
 * {@link DtoRegistry} class for more information about utility methods with DTO
 * objects and classes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DtoFactory {

	private static Map<Class<? extends IDto>, Class<? extends AbstractDto>> mapInterfaceToClass;

	static {
		mapInterfaceToClass = new LinkedHashMap<>();
	}

	/**
	 * Registers a DTO
	 * 
	 * @param iface The interface of the {@link IDto}
	 * @param clazz The class of the {@link IDto}
	 */
	public static <T extends IDto, T2 extends AbstractDto> void registerInterface(Class<T> iface, Class<T2> clazz) {
		if (iface.isInterface() && !mapInterfaceToClass.containsKey(iface)) {
			mapInterfaceToClass.put(iface, clazz);
		}
	}

	/**
	 * Return the concrete {@link IDto} class associated to the given {@link IDto}
	 * interface
	 * 
	 * @param iface The interface of the {@link IDto}
	 * @return The class of the {@link IDto}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IDto> Class<? extends IDto> getClassFromInterface(Class<T> iface) {
		if (iface.isInterface()) {
			return mapInterfaceToClass.get(iface);
		} else {
			return (Class<? extends AbstractDto>) iface;
		}
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface The interface representing the {@link IDto}
	 * @return The {@link IDto} instance object
	 */
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface) {
		return createInstanceFromInterface(iface, (Object) null);
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface      The interface representing the {@link IDto}
	 * @param attrValues The attributes map with the values to assign for each
	 *                   attribute
	 * @return The {@link IDto} instance object
	 */
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface, Map<String, Object> attrValues) {
		T dto = createInstanceFromInterface(iface, (Object) null);
		PuiObjectUtils.populateObject(dto, attrValues);

		return dto;
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface The interface representing the {@link IDto}
	 * @param args  The constructor arguments (can be null, but be sure that the
	 *              {@link IDto} class contains a default constructor with no
	 *              parameters)
	 * @return The {@link IDto} instance object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface, Object... args) {
		Class<T> clazz = null;
		if (iface.isInterface()) {
			clazz = (Class<T>) getClassFromInterface(iface);
			if (clazz == null) {
				// if not exists, try to find the implementation in the loaded classes
				clazz = DtoRegistry.getDtoImplementation(iface);
				if (clazz == null) {
					// if not exists, something strange is happening...
					throw new IllegalStateException();
				}
			}
		} else {
			clazz = iface;
		}

		try {
			T instance;
			if (args != null && args[0] != null) {
				List<Class<?>> params = new ArrayList<>();
				for (Object arg : args) {
					params.add(arg.getClass());
				}

				Constructor<T> construct = clazz.getConstructor(params.toArray(new Class<?>[0]));
				instance = construct.newInstance(args);
			} else {
				Constructor<T> construct = clazz.getConstructor();
				instance = construct.newInstance();
			}
			return instance;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	/**
	 * Create a new Translation DTO for the given DTO, only if exists a translation
	 * table for this DTO
	 * 
	 * @param <T>  The type of the whole DTO
	 * @param <TT> The type of the Translation DTO
	 * @param dto  The original DTO
	 * @return The translation DTO
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ITableDto, TT extends ITableDto> TT createTranslationDto(T dto) {
		String translationTableName = DtoRegistry.getTranslationEntityFromDto(dto.getClass());
		if (translationTableName == null) {
			return null;
		}

		List<String> pkFields = DtoRegistry.getPkFields(dto.getClass());
		List<String> langFields = DtoRegistry.getLangFieldNames(dto.getClass());
		TT transDto;
		try {
			transDto = (TT) DtoRegistry.getDtoFromEntity(translationTableName).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}

		for (String pkField : pkFields) {
			Field origField = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), pkField);
			Field transField = DtoRegistry.getJavaFieldFromFieldName(transDto.getClass(), pkField);
			try {
				Object val = FieldUtils.readField(origField, dto);
				FieldUtils.writeField(transField, transDto, val);
			} catch (IllegalAccessException e) {
				return null;
			}
		}

		for (String langField : langFields) {
			Field origField = DtoRegistry.getJavaFieldFromLangFieldName(dto.getClass(), langField);
			Field transField = DtoRegistry.getJavaFieldFromFieldName(transDto.getClass(), langField);
			try {
				Object val = FieldUtils.readField(origField, dto);
				FieldUtils.writeField(transField, transDto, val);
			} catch (IllegalAccessException e) {
				return null;
			}
		}

		return transDto;
	}

	private DtoFactory() {
	}
}
