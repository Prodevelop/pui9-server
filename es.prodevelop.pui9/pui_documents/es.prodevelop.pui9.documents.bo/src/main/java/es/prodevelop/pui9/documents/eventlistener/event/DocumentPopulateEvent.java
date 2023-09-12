package es.prodevelop.pui9.documents.eventlistener.event;

import java.util.List;

import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;

/**
 * Event for populate a list of documents
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DocumentPopulateEvent extends PuiEvent<List<IVPuiDocument>> {

	private static final long serialVersionUID = 1L;

	public DocumentPopulateEvent(List<IVPuiDocument> documents) {
		super(documents);
	}

}
