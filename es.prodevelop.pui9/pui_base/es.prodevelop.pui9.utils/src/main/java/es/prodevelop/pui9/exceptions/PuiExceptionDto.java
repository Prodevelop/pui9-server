package es.prodevelop.pui9.exceptions;

import java.time.Instant;

import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class is a representation of the Exception thrown by the application,
 * and will be sent to the client as response when an Exception is thrown in the
 * server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiExceptionDto implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private int internalCode;
	private int statusCode;
	private String className;
	private String errorClassName;
	private String url;
	private String message;
	private String detailedMessage;
	private Instant datetime;

	public int getInternalCode() {
		return internalCode;
	}

	public void setInternalCode(int internalCode) {
		this.internalCode = internalCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getErrorClassName() {
		return errorClassName;
	}

	public void setErrorClassName(String errorClassName) {
		this.errorClassName = errorClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

	public Instant getDatetime() {
		return datetime;
	}

	public void setDatetime(Instant datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return GsonSingleton.getSingleton().getGson().toJson(this);
	}

}
