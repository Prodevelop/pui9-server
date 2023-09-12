package es.prodevelop.pui9.docgen.list.adapters;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.search.SearchRequest;

/**
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiDocgenTemplateListAdapter implements IListAdapter<IVPuiDocgenTemplate> {

	private static final String WRITE_PUI_DOCGEN_FUNC = "WRITE_PUI_DOCGEN";

	@Override
	public String getFixedFilterParameters(SearchRequest req) {
		FilterBuilder filter = FilterBuilder.newAndFilter();

		Set<String> functionalities;
		if (PuiUserSession.getCurrentSession() != null) {
			functionalities = PuiUserSession.getCurrentSession().getFunctionalities();
		} else {
			functionalities = Collections.emptySet();
		}

		if (!functionalities.contains(WRITE_PUI_DOCGEN_FUNC)) {
			filter.addIsNull(IVPuiDocgenTemplate.MODELS_COLUMN);
		}

		return filter.asFilterGroup().toJson();
	}

}
