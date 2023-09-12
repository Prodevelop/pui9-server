package es.prodevelop.pui9.docgen.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_docgen_template")
public class VPuiDocgenTemplate extends AbstractViewDto implements IVPuiDocgenTemplate {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 1000, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.MAIN_MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String mainmodel;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.MODELS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String models;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String filename;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.COLUMN_FILENAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String columnfilename;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenTemplate.LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String label;

	@PuiGenerated
	@Override
	public Integer getId() {
		return id;
	}

	@PuiGenerated
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@PuiGenerated
	@Override
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@PuiGenerated
	@Override
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@PuiGenerated
	@Override
	public String getMainmodel() {
		return mainmodel;
	}

	@PuiGenerated
	@Override
	public void setMainmodel(String mainmodel) {
		this.mainmodel = mainmodel;
	}

	@PuiGenerated
	@Override
	public String getModels() {
		return models;
	}

	@PuiGenerated
	@Override
	public void setModels(String models) {
		this.models = models;
	}

	@PuiGenerated
	@Override
	public String getFilename() {
		return filename;
	}

	@PuiGenerated
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@PuiGenerated
	@Override
	public String getColumnfilename() {
		return columnfilename;
	}

	@PuiGenerated
	@Override
	public void setColumnfilename(String columnfilename) {
		this.columnfilename = columnfilename;
	}

	@PuiGenerated
	@Override
	public String getLabel() {
		return label;
	}

	@PuiGenerated
	@Override
	public void setLabel(String label) {
		this.label = label;
	}
}
