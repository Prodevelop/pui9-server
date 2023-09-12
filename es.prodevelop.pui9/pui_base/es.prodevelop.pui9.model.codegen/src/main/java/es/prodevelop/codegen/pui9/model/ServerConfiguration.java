package es.prodevelop.codegen.pui9.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ServerConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient PuiConfiguration configuration;
	private transient boolean generateService;
	private transient boolean generateController;
	private transient boolean useAdvancedFunctionalities;
	private transient boolean keepClassHierarchy = true;
	private transient String readFunctionality = "";
	private transient String writeFunctionality = "";
	private transient String insertFunctionality = "";
	private transient String updateFunctionality = "";
	private transient String deleteFunctionality = "";
	private transient String getFunctionality = "";
	private transient String listFunctionality = "";
	private transient String baseJavaPackage;

	private boolean generate;
	private boolean geoProject;
	private String dtoProject;
	private String daoProject;
	private String boProject;
	private String webProject;
	private String basePackage;
	private String superController = "AbstractController";

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isGenerateService() {
		return generateService;
	}

	public void setGenerateService(boolean generateService) {
		this.generateService = generateService;
	}

	public boolean isGenerateController() {
		return generateController;
	}

	public void setGenerateController(boolean generateController) {
		this.generateController = generateController;
	}

	public boolean isUseAdvancedFunctionalities() {
		return useAdvancedFunctionalities;
	}

	public void setUseAdvancedFunctionalities(boolean useAdvancedFunctionalities) {
		this.useAdvancedFunctionalities = useAdvancedFunctionalities;
	}

	public boolean isKeepClassHierarchy() {
		return keepClassHierarchy;
	}

	public void setKeepClassHierarchy(boolean keepClassHierarchy) {
		this.keepClassHierarchy = keepClassHierarchy;
	}

	public String getReadFunctionality() {
		return readFunctionality;
	}

	public void setReadFunctionality(String readFunctionality) {
		this.readFunctionality = readFunctionality;
	}

	public String getWriteFunctionality() {
		return writeFunctionality;
	}

	public void setWriteFunctionality(String writeFunctionality) {
		this.writeFunctionality = writeFunctionality;
	}

	public String getInsertFunctionality() {
		return insertFunctionality;
	}

	public void setInsertFunctionality(String insertFunctionality) {
		this.insertFunctionality = insertFunctionality;
	}

	public String getUpdateFunctionality() {
		return updateFunctionality;
	}

	public void setUpdateFunctionality(String updateFunctionality) {
		this.updateFunctionality = updateFunctionality;
	}

	public String getDeleteFunctionality() {
		return deleteFunctionality;
	}

	public void setDeleteFunctionality(String deleteFunctionality) {
		this.deleteFunctionality = deleteFunctionality;
	}

	public String getGetFunctionality() {
		return getFunctionality;
	}

	public void setGetFunctionality(String getFunctionality) {
		this.getFunctionality = getFunctionality;
	}

	public String getListFunctionality() {
		return listFunctionality;
	}

	public void setListFunctionality(String listFunctionality) {
		this.listFunctionality = listFunctionality;
	}

	public String getBaseJavaPackage() {
		return baseJavaPackage;
	}

	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	public boolean isGeoProject() {
		return geoProject;
	}

	public void setGeoProject(boolean geoProject) {
		this.geoProject = geoProject;
	}

	public String getDtoProject() {
		return dtoProject;
	}

	public void setDtoProject(String dtoProject) {
		this.dtoProject = dtoProject;
	}

	public String getDaoProject() {
		return daoProject;
	}

	public void setDaoProject(String daoProject) {
		this.daoProject = daoProject;
	}

	public String getBoProject() {
		return boProject;
	}

	public void setBoProject(String boProject) {
		this.boProject = boProject;
	}

	public String getWebProject() {
		return webProject;
	}

	public void setWebProject(String webProject) {
		this.webProject = webProject;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getSuperController() {
		return superController;
	}

	public void setSuperController(String superController) {
		this.superController = superController;
	}

	/**
	 * Before calling this method, take into accound these conditions:
	 * <p>
	 * <ul>
	 * <li>All the Java projects are set</li>
	 * </ul>
	 */
	public void computeJavaAttributes() {
		String srcPackage = "/src/main/java/";

		if (StringUtils.isEmpty(basePackage)) {
			basePackage = dtoProject;
			int dtoFormatIndex = basePackage.lastIndexOf(".dto");
			if (dtoFormatIndex > 0) {
				basePackage = basePackage.substring(0, dtoFormatIndex);
			}
		}

		baseJavaPackage = srcPackage + convertProjectToPackage(basePackage) + "/";
	}

	private String convertProjectToPackage(String projectName) {
		return StringUtils.replace(projectName, ".", "/");
	}

	@Override
	public String toString() {
		return "Generate: " + generate + "\nGenerate Service: " + generateService + "\nGenerate Controller: "
				+ generateController + "\nDto Project: " + dtoProject + "\nDao Project: " + daoProject
				+ "\nBo Project: " + boProject + "\nWebProject: " + webProject;
	}

}
