package org.grgodgames.lib.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type XYZ point.
 *
 * @author Greg Brown (GrGod123)
 * @version 1.0
 * @since 0.0
 */
public final class XYZPoint implements Point {
	/** The constant SIZE. */
	private static final int            SIZE      = 3;
	/** The Triangles. */
	private final        List<Triangle> triangles = new ArrayList<>(16);
	/** The Coords. */
	private final        double[]       coords    = new double[3];
	/** The Color. */
	private Color    color;
	/** The Normal. */
	private Vector   normal;
	/** The Tex coord. */
	private TexCoord texCoord;

	/**
	 * Instantiates a new XYZ point.
	 *
	 * @param coords
	 *   the coords
	 */
	public XYZPoint(double... coords) {
		setCoords(coords);
	}

	@Override
	public Point newPoint(double... doubles) {
		return new XYZPoint(doubles);
	}

	private void setNormal(Vector normal) {
		this.normal = normal.normalise();
	}

	@Override
	public void translate(Vector vector) {
		double[] translate = vector.toArray(SIZE);
		for(int i = 0; i < coords.length; i++) {
			coords[i] += translate[i];
		}
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public double[] toArray() {
		double[] doubles = new double[coords.length];
		System.arraycopy(coords, 0, doubles, 0, doubles.length);
		return doubles;
	}

	@Override
	public Vector minus(Point point) {
		double[] thisPoint = toArray(SIZE);
		double[] thatPoint = point.toArray(SIZE);

		double[] doubles = new double[SIZE];
		for(int i = 0; i < doubles.length; i++) {
			doubles[i] = thisPoint[i] - thatPoint[i];
		}

		return new XYZVector(doubles);
	}

	@Override
	public void addTriangles(Triangle... triangles) {
		Collections.addAll(this.triangles, triangles);
	}

	@Override
	public void round(int places) {
		multiply(StrictMath.pow(10, places));

		for(int i = 0; i < coords.length; i++) {
			coords[i] = Math.round(coords[i]);
		}

		multiply(StrictMath.pow(0.1, places));
	}

	@Override
	public void setCoords(double... coords) {
		int length = (coords.length < this.coords.length) ? coords.length : this.coords.length;
		System.arraycopy(coords, 0, this.coords, 0, length);
	}

	@Override
	public double[] toArray(int size) {
		double[] doubles = new double[size];
		if(size > SIZE) {
			System.arraycopy(toArray(), 0, doubles, 0, toArray().length);
		} else {
			for(int i = 0; i < doubles.length; i++) {
				doubles[i] = toArray()[i];
			}
		}
		return doubles;
	}

	@Override
	public byte[] getColorArray() {
		assert color != null;
		return color.toArray();
	}

	@Override
	public int getTexCoordSize() {
		assert texCoord != null;
		return texCoord.getSize();
	}

	@Override
	public int getColorSize() {
		assert color != null;
		return color.getSize();
	}

	@Override
	public short[] getTexCoordArray() {
		assert texCoord != null;
		return texCoord.toArray();
	}

	@Override
	public double[] getNormalArray(Triangle triangle) {
		assert normal != null;
		double[] normal = this.normal.toArray();
		if(triangles.contains(triangle)) {
			normal = triangles.get(triangles.indexOf(triangle)).getNormalArray();
		}
		return normal;
	}

	@Override
	public void initPoint(TexCoord texCoord, Vector normal, Color color) {
		this.texCoord = texCoord;
		setNormal(normal);
		this.color = color;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(coords);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof Point)) {
			return false;
		}

		Point xyzPoint = (Point) o;

		return Arrays.equals(coords, xyzPoint.toArray());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("%1$05.2f").append(", ");
		sb.append("%2$05.2f").append(", ");
		sb.append("%3$05.2f").append('}');
		return String.format(sb.toString(), coords[0], coords[1], coords[2]);
	}

	/**
	 * Multiply void.
	 *
	 * @param i
	 *   the i
	 */
	private void multiply(double i) {
		for(int j = 0; j < coords.length; j++) {
			coords[j] *= i;
		}
	}
}
