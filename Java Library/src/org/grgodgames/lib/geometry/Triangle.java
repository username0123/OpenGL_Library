package org.grgodgames.lib.geometry;

/**
 * The interface Triangle.
 */
public interface Triangle {
	/**
	 * Gets normal.
	 *
	 * @return the normal
	 */
	Vector getNormal();
	/**
	 * Get points.
	 *
	 * @return the point [ ]
	 */
	Point[] getPoints();
	double[] getNormalArray();
}
