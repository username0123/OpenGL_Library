package org.grgodgames.lib;

/**
 * The interface Handler.
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 */
public interface Handler<T extends IdObject> {
	/**
	 * Init void.
	 */
	void init();
	/**
	 * Update void.
	 *
	 * @param delta the delta
	 */
	void update(double delta);
	/**
	 * Destroy void.
	 */
	void destroy();
	/**
	 * Tick void.
	 */
	void tick();
	/**
	 * Add object.
	 *
	 * @param t the t
	 */
	void addObject(T t);
}
