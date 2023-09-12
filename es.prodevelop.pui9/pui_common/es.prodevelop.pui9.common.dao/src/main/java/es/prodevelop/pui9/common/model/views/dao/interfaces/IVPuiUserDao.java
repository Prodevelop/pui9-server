package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiUserDao extends IViewDao<IVPuiUser> {
	@PuiGenerated
	java.util.List<IVPuiUser> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByEmail(String email) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByLanguage(String language) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByLastpasswordchange(java.time.Instant lastpasswordchange) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByLoginwrongattempts(Integer loginwrongattempts) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiUser> findByChangepasswordnextlogin(Integer changepasswordnextlogin) throws PuiDaoFindException;
}
