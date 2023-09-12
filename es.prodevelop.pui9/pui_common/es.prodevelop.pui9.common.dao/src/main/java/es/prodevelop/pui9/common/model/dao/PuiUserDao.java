package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiUserDao extends AbstractTableDao<IPuiUserPk, IPuiUser> implements IPuiUserDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserPk.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByPassword(String password) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.PASSWORD_FIELD, password);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByLanguage(String language) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LANGUAGE_FIELD, language);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByEmail(String email) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.EMAIL_FIELD, email);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DISABLED_FIELD, disabled);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DISABLED_DATE_FIELD, disableddate);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DATEFORMAT_FIELD, dateformat);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByResetpasswordtoken(String resetpasswordtoken) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.RESET_PASSWORD_TOKEN_FIELD, resetpasswordtoken);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LAST_ACCESS_TIME_FIELD, lastaccesstime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LAST_ACCESS_IP_FIELD, lastaccessip);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByLastpasswordchange(java.time.Instant lastpasswordchange)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LAST_PASSWORD_CHANGE_FIELD, lastpasswordchange);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByLoginwrongattempts(Integer loginwrongattempts) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LOGIN_WRONG_ATTEMPTS_FIELD, loginwrongattempts);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByChangepasswordnextlogin(Integer changepasswordnextlogin)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.CHANGE_PASSWORD_NEXT_LOGIN_FIELD, changepasswordnextlogin);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findBySecret2fa(String secret2fa) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.SECRET_2FA_FIELD, secret2fa);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUser> findByResetpasswordtokendate(java.time.Instant resetpasswordtokendate)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.RESET_PASSWORD_TOKEN_DATE_FIELD, resetpasswordtokendate);
	}
}
