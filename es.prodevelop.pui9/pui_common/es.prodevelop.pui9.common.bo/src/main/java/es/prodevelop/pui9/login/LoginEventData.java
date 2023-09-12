package es.prodevelop.pui9.login;

import java.io.Serializable;

/**
 * This class represents the data for Login/Logout of a user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class LoginEventData implements Serializable {

	private static final long serialVersionUID = 1L;

	public static LoginEventData success(PuiUserSession puiUserSession) {
		return new LoginEventData(puiUserSession);
	}

	public static LoginEventData error(String usr, String ip, String client, String error) {
		return new LoginEventData(usr, ip, client, error);
	}

	private PuiUserSession puiUserSession;
	private String usr;
	private String ip;
	private String client;
	private String error;

	private LoginEventData(PuiUserSession puiUserSession) {
		this.puiUserSession = puiUserSession;
		this.usr = puiUserSession.getUsr();
		this.ip = puiUserSession.getIp();
		this.client = puiUserSession.getClient();
		this.error = null;
	}

	private LoginEventData(String usr, String ip, String client, String error) {
		this.puiUserSession = null;
		this.usr = usr;
		this.ip = ip;
		this.client = client;
		this.error = error;
	}

	public PuiUserSession getPuiUserSession() {
		return puiUserSession;
	}

	public String getUsr() {
		return usr;
	}

	public String getIp() {
		return ip;
	}

	public String getClient() {
		return client;
	}

	public String getError() {
		return error;
	}

}