package es.prodevelop.pui9.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.exceptions.PuiDaoAttributeLengthException;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoDataAccessException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoDuplicatedException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnUpdateException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNotExistsException;
import es.prodevelop.pui9.exceptions.PuiDaoNullParametersException;
import es.prodevelop.pui9.exceptions.PuiDaoSumException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * DAO component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiDaoResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();
		messages.put(PuiDaoAttributeLengthException.CODE, getAttributeLengthMessage_101());
		messages.put(PuiDaoCountException.CODE, getCountMessage_102());
		messages.put(PuiDaoDataAccessException.CODE, getDataAccessMessage_103());
		messages.put(PuiDaoDuplicatedException.CODE, getDuplicatedMessage_104());
		messages.put(PuiDaoFindException.CODE, getFindErrorMessage_105());
		messages.put(PuiDaoIntegrityOnDeleteException.CODE, getIntegrityOnDeleteMessage_106());
		messages.put(PuiDaoIntegrityOnInsertException.CODE, getIntegrityOnInsertMessage_107());
		messages.put(PuiDaoIntegrityOnUpdateException.CODE, getIntegrityOnUpdateMessage_108());
		messages.put(PuiDaoListException.CODE, getListMessage_109());
		messages.put(PuiDaoNullParametersException.CODE, getNullParametersMessage_110());
		messages.put(PuiDaoNoNumericColumnException.CODE, getNoNumericExceptionMessage_111());
		messages.put(PuiDaoNotExistsException.CODE, getNotExistsExceptionMessage_112());
		messages.put(PuiDaoSumException.CODE, getSumExceptionMessage_113());
		messages.put(PuiDaoInsertException.CODE, getInsertExceptionMessage_114());
		messages.put(PuiDaoUpdateException.CODE, getUpdateExceptionMessage_115());
		messages.put(PuiDaoDeleteException.CODE, getDeleteExceptionMessage_116());

		return messages;
	}

	protected abstract String getAttributeLengthMessage_101();

	protected abstract String getCountMessage_102();

	protected abstract String getDataAccessMessage_103();

	protected abstract String getDuplicatedMessage_104();

	protected abstract String getFindErrorMessage_105();

	protected abstract String getIntegrityOnDeleteMessage_106();

	protected abstract String getIntegrityOnInsertMessage_107();

	protected abstract String getIntegrityOnUpdateMessage_108();

	protected abstract String getListMessage_109();

	protected abstract String getNullParametersMessage_110();

	protected abstract String getNoNumericExceptionMessage_111();

	protected abstract String getNotExistsExceptionMessage_112();

	protected abstract String getSumExceptionMessage_113();

	protected abstract String getInsertExceptionMessage_114();

	protected abstract String getUpdateExceptionMessage_115();

	protected abstract String getDeleteExceptionMessage_116();

}
