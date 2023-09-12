package es.prodevelop.pui9.model.dao.registry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;

/**
 * This is a registry for all the DAOs in the application. It brings a lot of
 * useful methods that can be used to operate quicklier with the DAO and DTO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
@SuppressWarnings("rawtypes")
public class DaoRegistry {

	private List<Class<? extends IDao>> registeredDaos;

	private Map<Class<? extends IDao>, String> mapEntityNameFromDao;
	private Map<String, Class<? extends IDao>> mapDaoFromEntityName;
	private Map<Class<? extends IDao>, String> mapEntityLangNameFromDao;
	private Map<String, List<Class<? extends IDto>>> mapDtoListFromEntityName;

	private Map<String, Class<? extends ITableDao>> mapTableDaoFromModelId;
	private Map<String, Class<? extends IViewDao>> mapViewDaoFromModelId;
	private Map<String, List<Class<? extends ITableDto>>> mapTableDtoListFromModelId;
	private Map<String, Class<? extends IViewDto>> mapViewDtoFromModelId;
	private Map<Class<? extends IDto>, Set<String>> mapModelIdFromDto;
	private Map<Class<? extends IDao>, Set<String>> mapModelIdFromDao;

	private Map<Class<? extends IDao>, List<Class<? extends IDto>>> mapDtosFromDao;
	private Map<Class<? extends IDto>, Class<? extends IDao>> mapDaoFromDto;
	private Map<Class<? extends ITableDao>, List<Class<? extends IViewDao>>> mapViewDaoListFromTableDao;
	private Map<Class<? extends IViewDao>, Class<? extends ITableDao>> mapTableDaoFromViewDao;
	private Map<Class<? extends ITableDto>, List<Class<? extends IViewDto>>> mapViewDtoListFromTableDto;
	private Map<Class<? extends IViewDto>, Class<? extends ITableDto>> mapTableDtoFromViewDto;

	private DaoRegistry() {
		registeredDaos = new ArrayList<>();

		mapEntityNameFromDao = new LinkedHashMap<>();
		mapDaoFromEntityName = new LinkedHashMap<>();
		mapEntityLangNameFromDao = new LinkedHashMap<>();
		mapDtoListFromEntityName = new LinkedHashMap<>();

		mapTableDtoListFromModelId = new LinkedHashMap<>();
		mapViewDtoFromModelId = new LinkedHashMap<>();
		mapTableDaoFromModelId = new LinkedHashMap<>();
		mapViewDaoFromModelId = new LinkedHashMap<>();
		mapModelIdFromDto = new LinkedHashMap<>();
		mapModelIdFromDao = new LinkedHashMap<>();

		mapDtosFromDao = new LinkedHashMap<>();
		mapDaoFromDto = new LinkedHashMap<>();
		mapViewDaoListFromTableDao = new LinkedHashMap<>();
		mapTableDaoFromViewDao = new LinkedHashMap<>();
		mapViewDtoListFromTableDto = new LinkedHashMap<>();
		mapTableDtoFromViewDto = new LinkedHashMap<>();
	}

	/**
	 * Mark two DAOs as related, in the sense that given {@link ITableDao} is
	 * related with the {@link IViewDao} in terms of managing the same table and
	 * view (a model)
	 * 
	 * @param tableDaoClass The {@link ITableDao} DAO
	 * @param viewDaoClass  The {@link IViewDao} DAO
	 */
	public void relatedDaos(Class<? extends ITableDao> tableDaoClass, Class<? extends IViewDao> viewDaoClass) {
		if (!mapViewDaoListFromTableDao.containsKey(tableDaoClass)) {
			mapViewDaoListFromTableDao.put(tableDaoClass, new ArrayList<>());
		}

		mapViewDaoListFromTableDao.get(tableDaoClass).add(viewDaoClass);
		mapTableDaoFromViewDao.put(viewDaoClass, tableDaoClass);
	}

	/**
	 * Mark a {@link ITableDto} DTO and a {@link IViewDto} DTO as related, in terms
	 * that both manages the same model
	 * 
	 * @param tableDtoPkClass The DTO PK class of the Table
	 * @param tableDtoClass   The DTO class of the Table
	 * @param viewDtoClass    The DTO class of the View
	 */
	public void relatedDtos(Class<? extends ITableDto> tableDtoPkClass, Class<? extends ITableDto> tableDtoClass,
			Class<? extends IViewDto> viewDtoClass) {
		if (!mapViewDtoListFromTableDto.containsKey(tableDtoPkClass)) {
			mapViewDtoListFromTableDto.put(tableDtoPkClass, new ArrayList<>());
		}

		if (!mapViewDtoListFromTableDto.containsKey(tableDtoClass)) {
			mapViewDtoListFromTableDto.put(tableDtoClass, new ArrayList<>());
		}

		mapViewDtoListFromTableDto.get(tableDtoPkClass).add(viewDtoClass);
		mapViewDtoListFromTableDto.get(tableDtoClass).add(viewDtoClass);
		mapTableDtoFromViewDto.put(viewDtoClass, tableDtoClass);
	}

	/**
	 * Register the model associated to the Table and View DAOs and DTOs
	 * 
	 * @param modelId
	 * @param tableDtoPkClass
	 * @param tableDtoClass
	 * @param viewDtoClass
	 * @param tableDaoClass
	 * @param viewDaoClass
	 */
	@SuppressWarnings("unchecked")
	public void registerModelDaos(String modelId, Class<? extends ITableDto> tableDtoPkClass,
			Class<? extends ITableDto> tableDtoClass, Class<? extends IViewDto> viewDtoClass,
			Class<? extends ITableDao> tableDaoClass, Class<? extends IViewDao> viewDaoClass) {
		Class<? extends ITableDto> tableDtoPkIface = (Class<? extends ITableDto>) tableDtoPkClass.getInterfaces()[0];
		Class<? extends ITableDto> tableDtoIface = (Class<? extends ITableDto>) tableDtoClass.getInterfaces()[0];
		Class<? extends IViewDto> viewDtoIface = (Class<? extends IViewDto>) viewDtoClass.getInterfaces()[0];
		Class<? extends ITableDao> tableDaoIface = (Class<? extends ITableDao>) tableDaoClass.getInterfaces()[0];
		Class<? extends IViewDao> viewDaoIface = (Class<? extends IViewDao>) viewDaoClass.getInterfaces()[0];

		mapTableDaoFromModelId.put(modelId, tableDaoIface);
		mapViewDaoFromModelId.put(modelId, viewDaoIface);

		if (!tableDaoIface.equals(INullTableDao.class)) {
			mapModelIdFromDao.computeIfAbsent(tableDaoClass, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDao.computeIfAbsent(tableDaoIface, k -> new LinkedHashSet()).add(modelId);
		}
		if (!viewDaoIface.equals(INullViewDao.class)) {
			mapModelIdFromDao.computeIfAbsent(viewDaoClass, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDao.computeIfAbsent(viewDaoIface, k -> new LinkedHashSet()).add(modelId);
		}

		if (!tableDtoIface.equals(INullTable.class)) {
			mapModelIdFromDto.computeIfAbsent(tableDtoPkIface, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDto.computeIfAbsent(tableDtoPkClass, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDto.computeIfAbsent(tableDtoIface, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDto.computeIfAbsent(tableDtoClass, k -> new LinkedHashSet()).add(modelId);
		}
		if (!viewDtoIface.equals(INullView.class)) {
			mapModelIdFromDto.computeIfAbsent(viewDtoIface, k -> new LinkedHashSet()).add(modelId);
			mapModelIdFromDto.computeIfAbsent(viewDtoClass, k -> new LinkedHashSet()).add(modelId);
		}

		mapTableDtoListFromModelId.put(modelId, new ArrayList<>());
		mapTableDtoListFromModelId.get(modelId).add(tableDtoPkClass);
		mapTableDtoListFromModelId.get(modelId).add(tableDtoClass);
		mapViewDtoFromModelId.put(modelId, viewDtoClass);
	}

	/**
	 * Registers a DAO
	 * 
	 * @param daoClass The DAO class to register
	 */
	@SuppressWarnings("unchecked")
	public void registerDao(Class<? extends IDao> daoClass) {
		if (registeredDaos.contains(daoClass)) {
			return;
		}

		Class<? extends IDao> iface = (Class<? extends IDao>) daoClass.getInterfaces()[0];
		ParameterizedType superType = (ParameterizedType) daoClass.getGenericSuperclass();

		registeredDaos.add(iface);

		mapDtosFromDao.put(iface, new ArrayList<>());
		mapDtosFromDao.put(daoClass, new ArrayList<>());

		// invert the list parameters, as the main information is not in the PK
		// (the first one)
		List<Class<IDto>> typeParameters = new ArrayList<>();
		for (Type type : superType.getActualTypeArguments()) {
			typeParameters.add(0, (Class<IDto>) type);
		}

		// register the DTO and DTO PK classes
		String tablename = "";
		for (Class<IDto> type : typeParameters) {
			Class<? extends IDto> dtoIface = type;
			Class<? extends IDto> dtoClass = DtoRegistry.getDtoImplementation(dtoIface);

			PuiEntity puiTable = dtoClass.getAnnotation(PuiEntity.class);
			if (puiTable != null) {
				tablename = puiTable.tablename().toLowerCase();
				String tabletranslationname = puiTable.tabletranslationname();

				mapEntityNameFromDao.put(iface, tablename);
				mapDaoFromEntityName.put(tablename, iface);
				if (tabletranslationname.isEmpty()) {
					mapEntityLangNameFromDao.put(iface, null);
				} else {
					mapEntityLangNameFromDao.put(iface, tabletranslationname);
				}

				mapDtoListFromEntityName.put(tablename, new ArrayList<>());
			}

			DtoRegistry.registerDto(dtoClass);
			mapDtosFromDao.get(iface).add(dtoClass);
			mapDtosFromDao.get(daoClass).add(dtoClass);
			mapDaoFromDto.put(dtoIface, iface);
			mapDaoFromDto.put(dtoClass, iface);
			mapDtoListFromEntityName.get(tablename).add(dtoClass);
		}
	}

	/**
	 * Check if a DAO with the entity name was registered. If not, it tries to
	 * generate the DTO and DAO classes automatically
	 * 
	 * @param entityName The name of the entity in the database
	 */
	private void checkDaoExistsAndGenerate(String entityName) {
		if (ObjectUtils.isEmpty(entityName)) {
			return;
		}
		if (!mapDaoFromEntityName.containsKey(entityName.toLowerCase())) {
			try {
				// this call only flushed the reflections cache
				DtoRegistry.getDtoImplementation(null, true);
				PuiApplicationContext.getInstance().getBean(DynamicClassGenerator.class)
						.executeCodeGeneration(entityName, entityName);
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * Check if there exists the a DAO for the given table/view
	 * 
	 * @param entityName The name of the entity in the database
	 * @return true if it was registered; false if not
	 */
	public boolean existsDaoForEntity(String entityName) {
		if (ObjectUtils.isEmpty(entityName)) {
			return false;
		}
		return mapDaoFromEntityName.containsKey(entityName.toLowerCase());
	}

	/**
	 * Check if the given DAO has language support
	 * 
	 * @param dao The DAO
	 * @return true if has language support; false if not
	 */
	public boolean hasLanguageSupport(IDao dao) {
		return hasLanguageSupport(unwrapDaoClass(dao));
	}

	/**
	 * Check if the given DAO class has language support
	 * 
	 * @param daoClass The DAO class
	 * @return true if has language support; false if not
	 */
	public boolean hasLanguageSupport(Class<? extends IDao> daoClass) {
		return getTableLangName(daoClass) != null;
	}

	/**
	 * Get the entity name of the given DAO
	 * 
	 * @param dao The DAO
	 * @return The entity name in the database
	 */
	public String getEntityName(IDao dao) {
		return getEntityName(unwrapDaoClass(dao));
	}

	/**
	 * Get the entity name of the given DAO class
	 * 
	 * @param daoClass The DAO class
	 * @return The entity name in the database
	 */
	public String getEntityName(Class<? extends IDao> daoClass) {
		daoClass = getDaoInterface(daoClass);
		return mapEntityNameFromDao.get(daoClass);
	}

	/**
	 * Get all the entity names registered by all the DAOs
	 * 
	 * @return A list of all the registered entity names
	 */
	public List<String> getAllEntityNames() {
		List<String> list = new ArrayList<>();
		list.addAll(mapEntityNameFromDao.values());
		return list;
	}

	/**
	 * Get the Dao class from the given entity name
	 * 
	 * @param entityName          The entity name
	 * @param generateIfNotExists generate the DAO/DTO classes if it doesn't exist
	 * @return The related DAO class
	 */
	@SuppressWarnings("unchecked")
	public <T extends IDao> Class<T> getDaoFromEntityName(String entityName, boolean generateIfNotExists) {
		if (ObjectUtils.isEmpty(entityName)) {
			return null;
		}

		if (generateIfNotExists) {
			checkDaoExistsAndGenerate(entityName);
		}
		return (Class<T>) mapDaoFromEntityName.get(entityName.toLowerCase());
	}

	/**
	 * Get the DtoClass from the entity name. If specified that wants the PK, the
	 * DTO PK Class will be retrieved. If not, the normal DTO will be given
	 * 
	 * @param entityName          The entity name If retrieve the DTO that
	 *                            represents the PK (in case of Table DAO)
	 * @param wantPkDto           true if want the DTO PK; false if want the whole
	 *                            DTO
	 * @param generateIfNotExists generate the DAO/DTO classes if it doesn't exist
	 * @return The associated DTO class
	 */
	public <T extends IDto> Class<T> getDtoFromEntityName(String entityName, boolean wantPkDto,
			boolean generateIfNotExists) {
		if (ObjectUtils.isEmpty(entityName)) {
			return null;
		}

		if (generateIfNotExists) {
			checkDaoExistsAndGenerate(entityName);
		}

		List<Class<? extends IDto>> classes = mapDtoListFromEntityName.get(entityName.toLowerCase());
		Class<T> dtoClass = getDto(classes, wantPkDto);
		if (dtoClass == null && !generateIfNotExists) {
			return null;
		}
		return dtoClass;
	}

	/**
	 * Get the Table Dto Class from the model ID. If specified that wants the PK,
	 * the DTO PK Class will be retrieved. If not, the normal DTO will be given
	 * 
	 * @param modelId             The model ID of the DTO
	 * @param wantPkDto           true if want the DTO PK; false if want the whole
	 * @param generateIfNotExists generate the DAO/DTO classes if it doesn't exist
	 * 
	 * @return The associated DTO class
	 */
	public <T extends ITableDto> Class<T> getTableDtoFromModelId(String modelId, boolean wantPkDto,
			boolean generateIfNotExists) {
		if (ObjectUtils.isEmpty(modelId)) {
			return null;
		}

		Class<? extends ITableDao> daoClass = getTableDaoFromModelId(modelId);
		String entityName = getEntityName(daoClass);
		if (daoClass == null && generateIfNotExists) {
			checkDaoExistsAndGenerate(entityName);
		} else if (daoClass == null) {
			return null;
		}

		List<Class<? extends IDto>> classes = mapDtoListFromEntityName.get(entityName.toLowerCase());
		Class<T> dtoClass = getDto(classes, wantPkDto);
		if (dtoClass == null && !generateIfNotExists) {
			return null;
		}
		return dtoClass;
	}

	/**
	 * Get the DTO related with the given Model ID. If specified that wants the PK,
	 * the DTO PK Class will be retrieved. If not, the normal DTO will be given
	 * 
	 * @param modelId   The model ID
	 * @param wantPkDto If retrieve the DTO that represents the PK (in case of Table
	 *                  DAO)
	 * @return The associated DTO class
	 */
	public Class<? extends ITableDto> getTableDtoFromModelId(String modelId, boolean wantPkDto) {
		List<Class<? extends ITableDto>> classes = mapTableDtoListFromModelId.get(modelId.toLowerCase());
		if (ObjectUtils.isEmpty(classes)) {
			return null;
		}

		Class<? extends ITableDto> tableDtoClass = null;
		for (Class<? extends ITableDto> clazz : classes) {
			if (tableDtoClass == null) {
				tableDtoClass = clazz;
				continue;
			}
			if (tableDtoClass.isAssignableFrom(clazz) && wantPkDto) {
				continue;
			}
			tableDtoClass = clazz;
		}
		return tableDtoClass;
	}

	/**
	 * Get the View Dto Class from the Model ID
	 * 
	 * @param modelId The model ID of the DTO
	 * @return The associated DTO class
	 */
	public Class<? extends IViewDto> getViewDtoFromModelId(String modelId) {
		return mapViewDtoFromModelId.get(modelId.toLowerCase());
	}

	/**
	 * Get the View Dto Class from the model ID
	 * 
	 * @param modelId             The model ID of the DTO
	 * @param generateIfNotExists generate the DAO/DTO classes if it doesn't exist
	 * 
	 * @return The associated DTO class
	 */
	public <T extends IViewDto> Class<T> getViewDtoFromModelId(String modelId, boolean generateIfNotExists) {
		if (ObjectUtils.isEmpty(modelId)) {
			return null;
		}

		Class<? extends IViewDao> daoClass = getViewDaoFromModelId(modelId);
		String entityName = getEntityName(daoClass);
		if (daoClass == null && generateIfNotExists) {
			checkDaoExistsAndGenerate(entityName);
		} else if (daoClass == null) {
			return null;
		}

		List<Class<? extends IDto>> classes = mapDtoListFromEntityName.get(entityName.toLowerCase());
		Class<T> dtoClass = getDto(classes, false);
		if (dtoClass == null && !generateIfNotExists) {
			return null;
		}
		return dtoClass;
	}

	/**
	 * Get the Table Translation name of the given DAO
	 * 
	 * @param dao The DAO
	 * @return The related table translation name
	 */
	public String getTableLangName(IDao dao) {
		return getTableLangName(unwrapDaoClass(dao));
	}

	/**
	 * Get the Table Translation name of the given DAO class
	 * 
	 * @param daoClass The DAO class
	 * @return The related table translation name
	 */
	public String getTableLangName(Class<? extends IDao> daoClass) {
		daoClass = getDaoInterface(daoClass);
		return mapEntityLangNameFromDao.get(daoClass);
	}

	/**
	 * Get the list of DAO classes that are translations
	 * 
	 * @return A list of all the DAO classes that are translations
	 */
	public List<Class<? extends IDao>> getAllTableDaoLang() {
		List<Class<? extends IDao>> list = new ArrayList<>();
		for (Entry<Class<? extends IDao>, String> entry : mapEntityLangNameFromDao.entrySet()) {
			if (entry.getValue() != null) {
				list.add(getDaoFromEntityName(entry.getValue(), false));
			}
		}
		return list;
	}

	/**
	 * Get all the registered DAO classes
	 * 
	 * @return A list of all the registered DAO classes
	 */
	public List<Class<? extends IDao>> getAllDaos() {
		List<Class<? extends IDao>> allDaos = new ArrayList<>();
		allDaos.addAll(mapTableDaoFromModelId.values());
		allDaos.addAll(mapViewDaoFromModelId.values());

		return allDaos;
	}

	/**
	 * Get the Table DAO class represented by the given model ID
	 * 
	 * @param modelId The DAO id
	 * @return The related DAO class
	 */
	public Class<? extends ITableDao> getTableDaoFromModelId(String modelId) {
		return mapTableDaoFromModelId.get(modelId);
	}

	/**
	 * Get the View DAO class represented by the given model ID
	 * 
	 * @param modelId The DAO id
	 * @return The related DAO class
	 */
	public Class<? extends IViewDao> getViewDaoFromModelId(String modelId) {
		return mapViewDaoFromModelId.get(modelId);
	}

	/**
	 * Get all registered model IDs
	 * 
	 * @return A list of all the registered model IDs
	 */
	public List<String> getAllModelId() {
		Set<String> set = new HashSet<>();
		set.addAll(mapTableDaoFromModelId.keySet());
		set.addAll(mapViewDaoFromModelId.keySet());

		List<String> list = new ArrayList<>(set);
		Collections.sort(list);

		return list;
	}

	/**
	 * Get the model ID related with the given DTO class
	 * 
	 * @param dtoClass The DTO class
	 * @return The model ID related with the DTO class
	 */
	public Set<String> getModelIdFromDto(Class<? extends IDto> dtoClass) {
		return mapModelIdFromDto.get(dtoClass);
	}

	/**
	 * Get the model ID related with the given DAO class
	 * 
	 * @param daoClass The DAO class
	 * @return The model ID related with the given DAO class
	 */
	public Set<String> getModelIdFromDao(Class<? extends IDao> daoClass) {
		daoClass = getDaoInterface(daoClass);
		return mapModelIdFromDao.get(daoClass);
	}

	/**
	 * Get the DTO related with the given IDao. If specified that wants the PK, the
	 * DTO PK Class will be retrieved. If not, the normal DTO will be given
	 * 
	 * @param daoClass  The DAO class
	 * @param wantPkDto If retrieve the DTO that represents the PK (in case of Table
	 *                  DAO)
	 * @return The associated DTO class
	 */
	public <T extends IDto> Class<T> getDtoFromDao(Class<? extends IDao> daoClass, boolean wantPkDto) {
		List<Class<? extends IDto>> classes = mapDtosFromDao.get(daoClass);
		Class<T> dtoClass = getDto(classes, wantPkDto);
		if (dtoClass == null) {
			return null;
		}
		return dtoClass;
	}

	/**
	 * Get the DAO class related with the given DTO class
	 * 
	 * @param dtoClass The DTO class
	 * @return The related DAO class
	 */
	@SuppressWarnings("unchecked")
	public <T extends IDao> Class<T> getDaoFromDto(Class<? extends IDto> dtoClass) {
		Class<? extends IDao> daoClass = mapDaoFromDto.get(dtoClass);
		return (Class<T>) daoClass;
	}

	/**
	 * Get the list of View DAO classes from the Table DAO class
	 * 
	 * @param tableDaoClass The Table DAO class
	 * @return The list of associated View DAO classes
	 */
	public List<Class<? extends IViewDao>> getViewDaoFromTableDao(Class<? extends ITableDao> tableDaoClass) {
		return mapViewDaoListFromTableDao.get(tableDaoClass);
	}

	/**
	 * Get the Table DAO class from the View DAO class
	 * 
	 * @param viewDaoClass The View DAO class
	 * @return The associated Table DAO class
	 */
	public Class<? extends ITableDao> getTableDaoFromViewDao(Class<? extends IViewDao> viewDaoClass) {
		return mapTableDaoFromViewDao.get(viewDaoClass);
	}

	/**
	 * Get the list of View DTO classes from the Table DTO class
	 * 
	 * @param tableDtoClass The Table DTO class
	 * @return The list of associated View DTO classes
	 */
	public List<Class<? extends IViewDto>> getViewDtoFromTableDto(Class<? extends ITableDto> tableDtoClass) {
		return mapViewDtoListFromTableDto.get(tableDtoClass);
	}

	/**
	 * Get the Table DTO class from the View DTO class
	 * 
	 * @param viewDtoClass The View DTO class
	 * @return The associated Table DAO class
	 */
	public Class<? extends ITableDto> getTableDtoFromViewDto(Class<? extends IViewDto> viewDtoClass) {
		return mapTableDtoFromViewDto.get(viewDtoClass);
	}

	/**
	 * Unwrap the DAO class of the given DAO
	 * 
	 * @param dao The DAO
	 * @return The real DAO class (not a proxy)
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IDao> unwrapDaoClass(IDao dao) {
		if (dao == null) {
			return null;
		}
		Class<? extends IDao> clazz = dao.getDaoClass();
		clazz = getDaoInterface(clazz);
		return clazz;
	}

	private Map<Class<? extends IDao>, Class<? extends IDao>> daoCache = new LinkedHashMap<>();

	/**
	 * Get the DAO interface of the DAO class
	 * 
	 * @param daoClass The DAO class
	 * @return The DAO interface
	 */
	@SuppressWarnings("unchecked")
	private <T extends IDao> Class<? extends T> getDaoInterface(Class<? extends T> daoClass) {
		if (daoClass == null) {
			return null;
		}

		if (daoClass.isInterface()) {
			return daoClass;
		} else {
			Class<? extends T> cacheDaoClass = (Class<? extends T>) daoCache.get(daoClass);
			if (cacheDaoClass == null) {
				for (Class<?> iface : daoClass.getInterfaces()) {
					if (iface.getSimpleName().contains(daoClass.getSimpleName())) {
						cacheDaoClass = (Class<? extends T>) iface;
						daoCache.put(daoClass, cacheDaoClass);
						break;
					}
				}
			}
			return cacheDaoClass;
		}
	}

	/**
	 * Get the real DTO requested (PK or not)
	 * 
	 * @param classes   The list of DTO classes retrieved for a DAO
	 * @param wantPkDto If want the PK or not
	 * @return The real DTO requested
	 */
	@SuppressWarnings("unchecked")
	private <T extends IDto> Class<T> getDto(List<Class<? extends IDto>> classes, boolean wantPkDto) {
		if (ObjectUtils.isEmpty(classes) || classes.size() > 2) {
			return null;
		}

		if (classes.size() == 1) {
			// if it's a view
			return (Class<T>) classes.get(0);
		} else {
			// if it's a table
			Class<? extends IDto> class1 = classes.get(0);
			Class<? extends IDto> class2 = classes.get(1);
			if (wantPkDto) {
				if (class1.isAssignableFrom(class2)) {
					return (Class<T>) class1;
				} else if (class2.isAssignableFrom(class1)) {
					return (Class<T>) class2;
				} else {
					throw new IllegalArgumentException("Bad DTO detected");
				}
			} else {
				if (class1.isAssignableFrom(class2)) {
					return (Class<T>) class2;
				} else if (class2.isAssignableFrom(class1)) {
					return (Class<T>) class1;
				} else {
					throw new IllegalArgumentException("Bad DTO detected");
				}
			}
		}
	}

}
