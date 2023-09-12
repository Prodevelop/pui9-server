package es.prodevelop.pui9.keycloak.dto;

/**
 * Keycloak credentials to perform the login
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class KeycloakCredentials {

	private String usr;
	private String password;

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}