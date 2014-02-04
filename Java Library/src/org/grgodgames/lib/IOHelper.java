package org.grgodgames.lib;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * The type IO helper.
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
@Utility
public final class IOHelper {
	private static final GameLogger LOGGER         = GameLogger.loggerOf(Logger.getLogger(IOHelper.class.getName()));
	private static final char       FILE_SEPARATOR = File.separatorChar;
	public static final  String     LINE_SEPARATOR = System.lineSeparator();
	public static final  Locale     LOCALE         = Locale.getDefault();

	private IOHelper() {}

	/**
	 * Read file.
	 *
	 *
	 *
	 * @param source    the source
	 * @param lineSeparator the line separator
	 * @return the string builder
	 */
	public static StringBuilder readFile(File source, String lineSeparator) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder(256);
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8));
			String line;
			line = reader.readLine();
			while(line != null) {
				builder.append(line);
				builder.append(lineSeparator);
				line = reader.readLine();
			}
		} catch(FileNotFoundException e) {
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning(MessageFormat.format("Failed to find file: {0}", source), e);
			}
		} catch(IOException e) {
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning(MessageFormat.format("Failed to read file: {0}", source), e);
			}
		} finally {
			assert reader != null;
			closeReader(reader);
		}

		return builder;
	}

	/**
	 * Close reader.
	 *
	 * @param reader
	 *   the reader
	 */
	private static void closeReader(Reader reader) {
		try {
			reader.close();
		} catch(IOException e) {
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning("Failed To Close Reader", e);
			}
		}
	}

	public static File toFile(String... path) {
		File file;
		StringBuilder fullPath = new StringBuilder(path[0]);
		for(int i = 1; i < path.length; i++){
			fullPath.append(FILE_SEPARATOR).append(path[i]);
		}
		file= new File(fullPath.toString());
		return file;
	}
}
