package es.prodevelop.pui9.docgen.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiDocgenModelPk extends ITableDto {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);
}
