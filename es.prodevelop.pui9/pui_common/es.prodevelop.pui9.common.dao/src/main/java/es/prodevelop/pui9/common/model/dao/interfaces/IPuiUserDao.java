package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiUserDao extends ITableDao<IPuiUserPk, IPuiUser> {
	@PuiGenerated
	java.util.List<IPuiUser> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByPassword(String password) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByLanguage(String language) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByEmail(String email) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByResetpasswordtoken(String resetpasswordtoken) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByLastpasswordchange(java.time.Instant lastpasswordchange) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByLoginwrongattempts(Integer loginwrongattempts) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByChangepasswordnextlogin(Integer changepasswordnextlogin) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findBySecret2fa(String secret2fa) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUser> findByResetpasswordtokendate(java.time.Instant resetpasswordtokendate)
			throws PuiDaoFindException;
}
