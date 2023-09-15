package es.prodevelop.pui9.json.adapters;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;
import es.prodevelop.pui9.filter.TodayRuleData;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * Type adapter for {@link AbstractFilterRule} type to be used with GSON. Allows
 * to serialize and deserialize {@link AbstractFilterRule} objects in the
 * serialization/deserialization process
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class AbstractFilterRuleTypeAdapter extends AbstractPuiGsonTypeAdapter<AbstractFilterRule> {

	@Override
	public Class<AbstractFilterRule> getType() {
		return AbstractFilterRule.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractFilterRule read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		in.beginObject();
		Map<String, Object> mapValues = new LinkedHashMap<>();
		while (in.hasNext()) {
			String key = in.nextName();
			Object value = null;
			switch (in.peek()) {
			case NUMBER:
				String jsonValue = in.nextString();
				value = new BigDecimal(jsonValue);
				if (((BigDecimal) value).scale() <= 0) {
					BigDecimal bd = (BigDecimal) value;
					if (bd.longValue() > Integer.MAX_VALUE) {
						value = Long.valueOf(((BigDecimal) value).longValue());
					} else {
						value = Integer.valueOf(((BigDecimal) value).intValue());
					}
				}
				break;
			case STRING:
				value = in.nextString();
				break;
			case BOOLEAN:
				value = in.nextBoolean();
				break;
			case NULL:
				in.nextNull();
				break;
			case BEGIN_ARRAY:
				value = GsonSingleton.getSingleton().getGson().fromJson(in, new TypeReference<List<Object>>() {
				}.getType());
				break;
			case BEGIN_OBJECT:
				value = GsonSingleton.getSingleton().getGson().fromJson(in, LinkedHashMap.class);
				break;
			default:
				break;
			}
			mapValues.put(key, value);
		}
		in.endObject();

		if (!mapValues.containsKey("op")) {
			return null;
		}

		FilterRuleOperation type = FilterRuleOperation.valueOf((String) mapValues.get("op"));
		if (type == null) {
			return null;
		}

		AbstractFilterRule rule = null;

		try {
			Constructor<AbstractFilterRule> constructor = null;
			for (Constructor<?> c : type.clazz.getDeclaredConstructors()) {
				if (c.getParameterCount() == 0) {
					constructor = (Constructor<AbstractFilterRule>) c;
					break;
				}
			}
			if (constructor == null) {
				return null;
			}

			constructor.setAccessible(true);
			rule = constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			return null;
		}
		PuiObjectUtils.populateObject(rule, mapValues);

		Object data;
		if (TodayRuleData.class.isAssignableFrom(type.dataClass)) {
			data = new TodayRuleData();
			PuiObjectUtils.populateObject(data, (Map<String, Object>) rule.getData());
		} else {
			data = rule.getData();
		}

		return rule.withData(data);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void write(JsonWriter out, AbstractFilterRule value) throws IOException {
		if (value == null) {
			out.nullValue();
		} else {
			TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) (Object) GsonSingleton.getSingleton().getGson()
					.getAdapter(value.getClass());
			typeAdapter.write(out, value);
		}
	}

}