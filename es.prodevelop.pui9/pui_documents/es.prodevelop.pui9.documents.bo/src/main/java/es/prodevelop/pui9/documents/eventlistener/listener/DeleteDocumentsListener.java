package es.prodevelop.pui9.documents.eventlistener.listener;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;

/**
 * Delete all the documents associated with the deleted registry
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DeleteDocumentsListener extends PuiListener<DeleteDaoEvent> {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiDocumentService documentService;

	@Override
	protected boolean passFilter(DeleteDaoEvent event) {
		Set<String> models = daoRegistry.getModelIdFromDto(event.getSource().getClass());
		List<String> modelsWithDocuments = documentService.getModelsWithDocuments();
		return !ObjectUtils.isEmpty(models) && CollectionUtils.containsAny(modelsWithDocuments, models);
	}

	@Override
	protected void process(DeleteDaoEvent event) throws PuiException {
		new Thread(() -> {
			List<IPuiDocument> documents = documentService.getDtoDocuments(event.getSource());
			for (IPuiDocument doc : documents) {
				try {
					documentService.delete(doc.createPk());
				} catch (PuiServiceDeleteException e) {
					// do nothing
				}
			}
		}, "DELETE_DTO_DOCUMENTS").start();
	}

}
