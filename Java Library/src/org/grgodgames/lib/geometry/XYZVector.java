package org.grgodgames.lib.geometry;

import java.util.Arrays;

/** The type XYZ vector. */
public final class XYZVector implements Vector {
	/** The constant SIZE. */
	private static final int      SIZE  = 3;
	/** The Value. */
	private final        double[] value = new double[SIZE];

	/**
	 * Instantiates a new XYZ vector.
	 *
	 * @param value
	 *   the value
	 */
	public XYZVector(double... value) {
		int length = (value.length < SIZE) ? value.length : SIZE;
		System.arraycopy(value, 0, this.value, 0, length);
	}

	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	/**
	 * Equals boolean.
	 *
	 * @param o
	 *   the o
	 *
	 * @return the boolean
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if((o == null) || (getClass() != o.getClass())) {
			return false;
		}

		Vector vector = (Vector) o;

		return Arrays.equals(toArray(SIZE), vector.toArray(SIZE));
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

	/**
	 * To array.
	 *
	 * @return the double [ ]
	 */
	@Override
	public double[] toArray() {
		double[] doubles = new double[SIZE];
		System.arraycopy(value, 0, doubles, 0, doubles.length);
		return doubles;
	}

	/**
	 * Cross vector.
	 *
	 * @param vector
	 *   the vector
	 *
	 * @return the vector
	 */
	@Override
	public Vector cross(Vector vector) {
		if(vector.getSize() != SIZE) {
			throw new IllegalArgumentException("The vectors are of different sizes");
		}
		double[] thisVec = toArray();
		double[] thatVec = vector.toArray();

		double x = (thisVec[1] * thatVec[2]) - (thisVec[2] * thatVec[1]);
		double y = (thisVec[2] * thatVec[0]) - (thisVec[0] * thatVec[2]);
		double z = (thisVec[0] * thatVec[1]) - (thisVec[1] * thatVec[0]);

		return new XYZVector(x,y,z);
	}

	@Override
	public Vector multiply(double multiplier) {
		for(int i = 0; i < SIZE; i++) {
			value[i] *= multiplier;
		}

		return new XYZVector(value);
	}

	@Override
	public Vector normalise() {
		double length = getLength();
		for(int i = 0; i < SIZE; i++) {
			value[i] /= length;
		}
		return new XYZVector(value);
	}

	private double getLength() {
		double length = 0;
		for(double aValue : value) {
			length += aValue * aValue;
		}
		return Math.sqrt(length);
	}

	@Override
	public Vector setLength(double length) {
		normalise();
		for(int i = 0; i < SIZE; i++) {
			value[i] *= length;
		}
		return new XYZVector(value);
	}
}
