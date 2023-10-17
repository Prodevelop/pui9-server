package es.prodevelop.pui9.common.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.PuiModelColumn;
import es.prodevelop.pui9.common.configuration.PuiModelConfiguration;
import es.prodevelop.pui9.common.exceptions.PuiCommonModelException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiModelDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelFilterService;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelConfigService;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelFilterService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.mvc.configuration.IPuiRequestMappingHandlerMapping;
import es.prodevelop.pui9.mvc.configuration.PuiControllersInfo;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

@PuiGenerated
@Service
public class PuiModelService extends AbstractService<IPuiModelPk, IPuiModel, IVPuiModel, IPuiModelDao, IVPuiModelDao>
		implements IPuiModelService {

	private static final String GRID_LITERAL = "grid.";

	@Autowired
	private IPuiUserModelFilterService userModelFilterService;

	@Autowired
	private IPuiModelFilterService modelFilterService;

	@Autowired
	private IPuiUserModelConfigService userModelConfigService;

	@Autowired
	private IPuiRequestMappingHandlerMapping requestMapping;

	private Map<String, PuiModelConfiguration> modelConfigCache = new LinkedHashMap<>();
	private Type modelConfigType = new TypeToken<Map<String, PuiModelConfiguration>>() {
		private static final long serialVersionUID = 1L;
	}.getType();

	@PostConstruct
	private void postConstructModelService() {
		// wait the server to be initialized
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadPuiModels", true, 1, 60, TimeUnit.MINUTES,
				() -> reloadModels(true));
	}

	@Override
	public void reloadModels(boolean force) {
		synchronized (modelConfigCache) {
			if (force || ObjectUtils.isEmpty(modelConfigCache)) {
				modelConfigCache.clear();
				modelConfigCache.putAll(getModelConfiguration());
			}
		}
	}

	@Override
	public IPuiModel guessModel(SearchRequest req) throws PuiServiceGetException {
		IPuiModel puiModel = null;
		if (ObjectUtils.isEmpty(req.getModel())) {
			if (req.getDtoClass() != null) {
				String entity = getDaoRegistry().getEntityName(getDaoRegistry().getDaoFromDto(req.getDtoClass()));
				PuiModelConfiguration modelConfig = getInternalModelConfigurations().values().stream()
						.filter(config -> Objects.equals(config.getEntity(), entity)).findFirst()
						.orElseGet(() -> new PuiModelConfiguration((String) null));
				puiModel = modelConfig.getModel();
			}
		} else {
			puiModel = getModelConfiguration(req.getModel()).getModel();
		}

		if (puiModel == null && req.getDtoClass() == null) {
			throw new PuiServiceGetException(new PuiCommonModelException(req.getModel()));
		}

		if (puiModel != null) {
			if (ObjectUtils.isEmpty(req.getModel())) {
				req.setModel(puiModel.getModel());
			}

			Class<IDto> dtoClass = DtoRegistry.getDtoFromEntity(puiModel.getEntity());
			req.setDtoClass(dtoClass);
		}

		return puiModel;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> SearchResponse<TYPE> search(SearchRequest req) throws PuiServiceGetException {
		IPuiModel puiModel = guessModel(req);

		if (ObjectUtils.isEmpty(req.getQueryLang())) {
			req.setQueryLang(PuiUserSession.getSessionLanguage().getIsocode());
		}

		Class<? extends IService> serviceClass = null;
		Class<? extends IDao> daoClass = null;
		if (puiModel != null) {
			serviceClass = getServiceRegistry().getServiceFromModelId(puiModel.getModel());
			daoClass = getDaoRegistry().getDaoFromEntityName(puiModel.getEntity(), false);
			if (!ObjectUtils.isEmpty(puiModel.getFilter())) {
				if (puiModel.getFilter().equals(IListAdapter.SEARCH_PARAMETER)) {
					req.setDbFilters(IListAdapter.EMPTY_FILTER);
				} else {
					try {
						req.setDbFilters(FilterGroup.fromJson(puiModel.getFilter()));
					} catch (JsonSyntaxException e) {
						// do nothing
					}
				}
			} else {
				req.setDbFilters(null);
			}
			if (ObjectUtils.isEmpty(req.getOrder())) {
				req.setOrder(getModelConfiguration(req.getModel()).getDefaultConfiguration().getOrder());
			}
		} else if (req.getDtoClass() != null) {
			serviceClass = getServiceRegistry().getServiceFromDto(req.getDtoClass());
			daoClass = getDaoRegistry().getDaoFromDto(req.getDtoClass());
		}

		if (serviceClass != null) {
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			if (IViewDto.class.isAssignableFrom(req.getDtoClass())) {
				return service.searchView(req);
			} else if (ITableDto.class.isAssignableFrom(req.getDtoClass())) {
				return service.searchTable(req);
			} else {
				return new SearchResponse<>();
			}
		}

		if (daoClass != null) {
			IDao dao = PuiApplicationContext.getInstance().getBean(daoClass);
			try {
				return dao.findPaginated(req);
			} catch (PuiDaoListException e) {
				throw new PuiServiceGetException(e);
			}
		}

		return new SearchResponse<>();
	}

	@Override
	public Map<String, PuiModelConfiguration> getPuiModelConfigurations() {
		String json = GsonSingleton.getSingleton().getGson().toJson(getInternalModelConfigurations());
		Map<String, PuiModelConfiguration> config = GsonSingleton.getSingleton().getGson().fromJson(json,
				modelConfigType);
		fillUserInformation(config);
		return config;
	}

	@Override
	public PuiModelConfiguration getPuiModelConfiguration(String model) {
		Map<String, PuiModelConfiguration> config = new LinkedHashMap<>();
		config.put(model, getInternalModelConfigurations().get(model));

		String json = GsonSingleton.getSingleton().getGson().toJson(config);
		config = GsonSingleton.getSingleton().getGson().fromJson(json, modelConfigType);

		fillUserInformation(config);

		return config.get(model);
	}

	@Override
	public Map<String, PuiModelConfiguration> getOriginalPuiModelConfigurations() {
		String json = GsonSingleton.getSingleton().getGson().toJson(getInternalModelConfigurations());
		return GsonSingleton.getSingleton().getGson().fromJson(json, modelConfigType);
	}

	@Override
	public PuiModelConfiguration getModelConfiguration(String model) {
		if (ObjectUtils.isEmpty(model)) {
			return null;
		}

		return getInternalModelConfigurations().getOrDefault(model, new PuiModelConfiguration(""));
	}

	@Override
	public List<String> getAllModels() {
		List<String> allModels = new ArrayList<>();
		try {
			getAll().forEach(model -> allModels.add(model.getModel()));
		} catch (PuiServiceGetException e) {
			// do nothing
		}
		return allModels;
	}

	private Map<String, PuiModelConfiguration> getInternalModelConfigurations() {
		synchronized (modelConfigCache) {
			if (ObjectUtils.isEmpty(modelConfigCache)) {
				reloadModels(false);
			}
			return modelConfigCache;
		}
	}

	/**
	 * Build a map from model ID to model Configuration
	 */
	private Map<String, PuiModelConfiguration> getModelConfiguration() {
		Map<String, PuiModelConfiguration> configurations = new LinkedHashMap<>();
		List<IPuiModel> puiModelList;
		try {
			puiModelList = getAll();
		} catch (PuiServiceGetException e) {
			puiModelList = Collections.emptyList();
		}

		for (IPuiModel puiModel : puiModelList) {
			PuiModelConfiguration configuration = new PuiModelConfiguration(puiModel);
			Class<? extends ITableDto> tableDtoClass = getDaoRegistry().getTableDtoFromModelId(puiModel.getModel(),
					false);
			if (tableDtoClass != null) {
				configuration.setTable(DtoRegistry.getEntityFromDto(tableDtoClass));
			}
			try {
				if (puiModel.getConfiguration() != null) {
					configuration.setDefaultConfiguration(puiModel.getConfiguration());
					if (ObjectUtils.isEmpty(puiModel.getConfiguration().getLabel())) {
						puiModel.getConfiguration().setLabel(puiModel.getModel());
					}
				}
			} catch (Exception e) {
				// do nothing
			}
			addColumns(configuration);
			if (!configuration.getColumns().isEmpty()) {
				configurations.put(puiModel.getModel(), configuration);
			}
		}

		addModelFilters(configurations);

		// load rest of models not present in pui_model
		Map<String, PuiControllersInfo> controllerInfo = requestMapping.getUrlsAndFunctionalitiesByController();
		controllerInfo.forEach((model, info) -> {
			if (!configurations.containsKey(model)) {
				PuiModelConfiguration config = new PuiModelConfiguration(model);
				Class<? extends ITableDto> tableDtoClass = getDaoRegistry().getTableDtoFromModelId(model, false);
				if (tableDtoClass != null) {
					config.setTable(DtoRegistry.getEntityFromDto(tableDtoClass));
				}
				configurations.put(model, config);
			}
			configurations.get(model).getFunctionalities().putAll(info.getFunctionalities());
			configurations.get(model).getUrl().putAll(info.getUrl());
		});

		return configurations;
	}

	private void fillUserInformation(Map<String, PuiModelConfiguration> configurations) {
		addUserModelFilters(configurations);
		addUserConfigurations(configurations);
	}

	/**
	 * Add the columns of the given entity (typically is a view) to the model
	 * configuration
	 * 
	 * @param configuration The model configuration
	 */
	@SuppressWarnings("rawtypes")
	private void addColumns(PuiModelConfiguration configuration) {
		Class<? extends IDao> entityDaoClass = getDaoRegistry().getDaoFromEntityName(configuration.getEntity(), false);
		if (entityDaoClass == null) {
			return;
		}

		Class<? extends IDto> entityDtoClass = getDaoRegistry().getDtoFromDao(entityDaoClass, false);
		if (entityDtoClass == null) {
			return;
		}

		List<String> fields = DtoRegistry.getAllFields(entityDtoClass);
		List<String> pkFields = new ArrayList<>();
		try {
			Class<? extends IDto> tableDtoClass = getDaoRegistry().getTableDtoFromModelId(configuration.getName(),
					true);
			if (tableDtoClass != null) {
				pkFields = DtoRegistry.getPkFields(tableDtoClass);
			}
		} catch (Exception e) {
			// do nothing
		}

		for (String fieldName : fields) {
			String title = GRID_LITERAL + configuration.getModel().getModel() + "." + fieldName;
			boolean isPk = pkFields.contains(fieldName);
			ColumnVisibility visibility = DtoRegistry.getColumnVisibility(entityDtoClass, fieldName);
			if (visibility == null) {
				visibility = ColumnVisibility.visible;
			}
			ColumnType type;
			if (DtoRegistry.getStringFields(entityDtoClass).contains(fieldName)) {
				type = ColumnType.text;
			} else if (DtoRegistry.getNumericFields(entityDtoClass).contains(fieldName)) {
				type = ColumnType.numeric;
			} else if (DtoRegistry.getFloatingFields(entityDtoClass).contains(fieldName)) {
				type = ColumnType.decimal;
			} else if (DtoRegistry.getDateTimeFields(entityDtoClass).contains(fieldName)) {
				type = ColumnType.datetime;
			} else if (DtoRegistry.getBooleanFields(entityDtoClass).contains(fieldName)) {
				type = ColumnType.logic;
			} else {
				type = ColumnType.text;
			}

			configuration.addColumn(new PuiModelColumn(fieldName, title, type, isPk, visibility));
		}
	}

	private void addModelFilters(Map<String, PuiModelConfiguration> configurations) {
		FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addIn(IPuiUserModelFilter.MODEL_COLUMN,
				new ArrayList<>(configurations.keySet()));
		List<IPuiModelFilter> modelFilters;
		try {
			modelFilters = modelFilterService.getAllWhere(filterBuilder);
		} catch (PuiServiceGetException e) {
			modelFilters = Collections.emptyList();
		}

		for (IPuiModelFilter pmf : modelFilters) {
			if (configurations.containsKey(pmf.getModel())) {
				configurations.get(pmf.getModel()).addModelFilter(pmf);
			}
		}
	}

	private void addUserModelFilters(Map<String, PuiModelConfiguration> configurations) {
		if (PuiUserSession.getCurrentSession() == null) {
			return;
		}

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
				.addIn(IPuiUserModelFilter.MODEL_COLUMN, new ArrayList<>(configurations.keySet()))
				.addEqualsExact(IPuiUserModelFilter.USR_COLUMN, PuiUserSession.getCurrentSession().getUsr());
		List<IPuiUserModelFilter> userFilters;
		try {
			userFilters = userModelFilterService.getAllWhere(filterBuilder);
		} catch (PuiServiceGetException e) {
			userFilters = Collections.emptyList();
		}
		for (IPuiUserModelFilter umf : userFilters) {
			configurations.get(umf.getModel()).addUserFilter(umf);
		}
	}

	private void addUserConfigurations(Map<String, PuiModelConfiguration> configurations) {
		if (PuiUserSession.getCurrentSession() == null) {
			return;
		}

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
				.addIn(IPuiUserModelConfig.MODEL_COLUMN, new ArrayList<>(configurations.keySet()))
				.addEqualsExact(IPuiUserModelFilter.USR_COLUMN, PuiUserSession.getCurrentSession().getUsr());
		List<IPuiUserModelConfig> puiUserModelConfigList;
		try {
			puiUserModelConfigList = userModelConfigService.getAllWhere(filterBuilder);
		} catch (PuiServiceGetException e) {
			puiUserModelConfigList = Collections.emptyList();
		}
		for (IPuiUserModelConfig umc : puiUserModelConfigList) {
			configurations.get(umc.getModel()).addConfiguration(umc.getType(), umc);
		}
	}

}
