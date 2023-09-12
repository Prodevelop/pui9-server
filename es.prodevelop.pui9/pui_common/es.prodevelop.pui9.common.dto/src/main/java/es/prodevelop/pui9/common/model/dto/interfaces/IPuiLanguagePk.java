package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiLanguagePk extends ITableDto {
	@PuiGenerated
	String ISOCODE_COLUMN = "isocode";
	@PuiGenerated
	String ISOCODE_FIELD = "isocode";

	@PuiGenerated
	String getIsocode();

	@PuiGenerated
	void setIsocode(String isocode);
}
