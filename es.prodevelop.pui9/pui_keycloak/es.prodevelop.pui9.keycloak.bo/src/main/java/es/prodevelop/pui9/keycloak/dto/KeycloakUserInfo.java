package es.prodevelop.pui9.keycloak.dto;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * User info for Keycloak web services
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class KeycloakUserInfo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	public static KeycloakUserInfo builder() {
		return new KeycloakUserInfo();
	}

	private String usr;
	private String firstname;
	private String lastname;
	private String email;

	public KeycloakUserInfo withUser(String usr) {
		this.usr = usr;
		return this;
	}

	public KeycloakUserInfo withFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	public KeycloakUserInfo withLastname(String lastname) {
		this.lastname = lastname;
		return this;
	}

	public KeycloakUserInfo withEmail(String email) {
		this.email = email;
		return this;
	}

	public String getUsr() {
		return usr;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

}