package es.prodevelop.pui9.login;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class is a micro representation of a session. All the properties here
 * exists in {@link PuiUserSession}, that is the main class of a PUI Session.
 * This class is returned to the client when log in
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiUserInfo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	public static PuiUserInfo forUser(String usr) {
		return new PuiUserInfo(usr);
	}

	private String usr;
	private String name;
	private String language;
	private String email;
	private String dateformat;
	private String jwt;
	private String uuid;
	private Instant lastLoginTime;
	private String lastLoginIp;
	private PasswordValidity passwordValidity;
	private Boolean use2fa = false;
	private Set<String> profiles = new LinkedHashSet<>();
	private Set<String> functionalities = new LinkedHashSet<>();
	private Map<String, Object> properties = new LinkedHashMap<>();

	private PuiUserInfo(String usr) {
		this.usr = usr;
	}

	public PuiUserInfo withName(String name) {
		this.name = name;
		return this;
	}

	public PuiUserInfo withLanguage(String language) {
		this.language = language;
		return this;
	}

	public PuiUserInfo withEmail(String email) {
		this.email = email;
		return this;
	}

	public PuiUserInfo withDateFormat(String dateformat) {
		this.dateformat = dateformat;
		return this;
	}

	public PuiUserInfo withJwt(String jwt) {
		this.jwt = jwt;
		return this;
	}

	public PuiUserInfo withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public PuiUserInfo withLastLoginTime(Instant lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
		return this;
	}

	public PuiUserInfo withLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
		return this;
	}

	public PuiUserInfo with2fa(Boolean use2fa) {
		this.use2fa = use2fa;
		return this;
	}

	public PuiUserInfo withPasswordValidity(PasswordValidity passwordValidity) {
		this.passwordValidity = passwordValidity;
		return this;
	}

	public PuiUserInfo withProfiles(Set<String> profiles) {
		this.profiles = profiles;
		return this;
	}

	public PuiUserInfo withFunctionalities(Set<String> functionalities) {
		this.functionalities = functionalities;
		return this;
	}

	public String getUsr() {
		return usr;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getEmail() {
		return email;
	}

	public String getDateformat() {
		return dateformat;
	}

	public String getJwt() {
		return jwt;
	}

	public String getUuid() {
		return uuid;
	}

	public Instant getLastLoginTime() {
		return lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public PasswordValidity getPasswordValidity() {
		return passwordValidity;
	}

	public Boolean is2fa() {
		return use2fa;
	}

	public Set<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<String> profiles) {
		this.profiles = profiles;
	}

	public Set<String> getFunctionalities() {
		return functionalities;
	}

	public void addProperty(String property, Object value) {
		properties.put(property, value);
	}

}
