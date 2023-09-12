package es.prodevelop.pui9.login.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.cypher.AESCypher;

@Component
public class PuiLdapSpringSecurityContextSource extends LdapContextSource {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private String aesSecret;

	private boolean initialized;

	@Override
	public void afterPropertiesSet() {
		String url = variableService.getVariable(PuiVariableValues.LDAP_URL.name());
		String domain = variableService.getVariable(PuiVariableValues.LDAP_DOMAIN.name());
		String user = variableService.getVariable(PuiVariableValues.LDAP_USER.name());
		String password = variableService.getVariable(PuiVariableValues.LDAP_PASSWORD.name());
		password = AESCypher.decrypt(password, aesSecret);

		if (ObjectUtils.isEmpty(url) || ObjectUtils.isEmpty(domain) || ObjectUtils.isEmpty(user)
				|| ObjectUtils.isEmpty(password)) {
			initialized = false;
			return;
		}

		String providerUrl = buildProviderUrl(url, domainToDC(domain));
		configureProviderUrl(providerUrl);

		setUserDn(user);
		setPassword(password);

		super.afterPropertiesSet();

		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	private String domainToDC(String domainName) {
		StringBuilder dcs = new StringBuilder();
		List<String> subdomains = Arrays.asList(domainName.split("\\."));
		for (Iterator<String> it = subdomains.iterator(); it.hasNext();) {
			String subdomain = it.next();
			if (ObjectUtils.isEmpty(subdomain)) {
				continue;
			}
			dcs.append("DC=");
			dcs.append(subdomain);
			if (it.hasNext()) {
				dcs.append(",");
			}
		}
		return dcs.toString();
	}

	private String buildProviderUrl(String url, String baseDn) {
		if (ObjectUtils.isEmpty(url)) {
			return null;
		}

		StringBuilder providerUrl = new StringBuilder();

		providerUrl.append(url);
		if (!url.endsWith("/")) {
			providerUrl.append("/");
		}
		providerUrl.append(baseDn);
		providerUrl.append(" ");

		return providerUrl.toString();
	}

	private void configureProviderUrl(String providerUrl) {
		StringTokenizer st = new StringTokenizer(providerUrl);
		ArrayList<String> urls = new ArrayList<>();
		String rootDn = null;

		// Work out rootDn from the first URL and check that the other URLs (if any)
		// match
		while (st.hasMoreTokens()) {
			String url = st.nextToken();
			String urlRootDn = LdapUtils.parseRootDnFromUrl(url);

			urls.add(url.substring(0, url.lastIndexOf(urlRootDn)));

			if (rootDn == null) {
				rootDn = urlRootDn;
			} else if (!rootDn.equals(urlRootDn)) {
				throw new IllegalArgumentException("Root DNs must be the same when using multiple URLs");
			}
		}

		setUrls(urls.toArray(new String[0]));
		setBase(rootDn);
		setPooled(true);
		setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy() {
			@Override
			public void setupEnvironment(Hashtable<String, Object> env, String dn, String password) {
				super.setupEnvironment(env, dn, password);
				// Remove the pooling flag unless we are authenticating as the 'manager'
				// user.
				if (!getUserDn().equals(dn) && env.containsKey(SUN_LDAP_POOLING_FLAG)) {
					env.remove(SUN_LDAP_POOLING_FLAG);
				}
			}
		});
	}

}
