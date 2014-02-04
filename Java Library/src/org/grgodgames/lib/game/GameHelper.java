package org.grgodgames.lib.game;


import org.grgodgames.lib.GameLogger;
import org.grgodgames.lib.Handler;
import org.grgodgames.lib.Utility;

import org.lwjgl.Sys;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.util.glu.GLU.gluErrorString;

/**
 * The type Game helper.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
@Utility
public final class GameHelper {
	/** The constant LOGGER. All loggers should be children of this logger */
	private static final Logger     LOGGER = Logger.getLogger(GameHelper.class.getName());
	/** The constant GAMES. */
	private static final List<Game> GAMES  = new ArrayList<>(1);

	/** Instantiates a new Game helper. */
	private GameHelper() {}

	/**
	 * Gets LOGGER.
	 *
	 * @return the LOGGER
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Check for error.
	 *
	 * @return the boolean
	 */
	public static boolean checkForError(GameLogger logger, Class<?> aClass) {return checkForError(logger, aClass, "");}

	/**
	 * Check for error.
	 *
	 * @return the boolean
	 */
	public static boolean checkForError(GameLogger logger, Class<?> aClass, String identifier) {
		boolean error = false;
		int errorCode = glGetError();
		if(errorCode != GL_NO_ERROR) {
			error = true;
			if(logger.isWarningEnabled()) {
				String s = MessageFormat.format("{0} in {1}", identifier, aClass.getSimpleName());
				logger.logWarning(MessageFormat.format("{0}: OpebGL Error: {1}", s, gluErrorString(errorCode)), null);
			}
		}
		return error;
	}

	/**
	 * Gets active game.
	 *
	 * @return the active game
	 */
	public static Game getActiveGame() {
		return GAMES.get(GAMES.size() - 1);
	}

	/**
	 * Destroy void.
	 *
	 * @param game
	 *   the game
	 */
	static void destroy(Game game) {
		for(Handler<?> handler : game.getHandlers()) {
			handler.destroy();
		}
		GAMES.remove(game);
	}

	/**
	 * Gets time.
	 *
	 * @return the time
	 */
	static double getTime() {
		double time = Sys.getTime();
		time /= Sys.getTimerResolution();
		return time * 1000;
	}

	public static void destroy() {
		destroy(getActiveGame());
	}

	/**
	 * Add game.
	 *
	 * @param game
	 *   the game
	 */
	static void addGame(Game game) {
		GAMES.add(game);
	}
}
