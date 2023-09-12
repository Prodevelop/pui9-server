package es.prodevelop.pui9.documents.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDocument extends IPuiDocumentPk {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String LANGUAGE_COLUMN = "language";
	@PuiGenerated
	String LANGUAGE_FIELD = "language";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String FILENAME_COLUMN = "filename";
	@PuiGenerated
	String FILENAME_FIELD = "filename";
	@PuiGenerated
	String FILENAME_ORIG_COLUMN = "filename_orig";
	@PuiGenerated
	String FILENAME_ORIG_FIELD = "filenameorig";
	@PuiGenerated
	String ROLE_COLUMN = "role";
	@PuiGenerated
	String ROLE_FIELD = "role";
	@PuiGenerated
	String THUMBNAILS_COLUMN = "thumbnails";
	@PuiGenerated
	String THUMBNAILS_FIELD = "thumbnails";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	String getLanguage();

	@PuiGenerated
	void setLanguage(String language);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getFilename();

	@PuiGenerated
	void setFilename(String filename);

	@PuiGenerated
	String getFilenameorig();

	@PuiGenerated
	void setFilenameorig(String filenameorig);

	@PuiGenerated
	String getRole();

	@PuiGenerated
	void setRole(String role);

	@PuiGenerated
	String getThumbnails();

	@PuiGenerated
	void setThumbnails(String thumbnails);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	String getUrl();

	void setUrl(String url);
}
