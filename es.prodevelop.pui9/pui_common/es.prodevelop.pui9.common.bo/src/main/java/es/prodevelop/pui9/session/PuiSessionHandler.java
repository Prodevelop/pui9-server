package es.prodevelop.pui9.session;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.common.base.Functions;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiSessionDao;
import es.prodevelop.pui9.common.model.dto.PuiSession;
import es.prodevelop.pui9.common.model.dto.PuiSessionPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSession;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSessionPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.cypher.AESCypher;
import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.SessionCreatedEvent;
import es.prodevelop.pui9.eventlistener.event.SessionDestroyedEvent;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.IPuiSessionContext;
import es.prodevelop.pui9.login.PuiUserDetailsService;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserNotAuthenticatedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * This component manages the session on PUI9 applications. It creates new
 * sessions, recreates old sessions, destroy sessions...<br>
 * <br>
 * It uses jjwt library to build a JWT token with the user information. This JWT
 * is also used as session token.<br>
 * <br>
 * To sign this this JWT token it uses an HMAC using SHA-512 algorithm, with a
 * private key. This private key is always the same, so it should be saved into
 * database. If you want to regenerate it, please execute the following code and
 * save the result:
 * 
 * <pre>
 * java.security.Key key = io.jsonwebtoken.security.Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
 * String secretString = io.jsonwebtoken.io.Encoders.BASE64.encode(key.getEncoded());
 * </pre>
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiSessionHandler implements IPuiSessionContext {

	private static final Integer SESSIONS_CACHE_CLEANER_TIMER_MINUTES = 5;
	private static final Integer SESSIONS_DATABASE_CLEANER_TIMER_MINUTES = 30;

	private static final String JWT_ISSUER = "PUI9_SERVER";
	private static final String JWT_CLAIM_PERSISTENT = "persistent";
	private static final String JWT_CLAIM_IP = "ip";
	private static final String JWT_CLAIM_TIMEZONE = "timezone";
	private static final String JWT_CLAIM_USER_AGENT = "useragent";
	private static final String JWT_CLAIM_CLIENT = "client";

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiUserService userService;

	@Autowired
	private PuiUserDetailsService userDetailsService;

	@Autowired
	private IPuiSessionDao puiSessionDao;

	@Autowired
	private PuiEventLauncher eventLauncher;

	@Autowired
	private String aesSecret;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	private Map<String, Authentication> sessionsCache = new LinkedHashMap<>();

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("SessionsCacheCleaner", true, 0,
				SESSIONS_CACHE_CLEANER_TIMER_MINUTES, TimeUnit.MINUTES, this::cleanCacheSessions);

		multiInstanceProcessBackExec.registerNewExecutor("SessionsDatabaseCleaner", 0,
				SESSIONS_DATABASE_CLEANER_TIMER_MINUTES, TimeUnit.MINUTES, this::cleanDatabaseSessions);
	}

	/**
	 * Insert a new session on database and keep it on cache
	 * 
	 * @param jwt  The session JWT token
	 * @param auth The authentication user session info
	 */
	public void add(String jwt, Authentication auth) {
		PuiUserSession cacheSession = (PuiUserSession) auth.getPrincipal();
		cacheSession.withLastUse(Instant.now());

		IPuiSession dbSession = new PuiSession();
		dbSession.setUuid(cacheSession.getUuid());
		dbSession.setUsr(cacheSession.getUsr());
		dbSession.setCreated(cacheSession.getCreation());
		dbSession.setExpiration(cacheSession.getExpiration());
		dbSession.setLastuse(cacheSession.getLastUse());
		dbSession.setPersistent(cacheSession.isPersistent() ? PuiConstants.TRUE_INT : PuiConstants.FALSE_INT);
		dbSession.setJwt(jwt);

		try {
			puiSessionDao.insert(dbSession);
			synchronized (sessionsCache) {
				sessionsCache.put(jwt, auth);
			}
		} catch (PuiDaoInsertException e) {
			// do nothing
		}
	}

	/**
	 * Get the session related with the given JWT token. <br>
	 * <ul>
	 * <li>It tries to get it from cache</li>
	 * <li>If it doesn't exist, tries to get it from database</li>
	 * <li>If the session if not found on database, and the application defines in
	 * the variables that sessions can be recreated, it tries to recreate it</li>
	 * <li>Finally it checks the expiration time</li>
	 * </ul>
	 * 
	 * @param jwt The session JWT token
	 * @return The authentication info
	 * @throws PuiServiceNoSessionException          If no session exists with the
	 *                                               given token, and cannot be
	 *                                               recreated
	 * @throws PuiServiceUserSessionTimeoutException If session timeout is reached
	 */
	public synchronized Authentication get(String jwt)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		Authentication auth;
		synchronized (sessionsCache) {
			auth = sessionsCache.get(jwt);
		}
		boolean recreated = false;
		if (auth == null) {
			// the session is not cached. Try to find it in the DDBB
			Jws<Claims> jwsClaims = getJwsClaims(jwt);

			IPuiSession puiSession;
			try {
				puiSession = puiSessionDao.findOne(new PuiSessionPk(jwsClaims.getPayload().getId()));
			} catch (PuiDaoFindException e) {
				puiSession = null;
			}

			Boolean recreateIfNotInDb = variableService.getVariable(Boolean.class,
					PuiVariableValues.SESSION_RECREATE_IF_NOT_IN_DB.name());
			if (recreateIfNotInDb == null) {
				recreateIfNotInDb = false;
			}
			if (puiSession == null) {
				if (!recreateIfNotInDb.booleanValue()) {
					// no session stored in the DDBB
					throw new PuiServiceNoSessionException();
				} else {
					auth = recreateSession(jwt, jwsClaims, null);
				}
			} else {
				// session exists in the DDBB, recreate and cache it
				auth = recreateSession(jwt, jwsClaims, puiSession);
			}

			synchronized (sessionsCache) {
				sessionsCache.put(jwt, auth);
			}
			recreated = true;
		}

		PuiUserSession session = (PuiUserSession) auth.getPrincipal();

		// check the expiration time of the session
		checkExpirationTime(session);

		if (recreated) {
			eventLauncher.fireSync(new SessionCreatedEvent(session, null));
		}

		return auth;
	}

	/**
	 * Removes the session with the given JWT token. It removes the session from
	 * cache and from database. Can return null if no session exists for given JWT
	 * token
	 * 
	 * @param jwt The JWT session token
	 * @return The authentication info if a session for the given token exists. Null
	 *         if no session exists
	 */
	public Authentication remove(String jwt) {
		Authentication auth;
		synchronized (sessionsCache) {
			auth = sessionsCache.remove(jwt);
		}
		String uuid = null;
		if (auth == null) {
			// it's a session cached in this server
			Jws<Claims> jwsClaims;
			try {
				jwsClaims = getJwsClaims(jwt);
			} catch (PuiServiceNoSessionException e2) {
				try {
					puiSessionDao.deleteWhere(FilterBuilder.newAndFilter().addEqualsExact(IPuiSession.JWT_COLUMN, jwt));
				} catch (PuiDaoDeleteException e1) {
					// do nothing
				}
				return null;
			}
			uuid = jwsClaims.getPayload().getId();
		} else {
			// it's a session maybe cached in other server, or an old session still not
			// cached
			uuid = ((PuiUserSession) auth.getPrincipal()).getUuid();
		}

		try {
			IPuiSessionPk pk = new PuiSessionPk(uuid);
			IPuiSession puiSession = puiSessionDao.findOne(pk);
			if (puiSession != null) {
				puiSessionDao.delete(pk);
			}

			eventLauncher.fireSync(new SessionDestroyedEvent(jwt, uuid,
					auth != null ? ((PuiUserSession) auth.getPrincipal()) : null, puiSession));
		} catch (PuiDaoDeleteException | PuiDaoFindException e) {
			// do nothing
		}

		return auth;
	}

	/**
	 * Get the user info object for the given JWT session token
	 * 
	 * @param jwt The JWT session token
	 * @return The associated user info session object
	 * @throws PuiServiceNoSessionException          If no authorization string is
	 *                                               provided
	 * @throws PuiServiceUserSessionTimeoutException If the given authorization
	 *                                               token is expired
	 */
	public PuiUserInfo getUserInfo(String jwt)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");

		Authentication auth = get(jwt);
		PuiUserSession pus = (PuiUserSession) auth.getPrincipal();
		return pus.asPuiUserInfo();
	}

	/**
	 * Get a list of all the current sessions in the system. Includes all the
	 * sessions in cache and all the sessions in database not cached
	 * 
	 * @return
	 */
	public List<PuiUserSession> getAllSessions() {
		List<PuiUserSession> sessions = new ArrayList<>();
		synchronized (sessionsCache) {
			sessions.addAll(sessionsCache.values().stream().map(auth -> (PuiUserSession) auth.getPrincipal())
					.collect(Collectors.toList()));
		}
		List<String> uuidList = sessions.stream().map(PuiUserSession::getUuid).collect(Collectors.toList());

		try {
			List<IPuiSession> storedSessions = puiSessionDao
					.findWhere(FilterBuilder.newAndFilter().addNotIn(IPuiSessionPk.UUID_COLUMN, uuidList));
			List<String> usrList = storedSessions.stream().map(IPuiSession::getUsr).distinct()
					.collect(Collectors.toList());

			List<IPuiUser> users = userService.getTableDao()
					.findWhere(FilterBuilder.newAndFilter().addIn(IPuiUserPk.USR_COLUMN, usrList));
			Map<String, IPuiUser> usersMap = new LinkedHashMap<>();
			users.forEach(u -> usersMap.put(u.getUsr(), u));

			storedSessions.forEach(dbSession -> {
				IPuiUser dbUser = usersMap.get(dbSession.getUsr());
				boolean credentialsExpired = !userService.checkPasswordValidity(dbUser).isValid();

				PuiUserSession pus = PuiUserSession.createNew(dbSession.getUsr()).withName(dbUser.getName())
						.withPassword(null).withLanguage(new PuiLanguage(dbUser.getLanguage()))
						.withEmail(dbUser.getEmail()).withDateFormat(dbUser.getDateformat())
						.withDisabled(dbUser.getDisabled().equals(PuiConstants.TRUE_INT)).withAccountExpired(false)
						.withCredentialsExpired(credentialsExpired).withLastLoginTime(dbUser.getLastaccesstime())
						.withLastLoginIp(dbUser.getLastaccessip()).withCached(false).withUuid(dbSession.getUuid())
						.withCreation(dbSession.getCreated()).withJwt(dbSession.getJwt());

				if (!dbSession.getPersistent().equals(PuiConstants.TRUE_INT) && getTimeLogout() != null) {
					pus.withExpiration(pus.getCreation().plus(getTimeLogout(), ChronoUnit.MINUTES));
				} else {
					pus.withExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusYears(100).toInstant());
				}

				Jws<Claims> jwsClaims;
				try {
					jwsClaims = getJwsClaims(dbSession.getJwt());
				} catch (PuiServiceNoSessionException e) {
					return;
				}

				if (jwsClaims.getPayload().containsKey(JWT_CLAIM_TIMEZONE)) {
					pus.withZoneId(ZoneId.of(jwsClaims.getPayload().get(JWT_CLAIM_TIMEZONE, String.class)));
				}
				if (jwsClaims.getPayload().containsKey(JWT_CLAIM_PERSISTENT)) {
					pus.withPersistent(jwsClaims.getPayload().get(JWT_CLAIM_PERSISTENT, Boolean.class));
				}
				if (jwsClaims.getPayload().containsKey(JWT_CLAIM_IP)) {
					pus.withIp(jwsClaims.getPayload().get(JWT_CLAIM_IP, String.class));
				}
				if (jwsClaims.getPayload().containsKey(JWT_CLAIM_USER_AGENT)) {
					pus.withUserAgent(jwsClaims.getPayload().get(JWT_CLAIM_USER_AGENT, String.class));
				}
				if (jwsClaims.getPayload().containsKey(JWT_CLAIM_CLIENT)) {
					pus.withClient(jwsClaims.getPayload().get(JWT_CLAIM_CLIENT, String.class));
				}

				sessions.add(pus);
			});
		} catch (PuiDaoFindException e) {
			// do nothing
		}

		return sessions;
	}

	/**
	 * Build the JWT token based on the logged user
	 * 
	 * @return The JWT token
	 */
	public void buildJwt(PuiUserSession userSession) {
		userSession.withUuid(UUID.randomUUID().toString());
		userSession.withCreation(Instant.now());
		if (!userSession.isPersistent() && getTimeLogout() != null) {
			userSession.withExpiration(userSession.getCreation().plus(getTimeLogout(), ChronoUnit.MINUTES));
		} else {
			userSession.withExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusYears(100).toInstant());
		}

		JwtBuilder builder = Jwts.builder();
		builder.issuer(JWT_ISSUER);
		builder.issuedAt(Date.from(userSession.getCreation()));
		builder.id(userSession.getUuid());
		builder.subject(userSession.getUsr());
		builder.claim(JWT_CLAIM_TIMEZONE, userSession.getZoneId().getId());
		builder.claim(JWT_CLAIM_PERSISTENT, userSession.isPersistent());
		builder.claim(JWT_CLAIM_IP, userSession.getIp());
		builder.claim(JWT_CLAIM_USER_AGENT, userSession.getUserAgent());
		builder.claim(JWT_CLAIM_CLIENT, userSession.getClient());

		builder.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecretJwt())));
		String jwt = builder.compact();
		userSession.withJwt(jwt);
	}

	@Override
	public void setContextSession(String jwt) throws PuiServiceNoSessionException,
			PuiServiceUserSessionTimeoutException, PuiServiceUserNotAuthenticatedException {
		if (ObjectUtils.isEmpty(jwt)) {
			throw new PuiServiceNoSessionException();
		}

		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");

		Authentication auth = get(jwt);
		PuiUserSession userSession = (PuiUserSession) auth.getPrincipal();
		SecurityContextHolder.getContext().setAuthentication(auth);
		if (!userSession.isAuthenticated()) {
			throw new PuiServiceUserNotAuthenticatedException();
		}
	}

	@Override
	public void removeContextSession() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Recreate the Session based on the given JWT token
	 * 
	 * @return The Pui Session Data
	 */
	private Authentication recreateSession(String jwt, Jws<Claims> jwsClaims, IPuiSession puiSession) {
		String usr = jwsClaims.getPayload().getSubject();

		// simulate a user login
		PuiUserSession userSession = (PuiUserSession) userDetailsService.loadUserByUsername(usr);
		userDetailsService.fillAuthorities(userSession);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userSession, null,
				new NullAuthoritiesMapper().mapAuthorities(userSession.getAuthorities()));

		if (jwsClaims.getPayload().containsKey(JWT_CLAIM_TIMEZONE)) {
			userSession.withZoneId(ZoneId.of(jwsClaims.getPayload().get(JWT_CLAIM_TIMEZONE, String.class)));
		}
		if (jwsClaims.getPayload().containsKey(JWT_CLAIM_PERSISTENT)) {
			userSession.withPersistent(jwsClaims.getPayload().get(JWT_CLAIM_PERSISTENT, Boolean.class));
		}
		if (jwsClaims.getPayload().containsKey(JWT_CLAIM_IP)) {
			userSession.withIp(jwsClaims.getPayload().get(JWT_CLAIM_IP, String.class));
		}
		if (jwsClaims.getPayload().containsKey(JWT_CLAIM_USER_AGENT)) {
			userSession.withUserAgent(jwsClaims.getPayload().get(JWT_CLAIM_USER_AGENT, String.class));
		}
		if (jwsClaims.getPayload().containsKey(JWT_CLAIM_CLIENT)) {
			userSession.withClient(jwsClaims.getPayload().get(JWT_CLAIM_CLIENT, String.class));
		}

		userSession.withUuid(!ObjectUtils.isEmpty(jwsClaims.getPayload().getId()) ? jwsClaims.getPayload().getId()
				: UUID.randomUUID().toString());
		userSession.withCreation(jwsClaims.getPayload().getIssuedAt().toInstant());
		if (puiSession != null) {
			userSession.withExpiration(puiSession.getExpiration());
			userSession.withLastUse(puiSession.getLastuse());
		} else {
			userSession.withExpiration(userSession.getCreation().plus(getTimeLogout(), ChronoUnit.MINUTES));
			userSession.withLastUse(Instant.now());
		}
		userSession.withJwt(jwt);

		return auth;
	}

	/**
	 * Parse the claims for the given JWT token
	 * 
	 * @param jwt The original JWT token
	 * @return The list of Claims associated to the JWT token
	 * @throws PuiServiceNoSessionException If an error happens while decoding the
	 *                                      JWT
	 */
	protected Jws<Claims> getJwsClaims(String jwt) throws PuiServiceNoSessionException {
		try {
			byte[] decoded = Decoders.BASE64.decode(getSecretJwt());
			return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(decoded)).build().parseSignedClaims(jwt);
		} catch (JwtException e) {
			throw new PuiServiceNoSessionException();
		}
	}

	/**
	 * Get the Secret key to decode the JWT
	 * 
	 * @return The secret key to decode the JWT
	 */
	protected String getSecretJwt() {
		String secretJwt = variableService.getVariable(PuiVariableValues.SESSION_JWT_SECRET.name());
		return AESCypher.decrypt(secretJwt, aesSecret);
	}

	/**
	 * Check the expiration time of the given JWT token
	 * 
	 * @param jwt The JWT token
	 * @throws PuiServiceUserSessionTimeoutException If the given authorization
	 *                                               token is expired
	 */
	private void checkExpirationTime(PuiUserSession session) throws PuiServiceUserSessionTimeoutException {
		// if it's persistent, do nothing, it's a valid session
		if (session.isPersistent()) {
			return;
		}

		if (Instant.now().isAfter(session.getExpiration())) {
			remove(session.getJwt());
			throw new PuiServiceUserSessionTimeoutException();
		}

		session.withLastUse(Instant.now());
		if (!session.isPersistent() && getTimeLogout() != null) {
			session.withExpiration(session.getLastUse().plus(getTimeLogout(), ChronoUnit.MINUTES));
		}
	}

	/**
	 * Get the minutes to invalidate a session (from the PuiVariable table)
	 * 
	 * @return The number of minutes to invalidate a session
	 */
	private Long getTimeLogout() {
		Long timelogout;
		try {
			timelogout = variableService.getVariable(Long.class, PuiVariableValues.SESSION_TIMEOUT.name());
		} catch (Exception e) {
			timelogout = null;
		}
		return timelogout;
	}

	private void cleanCacheSessions() {
		List<String> ids;
		synchronized (sessionsCache) {
			if (sessionsCache.isEmpty()) {
				return;
			}

			ids = sessionsCache.values().stream()
					.map(cacheSession -> ((PuiUserSession) cacheSession.getPrincipal()).getUuid())
					.collect(Collectors.toList());
		}
		List<IPuiSession> dbSessions;
		try {
			dbSessions = puiSessionDao.findWhere(FilterBuilder.newAndFilter().addIn(IPuiSessionPk.UUID_COLUMN, ids));
		} catch (PuiDaoFindException e) {
			dbSessions = Collections.emptyList();
		}

		Map<String, IPuiSession> mapDbSessions = dbSessions.stream()
				.collect(Collectors.toMap(IPuiSession::getUuid, Functions.identity()));
		Instant now = Instant.now();

		Set<Entry<String, Authentication>> entries;
		synchronized (sessionsCache) {
			entries = new HashSet<>(sessionsCache.entrySet());
		}

		for (Entry<String, Authentication> entry : entries) {
			String jwt = entry.getKey();
			PuiUserSession cacheSession = (PuiUserSession) entry.getValue().getPrincipal();
			IPuiSession dbSession = mapDbSessions.get(cacheSession.getUuid());

			if (cacheSession.isPersistent()) {
				continue;
			}

			if (dbSession == null) {
				// if no db session, remove from cache
				remove(jwt);
			} else if (now.isAfter(cacheSession.getExpiration()) && now.isAfter(dbSession.getExpiration())) {
				// if both cacheSession and dbSession are expired, remove from cache and ddbb
				remove(jwt);
			} else if (cacheSession.getExpiration().isAfter(dbSession.getExpiration())) {
				// if cacheSession is used after dbSession, update ddbb
				dbSession.setExpiration(cacheSession.getExpiration());
				dbSession.setLastuse(cacheSession.getLastUse());
				try {
					puiSessionDao.update(dbSession);
				} catch (PuiDaoUpdateException e) {
					// do nothing
				}
			} else if (dbSession.getExpiration().isAfter(cacheSession.getExpiration())) {
				// if dbSession is used after cacheSession, update cache
				cacheSession.withExpiration(dbSession.getExpiration());
				cacheSession.withLastUse(dbSession.getLastuse());
			}
		}
	}

	private void cleanDatabaseSessions() {
		Instant now = Instant.now();
		Integer maxDaysPersistent = variableService.getVariable(Integer.class,
				PuiVariableValues.SESSION_PERSISTENT_DURATION.name());

		SearchRequest req = new SearchRequest();
		req.setRows(1000);
		req.setOrder(Collections.singletonList(Order.newOrderAsc(IPuiUserPk.USR_COLUMN)));
		if (maxDaysPersistent == null) {
			req.setFilter(FilterBuilder.newAndFilter().addEquals(IPuiSession.PERSISTENT_COLUMN, PuiConstants.FALSE_INT)
					.asFilterGroup());
		}

		puiSessionDao.executePaginagedOperation(req, dbSession -> {
			if (dbSession.getPersistent().equals(PuiConstants.TRUE_INT)) {
				if (maxDaysPersistent != null) {
					Instant maxLastUse = Instant.now().minusSeconds(TimeUnit.DAYS.toSeconds(maxDaysPersistent));
					if (dbSession.getLastuse().isBefore(maxLastUse)) {
						remove(dbSession.getJwt());
					}
				}
			} else {
				Authentication auth;
				synchronized (sessionsCache) {
					auth = sessionsCache.get(dbSession.getJwt());
				}
				PuiUserSession cacheSession = null;
				if (auth != null) {
					cacheSession = (PuiUserSession) auth.getPrincipal();
				}

				if (cacheSession != null && cacheSession.getExpiration().isAfter(dbSession.getExpiration())) {
					// if cacheSession is used after dbSession, update ddbb
					dbSession.setExpiration(cacheSession.getExpiration());
					dbSession.setLastuse(cacheSession.getLastUse());
					try {
						puiSessionDao.update(dbSession);
					} catch (PuiDaoSaveException e) {
						// do nothing
					}
				} else if (now.isAfter(dbSession.getExpiration())) {
					// if both cacheSession and dbSession are expired, remove from cache and ddbb
					remove(dbSession.getJwt());
				}
			}
		}, null);
	}

}