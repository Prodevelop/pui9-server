package es.prodevelop.pui9.login;

import es.prodevelop.pui9.utils.IPuiObject;

public class TwoFactorAuthenticationData implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String label;
	private String issuer;
	private String secret;
	private String otpAuthUri;
	private String qrImageUri;
	private String generatorUri;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getOtpAuthUri() {
		return otpAuthUri;
	}

	public void setOtpAuthUri(String otpAuthUri) {
		this.otpAuthUri = otpAuthUri;
	}

	public String getQrImageUri() {
		return qrImageUri;
	}

	public void setQrImageUri(String qrImageUri) {
		this.qrImageUri = qrImageUri;
	}

	public String getGeneratorUri() {
		return generatorUri;
	}

	public void setGeneratorUri(String generatorUri) {
		this.generatorUri = generatorUri;
	}

}