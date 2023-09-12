package es.prodevelop.pui9.docgen.parsers;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import es.prodevelop.pui9.docgen.dto.DocgenMappingList;
import es.prodevelop.pui9.docgen.dto.StringList;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Interface for representing Document Template Parsers
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDocumentParser {

	String TAG_START = "[{][$]";
	String TAG_END = "[$][}]";
	String TAG_SPECIAL_START = "[<][$]";
	String TAG_SPECIAL_END = "[$][>]";
	String ANY_CHARACTER = "([^{]*)";
	String ANY_CHARACTER_SUM = "([^$]*)";
	String SUM = "SUM.";
	String ROW_INI = "ROW_INI";
	String ROW_END = "ROW_END";
	String CHILDREN_COUNT = "CHILDREN_COUNT";

	String TAG_ATTRIBUTE_REGEX = TAG_START + ANY_CHARACTER + TAG_END;
	String ROW_INI_REGEX = TAG_SPECIAL_START + ROW_INI + TAG_SPECIAL_END;
	String ROW_END_REGEX = TAG_SPECIAL_START + ROW_END + TAG_SPECIAL_END;
	String TAG_SUM_REGEX = TAG_SPECIAL_START + SUM + ANY_CHARACTER_SUM + TAG_SPECIAL_END;
	String TAG_CHILDREN_COUNT_REGEX = TAG_SPECIAL_START + CHILDREN_COUNT + TAG_SPECIAL_END;

	/**
	 * Get the File Extension that this Parser supports
	 */
	String getFileExtension();

	/**
	 * Get the list of found tags in the given File (represented by the InputStream)
	 */
	List<String> getTags(InputStream inputStream);

	/**
	 * Parse the given template file using the list of given DTOs. The list of PK
	 * fields that represents the DTOs should be given when the template has
	 * details. Also the list of tag mapping should be provided
	 */
	FileDownload parse(File file, List<IDto> list, List<String> pkFields, DocgenMappingList mapping,
			StringList columnsFilename, boolean isGeneratePdf) throws Exception;

}
