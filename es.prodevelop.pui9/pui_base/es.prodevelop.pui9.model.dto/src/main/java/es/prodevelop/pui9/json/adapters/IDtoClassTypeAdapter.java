package es.prodevelop.pui9.json.adapters;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Type adapter for Class type to be used with GSON. Allows to serialize and
 * deserialize classes that conforms to {@link IDto} objects
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@SuppressWarnings("rawtypes")
public class IDtoClassTypeAdapter extends AbstractPuiGsonTypeAdapter<Class> {

	@Override
	public Class<Class> getType() {
		return Class.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(JsonWriter out, Class value) throws IOException {
		if (value == null || !IDto.class.isAssignableFrom(value)) {
			out.nullValue();
			return;
		}

		Class<IDto> dtoClass = (Class<IDto>) value;

		out.beginObject();

		out.name("className");
		out.value(dtoClass.getCanonicalName());
		if (!StringUtils.isEmpty(DtoRegistry.getEntityFromDto(dtoClass))) {
			out.name("entity");
			out.value(DtoRegistry.getEntityFromDto(dtoClass));
		}
		out.name("fields");

		out.beginArray();

		List<Field> fields = DtoRegistry.getAllJavaFields(dtoClass);

		for (Field field : fields) {
			try {
				out.beginObject();
				PuiField puiField;
				PuiViewColumn puiViewColumn;
				try {
					puiField = field.getAnnotation(PuiField.class);
					puiViewColumn = field.getAnnotation(PuiViewColumn.class);
				} catch (Exception e) {
					puiField = null;
					puiViewColumn = null;
				}
				out.name("fieldName");
				out.value(field.getName());
				out.name("type");
				out.value(field.getType().toString().replace("class", "").trim());
				if (puiField != null) {
					out.name("isPartOfEntity");
					out.value(true);
					out.name("columnName");
					out.value(puiField.columnname());
					out.name("isPk");
					out.value(puiField.ispk());
					out.name("isSequence");
					out.value(puiField.issequence());
					out.name("isAutoincrementable");
					out.value(puiField.autoincrementable());
					out.name("isNullable");
					out.value(puiField.nullable());
					out.name("isGeometry");
					out.value(puiField.isgeometry());
					out.name("geometryType");
					out.value(puiField.geometrytype().name());
					out.name("maxLength");
					out.value(puiField.maxlength());
				} else {
					out.name("isPartOfEntity");
					out.value(false);
				}
				if (puiViewColumn != null) {
					out.name("visibility");
					out.value(puiViewColumn.visibility().name());
					out.name("order");
					out.value(puiViewColumn.order());
				}
			} catch (Exception e) {
				throw new IOException("problem writing json: " + e);
			} finally {
				out.endObject();
			}
		}
		out.endArray();

		out.endObject();
	}

	@Override
	public Class read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		} else {
			throw new UnsupportedOperationException(
					"Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
		}
	}

}