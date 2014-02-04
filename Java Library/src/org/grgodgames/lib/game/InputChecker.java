package org.grgodgames.lib.game;

import org.grgodgames.lib.GameLogger;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * The type Input checker.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
final class InputChecker {
	private static final GameLogger           LOGGER          = GameLogger.loggerOf(Logger.getLogger(InputChecker.class.getName()));
	private static final double               SECOND_DIVISION = 1000.0;
	private final        Collection<KeyInput> keyboard        = new ArrayList<>(32);
	private final double updateTime;
	private final Game   game;
	private final Thread thread;
	private       double time;
	private          boolean updateMouse = true;
	private volatile boolean update      = false;

	/**
	 * Instantiates a new Input checker.
	 *
	 * @param keyboard
	 *   the keyboard
	 * @param game
	 *   the game
	 */
	InputChecker(KeyInput[] keyboard, Game game, double time) {
		thread = new MyThread("input");
		Collections.addAll(this.keyboard, keyboard);
		this.game = game;
		this.time = time;
		this.updateTime = SECOND_DIVISION / game.getMaxFPS();
	}

	public Thread getThread() {
		return thread;
	}

	public void start() {
		if(!thread.isAlive()) {
			thread.start();
		}
		update = true;
	}

	public void stop() {
		update = false;
	}

	/** Update keyboard. */
	private void updateKeyboard(double delta) {
		for(KeyInput key : keyboard) {
			if(update) { key.update(delta, Keyboard.isKeyDown(key.toInt())); }
		}
	}

	/** Update mouse. */
	private void updateMouse() {
		if(updateMouse) {
			updateMouse = false;
			if(LOGGER.isWarningEnabled()) {
				LOGGER.logWarning("InputChecker: updateMouse: STUB", null);
			}
		}
		//TODO updateMouse in InputChecker
	}

	/**
	 * Gets delta.
	 *
	 * @return the delta
	 */
	private double getDelta() {
		double temp = GameHelper.getTime() - time;
		time = GameHelper.getTime();
		return temp;
	}

	private final class MyThread extends Thread {
		private MyThread(String name) {super(name);}

		@Override
		public void run() {
			super.run();
			double runningDelta = 0;
			while(game.isRunning()) {
				double delta = getDelta();
				runningDelta += delta;
				while((runningDelta > updateTime) && update) {
					game.setKeyboard(keyboard);
					updateKeyboard(runningDelta);
					updateMouse();
					runningDelta -= updateTime;
				}
			}
		}
	}
}
