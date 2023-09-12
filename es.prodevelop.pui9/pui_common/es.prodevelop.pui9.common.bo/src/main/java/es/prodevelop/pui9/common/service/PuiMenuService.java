package es.prodevelop.pui9.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMenuDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiMenuService;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiMenuService extends AbstractService<IPuiMenuPk, IPuiMenu, INullView, IPuiMenuDao, INullViewDao>
		implements IPuiMenuService {

	@Override
	public List<IPuiMenu> getMenuForLoggerUser() {
		try {
			Map<Integer, IPuiMenu> menuMap = new LinkedHashMap<>();
			// first of all, get all the menu items from the database
			List<IPuiMenu> allMenu = getAll();

			// then, filter only the items that has not functionality (always should be
			// shown) and those that the user has permission to see, and add all of them to
			// a HashMap
			Set<String> functionalities;
			if (PuiUserSession.getCurrentSession() != null) {
				functionalities = PuiUserSession.getCurrentSession().getFunctionalities();
			} else {
				functionalities = Collections.emptySet();
			}
			allMenu.stream().filter(m -> m.getFunctionality() == null || functionalities.contains(m.getFunctionality()))
					.forEach(m -> menuMap.put(m.getNode(), m));

			// iterate all the HashMap and put those items with a parent inside of them
			menuMap.forEach((id, menu) -> {
				if (menu.getParent() != null) {
					menuMap.get(menu.getParent()).getChildren().add(menu);
				}
			});

			// create the final menu list
			List<IPuiMenu> menu = new ArrayList<>();

			// populate the menu list with all the items in the HashMap that has no parent
			// (first level menu) and its funcationality is null or the user has permission
			menuMap.values().stream()
					.filter(m -> m.getParent() == null
							&& (m.getFunctionality() == null || functionalities.contains(m.getFunctionality())))
					.forEach(menu::add);

			// purgue those menu entries that has no functionality (parent nodes) has no
			// children
			purgueEmptyMenu(menu);

			// sort the menu list by the ID
			sortMenu(menu);

			return menu;
		} catch (PuiServiceGetException e1) {
			return Collections.emptyList();
		}
	}

	private void purgueEmptyMenu(List<IPuiMenu> menu) {
		menu.forEach(m -> purgueEmptyMenu(m.getChildren()));
		menu.removeIf(m -> m.getFunctionality() == null && ObjectUtils.isEmpty(m.getChildren()));
	}

	private void sortMenu(List<IPuiMenu> menu) {
		if (ObjectUtils.isEmpty(menu)) {
			return;
		}

		Collections.sort(menu, new MenuComparator());
		for (IPuiMenu item : menu) {
			sortMenu(item.getChildren());
		}
	}

	private class MenuComparator implements Comparator<IPuiMenu> {

		@Override
		public int compare(IPuiMenu o1, IPuiMenu o2) {
			return o1.getNode().compareTo(o2.getNode());
		}

	}

}