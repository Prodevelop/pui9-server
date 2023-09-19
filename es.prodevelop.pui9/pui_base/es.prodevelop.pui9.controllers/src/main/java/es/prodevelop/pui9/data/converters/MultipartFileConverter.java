package es.prodevelop.pui9.data.converters;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.file.AttachmentDefinition;

/**
 * This class allows to set a @RequestParam parameter in your controllers
 * indicating that the type of this parameter is a {@link MultipartFile}.
 * Automatically, the value is converted into a Date using the
 * {@link AttachmentDefinition} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class MultipartFileConverter implements GenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(MultipartFile.class, AttachmentDefinition.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		MultipartFile mf = (MultipartFile) source;
		AttachmentDefinition pdd = new AttachmentDefinition();
		try {
			pdd.setInputStream(mf.getInputStream());
		} catch (IOException e) {
			// do nothing
		}
		pdd.setOriginalFileName(mf.getOriginalFilename());
		pdd.setFileName(FilenameUtils.getBaseName(mf.getOriginalFilename()));
		pdd.setFileExtension(FilenameUtils.getExtension(mf.getOriginalFilename()).toLowerCase());
		pdd.setFileSize(mf.getSize());

		return pdd;
	}

}