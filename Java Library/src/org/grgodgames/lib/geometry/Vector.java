package org.grgodgames.lib.geometry;

/**
 * The interface Vector.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0.0
 * @since CORE
 */
public interface Vector {
	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	int getSize();
	/**
	 * To array.
	 *
	 * @return the double [ ]
	 */
	double[] toArray();
	/**
	 * Cross vector.
	 *
	 * @param vector
	 *   the vector
	 *
	 * @return the vector
	 */
	Vector cross(Vector vector);
	/**
	 * To array.
	 *
	 * @param size
	 *   the size
	 *
	 * @return the double [ ]
	 */
	double[] toArray(int size);
	/**
	 * Multiply vector.
	 *
	 * @param multiplier
	 *   the multiplier
	 *
	 * @return the vector
	 */
	Vector multiply(double multiplier);
	/** Normalise void.  @return the vector */
	Vector normalise();

	/**
	 * Sets length.
	 *
	 * @param length
	 *   the length
	 *
	 * @return the length
	 */
	Vector setLength(double length);
}
