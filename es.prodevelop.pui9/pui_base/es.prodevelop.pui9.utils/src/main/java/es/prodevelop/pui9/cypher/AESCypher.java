package es.prodevelop.pui9.cypher;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ObjectUtils;

/**
 * This class offers methods to encode and decore a text using the AES cypher
 * algorithm.<br>
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class AESCypher {

	private static String algorithm = "AES/ECB/PKCS5Padding";

	/**
	 * Encrypt the given plain text using the given secret key
	 * 
	 * @param plainText The text in plain
	 * @param secret    The secret key
	 * @return The text encrypted
	 */
	public static String encrypt(final String plainText, final String secret) {
		if (ObjectUtils.isEmpty(plainText)) {
			return plainText;
		}

		try {
			SecretKeySpec secretKey = buildKey(secret);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder()
					.encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8.name())));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Decrypt the given encrypted text using the given secret key
	 * 
	 * @param cipherText The encrypted text
	 * @param secret     The secret key
	 * @return The text in plain
	 */
	public static String decrypt(final String cipherText, final String secret) {
		if (ObjectUtils.isEmpty(cipherText)) {
			return cipherText;
		}

		try {
			SecretKeySpec secretKey = buildKey(secret);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private static SecretKeySpec buildKey(final String secret) {
		try {
			byte[] key = secret.getBytes(StandardCharsets.UTF_8.name());
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 32);
			return new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private AESCypher() {
	}

}
