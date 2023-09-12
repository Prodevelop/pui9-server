package es.prodevelop.pui9.login;

import java.time.ZoneId;

import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.utils.PuiDateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class LoginData {

	@Schema(description = "The user (in plain)", requiredMode = RequiredMode.REQUIRED)
	private String usr;
	@Schema(description = "The password (in plain)", requiredMode = RequiredMode.REQUIRED)
	private String password;
	@Schema(description = "Persistent session", requiredMode = RequiredMode.NOT_REQUIRED, example = "false")
	private Boolean persistent = false;
	@Schema(description = "The client name", requiredMode = RequiredMode.NOT_REQUIRED, example = "OPENAPI_CLIENT")
	private String client;
	@Schema(hidden = true)
	private String timezone;
	@Schema(hidden = true)
	private String ip;
	@Schema(hidden = true)
	private String userAgent;
	@Schema(hidden = true)
	private MultiValueMap<String, String> headers;

	public LoginData withUsr(String usr) {
		this.usr = usr;
		return this;
	}

	public LoginData withPassword(String password) {
		this.password = password;
		return this;
	}

	public LoginData withPersistent(Boolean persistent) {
		this.persistent = persistent;
		return this;
	}

	public LoginData withIp(String ip) {
		this.ip = ip;
		return this;
	}

	public LoginData withUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public LoginData withTimezone(String timezone) {
		this.timezone = timezone;
		return this;
	}

	public LoginData withHeaders(MultiValueMap<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public LoginData withClient(String client) {
		this.client = client;
		return this;
	}

	public String getUsr() {
		return usr;
	}

	public String getPassword() {
		return password;
	}

	public Boolean isPersistent() {
		return persistent;
	}

	public String getIp() {
		return ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getTimezone() {
		return timezone;
	}

	public MultiValueMap<String, String> getHeaders() {
		return headers;
	}

	public String getClient() {
		return client;
	}

	@Schema(hidden = true)
	public ZoneId getJavaZoneId() {
		return !ObjectUtils.isEmpty(timezone) ? ZoneId.of(timezone) : PuiDateUtil.utcZone;
	}

}