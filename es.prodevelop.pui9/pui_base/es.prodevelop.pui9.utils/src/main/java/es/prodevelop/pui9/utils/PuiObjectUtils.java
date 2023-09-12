package es.prodevelop.pui9.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.primitives.Primitives;

import es.prodevelop.pui9.json.GsonSingleton;

/**
 * Utility class to manage objects: copy properties, copy objects...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiObjectUtils {

	private static Map<Class<?>, Map<String, Field>> mapCache = new LinkedHashMap<>();

	/**
	 * Copy a whole object into another one (external references are copied by
	 * reference. If you need a deep copy, use {@link #copyDeepObject(Object)})
	 * 
	 * @param <T>  The type of the object
	 * @param orig The original object
	 * @return A copy of the given object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T copyObject(T orig) {
		if (orig == null) {
			return null;
		}

		try {
			T dest = (T) orig.getClass().newInstance();
			copyProperties(dest, orig);
			return dest;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * Copy a whole object and its references into another one. References are also
	 * copied. This is a deep copy of the object
	 * 
	 * @param <T>  The type of the object
	 * @param orig The original object
	 * @return A deep copy of the given object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T copyDeepObject(T orig) {
		if (orig == null) {
			return null;
		}

		String origJson = GsonSingleton.getSingleton().getGson().toJson(orig);
		return GsonSingleton.getSingleton().getGson().fromJson(origJson, (Class<T>) orig.getClass());
	}

	/**
	 * Copy all the attributes from the original object to the destination object
	 * 
	 * @param dest The destination object
	 * @param orig The original object
	 */
	public static void copyProperties(Object dest, Object orig) {
		if (dest == null || orig == null) {
			return;
		}

		Map<String, Field> destFields = getFields(dest.getClass());
		Map<String, Field> origFields = getFields(orig.getClass());

		for (Entry<String, Field> entry : destFields.entrySet()) {
			try {
				String destFieldName = entry.getKey();
				Field destField = entry.getValue();

				if (!origFields.containsKey(destFieldName)) {
					continue;
				}

				Field origField = origFields.get(destFieldName);

				if (!destField.getType().equals(origField.getType())) {
					continue;
				}

				Object value = origField.get(orig);
				destField.set(dest, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}
	}

	public static void populateObject(Object object, Map<String, Object> fieldValuesMap) {
		if (object == null || fieldValuesMap == null || fieldValuesMap.isEmpty()) {
			return;
		}

		Map<String, Field> fields = getFields(object.getClass());

		for (Entry<String, Object> entry : fieldValuesMap.entrySet()) {
			if (!fields.containsKey(entry.getKey())) {
				continue;
			}

			Field field = fields.get(entry.getKey());
			try {
				Object val = entry.getValue();
				Class<?> fieldType = field.getType();
				if (field.getType().equals(BigDecimal.class) && (val instanceof Double)) {
					val = BigDecimal.valueOf((Double) val);
				}
				if (field.getType().equals(Integer.class) && (val instanceof Double)) {
					val = Integer.valueOf(((Double) val).intValue());
				}
				if (Enum.class.isAssignableFrom(fieldType)) {
					for (Object enumVal : fieldType.getEnumConstants()) {
						if (enumVal.toString().equals(val)) {
							val = enumVal;
							break;
						}
					}
				}

				if (val != null) {
					if (Primitives.isWrapperType(fieldType) && !Primitives.isWrapperType(val.getClass())) {
						val = convertToWrapper(val);
					} else if (!Primitives.isWrapperType(fieldType) && Primitives.isWrapperType(val.getClass())) {
						val = convertToPrimitive(val);
					}
				}

				field.setAccessible(true);

				if (val == null || field.getType().isAssignableFrom(val.getClass())) {
					field.set(object, val);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}
	}

	public static int computeHashCode(IPuiObject object) {
		Map<String, Field> fields = getFields(object.getClass());
		if (ObjectUtils.isEmpty(fields)) {
			return new SecureRandom().nextInt();
		}

		HashCodeBuilder hcBuilder = new HashCodeBuilder();
		fields.entrySet().forEach(entry -> {
			try {
				Field field = entry.getValue();
				Object value = FieldUtils.readField(field, object, true);
				if (value instanceof IPuiObject) {
					hcBuilder.append(computeHashCode((IPuiObject) value));
				} else {
					hcBuilder.append(value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		});

		return hcBuilder.toHashCode();
	}

	/**
	 * Get all the fields from the Class, including the inherited ones
	 * 
	 * @param clazz The class type of the object
	 * @return A map with the name and Field
	 */
	public static Map<String, Field> getFields(Class<?> clazz) {
		if (!mapCache.containsKey(clazz)) {
			synchronized (mapCache) {
				if (!mapCache.containsKey(clazz)) {
					Map<String, Field> map = new LinkedHashMap<>();

					Class<?> toRegister = clazz;
					while (toRegister != null) {
						List<Field> fields = Arrays.asList(toRegister.getDeclaredFields());
						for (Field field : fields) {
							field.setAccessible(true);
							map.put(field.getName(), field);
						}
						toRegister = toRegister.getSuperclass();
					}

					mapCache.put(clazz, map);
				}
			}
		}

		return mapCache.get(clazz);
	}

	private static Object convertToPrimitive(Object value) {
		if (value == null) {
			return null;
		}

		if (Integer.class.equals(value.getClass())) {
			return ((Integer) value).intValue();
		} else if (Long.class.equals(value.getClass())) {
			return ((Long) value).longValue();
		} else if (Float.class.equals(value.getClass())) {
			return ((Float) value).floatValue();
		} else if (Double.class.equals(value.getClass())) {
			return ((Double) value).doubleValue();
		} else if (Boolean.class.equals(value.getClass())) {
			return ((Boolean) value).booleanValue();
		} else {
			return null;
		}
	}

	private static Object convertToWrapper(Object value) {
		if (value == null) {
			return null;
		}

		if (int.class.equals(value.getClass())) {
			return Integer.valueOf((int) value);
		} else if (long.class.equals(value.getClass())) {
			return Long.valueOf((long) value);
		} else if (float.class.equals(value.getClass())) {
			return Float.valueOf((float) value);
		} else if (double.class.equals(value.getClass())) {
			return Double.valueOf((double) value);
		} else if (boolean.class.equals(value.getClass())) {
			return Boolean.valueOf((boolean) value);
		} else {
			return null;
		}
	}

	private PuiObjectUtils() {
	}

}
