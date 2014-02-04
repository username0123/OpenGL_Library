package org.grgodgames.lib.game;

/**
 * Description
 *
 * @author GrGod123
 * @version 1.0.0
 */
public interface KeyInput extends Cloneable{
	/**
	 * Is down.
	 *
	 * @return the boolean
	 */
	boolean isDown();
	/**
	 * Sets state.
	 *
	 * @param down    the down
	 */
	void setState(boolean down);
	/**
	 * Is pressed.
	 *
	 * @return the boolean
	 */
	boolean isPressed();
	/**
	 * To int.
	 *
	 * @return the int
	 */
	int toInt();
	/**
	 * Reduce cooldown.
	 *
	 * @param delta    the delta
	 */
	void reduceCooldown(double delta);
	/**
	 * Update void.
	 *
	 * @param delta the delta
	 * @param down the down
	 */
	void update(double delta, boolean down);

	KeyInput copy();
}
