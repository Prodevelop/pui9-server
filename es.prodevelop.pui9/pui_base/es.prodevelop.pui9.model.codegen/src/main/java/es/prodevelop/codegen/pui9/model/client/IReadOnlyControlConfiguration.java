package es.prodevelop.codegen.pui9.model.client;

public interface IReadOnlyControlConfiguration extends IControlConfiguration {

	Boolean getReadOnly();

	void setReadOnly(Boolean readOnly);

}
