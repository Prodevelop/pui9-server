package es.prodevelop.pui9.openapi.utils;

public final class PuiOpenapiConstants {

	public static final String BASE_URL_DEFAULT = null;
	public static final String BASE_URL = "${openapi.baseUrl:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).BASE_URL_DEFAULT}}";

	public static final String INFO_TITLE_DEFAULT = "PUI9 Application API";
	public static final String INFO_TITLE = "${openapi.title:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_TITLE_DEFAULT}}";

	public static final String INFO_DESCRIPTION_DEFAULT = "You can test your API from this beautiful site";
	public static final String INFO_DESCRIPTION = "${openapi.description:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_DESCRIPTION_DEFAULT}}";

	public static final String INFO_VERSION_DEFAULT = "1.0.0";
	public static final String INFO_VERSION = "${openapi.version:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_VERSION_DEFAULT}}";

	public static final String INFO_TERMS_OF_SERVICE_URL_DEFAULT = "termsOfService.html";
	public static final String INFO_TERMS_OF_SERVICE_URL = "${openapi.termsOfServiceUrl:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_TERMS_OF_SERVICE_URL_DEFAULT}}";

	public static final String INFO_LICENSE_DEFAULT = "Apache 2.0";
	public static final String INFO_LICENSE = "${openapi.license:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_LICENSE_DEFAULT}}";

	public static final String INFO_LICENSE_URL_DEFAULT = "http://www.apache.org/licenses/LICENSE-2.0";
	public static final String INFO_LICENSE_URL = "${openapi.licenseUrl:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).INFO_LICENSE_URL_DEFAULT}}";

	public static final String CONTACT_NAME_DEFAULT = "Administrator";
	public static final String CONTACT_NAME = "${openapi.contact.name:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).CONTACT_NAME_DEFAULT}}";

	public static final String CONTACT_EMAIL_DEFAULT = "admin@email.com";
	public static final String CONTACT_EMAIL = "${openapi.contact.email:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).CONTACT_EMAIL_DEFAULT}}";

	public static final String CONTACT_URL_DEFAULT = "http://www.admin.com";
	public static final String CONTACT_URL = "${openapi.contact.url:#{T(es.prodevelop.pui9.openapi.utils.PuiOpenapiConstants).CONTACT_URL_DEFAULT}}";

	public PuiOpenapiConstants() {
	}
}
