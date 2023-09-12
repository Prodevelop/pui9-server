package es.prodevelop.pui9.eventlistener.event;

/**
 * Event for Changing the Password
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PasswordChangedEvent extends PuiEvent<String> {

	private static final long serialVersionUID = 1L;

	private String user;
	private String password;

	public PasswordChangedEvent(String user, String password) {
		super(user);
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
