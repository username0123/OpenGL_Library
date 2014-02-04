package org.grgodgames.lib;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import java.util.logging.Handler;

/**
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
public final class GameLogger {
	private static final Logger LOGGER = Logger.getLogger(GameLogger.class.getName());
	private final Logger logger;

	static {
		for(Handler handler : LOGGER.getHandlers()){
			LOGGER.removeHandler(handler);
		}
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		LOGGER.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}

	private GameLogger(Logger logger) {this.logger = logger;}

	public static GameLogger loggerOf(Logger logger) {
		GameLogger gameLogger = new GameLogger(logger);
		gameLogger.setLevel(LOGGER.getLevel());
		gameLogger.logger.setParent(LOGGER);
		return gameLogger;
	}

	public static void setLogFile(File file) throws IOException {
		Handler handler = new FileHandler(file.getCanonicalPath());
		handler.setLevel(LOGGER.getLevel());
		LOGGER.addHandler(handler);
	}

	public void logDebug(String message) {
		if(logger.isLoggable(Level.FINE)) {
			logger.fine(message);
		}
	}

	public boolean isDebugEnabled() {
		return isLoggable(Level.FINE);
	}

	public void setLevel(Level level) {
		logger.setLevel(level);
	}

	public boolean isWarningEnabled() {
		return isLoggable(Level.WARNING);
	}

	public void logWarning(String message, Exception e) {
		logger.log(Level.WARNING, message, e);
	}

	public boolean isErrorEnabled() {
		return isLoggable(Level.SEVERE);
	}

	public void logError(String message, Exception e) {
		logger.log(Level.SEVERE, message, e);
	}

	private boolean isLoggable(Level level) {return logger.isLoggable(level);}
}
