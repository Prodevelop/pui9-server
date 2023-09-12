package es.prodevelop.pui9.model.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * Generic Base class for all DTO classes. All the {@link IDto} classes should
 * inherit from this class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDto implements IDto {

	private static final long serialVersionUID = 1L;

	protected final transient Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public boolean equals(Object other) {
		if (other != null && !getClass().equals(other.getClass())) {
			return false;
		}

		boolean result = getClass().isInstance(other) || this == other;
		if (result) {
			AbstractDto otherObj = (AbstractDto) other;
			EqualsBuilder eqBuilder = new EqualsBuilder();

			Map<String, Field> map = DtoRegistry.getMapFieldsFromFieldName(getClass());
			if (map == null) {
				return super.equals(other);
			}

			map.putAll(DtoRegistry.getLangMapFieldsFromFieldName(getClass()));

			map.entrySet().forEach(entry -> {
				try {
					Field field = entry.getValue();
					Object val1 = FieldUtils.readField(field, this, true);
					Object val2 = FieldUtils.readField(field, otherObj, true);
					eqBuilder.append(val1, val2);
				} catch (Exception e) {
					logger.error(getClass().getSimpleName() + ": Error testing the equality between two DTO", e);
				}
			});

			result = eqBuilder.isEquals();
		}
		return result;
	}

	@Override
	public int hashCode() {
		Map<String, Field> map = DtoRegistry.getMapFieldsFromFieldName(getClass());
		if (map == null) {
			return new SecureRandom().nextInt();
		}

		map.putAll(DtoRegistry.getLangMapFieldsFromFieldName(getClass()));
		HashCodeBuilder hcBuilder = new HashCodeBuilder();

		map.entrySet().forEach(entry -> {
			try {
				Field field = entry.getValue();
				Object value = FieldUtils.readField(field, this, true);
				if (value instanceof IPuiObject) {
					if (value instanceof IDto) {
						hcBuilder.append(value.hashCode());
					} else {
						hcBuilder.append(PuiObjectUtils.computeHashCode((IPuiObject) value));
					}
				} else {
					hcBuilder.append(value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(
						getClass().getSimpleName() + ": Error adding the field '" + entry.getKey() + "' to the hash",
						e);
			}
		});

		return hcBuilder.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ": ");

		Map<String, Field> map = DtoRegistry.getMapFieldsFromFieldName(getClass());
		if (map == null) {
			try {
				Map<String, Object> describe = PropertyUtils.describe(this);
				describe.remove("class");
				sb.append(describe);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				logger.error(getClass().getSimpleName() + ": Error calculating the toString() method", e);
			}

			return sb.toString();
		}

		map.putAll(DtoRegistry.getLangMapFieldsFromFieldName(getClass()));

		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String fieldName = it.next();
			try {
				Field field = map.get(fieldName);
				Object value = FieldUtils.readField(field, this, true);

				sb.append(fieldName + " = ");
				sb.append(value);
				if (it.hasNext()) {
					sb.append("; ");
				}
			} catch (Exception e) {
				logger.error(getClass().getSimpleName() + ": Error adding the field '" + fieldName
						+ "' to the toString() method", e);
			}
		}

		return sb.toString();
	}

}
