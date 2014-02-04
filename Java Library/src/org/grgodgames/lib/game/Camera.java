package org.grgodgames.lib.game;

import java.util.List;

/** The interface Camera.
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
public interface Camera {
	/**
	 * Update void.
	 *
	 * @param delta
	 *   the delta
	 * @param keyboard
	 */
	void update(double delta, Iterable<KeyInput> keyboard);
	/** Tick void. */
	void tick();
}
