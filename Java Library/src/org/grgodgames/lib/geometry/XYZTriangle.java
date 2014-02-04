package org.grgodgames.lib.geometry;

import java.util.Arrays;

/** The type XYZ triangle.
 * @author Greg Brown (GrGod123)
 * @since CORE
 * @version 1.0.0
 */
public final class XYZTriangle implements Triangle {
	/**
	 * The Points.
	 */
	private final Point[] points = new Point[3];
	/**
	 * The Normal.
	 */
	private final Vector normal;

	/**
	 * Instantiates a new XYZ triangle.
	 *
	 * @param point the point
	 * @param point1 the point 1
	 * @param point2 the point 2
	 */
	public XYZTriangle(Point point, Point point1, Point point2) {
		points[0] = point;
		points[1] = point1;
		points[2] = point2;
		normal = initNormal();
		initPoints();
	}

	/**
	 * Init points.
	 */
	private void initPoints() {
		for(Point point : points) {
			point.addTriangles(this);
		}
	}

	/**
	 * Init normal.
	 *
	 * @return the normal
	 */
	private Vector initNormal() {
		Vector vector = points[1].minus(points[0]);
		Vector vector1 = points[2].minus(points[0]);

		return vector.cross(vector1);
	}

	/**
	 * Gets normal.
	 *
	 * @return the normal
	 */
	@Override
	public Vector getNormal() {
		return normal;
	}

	/**
	 * Get points.
	 *
	 * @return the point [ ]
	 */
	@Override
	public Point[] getPoints() {
		Point[] points1 = new Point[points.length];
		System.arraycopy(points, 0, points1, 0, points1.length);
		return points1;
	}

	@Override
	public double[] getNormalArray() {
		return normal.toArray();
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(points);
		result = 31 * result + normal.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		Triangle triangle = (Triangle) obj;

		return normal.equals(triangle.getNormal()) && Arrays.equals(points, triangle.getPoints());
	}
}
