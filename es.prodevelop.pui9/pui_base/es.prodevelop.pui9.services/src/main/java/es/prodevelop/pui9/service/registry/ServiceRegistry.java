package es.prodevelop.pui9.service.registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * This is a Registry for all the Services. It brings some useful methods to
 * manage the Services of PUI
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
@SuppressWarnings("rawtypes")
public class ServiceRegistry {

	@Autowired
	private DaoRegistry daoRegistry;

	private List<Class<? extends IService>> registeredServices;
	private Map<Class<? extends IDto>, Class<? extends IService>> mapServiceFromDto;
	private Map<String, Class<? extends IService>> mapServiceFromModelId;

	private ServiceRegistry() {
		registeredServices = new ArrayList<>();
		mapServiceFromDto = new LinkedHashMap<>();
		mapServiceFromModelId = new LinkedHashMap<>();
	}

	/**
	 * Registers a Service
	 */
	@SuppressWarnings("unchecked")
	public void registerService(Class<? extends IService> serviceClass, Class<? extends ITableDto> tableDtoPkClass,
			Class<? extends ITableDto> tableDtoClass, Class<? extends IViewDto> viewDtoClass,
			Class<? extends ITableDao> tableDaoClass, Class<? extends IViewDao> viewDaoClass) {
		Class<? extends IService> serviceIface = (Class<? extends IService>) serviceClass.getInterfaces()[0];
		if (registeredServices.contains(serviceIface)) {
			return;
		}

		registeredServices.add(serviceIface);

		Class<? extends ITableDto> tableDtoPkIface = (Class<? extends ITableDto>) tableDtoPkClass.getInterfaces()[0];
		Class<? extends ITableDto> tableDtoIface = (Class<? extends ITableDto>) tableDtoClass.getInterfaces()[0];
		Class<? extends IViewDto> viewDtoIface = (Class<? extends IViewDto>) viewDtoClass.getInterfaces()[0];
		Class<? extends ITableDao> tableDaoIface = (Class<? extends ITableDao>) tableDaoClass.getInterfaces()[0];
		Class<? extends IViewDao> viewDaoIface = (Class<? extends IViewDao>) viewDaoClass.getInterfaces()[0];

		daoRegistry.relatedDaos(tableDaoClass, viewDaoClass);
		daoRegistry.relatedDaos(tableDaoIface, viewDaoIface);

		daoRegistry.relatedDtos(tableDtoPkClass, tableDtoClass, viewDtoClass);
		daoRegistry.relatedDtos(tableDtoPkIface, tableDtoIface, viewDtoIface);

		mapServiceFromDto.put(tableDtoPkClass, serviceIface);
		mapServiceFromDto.put(tableDtoPkIface, serviceIface);
		mapServiceFromDto.put(tableDtoClass, serviceIface);
		mapServiceFromDto.put(tableDtoIface, serviceIface);
		mapServiceFromDto.put(viewDtoClass, serviceIface);
		mapServiceFromDto.put(viewDtoIface, serviceIface);
	}

	@SuppressWarnings("unchecked")
	public void registerModelService(String modelId, Class<? extends IService> serviceClass) {
		Class<? extends IService> serviceIface = (Class<? extends IService>) serviceClass.getInterfaces()[0];
		mapServiceFromModelId.put(modelId, serviceIface);
	}

	/**
	 * Get all the registered Services
	 */
	public List<Class<? extends IService>> getAllServices() {
		return registeredServices;
	}

	/**
	 * Get the ID related with the given DTO class
	 */
	public Class<? extends IService> getServiceFromDto(Class<? extends IDto> clazz) {
		return mapServiceFromDto.get(clazz);
	}

	/**
	 * Get the Service class represented by the given Model
	 */
	public Class<? extends IService> getServiceFromModelId(String modelId) {
		return mapServiceFromModelId.get(modelId);
	}

}
