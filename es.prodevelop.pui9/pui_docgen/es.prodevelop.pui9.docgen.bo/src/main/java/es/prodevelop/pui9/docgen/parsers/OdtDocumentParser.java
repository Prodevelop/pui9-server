package es.prodevelop.pui9.docgen.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.InvalidNavigationException;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Node;

import com.google.common.io.Files;

import es.prodevelop.pui9.docgen.dto.DocgenMapping;
import es.prodevelop.pui9.docgen.dto.DocgenMappingList;
import es.prodevelop.pui9.docgen.dto.StringList;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This is a Document Parser for ODT files. It uses the "simple-odf" library
 * from Apache
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class OdtDocumentParser extends AbstractDocumentParser {

	private OdtDocumentParser() {
		DocumentParserRegistry.getSingleton().registerDocumentParser(this);
	}

	@Override
	public String getFileExtension() {
		return "odt";
	}

	@Override
	public List<String> getTags(InputStream inputStream) {
		TextDocument doc = null;
		try {
			doc = TextDocument.loadDocument(inputStream);
			Set<String> tags = getAttributeTags(doc);

			return new ArrayList<>(tags);
		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private Set<String> getAttributeTags(TextDocument doc) {
		Set<String> tags = new HashSet<>();

		try {
			tags.addAll(findTags(doc.getContentRoot(), TAG_ATTRIBUTE_REGEX));
			tags.addAll(findTags(doc.getContentRoot(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getHeader().getOdfElement(), TAG_ATTRIBUTE_REGEX));
			tags.addAll(findTags(doc.getHeader().getOdfElement(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getFooter().getOdfElement(), TAG_ATTRIBUTE_REGEX));
			tags.addAll(findTags(doc.getFooter().getOdfElement(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getStylesDom().getRootElement(), TAG_ATTRIBUTE_REGEX));
			tags.addAll(findTags(doc.getStylesDom().getRootElement(), TAG_SUM_REGEX));
		} catch (Exception e) {
			// do nothing
		}

		return tags;
	}

	private List<SumTag> getSumTags(TextDocument doc) {
		Set<String> tags = new HashSet<>();

		try {
			tags.addAll(findTags(doc.getContentRoot(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getHeader().getOdfElement(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getFooter().getOdfElement(), TAG_SUM_REGEX));
			tags.addAll(findTags(doc.getStylesDom().getRootElement(), TAG_SUM_REGEX));
		} catch (Exception e) {
			// do nothing
		}

		List<SumTag> sumTagList = new ArrayList<>();
		tags.forEach(tag -> {
			SumTag st = new SumTag();
			st.sumTag = TAG_SPECIAL_START + SUM + tag + TAG_SPECIAL_END;
			st.sumAttribute = tag;
			st.value = null;
			sumTagList.add(st);
		});

		return sumTagList;
	}

	private boolean existsChildrenCountTag(TextDocument doc) {
		TextNavigation searchRowIni = new TextNavigation(TAG_CHILDREN_COUNT_REGEX, doc);
		return searchRowIni.nextSelection() != null;
	}

	private Set<String> findTags(Node element, String regex) {
		String content = element.getTextContent();

		if (ObjectUtils.isEmpty(content)) {
			return Collections.emptySet();
		}

		Set<String> tags = new HashSet<>();

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(content);
		while (match.find()) {
			String tag = match.group(1);
			tags.add(tag);
		}

		return tags;
	}

	@Override
	public FileDownload parse(File file, List<IDto> list, List<String> pkFields, DocgenMappingList mapping,
			StringList columnsFilename, boolean isGeneratePdf) throws Exception {
		DateTimeFormatter oldFormatter = formatter;
		if (PuiUserSession.getCurrentSession() != null) {
			formatter = DateTimeFormatter.ofPattern(PuiUserSession.getCurrentSession().getDateformat() + " HH:mm")
					.withZone(PuiUserSession.getCurrentSession().getZoneId());
		}

		boolean hasDetails = checkDocumentHasDetails(file);
		List<DtoDetailsElement> items = parseDtos(list, pkFields, hasDetails);
		boolean oneDocumentPerRegistry = !ObjectUtils.isEmpty(columnsFilename);

		FileDownload fd;
		if (oneDocumentPerRegistry) {
			fd = parseOneDocumentPerRegistry(file, items, mapping, columnsFilename, hasDetails, isGeneratePdf);
		} else {
			fd = parseOneDocumentForAll(file, items, mapping, hasDetails, isGeneratePdf);
		}

		formatter = oldFormatter;

		return fd;
	}

	private FileDownload parseOneDocumentPerRegistry(File file, List<DtoDetailsElement> items,
			List<DocgenMapping> mapping, List<String> columnsFilename, boolean hasDetails, boolean isGeneratePdf)
			throws Exception {
		String itemExtension;
		if (isGeneratePdf) {
			itemExtension = "pdf";
		} else {
			itemExtension = getFileExtension();
		}

		Map<String, Integer> mapDocumentTimes = new LinkedHashMap<>();

		String zipFilename = Files.getNameWithoutExtension(file.getName());
		zipFilename = zipFilename.substring(0, zipFilename.lastIndexOf('_')) + ".zip";
		File zipFile = new File(FileUtils.getTempDirectory(), zipFilename + "_" + System.currentTimeMillis());
		FileOutputStream zipFos = new FileOutputStream(zipFile);

		try (ZipOutputStream zos = new ZipOutputStream(zipFos)) {
			for (Iterator<DtoDetailsElement> it = items.iterator(); it.hasNext();) {
				TextDocument doc = TextDocument.loadDocument(file);
				List<SumTag> sumTagList = null;
				Boolean existsChildrenCountTag = null;

				if (sumTagList == null) {
					sumTagList = getSumTags(doc);
				}
				if (existsChildrenCountTag == null) {
					existsChildrenCountTag = existsChildrenCountTag(doc);
				}

				DtoDetailsElement next = it.next();
				processDocument(doc, next, mapping, hasDetails, sumTagList, existsChildrenCountTag);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				doc.save(out);
				doc.close();

				InputStream is = new ByteArrayInputStream(out.toByteArray());
				out.close();

				InputStream isFinal;
				if (isGeneratePdf) {
					isFinal = getPdfConverter().convert(is);
				} else {
					isFinal = is;
				}

				try {
					StringBuilder nameSb = new StringBuilder();
					for (Iterator<String> itName = columnsFilename.iterator(); itName.hasNext();) {
						String columnFilename = itName.next();
						Field nameField = DtoRegistry.getJavaFieldFromAllFields(next.getDto().getClass(),
								columnFilename);
						Object val = nameField.get(next.getDto());
						String partialName = "";
						if (val != null) {
							partialName = val.toString();
						}
						nameSb.append(partialName);
						if (itName.hasNext() && nameSb.length() > 0) {
							nameSb.append("_");
						}
					}

					String name = nameSb.toString();
					if (ObjectUtils.isEmpty(name)) {
						name = String.valueOf(System.currentTimeMillis());
					}

					mapDocumentTimes.put(name, mapDocumentTimes.getOrDefault(name, -1) + 1);
					Integer times = mapDocumentTimes.get(name);
					String suffix = times == 0 ? "" : "_" + times;

					ZipEntry ze = new ZipEntry(name + suffix + "." + itemExtension);
					zos.putNextEntry(ze);
					zos.write(IOUtils.toByteArray(isFinal));
					zos.closeEntry();
				} finally {
					isFinal.close();
				}
			}
		}

		zipFos.close();

		InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(zipFile));
		FileUtils.deleteQuietly(zipFile);

		return new FileDownload(is, zipFilename);
	}

	private FileDownload parseOneDocumentForAll(File file, List<DtoDetailsElement> items, List<DocgenMapping> mapping,
			boolean hasDetails, boolean isGeneratePdf) throws Exception {
		// parse the document for each DTO
		TextDocument document = null;
		List<SumTag> sumTagList = null;
		Boolean existsChildrenCountTag = null;
		for (Iterator<DtoDetailsElement> it = items.iterator(); it.hasNext();) {
			TextDocument doc = TextDocument.loadDocument(file);
			if (sumTagList == null) {
				sumTagList = getSumTags(doc);
			}
			if (existsChildrenCountTag == null) {
				existsChildrenCountTag = existsChildrenCountTag(doc);
			}

			DtoDetailsElement next = it.next();
			processDocument(doc, next, mapping, hasDetails, sumTagList, existsChildrenCountTag);

			if (document == null) {
				document = doc;
			} else {
				document.insertContentFromDocumentAfter(doc, document.getParagraphByReverseIndex(0, false), true);
				doc.close();
			}

			if (it.hasNext() && isGeneratePdf) {
				document.addPageBreak();
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (document != null) {
			document.save(out);
			document.close();
		}

		InputStream is = new ByteArrayInputStream(out.toByteArray());
		out.close();

		InputStream isFinal;
		if (isGeneratePdf) {
			isFinal = getPdfConverter().convert(is);
		} else {
			isFinal = is;
		}

		String name = Files.getNameWithoutExtension(file.getName());
		String extension;
		if (isGeneratePdf) {
			extension = "pdf";
		} else {
			extension = getFileExtension();
		}

		name = name.substring(0, name.lastIndexOf('_'));

		return new FileDownload(isFinal, name + "." + extension);
	}

	private boolean checkDocumentHasDetails(File file) throws Exception {
		TextDocument document = TextDocument.loadDocument(file);
		boolean hasDetails = documentHasDetails(document);
		document.close();
		return hasDetails;
	}

	/**
	 * Process the given document
	 */
	private void processDocument(TextDocument document, DtoDetailsElement dde, List<DocgenMapping> mapping,
			boolean hasDetails, List<SumTag> sumTagList, boolean existsChildrenCountTag)
			throws InvalidNavigationException {
		if (hasDetails) {
			processDetails(document, dde, mapping, sumTagList);
		}
		processMainDocument(document, dde, mapping, sumTagList, existsChildrenCountTag);
	}

	/**
	 * Process the details of the document
	 */
	private void processDetails(TextDocument document, DtoDetailsElement dde, List<DocgenMapping> mapping,
			List<SumTag> sumTagList) throws InvalidNavigationException {
		OdfElement rowIni = getRowIniText(document).getElement();
		OdfElement rowEnd = getRowEndText(document).getElement();
		Node parentNode = rowIni.getParentNode();

		// collect all the nodes that are involved in the detail section
		List<Node> detailNodes = new ArrayList<>();
		int rowIniIndex = -1;
		for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
			Node child = parentNode.getChildNodes().item(i);
			if (child.equals(rowEnd)) {
				break;
			}
			if (child.equals(rowIni)) {
				rowIniIndex = i;
				continue;
			}
			if (rowIniIndex >= 0) {
				detailNodes.add(child);
			}
		}

		// clone and duplicate all the detail nodes, one for each detail (minus
		// one, because the existing nodes are valid ;-) )
		for (int i = 1; i < dde.getDetails().size(); i++) {
			for (Node detailNode : detailNodes) {
				Node clonedNode = detailNode.cloneNode(true);
				parentNode.insertBefore(clonedNode, rowEnd);
			}
		}

		// for each detail and mapping,
		for (IDto detailDto : dde.getDetails()) {
			for (DocgenMapping mapp : mapping) {
				String tag = TAG_START + mapp.getTag() + TAG_END;
				Optional<SumTag> stOpt = sumTagList.stream().filter(st -> st.sumAttribute.equals(mapp.getTag()))
						.findFirst();
				if (stOpt.isPresent()) {
					SumTag st = stOpt.get();
					if (st.value == null) {
						if (DtoRegistry.getNumericFields(detailDto.getClass()).contains(st.sumAttribute)) {
							st.value = Integer.parseInt("0");
						} else if (DtoRegistry.getFloatingFields(detailDto.getClass()).contains(st.sumAttribute)) {
							st.value = new BigDecimal("0");
						}
					}
					String value = getValue(mapp, detailDto, false);
					if (NumberUtils.isCreatable(value)) {
						if (DtoRegistry.getNumericFields(detailDto.getClass()).contains(st.sumAttribute)) {
							Integer val = NumberUtils.createInteger(value);
							st.value = st.value == null ? val : ((Integer) st.value) + val;
						} else if (DtoRegistry.getFloatingFields(detailDto.getClass()).contains(st.sumAttribute)) {
							BigDecimal val = NumberUtils.createBigDecimal(value);
							st.value = st.value == null ? val : ((BigDecimal) st.value).add(val);
						}
					}
				}
				String value = getValue(mapp, detailDto, true);
				substituteTagByText(document, tag, value, true);
			}
		}

		// remove the markers of the detail section
		rowIni.getParentNode().removeChild(rowIni);
		rowEnd.getParentNode().removeChild(rowEnd);
	}

	/**
	 * Process the main document
	 */
	private void processMainDocument(TextDocument document, DtoDetailsElement dde, List<DocgenMapping> mapping,
			List<SumTag> sumTagList, boolean existsChildrenCountTag) throws InvalidNavigationException {
		for (DocgenMapping mapp : mapping) {
			String tag = TAG_START + mapp.getTag() + TAG_END;
			String value = getValue(mapp, dde.getDto(), true);
			substituteTagByText(document, tag, value, false);
		}

		for (SumTag st : sumTagList) {
			String value = valueAsString(st.value, true);
			substituteTagByText(document, st.sumTag, value, false);
		}

		if (existsChildrenCountTag) {
			substituteTagByText(document, TAG_CHILDREN_COUNT_REGEX, String.valueOf(dde.getDetails().size()), false);
		}
	}

	/**
	 * Do the substitution of the given tag with the given value in the document.
	 * You can specify if only the first matching should be substituted or all the
	 * matchings
	 */
	private void substituteTagByText(TextDocument document, String tag, String value, boolean onlyOne)
			throws InvalidNavigationException {
		TextNavigation searchText = new TextNavigation(tag, document);
		while (searchText.hasNext()) {
			TextSelection item = (TextSelection) searchText.nextSelection();
			item.replaceWith(!ObjectUtils.isEmpty(value) ? value : "");
			if (onlyOne) {
				break;
			}
		}
	}

	/**
	 * Check if the given document has details
	 */
	private boolean documentHasDetails(TextDocument document) {
		TextSelection itemIni = getRowIniText(document);
		TextSelection itemEnd = getRowEndText(document);

		return itemIni != null && itemEnd != null;
	}

	/**
	 * Obtain the row init node
	 */
	private TextSelection getRowIniText(TextDocument document) {
		TextNavigation searchRowIni = new TextNavigation(ROW_INI_REGEX, document);
		return (TextSelection) searchRowIni.nextSelection();
	}

	/**
	 * Obtain the row end node
	 */
	private TextSelection getRowEndText(TextDocument document) {
		TextNavigation searchRowEnd = new TextNavigation(ROW_END_REGEX, document);
		return (TextSelection) searchRowEnd.nextSelection();
	}

	private class SumTag {
		private String sumTag;
		private String sumAttribute;
		private Object value;
	}

}
