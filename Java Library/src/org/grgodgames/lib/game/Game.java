package org.grgodgames.lib.game;

import org.grgodgames.lib.*;

import org.lwjgl.opengl.Display;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * The type Game.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
public abstract class Game {
	private static final GameLogger LOGGER = GameLogger.loggerOf(Logger.getLogger(Game.class.getName()));

	static {
		LOGGER.setLevel(Level.ALL);
	}

	private static final char                   CLOSE_BRACKET = '}';
	private final        Collection<Handler<?>> handlers      = new ArrayList<>(16);
	private final        List<KeyInput>         keyboard      = new ArrayList<>(32);
	private final        Map<Integer, Boolean>  mouse         = new HashMap<>(8);
	private final double deltaMultiplier;
	private final int    maxFPS;
	private final int    tickRate;
	private final Window window;
	private double  time    = 0.0;
	private boolean running = false;
	private Camera camera;

	/**
	 * Instantiates a new Game.
	 *
	 * @param window
	 *   the window
	 * @param maxFPS
	 *   the max fPS
	 * @param tickRate
	 *   the tick rate
	 * @param deltaMultiplier
	 *   the delta multiplier
	 */
	protected Game(Window window, int maxFPS, int tickRate, double deltaMultiplier) {
		this.window = window;
		this.maxFPS = maxFPS;
		this.tickRate = tickRate;
		this.deltaMultiplier = deltaMultiplier;

		addGame();
	}

	private static void logDebug(String message) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.logDebug(message);
		}
	}

	/**
	 * Add keys.
	 *
	 * @param keys
	 *   the keys
	 */
	public final void addKeys(KeyInput... keys) {
		logDebug(MessageFormat.format("Adding Keys:{0}", Arrays.toString(keys)));
		Collections.addAll(keyboard, keys);
	}

	/**
	 * Init mouse.
	 *
	 * @param buttons
	 *   the buttons
	 */
	public final void addButtons(int... buttons) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.logDebug(MessageFormat.format("Adding Buttons:{0}", Arrays.toString(buttons)));
		}
		for(int i : buttons) {
			mouse.put(i, false);
		}
	}

	/** Run void. */
	public final void runGame() {
		try {
			enableOpenGL();
			initHandlers();
			startRenderLoop();
		} catch(Exception e) {
			if(LOGGER.isErrorEnabled()) {
				LOGGER.logError("Error in running Game", e);
			}
			running = false;
		} finally {
			GameHelper.destroy(this);
		}
	}

	/** Init void. */
	public final void init() {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.logDebug("Initislising Game");
		}
		window.createWindow();
		window.initOpenGL();

		camera = newCamera();
	}

	/**
	 * Add handler.
	 *
	 * @param handler
	 *   the handler
	 */
	public final void addHandler(Handler<?> handler) {
		if(!running) {
			if(!handlers.add(handler)) {
				if(LOGGER.isWarningEnabled()) {
					LOGGER.logWarning(MessageFormat.format("Failed To Add Handler: {0}", handler), null);
				}
			}
		}
	}

	/**
	 * Gets keyboard.
	 *
	 * @return the keyboard
	 */
	public final List<KeyInput> getKeyboard() {
		return Collections.unmodifiableList(clone(keyboard));
	}

	private List<KeyInput> clone(List<KeyInput> keyboard) {
		List<KeyInput> clone = new ArrayList<>(keyboard.size());
		for(int i = 0; i < keyboard.size(); i++){
			clone.add(keyboard.get(i).copy());
		}
		return clone;
	}

	public final void setKeyboard(Collection<KeyInput> keyboard) {
		this.keyboard.addAll(keyboard);
	}

	@Override
	public final int hashCode() {
		int result;
		long temp;
		result = handlers.hashCode();
		result = 31 * result + keyboard.hashCode();
		result = 31 * result + mouse.hashCode();
		temp = Double.doubleToLongBits(deltaMultiplier);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + maxFPS;
		result = 31 * result + tickRate;
		result = 31 * result + window.hashCode();
		return result;
	}

	@Override
	public final boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof Game)) {
			return false;
		}

		Game game = (Game) o;

		if(Double.compare(game.deltaMultiplier, deltaMultiplier) != 0) {
			return false;
		}
		if(maxFPS != game.maxFPS) {
			return false;
		}
		if(tickRate != game.tickRate) {
			return false;
		}
		if(!handlers.equals(game.handlers)) {
			return false;
		}
		if(!keyboard.equals(game.keyboard)) {
			return false;
		}
		if(!mouse.equals(game.mouse)) {
			return false;
		}
		return window.equals(game.window);
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("Tick Rate=").append(tickRate);
		sb.append(", Max FPS=").append(maxFPS);
		sb.append(CLOSE_BRACKET);
		return sb.toString();
	}

	/**
	 * Is running.
	 *
	 * @return the boolean
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Gets handlers.
	 *
	 * @return the handlers
	 */
	public final Iterable<Handler<?>> getHandlers() {
		return Collections.unmodifiableCollection(handlers);
	}

	public final int getMaxFPS() {
		return maxFPS;
	}

	/** Config open gL. */
	protected abstract void configOpenGL();

	/**
	 * Is close not requested.
	 *
	 * @return the boolean
	 */
	protected abstract boolean isCloseNotRequested();

	/**
	 * New camera.
	 *
	 * @return the camera
	 */
	protected abstract Camera newCamera();

	private void addGame() {
		GameHelper.addGame(this);
	}

	private void enableOpenGL() {
		//glEnable(GL_TEXTURE_2D);
		//glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);

		configOpenGL();
		GameHelper.checkForError(LOGGER, Game.class);
	}

	private void initHandlers() {
		for(Handler<?> handler : handlers) {
			handler.init();
		}
	}

	private void startRenderLoop() {
		if(running) {
			if(LOGGER.isErrorEnabled()) {
				LOGGER.logError("Game Is Already Running", null);
			}
			GameHelper.destroy(this);
		}

		running = true;
		time = GameHelper.getTime();

		Thread tickTimer = new TickTimer(time, handlers.toArray(new Handler[handlers.size()]), tickRate, this,
		                                 camera).getThread();
		InputChecker inputChecker = new InputChecker(keyboard.toArray(new KeyInput[keyboard.size()]), this,
		                                       time);

		tickTimer.start();
		inputChecker.start();

		while(running) {
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			double rawDelta = getDelta();
			double delta = rawDelta * deltaMultiplier;

			inputChecker.stop();
			camera.update(delta, getKeyboard());
			inputChecker.start();
			update(delta);
			Display.update();
			Display.sync(maxFPS);
			running = isCloseNotRequested();
		}
	}

	private double getDelta() {
		double temp = GameHelper.getTime() - time;
		time = GameHelper.getTime();
		return temp;
	}

	private void update(double delta) {
		for(Handler<?> handler : handlers) {
			handler.update(delta);
		}
	}
}
