/*
 * Java CSV is a stream based library for reading and writing
 * CSV and other delimited data.
 *   
 * Copyright (C) Bruce Dunwiddie bruce@csvreader.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package es.prodevelop.pui9.csv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A stream based writer for writing delimited text data to a file or a stream.
 */
public class CsvWriter {
	private Writer outputStream = null;

	private String fileName = null;

	private boolean firstColumn = true;

	private boolean useCustomRecordDelimiter = false;

	private Charset charset = null;

	// this holds all the values for switches that the user is allowed to set
	private UserSettings userSettings = new UserSettings();

	private boolean initialized = false;

	private boolean closed = false;

	private String systemRecordDelimiter = System.getProperty("line.separator");

	private Integer currentLine = 1;

	/**
	 * Double up the text qualifier to represent an occurrence of the text
	 * qualifier.
	 */
	public static final int ESCAPE_MODE_DOUBLED = 1;

	/**
	 * Use a backslash character before the text qualifier to represent an
	 * occurrence of the text qualifier.
	 */
	public static final int ESCAPE_MODE_BACKSLASH = 2;

	/**
	 * Creates a {@link CsvWriter CsvWriter} object using a file as the data
	 * destination.
	 * 
	 * @param fileName  The path to the file to output the data.
	 * @param delimiter The character to use as the column delimiter.
	 * @param charset   The {@link java.nio.charset.Charset Charset} to use while
	 *                  writing the data.
	 */
	public CsvWriter(String fileName, char delimiter, Charset charset) {
		if (fileName == null) {
			throw new IllegalArgumentException("Parameter fileName can not be null.");
		}

		if (charset == null) {
			throw new IllegalArgumentException("Parameter charset can not be null.");
		}

		this.fileName = fileName;
		userSettings.Delimiter = delimiter;
		this.charset = charset;
	}

	/**
	 * Creates a {@link CsvWriter CsvWriter} object using a file as the data
	 * destination.&nbsp;Uses a comma as the column delimiter and UTF8 as the
	 * {@link java.nio.charset.Charset Charset}.
	 * 
	 * @param fileName The path to the file to output the data.
	 */
	public CsvWriter(String fileName) {
		this(fileName, Letters.SEMICOLON.code, StandardCharsets.UTF_8);
	}

	/**
	 * Creates a {@link CsvWriter CsvWriter} object using a Writer to write data to.
	 * 
	 * @param outputStream The stream to write the column delimited data to.
	 * @param delimiter    The character to use as the column delimiter.
	 */
	public CsvWriter(Writer outputStream, char delimiter) {
		if (outputStream == null) {
			throw new IllegalArgumentException("Parameter outputStream can not be null.");
		}

		this.outputStream = outputStream;
		userSettings.Delimiter = delimiter;
		initialized = true;
	}

	/**
	 * Creates a {@link CsvWriter CsvWriter} object using an OutputStream to write
	 * data to.
	 * 
	 * @param outputStream The stream to write the column delimited data to.
	 * @param charset      The charset of the output stream
	 */
	public CsvWriter(OutputStream outputStream, Charset charset) {
		this(new OutputStreamWriter(outputStream, charset), Letters.SEMICOLON.code);
	}

	/**
	 * Creates a {@link CsvWriter CsvWriter} object using an OutputStream to write
	 * data to.
	 * 
	 * @param outputStream The stream to write the column delimited data to.
	 * @param delimiter    The character to use as the column delimiter.
	 * @param charset      The {@link java.nio.charset.Charset Charset} to use while
	 *                     writing the data.
	 */
	public CsvWriter(OutputStream outputStream, char delimiter, Charset charset) {
		this(new OutputStreamWriter(outputStream, charset), delimiter);
	}

	/**
	 * Gets the character being used as the column delimiter.
	 * 
	 * @return The character being used as the column delimiter.
	 */
	public char getDelimiter() {
		return userSettings.Delimiter;
	}

	/**
	 * Sets the character to use as the column delimiter.
	 * 
	 * @param delimiter The character to use as the column delimiter.
	 */
	public void setDelimiter(char delimiter) {
		userSettings.Delimiter = delimiter;
	}

	public char getRecordDelimiter() {
		return userSettings.RecordDelimiter;
	}

	/**
	 * Sets the character to use as the record delimiter.
	 * 
	 * @param recordDelimiter The character to use as the record delimiter. Default
	 *                        is combination of standard end of line characters for
	 *                        Windows, Unix, or Mac.
	 */
	public void setRecordDelimiter(char recordDelimiter) {
		useCustomRecordDelimiter = true;
		userSettings.RecordDelimiter = recordDelimiter;
	}

	/**
	 * Gets the character to use as a text qualifier in the data.
	 * 
	 * @return The character to use as a text qualifier in the data.
	 */
	public char getTextQualifier() {
		return userSettings.TextQualifier;
	}

	/**
	 * Sets the character to use as a text qualifier in the data.
	 * 
	 * @param textQualifier The character to use as a text qualifier in the data.
	 */
	public void setTextQualifier(char textQualifier) {
		userSettings.TextQualifier = textQualifier;
	}

	/**
	 * Whether text qualifiers will be used while writing data or not.
	 * 
	 * @return Whether text qualifiers will be used while writing data or not.
	 */
	public boolean getUseTextQualifier() {
		return userSettings.UseTextQualifier;
	}

	/**
	 * Sets whether text qualifiers will be used while writing data or not.
	 * 
	 * @param useTextQualifier Whether to use a text qualifier while writing data or
	 *                         not.
	 */
	public void setUseTextQualifier(boolean useTextQualifier) {
		userSettings.UseTextQualifier = useTextQualifier;
	}

	public int getEscapeMode() {
		return userSettings.EscapeMode;
	}

	public void setEscapeMode(int escapeMode) {
		userSettings.EscapeMode = escapeMode;
	}

	public void setComment(char comment) {
		userSettings.Comment = comment;
	}

	public char getComment() {
		return userSettings.Comment;
	}

	/**
	 * Whether fields will be surrounded by the text qualifier even if the qualifier
	 * is not necessarily needed to escape this field.
	 * 
	 * @return Whether fields will be forced to be qualified or not.
	 */
	public boolean getForceQualifier() {
		return userSettings.ForceQualifier;
	}

	/**
	 * Use this to force all fields to be surrounded by the text qualifier even if
	 * the qualifier is not necessarily needed to escape this field. Default is
	 * false.
	 * 
	 * @param forceQualifier Whether to force the fields to be qualified or not.
	 */
	public void setForceQualifier(boolean forceQualifier) {
		userSettings.ForceQualifier = forceQualifier;
	}

	/**
	 * Writes another column of data to this record.
	 * 
	 * @param content        The data for the new column.
	 * @param preserveSpaces Whether to preserve leading and trailing whitespace in
	 *                       this column of data.
	 * @exception IOException Thrown if an error occurs while writing data to the
	 *                        destination stream.
	 */
	public void write(String content, boolean preserveSpaces) throws IOException {
		checkClosed();

		checkInit();

		if (content == null) {
			content = "";
		}

		if (!firstColumn) {
			outputStream.write(userSettings.Delimiter);
		}

		boolean textQualify = userSettings.ForceQualifier;

		if (!preserveSpaces && content.length() > 0) {
			content = content.trim();
		}

		if (!textQualify && userSettings.UseTextQualifier
				&& (content.indexOf(userSettings.TextQualifier) > -1 || content.indexOf(userSettings.Delimiter) > -1
						|| (!useCustomRecordDelimiter
								&& (content.indexOf(Letters.LF.code) > -1 || content.indexOf(Letters.CR.code) > -1))
						|| (useCustomRecordDelimiter && content.indexOf(userSettings.RecordDelimiter) > -1)
						|| (firstColumn && content.length() > 0 && content.charAt(0) == userSettings.Comment) ||
						// check for empty first column, which if on its own line must
						// be qualified or the line will be skipped
						(firstColumn && content.length() == 0))) {
			textQualify = true;
		}

		if (userSettings.UseTextQualifier && !textQualify && content.length() > 0 && preserveSpaces) {
			char firstLetter = content.charAt(0);

			if (firstLetter == Letters.SPACE.code || firstLetter == Letters.TAB.code) {
				textQualify = true;
			}

			if (!textQualify && content.length() > 1) {
				char lastLetter = content.charAt(content.length() - 1);

				if (lastLetter == Letters.SPACE.code || lastLetter == Letters.TAB.code) {
					textQualify = true;
				}
			}
		}

		if (textQualify) {
			outputStream.write(userSettings.TextQualifier);

			if (userSettings.EscapeMode == ESCAPE_MODE_BACKSLASH) {
				content = replace(content, "" + Letters.BACKSLASH.code,
						"" + Letters.BACKSLASH.code + Letters.BACKSLASH.code);
				content = replace(content, "" + userSettings.TextQualifier,
						"" + Letters.BACKSLASH.code + userSettings.TextQualifier);
			} else {
				content = replace(content, "" + userSettings.TextQualifier,
						"" + userSettings.TextQualifier + userSettings.TextQualifier);
			}
		} else if (userSettings.EscapeMode == ESCAPE_MODE_BACKSLASH) {
			content = replace(content, "" + Letters.BACKSLASH.code,
					"" + Letters.BACKSLASH.code + Letters.BACKSLASH.code);
			content = replace(content, "" + userSettings.Delimiter,
					"" + Letters.BACKSLASH.code + userSettings.Delimiter);

			if (useCustomRecordDelimiter) {
				content = replace(content, "" + userSettings.RecordDelimiter,
						"" + Letters.BACKSLASH.code + userSettings.RecordDelimiter);
			} else {
				content = replace(content, "" + Letters.CR.code, "" + Letters.BACKSLASH.code + Letters.CR.code);
				content = replace(content, "" + Letters.LF.code, "" + Letters.BACKSLASH.code + Letters.LF.code);
			}

			if (firstColumn && content.length() > 0 && content.charAt(0) == userSettings.Comment) {
				if (content.length() > 1) {
					content = "" + Letters.BACKSLASH.code + userSettings.Comment + content.substring(1);
				} else {
					content = "" + Letters.BACKSLASH.code + userSettings.Comment;
				}
			}
		}

		outputStream.write(content);

		if (textQualify) {
			outputStream.write(userSettings.TextQualifier);
		}

		firstColumn = false;
	}

	/**
	 * Writes another column of data to this record.&nbsp;Does not preserve leading
	 * and trailing whitespace in this column of data.
	 * 
	 * @param content The data for the new column.
	 * @exception IOException Thrown if an error occurs while writing data to the
	 *                        destination stream.
	 */
	public void write(String content) throws IOException {
		write(content, false);
	}

	public void writeComment(String commentText) throws IOException {
		checkClosed();

		checkInit();

		outputStream.write(userSettings.Comment);

		outputStream.write(commentText);

		if (useCustomRecordDelimiter) {
			outputStream.write(userSettings.RecordDelimiter);
		} else {
			outputStream.write(systemRecordDelimiter);
		}

		firstColumn = true;
	}

	/**
	 * Writes a new record using the passed in array of values.
	 * 
	 * @param values         Values to be written.
	 * 
	 * @param preserveSpaces Whether to preserver leading and trailing spaces in
	 *                       columns while writing out to the record or not.
	 * 
	 * @throws IOException Thrown if an error occurs while writing data to the
	 *                     destination stream.
	 */
	public void writeRecord(String[] values, boolean preserveSpaces) throws IOException {
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				write(values[i], preserveSpaces);
			}

			endRecord();
		}
	}

	/**
	 * Writes a new record using the passed in array of values.
	 * 
	 * @param values Values to be written.
	 * 
	 * @throws IOException Thrown if an error occurs while writing data to the
	 *                     destination stream.
	 */
	public void writeRecord(String[] values) throws IOException {
		writeRecord(values, false);
	}

	/**
	 * Ends the current record by sending the record delimiter.
	 * 
	 * @exception IOException Thrown if an error occurs while writing data to the
	 *                        destination stream.
	 */
	public void endRecord() throws IOException {
		checkClosed();

		checkInit();

		if (useCustomRecordDelimiter) {
			outputStream.write(userSettings.RecordDelimiter);
		} else {
			outputStream.write(systemRecordDelimiter);
		}

		firstColumn = true;
		currentLine++;
	}

	public Integer getCurrentLine() {
		return currentLine;
	}

	/**
	 * 
	 */
	private void checkInit() throws IOException {
		if (!initialized) {
			if (fileName != null) {
				outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), charset));
			}

			initialized = true;
		}
	}

	/**
	 * Clears all buffers for the current writer and causes any buffered data to be
	 * written to the underlying device.
	 * 
	 * @exception IOException Thrown if an error occurs while writing data to the
	 *                        destination stream.
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * Closes and releases all related resources.
	 */
	public void close() {
		if (!closed) {
			close(true);

			closed = true;
		}
	}

	/**
	 * 
	 */
	private void close(boolean closing) {
		if (!closed) {
			if (closing) {
				charset = null;
			}

			try {
				if (initialized) {
					outputStream.close();
				}
			} catch (Exception e) {
				// just eat the exception
			}

			outputStream = null;

			closed = true;
		}
	}

	/**
	 * 
	 */
	private void checkClosed() throws IOException {
		if (closed) {
			throw new IOException("This instance of the CsvWriter class has already been closed.");
		}
	}

	/**
	 * 
	 */
	protected void finalize() {
		close(false);
	}

	private enum Letters {
		LF('\n'),

		CR('\r'),

		QUOTE('\"'),

		SPACE(' '),

		TAB('\t'),

		POUND('#'),

		BACKSLASH('\\'),

		NULL('\0'),

		SEMICOLON(';'),

		COMMA(',');

		public final char code;

		private Letters(char code) {
			this.code = code;
		}
	}

	private class UserSettings {
		// having these as publicly accessible members will prevent
		// the overhead of the method call that exists on properties
		public char TextQualifier;

		public boolean UseTextQualifier;

		public char Delimiter;

		public char RecordDelimiter;

		public char Comment;

		public int EscapeMode;

		public boolean ForceQualifier;

		public UserSettings() {
			TextQualifier = Letters.QUOTE.code;
			UseTextQualifier = true;
			Delimiter = Letters.SEMICOLON.code;
			RecordDelimiter = Letters.NULL.code;
			Comment = Letters.POUND.code;
			EscapeMode = ESCAPE_MODE_DOUBLED;
			ForceQualifier = false;
		}
	}

	public static String replace(String original, String pattern, String replace) {
		final int len = pattern.length();
		int found = original.indexOf(pattern);

		if (found > -1) {
			StringBuilder sb = new StringBuilder();
			int start = 0;

			while (found != -1) {
				sb.append(original.substring(start, found));
				sb.append(replace);
				start = found + len;
				found = original.indexOf(pattern, start);
			}

			sb.append(original.substring(start));

			return sb.toString();
		} else {
			return original;
		}
	}
}