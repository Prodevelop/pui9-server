package es.prodevelop.pui9.utils;

import java.lang.reflect.Field;
import java.time.Instant;

import org.apache.commons.lang3.reflect.FieldUtils;

import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Automatically fills the mini audit values of a registry (insert and update)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMiniAudit {

	/**
	 * The default name for the user attribute that inserts the registry:
	 * <b>usralta</b>
	 */
	public static final String USR_ALTA = "usralta";

	/**
	 * The default name for the date attribute when the registry is inserted:
	 * <b>fecalta</b>
	 */
	public static final String FEC_ALTA = "fecalta";

	/**
	 * The default name for the user attribute that modifies the registry:
	 * <b>usrmodic</b>
	 */
	public static final String USR_MODIF = "usrmodif";

	/**
	 * The default name for the date attribute when the registry is modified:
	 * <b>fecmodif</b>
	 */
	public static final String FEC_MODIF = "fecmodif";

	/**
	 * Fill the information for a modification in the given DTO. Using by default
	 * the session of the user that fired the request. Using the default field
	 * names: {@link PuiMiniAudit#USR_MODIF} and {@link PuiMiniAudit#FEC_MODIF} <br>
	 * <br>
	 * Also keeps the information when the registry was inserted:
	 * {@link PuiMiniAudit#USR_ALTA} and {@link PuiMiniAudit#FEC_ALTA}
	 * 
	 * @param oldDto
	 * @param dto
	 */
	public static void fillModifyRegistry(IDto oldDto, IDto dto) {
		if (PuiUserSession.getCurrentSession() != null) {
			fillModifyRegistry(dto, PuiUserSession.getCurrentSession().getUsername());
		}

		setValue(dto, USR_ALTA, getValue(oldDto, USR_ALTA));
		setValue(dto, FEC_ALTA, getValue(oldDto, FEC_ALTA));
	}

	/**
	 * Fill the information for a modification in the given DTO. Using the provided
	 * date and user fields
	 * 
	 * @param dto       The DTO to set the insertion information
	 * @param userField The user attribute
	 * @param dateField The date attribute
	 */
	public static void fillModifyRegistry(IDto dto, String userField, String dateField) {
		String usr = PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getUsername()
				: null;
		fillModifyRegistry(dto, userField, dateField, usr);
	}

	/**
	 * Fill the information for a modification in the given DTO. Using by default
	 * the session of the user that fired the request. Using the default field
	 * names: {@link PuiMiniAudit#USR_MODIF} and {@link PuiMiniAudit#FEC_MODIF}
	 * 
	 * @param dto The DTO to set the modify information
	 */
	public static void fillModifyRegistry(IDto dto) {
		if (PuiUserSession.getCurrentSession() != null) {
			fillModifyRegistry(dto, PuiUserSession.getCurrentSession().getUsername());
		}
	}

	/**
	 * Fill the information for a modification in the given DTO. Using the provided
	 * user. Using the default field names: {@link PuiMiniAudit#USR_MODIF} and
	 * {@link PuiMiniAudit#FEC_MODIF}
	 * 
	 * @param dto      The DTO to set the modify information
	 * @param usrValue The user to be set
	 */
	public static void fillModifyRegistry(IDto dto, String usrValue) {
		fillModifyRegistry(dto, USR_MODIF, FEC_MODIF, usrValue);
	}

	/**
	 * Fill the information for a modification in the given DTO. Using the provided
	 * user. Using the provided date and user fields
	 * 
	 * @param dto       The DTO to set the modify information
	 * @param userField The user attribute
	 * @param dateField The date attribute
	 * @param usrValue  The user to be set
	 */
	public static void fillModifyRegistry(IDto dto, String userField, String dateField, String usrValue) {
		if (usrValue == null) {
			return;
		}

		setValue(dto, dateField, Instant.now());
		setValue(dto, userField, usrValue);
	}

	/**
	 * Fill the information for an insertion of the given DTO. Using by default the
	 * session of the user that fired the request. Using the default field names:
	 * {@link PuiMiniAudit#USR_ALTA} and {@link PuiMiniAudit#FEC_ALTA}
	 * 
	 * @param dto The DTO to set the insertion information
	 */
	public static void fillNewRegistry(IDto dto) {
		if (PuiUserSession.getCurrentSession() != null) {
			fillNewRegistry(dto, PuiUserSession.getCurrentSession().getUsername());
		}
	}

	/**
	 * Fill the information for an insertion of the given DTO. Using the provided
	 * date and user fields
	 * 
	 * @param dto       The DTO to set the insertion information
	 * @param userField The user attribute
	 * @param dateField The date attribute
	 */
	public static void fillNewRegistry(IDto dto, String userField, String dateField) {
		String usr = PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getUsername()
				: null;
		fillModifyRegistry(dto, userField, dateField, usr);
	}

	/**
	 * Fill the information for an insertion of the given DTO. Using the provided
	 * user. Using the default field names: {@link PuiMiniAudit#USR_ALTA} and
	 * {@link PuiMiniAudit#FEC_ALTA}
	 * 
	 * @param dto      The DTO to set the insertion information
	 * @param usrValue The user to be set
	 */
	public static void fillNewRegistry(IDto dto, String usrValue) {
		fillNewRegistry(dto, USR_ALTA, FEC_ALTA, usrValue);
	}

	/**
	 * Fill the information for an insertion of the given DTO. Using the provided
	 * user. Using the provided date and user fields
	 * 
	 * @param dto       The DTO to set the insertion information
	 * @param userField The user attribute
	 * @param dateField The date attribute
	 * @param usrValue  The user to be set
	 */
	public static void fillNewRegistry(IDto dto, String userField, String dateField, String usrValue) {
		if (usrValue == null) {
			return;
		}

		setValue(dto, dateField, Instant.now());
		setValue(dto, userField, usrValue);
	}

	/**
	 * Get the value for the given field name on the given DTO
	 * 
	 * @param dto       The DTO to be modified
	 * @param fieldName The field name to be modified
	 * @return The value of the field
	 */
	private static Object getValue(IDto dto, String fieldName) {
		try {
			Field field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), fieldName);
			if (field != null) {
				return FieldUtils.readField(field, dto, true);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Set the given value in the given field name on the given DTO
	 * 
	 * @param dto       The DTO to be modified
	 * @param fieldName The field name to be modified
	 * @param value     The value to be set
	 */
	private static void setValue(IDto dto, String fieldName, Object value) {
		try {
			Field field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), fieldName);
			if (field != null) {
				FieldUtils.writeField(field, dto, value, true);
			}
		} catch (Exception e) {
			// do nothing
		}
	}

}
