package org.grgodgames.lib.geometry;

/**
 * The interface Point.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0
 * @since Core
 */
public interface Point {
	/**
	 * New point.
	 *
	 * @param doubles
	 *   the doubles
	 *
	 * @return the point
	 */
	Point newPoint(double... doubles);

	/**
	 * Translate void.
	 *
	 * @param vector
	 *   the vector
	 */
	void translate(Vector vector);

	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	int getSize();

	/**
	 * Get coordinates.
	 *
	 * @return the double [ ]
	 */
	double[] toArray();

	/**
	 * Minus vector.
	 *
	 * @param point
	 *   the point
	 *
	 * @return the vector
	 */
	Vector minus(Point point);

	/**
	 * Add triangles.
	 *
	 * @param triangles
	 *   the triangles
	 */
	void addTriangles(Triangle... triangles);

	/**
	 * Round void.
	 *
	 * @param places
	 *   the places
	 */
	void round(int places);

	/**
	 * Set void.
	 *
	 * @param coords
	 *   the coords
	 */
	void setCoords(double... coords);

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
	 * Get color array.
	 *
	 * @return the double [ ]
	 */
	byte[] getColorArray();

	/**
	 * Gets tex coord size.
	 *
	 * @return the tex coord size
	 */
	int getTexCoordSize();

	/**
	 * Gets color size.
	 *
	 * @return the color size
	 */
	int getColorSize();

	/**
	 * Get tex coord array.
	 *
	 * @return the double [ ]
	 */
	short[] getTexCoordArray();

	/**
	 * Get normal array.
	 *
	 * @param triangle
	 *   the triangle
	 *
	 * @return the double [ ]
	 */
	double[] getNormalArray(Triangle triangle);

	/**
	 * Init point.
	 *
	 * @param texCoord
	 *   the tex coord
	 * @param normal
	 *   the normal
	 * @param color
	 *   the color
	 */
	void initPoint(TexCoord texCoord, Vector normal, Color color);
}
