package es.prodevelop.pui9.exceptions;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.Instant;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * Generic exception for PUI applications. Subclasses of this exceptions are
 * teated specially and may provide a unique CODE and status response for the
 * client requests
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiException extends Exception {

	private static final long serialVersionUID = 1L;
	public static final Integer DEFAULT_INTERNAL_CODE = -1;

	/**
	 * Look for an Exception not belonging to PuiException
	 * 
	 * @param cause The original cause
	 * @return The non Pui Exception
	 */
	private static Throwable lookForNonPuiException(Throwable cause) {
		Throwable e = cause;
		while (e instanceof PuiException) {
			e = e.getCause();
		}
		return e != null ? e : cause;
	}

	/**
	 * Build the message for the exception
	 * 
	 * @param cause      The original cause (could be null)
	 * @param message    The message (could be null)
	 * @param parameters The parameters of the messgae (could be null or empty)
	 * @return The message for the exception
	 */
	private static String getExceptionMessage(Throwable cause, String message, Object... parameters) {
		String msg;
		if (!StringUtils.isEmpty(message)) {
			msg = message;
		} else if (cause != null) {
			msg = cause.getMessage();
		} else {
			msg = "";
		}

		if (!ObjectUtils.isEmpty(parameters)) {
			msg = MessageFormat.format(msg, parameters);
		}

		return msg;
	}

	public static PuiException fromTransferObject(PuiExceptionDto dto) {
		PuiException ex = null;
		if (!StringUtils.isEmpty(dto.getErrorClassName())) {
			try {
				ex = (PuiException) Class.forName(dto.getErrorClassName()).newInstance();
				Field f = Throwable.class.getDeclaredField("detailMessage");
				f.setAccessible(true);
				f.set(ex, dto.getMessage());
			} catch (Exception e) {
				ex = new PuiException(dto.getMessage());
			}
		} else {
			ex = new PuiException(dto.getMessage());
		}

		try {
			ex.datetime = dto.getDatetime();
			ex.internalCode = dto.getInternalCode();
			ex.className = dto.getClassName();
			ex.methodName = null;
		} catch (Exception e) {
			// do nothing
		}
		return ex;
	}

	private int internalCode = DEFAULT_INTERNAL_CODE;
	private String className = "";
	private String methodName = "";
	private Instant datetime;
	private int status = 500; // INTERNAL_SERVER_ERROR
	private boolean shouldLog = true;

	/**
	 * Creates an Exception with the given message
	 * 
	 * @param message The message of the Exception
	 */
	public PuiException(String message) {
		this(null, null, message);
	}

	/**
	 * Creates an Exception with the given root cause
	 * 
	 * @param cause The main cause of the Exception
	 */
	public PuiException(Throwable cause) {
		this(cause, null, null);
	}

	/**
	 * Creates an Exception with the given root cause and the provided message
	 * (without parameters)
	 * 
	 * @param cause   The main cause of the Exception
	 * @param message The message of the Exception
	 */
	public PuiException(Throwable cause, String message) {
		this(cause, null, message);
	}

	/**
	 * Creates an Exception with the given root cause, the given code and provided
	 * message (with parameters)
	 * 
	 * @param cause        the main cause of the Exception
	 * @param internalCode The internal code of the Exception
	 * @param message      The message of the Exception
	 * @param parameters   The parametters of the message
	 */
	public PuiException(Throwable cause, Integer internalCode, String message, Object... parameters) {
		super(getExceptionMessage(cause, message, parameters), lookForNonPuiException(cause));

		if (cause instanceof PuiException) {
			this.internalCode = ((PuiException) cause).getInternalCode();
		} else {
			this.internalCode = internalCode != null ? internalCode : DEFAULT_INTERNAL_CODE;
		}

		datetime = Instant.now();
		setStackData();
	}

	/**
	 * Retrieve the last class and method that fired the exception from the Stack
	 * trace
	 */
	private void setStackData() {
		for (StackTraceElement ste : getStackTrace()) {
			if (ste.getClassName().contains(Thread.class.getSimpleName())) {
				continue;
			}
			if (ste.getClassName().endsWith(Exception.class.getSimpleName())) {
				continue;
			}

			className = ste.getClassName();
			methodName = ste.getMethodName();
			break;
		}
	}

	/**
	 * Get the internal unique exception code
	 * 
	 * @return The unique exception internal code
	 */
	public int getInternalCode() {
		return internalCode;
	}

	public String getClassName() {
		return className;
	}

	/**
	 * Get the status code for this Exception
	 * 
	 * @return The status code
	 */
	public int getStatusResponse() {
		return status;
	}

	/**
	 * Set the status code for this Exception
	 * 
	 * @param status The status code
	 */
	public void setStatusResponse(int status) {
		this.status = status;
	}

	/**
	 * If this Exception should be logged or not
	 * 
	 * @return True if should be logged; false if not
	 */
	public boolean shouldLog() {
		return shouldLog;
	}

	/**
	 * Set if this exception should be logged or not
	 * 
	 * @param shouldLog True or False
	 */
	public void setShouldLog(boolean shouldLog) {
		this.shouldLog = shouldLog;
	}

	/**
	 * Convert the PuiException into a {@link PuiExceptionDto} object (valid to send
	 * to the clients)
	 * 
	 * @return The Exception in DTO format
	 */
	public PuiExceptionDto asTransferObject() {
		PuiExceptionDto dto = new PuiExceptionDto();
		dto.setInternalCode(internalCode);
		dto.setStatusCode(getStatusResponse());
		dto.setMessage(getMessage());
		dto.setDatetime(datetime);
		if (getCause() != null) {
			dto.setDetailedMessage(getCause().getMessage());
		}

		return dto;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n#####################################################");
		sb.append("\nDate: " + PuiDateUtil.temporalAccessorToString(datetime));
		sb.append("\nInternalCode: " + internalCode);
		sb.append("\nClass: " + className);
		sb.append("\nMethod: " + methodName);
		sb.append("\nMessage: " + getMessage());
		if (getCause() != null) {
			sb.append("\nOriginalMessage: " + getCause().getMessage());
		}
		sb.append("\n#####################################################\n");

		return sb.toString();
	}

}
