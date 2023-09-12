package es.prodevelop.pui9.docgen.dto;

import java.util.List;

import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.search.SearchRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class GenerateDocgenRequest extends SearchRequest {
	private static final long serialVersionUID = 1L;

	@Schema(requiredMode = RequiredMode.REQUIRED, example = "")
	private IPuiDocgenTemplatePk pk;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<AbstractFilterRule> parameters;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<DocgenUserMapping> mappings;
	@Schema(requiredMode = RequiredMode.REQUIRED, example = "true")
	private Boolean generatePdf = false;

	public IPuiDocgenTemplatePk getPk() {
		return pk;
	}

	public void setPk(IPuiDocgenTemplatePk pk) {
		this.pk = pk;
	}

	public List<AbstractFilterRule> getParameters() {
		return parameters;
	}

	public void setParameters(List<AbstractFilterRule> parameters) {
		this.parameters = parameters;
	}

	public List<DocgenUserMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<DocgenUserMapping> mappings) {
		this.mappings = mappings;
	}

	public Boolean isGeneratePdf() {
		return generatePdf;
	}

	public void setGeneratePdf(Boolean generatePdf) {
		this.generatePdf = generatePdf;
	}

}
