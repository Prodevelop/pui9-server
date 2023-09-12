package es.prodevelop.pui9.docgen.parsers;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.docgen.dto.DocgenMapping;
import es.prodevelop.pui9.docgen.enums.DocgenMappingOriginEnum;
import es.prodevelop.pui9.docgen.pdf.converters.IPdfConverter;
import es.prodevelop.pui9.docgen.pdf.converters.PdfConverterRegistry;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This is an Abstract Document Parser with useful methods. All the parsers may
 * inherit this class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDocumentParser implements IDocumentParser {

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

	/**
	 * Get the PDF converter for this document parser
	 * 
	 * @return The PDF converter
	 */
	protected IPdfConverter getPdfConverter() {
		return PdfConverterRegistry.getSingleton().getPdfConverter(getFileExtension());
	}

	/**
	 * This method analyzes the list of DTO looking for details, and returns a
	 * modified list of elements with the list of details that belongs to each DTO
	 */
	protected List<DtoDetailsElement> parseDtos(List<IDto> list, List<String> pkFieldNames,
			boolean documentWithDetails) {
		List<DtoDetailsElement> parsedList = new ArrayList<>();

		if (!documentWithDetails) {
			for (IDto dto : list) {
				parsedList.add(new DtoDetailsElement(dto));
			}
		} else {
			if (!ObjectUtils.isEmpty(pkFieldNames)) {
				Class<? extends IDto> dtoClass = list.get(0).getClass();
				List<Field> pkFields = new ArrayList<>();
				for (String pkFieldName : pkFieldNames) {
					Field pkField = DtoRegistry.getJavaFieldFromFieldName(dtoClass, pkFieldName);
					pkFields.add(pkField);
				}

				// map pk hash to index in list
				Map<Integer, Integer> parents = new LinkedHashMap<>();
				for (IDto dto : list) {
					HashCodeBuilder hcb = new HashCodeBuilder();
					for (Field pkField : pkFields) {
						try {
							Object value = FieldUtils.readField(pkField, dto, true);
							hcb.append(value);
						} catch (IllegalAccessException e) {
							// do nothing
						}
					}
					int hash = hcb.toHashCode();
					if (!parents.containsKey(hash)) {
						parents.put(hash, parsedList.size());
						parsedList.add(new DtoDetailsElement(dto));
					}
					parsedList.get(parents.get(hash)).getDetails().add(dto);
				}
			}
		}

		return parsedList;
	}

	/**
	 * This method return the value that will be applied to the given mapping and
	 * DTO
	 */
	protected String getValue(DocgenMapping mapping, IDto dto, boolean formatResult) {
		String value = "";
		if (mapping.getOrigin().equals(DocgenMappingOriginEnum.V)) {
			Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), mapping.getField());
			if (field == null) {
				field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), mapping.getField());
			}
			try {
				Object val = FieldUtils.readField(field, dto, true);
				value = valueAsString(val, formatResult);
			} catch (Exception e) {
				// do nothing
			}
		} else {
			value = mapping.getField();
		}

		return value;
	}

	protected String valueAsString(Object val, boolean formatResult) {
		String value = "";
		if (val == null) {
			return value;
		}

		if (val instanceof Instant) {
			// use the formatter, and remove the " character
			value = PuiDateUtil.temporalAccessorToString((Instant) val, formatter);
		} else if (val instanceof BigDecimal) {
			if (formatResult) {
				NumberFormat nf = NumberFormat
						.getInstance(Locale.forLanguageTag(PuiUserSession.getSessionLanguage().getIsocode()));
				nf.setMinimumFractionDigits(2);
				nf.setMaximumFractionDigits(2);
				value = nf.format(val);
			} else {
				value = val.toString();
			}
		} else {
			value = val.toString();
		}

		return value;
	}

	/**
	 * This is a supporting class for representing a Master-Detail in the list of
	 * elements that are involved in the template generation
	 * 
	 * @author Marc Gil - mgil@prodevelop.es
	 */
	protected class DtoDetailsElement {
		private IDto dto;
		private List<IDto> details;

		// without details
		private DtoDetailsElement(IDto dto) {
			this(dto, null);
		}

		// with details
		private DtoDetailsElement(IDto dto, List<IDto> details) {
			this.dto = dto;
			this.details = details != null ? details : new ArrayList<>();
		}

		public IDto getDto() {
			return dto;
		}

		public List<IDto> getDetails() {
			return details;
		}
	}

}
