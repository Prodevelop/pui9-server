package es.prodevelop.pui9.eventlistener.event;

import java.util.Map;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Patch action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PatchEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	private ITableDto oldDto;
	private Map<String, Object> fieldValuesMap;

	public PatchEvent(ITableDto dtoPk, ITableDto oldDto, Map<String, Object> fieldValuesMap) {
		super(dtoPk);
		this.oldDto = oldDto;
		this.fieldValuesMap = fieldValuesMap;
	}

	public ITableDto getOldDto() {
		return oldDto;
	}

	public Map<String, Object> getFieldValuesMap() {
		return fieldValuesMap;
	}

}
