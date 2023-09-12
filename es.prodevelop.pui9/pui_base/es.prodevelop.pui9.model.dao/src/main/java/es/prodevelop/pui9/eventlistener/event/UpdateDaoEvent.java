package es.prodevelop.pui9.eventlistener.event;

import java.util.Collections;
import java.util.Map;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the DAO Update action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class UpdateDaoEvent extends AbstractDaoEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	private ITableDto oldDto;
	private Map<String, Object> fieldValuesMap;

	public UpdateDaoEvent(ITableDto dto) {
		super(dto);
		this.oldDto = null;
		this.fieldValuesMap = fieldValuesMap != null ? fieldValuesMap : Collections.emptyMap();
	}

	public UpdateDaoEvent(ITableDto dto, ITableDto oldDto) {
		super(dto);
		this.oldDto = oldDto;
		this.fieldValuesMap = fieldValuesMap != null ? fieldValuesMap : Collections.emptyMap();
	}

	public UpdateDaoEvent(ITableDto dto, Map<String, Object> fieldValuesMap) {
		super(dto);
		this.fieldValuesMap = fieldValuesMap != null ? fieldValuesMap : Collections.emptyMap();
		this.oldDto = null;
	}

	public ITableDto getOldDto() {
		return oldDto;
	}

	public Map<String, Object> getFieldValuesMap() {
		return fieldValuesMap;
	}

}
