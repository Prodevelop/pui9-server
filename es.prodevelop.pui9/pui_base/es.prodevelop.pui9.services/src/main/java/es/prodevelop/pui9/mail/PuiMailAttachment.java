package es.prodevelop.pui9.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;

/**
 * This class represents an attachment for the email
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMailAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filename;
	private String contentType;
	private byte[] content;

	public PuiMailAttachment() {
	}

	public PuiMailAttachment(String filename, String contentType, byte[] content) {
		this.filename = filename;
		this.contentType = contentType;
		this.content = content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * Get an attachment from the local disk with the given full path
	 * 
	 * @param fullPath The full path of the file
	 * @return The attachment
	 * @throws IOException If the file doesn't exist
	 */
	public static PuiMailAttachment getFileFromLocal(String fullPath) throws IOException {
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		FileInputStream fis = new FileInputStream(file);
		return getFileFromInputStream(fis, file.getName());
	}

	/**
	 * Get an attachment from the classpath (as path)
	 * 
	 * @param filePath The path of the file
	 * @return The attachment
	 * @throws IOException If the file doesn't exist
	 */
	public static PuiMailAttachment getFileFromClasspath(String filePath) throws IOException {
		InputStream is = PuiClassLoaderUtils.getClassLoader().getResourceAsStream(filePath);
		if (is == null) {
			throw new FileNotFoundException();
		}

		return getFileFromInputStream(is, filePath);
	}

	/**
	 * Get the file attachment from an input stream
	 * 
	 * @param is       The input stream of the file
	 * @param filePath The path of the file
	 * @return The attachment
	 * @throws IOException If any error occurs while reading the input stream
	 */
	public static PuiMailAttachment getFileFromInputStream(InputStream is, String filePath) throws IOException {
		PuiMailAttachment fileDto = new PuiMailAttachment();
		fileDto.setFilename(filePath);
		fileDto.setContentType(URLConnection.guessContentTypeFromStream(is));
		fileDto.setContent(IOUtils.toByteArray(is));

		is.close();

		return fileDto;
	}

}
