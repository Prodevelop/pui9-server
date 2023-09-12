package es.prodevelop.pui9.documents.model.views.dto.interfaces;

import java.util.Map;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiDocument extends IViewDto {
	@PuiGenerated
	String ID_COLUMN = "id";
	@PuiGenerated
	String ID_FIELD = "id";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String LANGUAGE_COLUMN = "language";
	@PuiGenerated
	String LANGUAGE_FIELD = "language";
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
	String ROLE_DESCRIPTION_COLUMN = "role_description";
	@PuiGenerated
	String ROLE_DESCRIPTION_FIELD = "roledescription";
	@PuiGenerated
	String THUMBNAILS_COLUMN = "thumbnails";
	@PuiGenerated
	String THUMBNAILS_FIELD = "thumbnails";
	@PuiGenerated
	String URL_COLUMN = "url";
	@PuiGenerated
	String URL_FIELD = "url";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	Integer getId();

	@PuiGenerated
	void setId(Integer id);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getLanguage();

	@PuiGenerated
	void setLanguage(String language);

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
	String getRoledescription();

	@PuiGenerated
	void setRoledescription(String roledescription);

	@PuiGenerated
	String getThumbnails();

	@PuiGenerated
	void setThumbnails(String thumbnails);

	@PuiGenerated
	String getUrl();

	@PuiGenerated
	void setUrl(String url);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);

	Map<String, Object> getExtraData();

	void setExtraData(Map<String, Object> extraData);

}
