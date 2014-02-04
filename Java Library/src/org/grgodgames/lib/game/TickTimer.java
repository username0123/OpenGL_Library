package org.grgodgames.lib.game;

import org.grgodgames.lib.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The type Tick timer.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
final class TickTimer {
	/** The Handlers. */
	private final Collection<Handler<?>> handlers = new ArrayList<>(8);
	/** The Tick rate. */
	private final int    tickRate;
	/** The Game. */
	private final Game   game;
	/** The Camera. */
	private final Camera camera;
	private final Thread thread;
	/** The Time. */
	private       double time;

	/**
	 * Instantiates a new Tick timer.
	 *
	 * @param time
	 *   the time
	 * @param handlers
	 *   the handlers
	 * @param tickRate
	 *   the tick rate
	 * @param game
	 *   the game
	 * @param camera
	 *   the camera
	 */
	TickTimer(double time, Handler<?>[] handlers, int tickRate, Game game, Camera camera) {
		thread = new MyThread();
		this.time = time;
		Collections.addAll(this.handlers, handlers);
		this.tickRate = tickRate;
		this.game = game;
		this.camera = camera;
	}

	public Thread getThread() {
		return thread;
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

	private void tickHandlers() {
		for(Handler<?> handler : handlers) {
			handler.tick();
		}
	}

	private final class MyThread extends Thread {
		private MyThread() {
			super("Tick Timer");
		}

		/** Run void. */
		@Override
		public void run() {
			super.run();
			double tickCount = 0;
			while(game.isRunning()) {
				tickCount += getDelta();
				if(tickCount > tickRate) {
					tickHandlers();
					camera.tick();
					tickCount -= tickRate;
				}
			}
		}
	}
}
