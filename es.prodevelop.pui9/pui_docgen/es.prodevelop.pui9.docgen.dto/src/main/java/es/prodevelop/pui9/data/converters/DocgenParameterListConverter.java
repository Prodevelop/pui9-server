package es.prodevelop.pui9.data.converters;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;

import es.prodevelop.pui9.docgen.dto.DocgenParameterList;
import es.prodevelop.pui9.json.GsonSingleton;

/**
 * This class allows to set a @RequestParam parameter in your controllers
 * indicating that the type of this parameter is a {@link DocgenParameterList}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DocgenParameterListConverter implements IPuiGenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, DocgenParameterList.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		String val = new String(source.toString().getBytes(), StandardCharsets.UTF_8);
		return GsonSingleton.getSingleton().getGson().fromJson(val, DocgenParameterList.class);
	}

}