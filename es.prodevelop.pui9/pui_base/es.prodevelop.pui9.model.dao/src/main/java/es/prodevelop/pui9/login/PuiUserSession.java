package es.prodevelop.pui9.login;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.enums.Pui9KnownClients;
import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.ModifySessionPropertyEvent;
import es.prodevelop.pui9.lang.LanguageThreadLocal;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class is a representation of a session. Contains the necessary
 * information for the session initialized by the user in the application.<br>
 * <br>
 * This class inherits from {@link UserDetails} and {@link CredentialsContainer}
 * from Spring Security to provide necessary methods for managing the sessions
 * automatically.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiUserSession implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	/**
	 * Get the current session of the user that is executing the current request
	 * 
	 * @return The current user session
	 */
	public static PuiUserSession getCurrentSession() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				? (PuiUserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
				: null;
	}

	/**
	 * Get the language for the current session
	 * 
	 * @return The language used in the request of the user
	 */
	public static PuiLanguage getSessionLanguage() {
		PuiLanguage sessionLang = getCurrentSession() != null ? getCurrentSession().getLanguage() : null;
		PuiLanguage threadLang = LanguageThreadLocal.getSingleton().getData();
		PuiLanguage dbDefaultLang = PuiLanguageUtils.getDefaultLanguage();

		if (sessionLang != null) {
			return !Objects.equals(sessionLang, threadLang) ? sessionLang : threadLang;
		} else if (threadLang != null) {
			return threadLang;
		} else if (dbDefaultLang != null) {
			return dbDefaultLang;
		} else {
			return PuiLanguage.DEFAULT_LANG;
		}
	}

	/**
	 * Create a new session for the given user. Aft4er creating it, should use with*
	 * methods to complete the session info
	 * 
	 * @param usr
	 * @return
	 */
	public static PuiUserSession createNew(String usr) {
		return new PuiUserSession().withUsr(usr);
	}

	private String usr;
	private String uuid;
	private transient String password;
	private String name;
	private PuiLanguage language;
	private String email;
	private String jwt;
	private Instant creation;
	private Instant expiration;
	private Set<String> profiles = new LinkedHashSet<>();
	private Boolean persistent = false;
	private Boolean cached = false;
	private String ip;
	private String userAgent;
	private String client;
	private String dateformat;
	private Instant lastLoginTime;
	private String lastLoginIp;
	private PasswordValidity passwordValidity;
	private Boolean use2fa = false;
	private String secret2fa;
	private Integer attemps2fa = 0;
	private Map<String, Object> properties = new LinkedHashMap<>();
	@Schema(hidden = true)
	private transient List<PuiUserSessionProfile> fullProfiles = new ArrayList<>();
	@Schema(hidden = true)
	private transient List<PuiUserSessionFunctionality> fullFunctionalities = new ArrayList<>();
	@Schema(hidden = true)
	private transient Set<String> functionalities = new LinkedHashSet<>();
	@Schema(hidden = true)
	private transient List<GrantedAuthority> authorities = new ArrayList<>();
	@Schema(hidden = true)
	private transient Boolean authenticated = false;
	@Schema(hidden = true)
	private transient Boolean disabled = false;
	@Schema(hidden = true)
	private transient Boolean accountExpired = false;
	@Schema(hidden = true)
	private transient Boolean accountLocked = false;
	@Schema(hidden = true)
	private transient Boolean credentialsExpired = false;
	@Schema(hidden = true)
	private transient ZoneId zoneId;
	@Schema(hidden = true)
	private transient Instant lastUse;

	private PuiUserSession() {
		this.cached = true;
	}

	public PuiUserSession withUsr(String usr) {
		this.usr = usr;
		return this;
	}

	public PuiUserSession withName(String name) {
		this.name = name;
		return this;
	}

	public PuiUserSession withPassword(String password) {
		this.password = password;
		return this;
	}

	public PuiUserSession withLanguage(PuiLanguage language) {
		this.language = language;
		return this;
	}

	public PuiUserSession withEmail(String email) {
		this.email = email;
		return this;
	}

	public PuiUserSession withDateFormat(String dateformat) {
		this.dateformat = dateformat;
		return this;
	}

	public PuiUserSession withDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	public PuiUserSession withAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
		return this;
	}

	public PuiUserSession withAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
		return this;
	}

	public PuiUserSession withCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
		return this;
	}

	public PuiUserSession withLastLoginTime(Instant lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
		return this;
	}

	public PuiUserSession withLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
		return this;
	}

	public PuiUserSession withPasswordValidity(PasswordValidity passwordValidity) {
		this.passwordValidity = passwordValidity;
		return this;
	}

	public PuiUserSession withUse2fa(Boolean use2fa) {
		this.use2fa = use2fa;
		return this;
	}

	public PuiUserSession withSecret2fa(String secret2fa) {
		this.secret2fa = secret2fa;
		return this;
	}

	public PuiUserSession withAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
		return this;
	}

	public PuiUserSession withCached(Boolean cached) {
		this.cached = cached;
		return this;
	}

	public PuiUserSession withCreation(Instant creation) {
		this.creation = creation;
		return this;
	}

	public PuiUserSession withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public PuiUserSession withJwt(String jwt) {
		this.jwt = jwt;
		return this;
	}

	public PuiUserSession withPersistent(Boolean persistent) {
		this.persistent = persistent;
		return this;
	}

	public PuiUserSession withExpiration(Instant expiration) {
		this.expiration = expiration;
		return this;
	}

	public PuiUserSession withIp(String ip) {
		this.ip = ip;
		return this;
	}

	public PuiUserSession withUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public PuiUserSession withClient(String client) {
		this.client = client;
		return this;
	}

	public PuiUserSession withZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	public PuiUserSession withLastUse(Instant lastUse) {
		this.lastUse = lastUse;
		return this;
	}

	public PuiUserSession withProperty(String property, Object value) {
		Object oldValue = properties.put(property, value);
		PuiApplicationContext.getInstance().getBean(PuiEventLauncher.class)
				.fireSync(new ModifySessionPropertyEvent(property, oldValue, value));
		return this;
	}

	public String getUsr() {
		return usr;
	}

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Boolean isPersistent() {
		return persistent;
	}

	public PuiLanguage getLanguage() {
		return language;
	}

	public String getEmail() {
		return email;
	}

	public String getDateformat() {
		return dateformat;
	}

	public Boolean isDisabled() {
		return disabled;
	}

	public List<PuiUserSessionProfile> getFullProfiles() {
		return fullProfiles;
	}

	public void setFullProfiles(List<PuiUserSessionProfile> profiles) {
		Collections.sort(profiles);
		this.fullProfiles.clear();
		this.fullProfiles.addAll(profiles);
		setProfiles(profiles);
	}

	public List<PuiUserSessionFunctionality> getFullFunctionalities() {
		return fullFunctionalities;
	}

	public void setFullFunctionalities(List<PuiUserSessionFunctionality> functionalities) {
		Collections.sort(functionalities);
		this.fullFunctionalities.clear();
		this.fullFunctionalities.addAll(functionalities);
		setFunctionalities(functionalities);
	}

	public Set<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<PuiUserSessionProfile> profiles) {
		Collections.sort(profiles);
		this.profiles.clear();
		profiles.forEach(p -> this.profiles.add(p.getProfile()));
	}

	public Set<String> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<PuiUserSessionFunctionality> functionalities) {
		Collections.sort(functionalities);
		this.functionalities.clear();
		this.authorities.clear();
		functionalities.forEach(f -> {
			if (!this.functionalities.contains(f.getFunctionality())) {
				this.functionalities.add(f.getFunctionality());
				this.authorities.add(new SimpleGrantedAuthority(f.getFunctionality()));
			}
		});
	}

	public String getJwt() {
		return jwt;
	}

	public Instant getCreation() {
		return creation;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public String getIp() {
		return ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getClient() {
		return client;
	}

	public ZoneId getZoneId() {
		return zoneId != null ? zoneId : ZoneId.systemDefault();
	}

	public Instant getLastUse() {
		return lastUse;
	}

	@Override
	public String getUsername() {
		return getUsr();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	@Override
	public boolean isEnabled() {
		return !disabled;
	}

	@Override
	public void eraseCredentials() {
		password = null;
		authorities.clear();
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

	public Boolean isUse2fa() {
		return use2fa;
	}

	public String getSecret2fa() {
		return secret2fa;
	}

	public void incrementAttempts2fa() {
		attemps2fa++;
	}

	public Integer getAttemps2fa() {
		return attemps2fa;
	}

	public Boolean isAuthenticated() {
		return authenticated;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String property) {
		return (T) properties.get(property);
	}

	public Boolean isCached() {
		return cached;
	}

	public Boolean isPui9Client() {
		return Objects.equals(client, Pui9KnownClients.PUI9_CLIENT.name());
	}

	public PuiUserInfo asPuiUserInfo() {
		PuiUserInfo userInfo = PuiUserInfo.forUser(getUsr()).withName(getName())
				.withLanguage(getLanguage().getIsocode()).withEmail(getEmail()).withDateFormat(getDateformat())
				.withJwt(PuiConstants.BEARER_PREFIX + getJwt()).withUuid(getUuid())
				.withLastLoginTime(getLastLoginTime()).withLastLoginIp(getLastLoginIp())
				.withPasswordValidity(getPasswordValidity()).withProfiles(getProfiles())
				.withFunctionalities(getFunctionalities()).with2fa(isUse2fa());
		getProperties().forEach(userInfo::addProperty);

		return userInfo;
	}

	public static class PuiUserSessionProfile implements Comparable<PuiUserSessionProfile> {
		private String profile;

		public static PuiUserSessionProfile of(String profile) {
			return new PuiUserSessionProfile(profile);
		}

		private PuiUserSessionProfile(String profile) {
			this.profile = profile;
		}

		public String getProfile() {
			return profile;
		}

		@Override
		public String toString() {
			return profile;
		}

		@Override
		public int compareTo(PuiUserSessionProfile o) {
			return profile.compareTo(o.getProfile());
		}
	}

	public static class PuiUserSessionFunctionality implements Comparable<PuiUserSessionFunctionality> {
		private String profile;
		private String functionality;

		public static PuiUserSessionFunctionality of(String functionality) {
			return new PuiUserSessionFunctionality(functionality);
		}

		public static PuiUserSessionFunctionality of(String profile, String functionality) {
			return new PuiUserSessionFunctionality(profile, functionality);
		}

		private PuiUserSessionFunctionality(String functionality) {
			this("NO_PROFILE_INFO", functionality);
		}

		private PuiUserSessionFunctionality(String profile, String functionality) {
			this.profile = profile;
			this.functionality = functionality;
		}

		public String getProfile() {
			return profile;
		}

		public String getFunctionality() {
			return functionality;
		}

		@Override
		public String toString() {
			return functionality + " (" + profile + ")";
		}

		@Override
		public int compareTo(PuiUserSessionFunctionality o) {
			return functionality.compareTo(o.getFunctionality());
		}
	}

}
