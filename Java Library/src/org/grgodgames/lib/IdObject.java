package org.grgodgames.lib;

/** The interface IdObject.
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
public interface  IdObject {
	/** Init void. */
	void init();
	/** Destroy void. */
	void destroy();
	/** Tick void. */
	void tick();
	/**
	 * Update void.
	 *
	 * @param delta the delta
	 */
	void update(double delta);
}
