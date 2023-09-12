package es.prodevelop.codegen.pui9.model;

import java.io.Serializable;

public class ClientConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient PuiConfiguration configuration;

	private transient boolean detailGrid = false;
	private transient boolean generateActions = true;
	private transient boolean generateModals = true;
	private transient boolean generateHeader = true;
	private transient boolean generateForm = true;
	private transient boolean editableForm = true;
	private transient boolean generateMiniAudit = false;
	private transient boolean generateDocumentsTab = false;
	private transient boolean generateDetailsTab = false;
	private transient boolean generateEmptyTabs = false;
	private transient Integer numberOfDetailsTab = 1;
	private transient Integer numberOfEmptyTabs = 1;

	private transient String componentsPath;
	private transient String i18nPath;
	private transient String routerPath;

	private boolean generate;
	private String clientProject;

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isDetailGrid() {
		return detailGrid;
	}

	public void setDetailGrid(boolean detailGrid) {
		this.detailGrid = detailGrid;
	}

	public boolean isGenerateActions() {
		return generateActions;
	}

	public void setGenerateActions(boolean generateActions) {
		this.generateActions = generateActions;
	}

	public boolean isGenerateModals() {
		return generateModals;
	}

	public void setGenerateModals(boolean generateModals) {
		this.generateModals = generateModals;
	}

	public boolean isGenerateHeader() {
		return generateHeader;
	}

	public void setGenerateHeader(boolean generateHeader) {
		this.generateHeader = generateHeader;
	}

	public boolean isGenerateForm() {
		return generateForm;
	}

	public void setGenerateForm(boolean generateForm) {
		this.generateForm = generateForm;
	}

	public boolean isEditableForm() {
		return editableForm;
	}

	public void setEditableForm(boolean editableForm) {
		this.editableForm = editableForm;
	}

	public boolean isGenerateMiniAudit() {
		return generateMiniAudit;
	}

	public void setGenerateMiniAudit(boolean generateMiniAudit) {
		this.generateMiniAudit = generateMiniAudit;
	}

	public boolean isGenerateDocumentsTab() {
		return generateDocumentsTab;
	}

	public void setGenerateDocumentsTab(boolean generateDocumentsTab) {
		this.generateDocumentsTab = generateDocumentsTab;
	}

	public boolean isGenerateDetailsTab() {
		return generateDetailsTab;
	}

	public void setGenerateDetailsTab(boolean generateDetailsTab) {
		this.generateDetailsTab = generateDetailsTab;
	}

	public boolean isGenerateEmptyTabs() {
		return generateEmptyTabs;
	}

	public void setGenerateEmptyTabs(boolean generateEmptyTabs) {
		this.generateEmptyTabs = generateEmptyTabs;
	}

	public Integer getNumberOfDetailsTab() {
		return numberOfDetailsTab;
	}

	public void setNumberOfDetailsTab(Integer numberOfDetailsTab) {
		this.numberOfDetailsTab = numberOfDetailsTab;
	}

	public Integer getNumberOfEmptyTabs() {
		return numberOfEmptyTabs;
	}

	public void setNumberOfEmptyTabs(Integer numberOfEmptyTabs) {
		this.numberOfEmptyTabs = numberOfEmptyTabs;
	}

	public boolean isGenerateTabs() {
		return this.generateDetailsTab || this.generateEmptyTabs || this.generateDocumentsTab;
	}

	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	public String getClientProject() {
		return clientProject;
	}

	public void setClientProject(String clientProject) {
		this.clientProject = clientProject;
	}

	public String getComponentsPath() {
		return componentsPath;
	}

	public String getI18nPath() {
		return i18nPath;
	}

	public String getRouterPath() {
		return routerPath;
	}

	/**
	 * Before calling this method, take into accound these conditions:
	 * <p>
	 * <ul>
	 * <li>All the Java projects are set</li>
	 * </ul>
	 */
	public void computeVueAttributes() {
		componentsPath = "/src/components/";
		i18nPath = "/src/i18n/";
		routerPath = "/src/router/router.js";
	}

	@Override
	public String toString() {
		return "Generate: " + generate + "\nClient Project: " + clientProject;
	}

}
