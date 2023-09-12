package es.prodevelop.pui9.keycloak.dto;

import es.prodevelop.pui9.login.LoginData;

/**
 * Login Data for Keycloak
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class KeycloakLoginData extends LoginData {

	public static KeycloakLoginData builder() {
		return new KeycloakLoginData();
	}

	private String jwt;

	public KeycloakLoginData withJwt(String jwt) {
		this.jwt = jwt;
		return this;
	}

	public String getJwt() {
		return jwt;
	}

}