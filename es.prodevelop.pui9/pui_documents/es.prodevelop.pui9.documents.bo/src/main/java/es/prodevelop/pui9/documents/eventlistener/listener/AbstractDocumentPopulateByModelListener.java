package es.prodevelop.pui9.documents.eventlistener.listener;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.documents.eventlistener.event.DocumentPopulateEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;

/**
 * Abstract Listener for populating documents. Mark concrete classes with the
 * {@link Component} annotation
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDocumentPopulateByModelListener extends PuiListener<DocumentPopulateEvent> {

	@Override
	protected boolean passFilter(DocumentPopulateEvent event) {
		if (ObjectUtils.isEmpty(event.getSource())) {
			return false;
		}

		return getDocumentModel().equals(event.getSource().get(0).getModel());
	}

	/**
	 * Get the model name of the documents for this listener
	 * 
	 * @return The model name of the documents for this listener
	 */
	protected abstract String getDocumentModel();

}
