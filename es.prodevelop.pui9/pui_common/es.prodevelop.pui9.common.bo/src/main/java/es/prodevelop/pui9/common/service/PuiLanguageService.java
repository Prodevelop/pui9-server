package es.prodevelop.pui9.common.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiLanguageDao;
import es.prodevelop.pui9.common.model.dto.PuiLanguagePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLanguageDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.common.service.interfaces.IPuiLanguageService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.exceptions.AbstractPuiDaoException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.messages.PuiMessagesRegistry;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
@Service
public class PuiLanguageService
		extends AbstractService<IPuiLanguagePk, IPuiLanguage, IVPuiLanguage, IPuiLanguageDao, IVPuiLanguageDao>
		implements IPuiLanguageService {

	@PostConstruct
	private void postConstructLanguageService() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadLanguages", true, 0, 1, TimeUnit.HOURS,
				this::reloadLanguages);
	}

	@Override
	protected void afterInsert(IPuiLanguage dto) throws PuiServiceException {
		new Thread(new InsertTranslationsRunnable(new PuiLanguage(dto.getIsocode()),
				PuiLanguageUtils.getDefaultLanguage()), "PuiThread_DuplicateDataForNewLanguage").start();

		if (dto.getIsdefault().equals(PuiConstants.TRUE_INT)) {
			try {
				IPuiLanguage oldDefaultLang = getTableDao()
						.findOne(new PuiLanguagePk(PuiLanguageUtils.getDefaultLanguage().getIsocode()));
				oldDefaultLang.setIsdefault(PuiConstants.FALSE_INT);
				getTableDao().update(oldDefaultLang);
			} catch (AbstractPuiDaoException e) {
				// do nothing
			}
			PuiLanguageUtils.getDefaultLanguage().setIsdefault(PuiConstants.FALSE_INT);
		}

		PuiLanguageUtils.addLanguage(convert(dto));
	}

	@Override
	protected void afterUpdate(IPuiLanguage oldDto, IPuiLanguage dto) throws PuiServiceException {
		if (!oldDto.getIsdefault().equals(dto.getIsdefault())) {
			try {
				if (dto.getIsdefault().equals(PuiConstants.TRUE_INT)) {
					IPuiLanguage oldDefaultLang = getTableDao()
							.findOne(new PuiLanguagePk(PuiLanguageUtils.getDefaultLanguage().getIsocode()));
					oldDefaultLang.setIsdefault(PuiConstants.FALSE_INT);
					getTableDao().update(oldDefaultLang);
					PuiLanguageUtils.getDefaultLanguage().setIsdefault(PuiConstants.FALSE_INT);

					for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
						PuiLanguage next = it.next();
						if (next.getIsocode().equals(dto.getIsocode())) {
							next.setIsdefault(PuiConstants.TRUE_INT);
							break;
						}
					}
				} else {
					for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
						PuiLanguage next = it.next();
						if (!next.getIsocode().equals(dto.getIsocode())) {
							next.setIsdefault(PuiConstants.TRUE_INT);
							IPuiLanguage newDefaultLang = getTableDao().findOne(new PuiLanguagePk(next.getIsocode()));
							newDefaultLang.setIsdefault(PuiConstants.TRUE_INT);
							getTableDao().update(newDefaultLang);
							break;
						}
					}
				}
			} catch (AbstractPuiDaoException e) {
				// do nothing
			}
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void beforeDelete(IPuiLanguage dto) throws PuiServiceException {
		List<Class<? extends IDao>> daoClasses = getDaoRegistry().getAllTableDaoLang();
		if (daoClasses.isEmpty()) {
			return;
		}

		PuiLanguage lang = new PuiLanguage(dto.getIsocode());

		for (Class<? extends IDao> daoClass : daoClasses) {
			ITableDao tableDao = (ITableDao) PuiApplicationContext.getInstance().getBean(daoClass);
			if (tableDao == null) {
				continue;
			}

			try {
				tableDao.deleteAll(lang);
			} catch (PuiDaoDeleteException e) {
				throw new PuiServiceDeleteException(e);
			}
		}
	}

	@Override
	protected void afterDelete(IPuiLanguage dto) throws PuiServiceException {
		if (dto.getIsdefault().equals(PuiConstants.TRUE_INT)) {
			try {
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage next = it.next();
					if (!next.getIsocode().equals(dto.getIsocode())) {
						next.setIsdefault(PuiConstants.TRUE_INT);
						IPuiLanguage newDefaultLang = getTableDao().findOne(new PuiLanguagePk(next.getIsocode()));
						newDefaultLang.setIsdefault(PuiConstants.TRUE_INT);
						getTableDao().update(newDefaultLang);
						break;
					}
				}
			} catch (AbstractPuiDaoException e) {
				// do nothing
			}
		}

		PuiLanguageUtils.removeLanguage(dto.getIsocode());
	}

	@Override
	public boolean exists(IPuiLanguagePk dtoPk, PuiLanguage language) throws PuiServiceExistsException {
		return PuiLanguageUtils.existLanguage(dtoPk.getIsocode());
	}

	@Override
	public IPuiLanguage getNew(PuiLanguage language) throws PuiServiceNewException {
		return new es.prodevelop.pui9.common.model.dto.PuiLanguage();
	}

	@Override
	public List<IPuiLanguage> getAll() throws PuiServiceGetException {
		List<IPuiLanguage> list = new ArrayList<>();
		for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
			list.add(convert(it.next()));
		}
		return list;
	}

	@Override
	public IPuiLanguagePk getDefaultLanguage() {
		return convert(PuiLanguageUtils.getDefaultLanguage());
	}

	private void reloadLanguages() {
		List<IPuiLanguage> allLangs;
		try {
			allLangs = getTableDao().findAll();
		} catch (PuiDaoFindException e) {
			allLangs = Collections.emptyList();
		}

		if (allLangs.isEmpty()) {
			return;
		}

		PuiLanguageUtils.clearLanguages();

		for (IPuiLanguage lang : allLangs) {
			PuiLanguageUtils.addLanguage(convert(lang));
		}

		if (!PuiLanguageUtils.hasLanguages()) {
			PuiLanguageUtils.addLanguage(PuiLanguage.DEFAULT_LANG);
		}

		reloadPuiMessagesRegistry();
	}

	private void reloadPuiMessagesRegistry() {
		List<String> langs = new ArrayList<>();
		for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
			langs.add(it.next().getIsocode());
		}
		PuiMessagesRegistry.getSingleton().setAvailableLanguages(langs.toArray(new String[0]));
		PuiMessagesRegistry.getSingleton().setDefaultLanguage(getDefaultLanguage().getIsocode());
	}

	private PuiLanguage convert(IPuiLanguage origLang) {
		PuiLanguage lang = new PuiLanguage();
		PuiObjectUtils.copyProperties(lang, origLang);
		return lang;
	}

	private IPuiLanguage convert(PuiLanguage origLang) {
		IPuiLanguage lang = new es.prodevelop.pui9.common.model.dto.PuiLanguage();
		PuiObjectUtils.copyProperties(lang, origLang);
		return lang;
	}

	/**
	 * Duplicate the records for every table that has language support. Using Java
	 * reflection, it's able to know which are the DAO elements that has translation
	 * table associated, and retrieve all the records for the default language in
	 * the database. Then, an insert operation is performed for every one of them,
	 * but changing the language to the new one
	 */
	private class InsertTranslationsRunnable implements Runnable {
		private PuiLanguage newLanguage;
		private PuiLanguage defaultLanguage;

		public InsertTranslationsRunnable(PuiLanguage newLanguage, PuiLanguage defaultLanguage) {
			this.newLanguage = newLanguage;
			this.defaultLanguage = defaultLanguage;
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void run() {
			List<Class<? extends IDao>> daoClasses = getDaoRegistry().getAllTableDaoLang();
			if (daoClasses.isEmpty()) {
				return;
			}

			for (Class<? extends IDao> daoClass : daoClasses) {
				ITableDao tableDao = (ITableDao) PuiApplicationContext.getInstance().getBean(daoClass);
				if (tableDao == null) {
					continue;
				}

				Class<? extends ITableDto> dtoClass = getDaoRegistry().getDtoFromDao(daoClass, false);
				if (dtoClass == null) {
					continue;
				}

				Field langField = DtoRegistry.getJavaFieldFromFieldName(dtoClass, IDto.LANG_FIELD_NAME);
				Field langStatusField = DtoRegistry.getJavaFieldFromFieldName(dtoClass, IDto.LANG_STATUS_FIELD_NAME);

				SearchRequest req = new SearchRequest();
				req.setRows(1000);
				req.setOrder(DtoRegistry.getPkFields(dtoClass).stream().map(Order::newOrderAsc)
						.collect(Collectors.toList()));
				req.setFilter(FilterBuilder.newAndFilter()
						.addEqualsExact(IDto.LANG_COLUMN_NAME, defaultLanguage.getIsocode()).asFilterGroup());

				tableDao.executePaginagedOperation(req, null, list -> {
					try {
						for (ITableDto dto : (List<ITableDto>) list) {
							FieldUtils.writeField(langField, dto, newLanguage.getIsocode());
							FieldUtils.writeField(langStatusField, dto, PuiConstants.FALSE_INT);
						}
						tableDao.bulkInsert((List<ITableDto>) list);
					} catch (PuiDaoInsertException | IllegalArgumentException | IllegalAccessException e) {
						// do nothing
					}
				});
			}

			logger.info("Duplicate done for language '" + newLanguage.getIsocode() + "'");
		}
	}

}