package es.prodevelop.pui9.docgen.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.dto.DocgenMappingList;
import es.prodevelop.pui9.docgen.dto.DocgenParameterList;
import es.prodevelop.pui9.docgen.dto.StringList;
import es.prodevelop.pui9.filter.FilterGroup;

@PuiGenerated
public interface IPuiDocgenTemplate extends IPuiDocgenTemplatePk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String MAIN_MODEL_COLUMN = "main_model";
	@PuiGenerated
	String MAIN_MODEL_FIELD = "mainmodel";
	@PuiGenerated
	String MODELS_COLUMN = "models";
	@PuiGenerated
	String MODELS_FIELD = "models";
	@PuiGenerated
	String FILENAME_COLUMN = "filename";
	@PuiGenerated
	String FILENAME_FIELD = "filename";
	@PuiGenerated
	String MAPPING_COLUMN = "mapping";
	@PuiGenerated
	String MAPPING_FIELD = "mapping";
	@PuiGenerated
	String FILTER_COLUMN = "filter";
	@PuiGenerated
	String FILTER_FIELD = "filter";
	@PuiGenerated
	String PARAMETERS_COLUMN = "parameters";
	@PuiGenerated
	String PARAMETERS_FIELD = "parameters";
	@PuiGenerated
	String COLUMN_FILENAME_COLUMN = "column_filename";
	@PuiGenerated
	String COLUMN_FILENAME_FIELD = "columnfilename";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getMainmodel();

	@PuiGenerated
	void setMainmodel(String mainmodel);

	@PuiGenerated
	StringList getModels();

	@PuiGenerated
	void setModels(StringList models);

	@PuiGenerated
	String getFilename();

	@PuiGenerated
	void setFilename(String filename);

	@PuiGenerated
	DocgenMappingList getMapping();

	@PuiGenerated
	void setMapping(DocgenMappingList mapping);

	@PuiGenerated
	FilterGroup getFilter();

	@PuiGenerated
	void setFilter(FilterGroup filter);

	@PuiGenerated
	DocgenParameterList getParameters();

	@PuiGenerated
	void setParameters(DocgenParameterList parameters);

	@PuiGenerated
	StringList getColumnfilename();

	@PuiGenerated
	void setColumnfilename(StringList columnfilename);
}
