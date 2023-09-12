package es.prodevelop.pui9.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.Files;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This is a helper class to specify that a Controller returns a File. You can
 * use an existing File or an input stream
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FileDownload implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private File file;
	private InputStream inputStream;
	private byte[] bytes;
	private String filename;
	private Boolean downloadable = true;

	public FileDownload(File file, String filename) {
		this(file, filename, true);
	}

	public FileDownload(File file, String filename, Boolean downloadable) {
		this.file = file;
		this.filename = filename;
		this.downloadable = downloadable;
	}

	public FileDownload(InputStream inputStream, String filename) {
		this(inputStream, filename, true);
	}

	public FileDownload(InputStream inputStream, String filename, Boolean downloadable) {
		this.inputStream = inputStream;
		this.filename = filename;
		this.downloadable = downloadable;
	}

	public FileDownload(byte[] bytes, String filename) {
		this(bytes, filename, true);
	}

	public FileDownload(byte[] bytes, String filename, Boolean downloadable) {
		this.bytes = bytes;
		this.filename = filename;
		this.downloadable = downloadable;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInputStream() {
		if (inputStream != null) {
			return inputStream;
		} else if (bytes != null) {
			return new ByteArrayInputStream(bytes);
		} else if (file != null) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public byte[] getBytes() {
		if (bytes != null) {
			return bytes;
		} else if (file != null) {
			try {
				return Files.toByteArray(file);
			} catch (IOException e) {
				return new byte[0];
			}
		} else if (inputStream != null) {
			byte[] array;
			try {
				array = new byte[inputStream.available()];
			} catch (IOException e1) {
				return new byte[0];
			}
			try {
				while (inputStream.read(array) > 0) {
					// do nothing
				}
			} catch (IOException e) {
				return new byte[0];
			}
			return array;
		} else {
			return new byte[0];
		}
	}

	public String getFilename() {
		return filename;
	}

	public Boolean isDownloadable() {
		return downloadable;
	}

}
