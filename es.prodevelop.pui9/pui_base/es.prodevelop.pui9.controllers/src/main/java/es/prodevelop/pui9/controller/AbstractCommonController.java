package es.prodevelop.pui9.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.eventlistener.event.DeleteEvent;
import es.prodevelop.pui9.eventlistener.event.ExportEvent;
import es.prodevelop.pui9.eventlistener.event.GetEvent;
import es.prodevelop.pui9.eventlistener.event.InsertEvent;
import es.prodevelop.pui9.eventlistener.event.ListEvent;
import es.prodevelop.pui9.eventlistener.event.PatchEvent;
import es.prodevelop.pui9.eventlistener.event.TemplateEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateEvent;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.export.DataExporterRegistry;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.IPuiSearchAdapter;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.services.exceptions.PuiServiceConcurrencyException;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

/**
 * This abstract controller offers all the common operations for entities like:
 * insert, update, delete, get and list for PUI grid.
 */
public abstract class AbstractCommonController<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>, S extends IService<TPK, T, V, DAO, VDAO>>
		extends AbstractPuiController implements IPuiCommonController<TPK, T, V, DAO, VDAO, S> {

	public static final String DTO_HASH_HEADER = "dtohash";

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private IPuiSearchAdapter puiSearchAdapter;

	@Autowired
	private S service;

	@PostConstruct
	private void postConstruct() {
		String modelId = getModelId();

		daoRegistry.registerModelDaos(modelId, getService().getTableDtoPkClass(), getService().getTableDtoClass(),
				getService().getViewDtoClass(), getService().getTableDao().getClass(),
				getService().getViewDao().getClass());
		serviceRegistry.registerModelService(modelId, getService().getClass());
	}

	/**
	 * Obtain the model ID name. By default it is obtained from the
	 * {@link RequestMapping} value of the Controller, but you can change it
	 * 
	 * @return
	 */
	protected String getModelId() {
		RequestMapping rm = getClass().getAnnotation(RequestMapping.class);
		if (rm == null) {
			return null;
		}

		String modelId;
		String[] value = rm.value();
		if (value.length == 0) {
			return null;
		} else {
			modelId = value[0].replace("/", "");
		}

		return modelId;
	}

	protected DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	public S getService() {
		return service;
	}

	protected DAO getTableDao() {
		return service.getTableDao();
	}

	protected VDAO getViewDao() {
		return service.getViewDao();
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@Operation(summary = "The Template for creating a new element", description = "Get the template for creating a new element")
	@GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
	public T template() throws PuiServiceNewException {
		T dto = getService().getNew();

		fireEventTemplate(dto);
		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@Operation(summary = "Obtain an element", description = "Get the element that matches the given PK")
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public T get(@Parameter(description = "The PK of the element", required = true) TPK dtoPk)
			throws PuiServiceGetException {
		T dto = getService().getByPk(dtoPk);

		getResponse().setHeader(DTO_HASH_HEADER, String.valueOf(dto.hashCode()));

		fireEventGet(dto);
		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@Operation(summary = "Insert a new element", description = "Insert a new element into the database")
	@PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T insert(@Parameter(description = "The data of the element", required = true) @RequestBody T dto)
			throws PuiServiceInsertException {
		getService().insert(dto);
		fireEventInsert(dto);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@Operation(summary = "Update an element", description = "Update an existing element in the database")
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T update(
			@Parameter(description = "The full data of the element be updated, including non changed values", required = true) @RequestBody T dto,
			@Parameter(description = "The old dto hash for concurrency check", in = ParameterIn.QUERY) @RequestHeader(value = DTO_HASH_HEADER, required = false) String dtoHash)
			throws PuiServiceGetException, PuiServiceUpdateException, PuiServiceConcurrencyException {
		TPK pk = dto.createPk();
		T oldDto = getService().getByPk(pk, PuiUserSession.getSessionLanguage());

		if (!ObjectUtils.isEmpty(dtoHash) && !Objects.equals(String.valueOf(oldDto.hashCode()), dtoHash)) {
			throw new PuiServiceConcurrencyException();
		}

		getService().update(dto);
		fireEventUpdate(oldDto, dto);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@Operation(summary = "Patch some attributes of an element", description = "Update only part of an existing element in the database")
	@PatchMapping(value = "/patch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T patch(@Parameter(description = "The PK of the element", required = true, in = ParameterIn.QUERY) TPK dtoPk,
			@Parameter(description = "", required = true) @RequestBody Map<String, Object> properties)
			throws PuiServiceGetException, PuiServiceUpdateException {
		T oldDto = getService().getByPk(dtoPk, PuiUserSession.getSessionLanguage());

		T dto = getService().patch(dtoPk, properties);
		fireEventPatch(dtoPk, oldDto, properties);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_DELETE, value = METHOD_FUNCTIONALITY_DELETE)
	@Operation(summary = "Delete an element", description = "Delete an element from the database")
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public TPK delete(
			@Parameter(description = "The PK of the element to be deleted", required = true, in = ParameterIn.QUERY) TPK dtoPk)
			throws PuiServiceGetException, PuiServiceDeleteException {
		T dto = getService().getByPk(dtoPk, PuiUserSession.getSessionLanguage());

		if (dto == null) {
			return null;
		}

		TPK deletedDtoPk = getService().delete(dtoPk);
		fireEventDelete(dto);

		return deletedDtoPk;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@Operation(summary = "List the data from the view", description = "List all the elements that accomplish the given condition")
	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<V> list(
			@Parameter(description = "The searching parameters") @RequestBody(required = false) SearchRequest req)
			throws PuiServiceGetException {
		if (puiSearchAdapter == null) {
			return new SearchResponse<>();
		}

		if (req == null) {
			req = new SearchRequest();
		}

		req.setModel(getModelId());
		req.setDtoClass(getViewDao().getDtoClass());
		req.setFromClient(true);

		SearchResponse<V> res = puiSearchAdapter.search(req);
		fireEventList(req, res);

		return res;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@Operation(summary = "List the data of a table", description = "List all the elements that accomplish the given condition")
	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<T> listFromTable(
			@Parameter(description = "The searching parameters") @RequestBody(required = false) SearchRequest req)
			throws PuiServiceGetException {
		if (puiSearchAdapter == null) {
			return new SearchResponse<>();
		}

		if (req == null) {
			req = new SearchRequest();
		}

		req.setModel(getModelId());
		req.setDtoClass(getTableDao().getDtoClass());
		req.setFromClient(true);

		SearchResponse<T> res = puiSearchAdapter.search(req);
		fireEventList(req, res);

		return res;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_EXPORT, value = METHOD_FUNCTIONALITY_EXPORT)
	@Operation(summary = "Export the grid data", description = "Export the current grid data")
	@PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload export(@RequestBody(required = false) ExportRequest req) throws PuiServiceExportException {
		if (req == null) {
			req = new ExportRequest();
		}

		req.setPage(SearchRequest.DEFAULT_PAGE);
		req.setRows(SearchRequest.NUM_MAX_ROWS);

		req.setModel(getModelId());
		if (allowListFromTable()) {
			req.setDtoClass(getTableDao().getDtoClass());
		} else {
			req.setDtoClass(getViewDao().getDtoClass());
		}
		req.setFromClient(true);

		FileDownload fd = DataExporterRegistry.getSingleton().getExporter(req.getExportType()).export(req);
		fireEventExport(req, fd);

		return fd;
	}

	@Override
	public boolean allowGet() {
		return hasRelatedTable();
	}

	@Override
	public boolean allowTemplate() {
		return allowInsert();
	}

	@Override
	public boolean allowInsert() {
		return hasRelatedTable();
	}

	@Override
	public boolean allowUpdate() {
		return hasRelatedTable();
	}

	@Override
	public boolean allowPatch() {
		return allowUpdate();
	}

	@Override
	public boolean allowDelete() {
		return hasRelatedTable();
	}

	@Override
	public boolean allowList() {
		return hasRelatedView();
	}

	@Override
	public boolean allowListFromTable() {
		return false;
	}

	@Override
	public boolean allowExport() {
		return allowList() || allowListFromTable();
	}

	protected void fireEventGet(T dto) {
		getEventLauncher().fireAsync(new GetEvent(dto));
	}

	protected void fireEventTemplate(T dto) {
		getEventLauncher().fireAsync(new TemplateEvent(dto));
	}

	protected void fireEventInsert(T dto) {
		getEventLauncher().fireAsync(new InsertEvent(dto));
	}

	protected void fireEventUpdate(T oldDto, T dto) {
		getEventLauncher().fireAsync(new UpdateEvent(dto, oldDto));
	}

	protected void fireEventPatch(TPK dtoPk, T oldDto, Map<String, Object> properties) {
		getEventLauncher().fireAsync(new PatchEvent(dtoPk, oldDto, properties));
	}

	protected void fireEventDelete(T dto) {
		getEventLauncher().fireAsync(new DeleteEvent(dto));
	}

	protected void fireEventList(SearchRequest req, SearchResponse<? extends IDto> res) {
		getEventLauncher().fireAsync(new ListEvent<>(req, res));
	}

	protected void fireEventExport(ExportRequest req, FileDownload fd) {
		getEventLauncher().fireAsync(new ExportEvent(req, fd));
	}

	private Boolean hasRelatedTable = null;
	private Boolean hasRelatedView = null;

	private Boolean hasRelatedTable() {
		if (hasRelatedTable == null) {
			hasRelatedTable = hasRelatedEntity(INullTable.class);
		}
		return hasRelatedTable;
	}

	private Boolean hasRelatedView() {
		if (hasRelatedView == null) {
			hasRelatedView = hasRelatedEntity(INullView.class);
		}
		return hasRelatedView;
	}

	@SuppressWarnings("unchecked")
	private <FEC extends IDto> Boolean hasRelatedEntity(Class<FEC> fakeEntityClass) {
		Class<FEC> entityDtoClass = null;

		try {
			Type superClass = getClass();
			while ((superClass instanceof Class)
					&& ((Class<?>) superClass).getSuperclass().equals(((Class<?>) superClass).getGenericSuperclass())) {
				superClass = ((Class<?>) superClass).getSuperclass();
			}
			superClass = ((Class<?>) superClass).getGenericSuperclass();

			// obtain the dtoViewClass
			for (Type type : ((ParameterizedType) superClass).getActualTypeArguments()) {
				if (type instanceof Class && fakeEntityClass.isAssignableFrom((Class<?>) type)) {
					entityDtoClass = (Class<FEC>) type;
					break;
				}
			}
		} catch (Exception e1) {
			// only for PUI controllers
			try {
				first: for (TypeVariable<?> typevar : getClass().getTypeParameters()) {
					for (Type type : typevar.getBounds()) {
						if (!(type instanceof Class)) {
							continue;
						}
						if (fakeEntityClass.isAssignableFrom((Class<?>) type)) {
							entityDtoClass = (Class<FEC>) type;
							break first;
						}
					}
				}
			} catch (Exception e2) {
				return false;
			}
		}

		return entityDtoClass == null || !fakeEntityClass.isAssignableFrom(entityDtoClass);
	}

}
