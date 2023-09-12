package es.prodevelop.pui9.lang;

import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;

import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.utils.PuiLanguage;

public class LanguageThreadLocal {

	private static LanguageThreadLocal singleton;

	public static LanguageThreadLocal getSingleton() {
		if (singleton == null) {
			singleton = new LanguageThreadLocal();
		}
		return singleton;
	}

	private ThreadLocal<PuiLanguage> threadLocal;

	private LanguageThreadLocal() {
		threadLocal = new ThreadLocal<>();
	}

	public void setData(String langs) {
		if (ObjectUtils.isEmpty(langs)) {
			return;
		}

		List<LanguageRange> allLangs = Locale.LanguageRange.parse(langs);
		if (ObjectUtils.isEmpty(allLangs)) {
			return;
		}

		String langTag = allLangs.get(0).getRange();
		Locale locale = Locale.forLanguageTag(langTag);
		setData(new PuiLanguage(locale));
	}

	public void setData(PuiLanguage lang) {
		threadLocal.set(lang);
	}

	public PuiLanguage getData() {
		return threadLocal.get();
	}

	public void removeData() {
		threadLocal.remove();
	}

}
