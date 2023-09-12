package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.dto.DocgenMappingList;
import es.prodevelop.pui9.docgen.dto.DocgenParameterList;
import es.prodevelop.pui9.docgen.dto.StringList;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.filter.FilterGroup;

@PuiGenerated
@PuiEntity(tablename = "pui_docgen_template")
public class PuiDocgenTemplate extends PuiDocgenTemplatePk implements IPuiDocgenTemplate {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 1000, islang = false, isgeometry = false, issequence = false)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.MAIN_MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String mainmodel;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.MODELS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private StringList models;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filename;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.MAPPING_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private DocgenMappingList mapping;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.FILTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private FilterGroup filter;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.PARAMETERS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private DocgenParameterList parameters;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenTemplate.COLUMN_FILENAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private StringList columnfilename;

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
	public StringList getModels() {
		return models;
	}

	@PuiGenerated
	@Override
	public void setModels(StringList models) {
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
	public DocgenMappingList getMapping() {
		return mapping;
	}

	@PuiGenerated
	@Override
	public void setMapping(DocgenMappingList mapping) {
		this.mapping = mapping;
	}

	@PuiGenerated
	@Override
	public FilterGroup getFilter() {
		return filter;
	}

	@PuiGenerated
	@Override
	public void setFilter(FilterGroup filter) {
		this.filter = filter;
	}

	@PuiGenerated
	@Override
	public DocgenParameterList getParameters() {
		return parameters;
	}

	@PuiGenerated
	@Override
	public void setParameters(DocgenParameterList parameters) {
		this.parameters = parameters;
	}

	@PuiGenerated
	@Override
	public StringList getColumnfilename() {
		return columnfilename;
	}

	@PuiGenerated
	@Override
	public void setColumnfilename(StringList columnfilename) {
		this.columnfilename = columnfilename;
	}
}
