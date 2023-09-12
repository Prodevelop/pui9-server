package es.prodevelop.pui9.login;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class to get the Password Encoders used in PUI
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiPasswordEncoders {

	/**
	 * Use bCrypt password encoder. This is the default password encoder used in PUI
	 */
	public static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

	/**
	 * A plain password encoder. Used in LDAP authentication
	 */
	public static final PasswordEncoder plainPasswordEncoder = new PasswordEncoder() {

		@Override
		public boolean matches(CharSequence rawPassword, String encodedPassword) {
			return Objects.equals(rawPassword, encodedPassword);
		}

		@Override
		public String encode(CharSequence rawPassword) {
			return String.valueOf(rawPassword);
		}
	};

	private PuiPasswordEncoders() {
	}

}
