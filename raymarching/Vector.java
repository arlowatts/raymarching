package raymarching;

import java.lang.Math;

/**
A class to store a set of three doubles and perform calculations on them.
*/
public class Vector {
	public double x, y, z;
	
	/**
	Creates a new vector with the specified x, y, and z values.
	
	@param x the x-coordinate of the vector
	@param y the y-coordinate of the vector
	@param z the z-coordinate of the vector
	*/
	public Vector(double x, double y, double z) {set(x, y, z);}
	
	/**
	Creates a new vector from the values of an array with length 3.
	
	@param v an array of three doubles to be the coordinates of the vector
	*/
	public Vector(double[] v) {set(v[0], v[1], v[2]);}
	
	/**
	Creates a new vector that is a copy of another vector.
	
	@param v a Vector to copy
	*/
	public Vector(Vector v) {set(v);}
	
	/**
	Creates a new instance of the zero vector.
	*/
	public Vector() {set(0, 0, 0);}
	
	/**
	Rotates the vector by the Euler angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>).
	
	@param rotation a <code>Vector</code> whose coordinates represent the Euler angles phi, theta, and psi by which to rotate the vector
	*/
	public void rotate(Vector rotation) {
		double[] sincos = getSincos(rotation);
		
		double t;
		
		t = x;
		x = x * sincos[3] - y * sincos[0];
		y = t * sincos[0] + y * sincos[3];
		
		t = y;
		y = y * sincos[4] - z * sincos[1];
		z = t * sincos[1] + z * sincos[4];
		
		t = x;
		x = x * sincos[5] - y * sincos[2];
		y = t * sincos[2] + y * sincos[5];
	}
	
	/**
	Performs a reversed rotation by the Euler angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>).
	
	@param rotation a <code>Vector</code> whose coordinates represent the Euler angles phi, theta, and psi by which to rotate the vector
	*/
	public void inverseRotate(Vector rotation) {
		rotation.multiply(-1);
		double[] sincos = getSincos(rotation);
		rotation.multiply(-1);
		
		double t;
		
		t = x;
		x = x * sincos[5] - y * sincos[2];
		y = t * sincos[2] + y * sincos[5];
		
		t = y;
		y = y * sincos[4] - z * sincos[1];
		z = t * sincos[1] + z * sincos[4];
		
		t = x;
		x = x * sincos[3] - y * sincos[0];
		y = t * sincos[0] + y * sincos[3];
	}
	
	/**
	Rotates the vector by the Euler angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>) about <code>origin</code>.
	
	@param rotation a <code>Vector</code> whose coordinates represent the Euler angles phi, theta, and psi by which to rotate the vector
	@param origin a <code>Vector</code> representing the point about which the vector will be rotated
	*/
	public void rotate(Vector rotation, Vector origin) {
		subtract(origin);
		rotate(rotation);
		add(origin);
	}
	
	/**
	Performs a reversed rotation by the Euler angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>) about <code>origin</code>.
	
	@param rotation a <code>Vector</code> whose coordinates represent the Euler angles phi, theta, and psi by which to rotate the vector
	@param origin a <code>Vector</code> representing the point about which the vector will be rotated
	*/
	public void inverseRotate(Vector rotation, Vector origin) {
		subtract(origin);
		inverseRotate(rotation);
		add(origin);
	}
	
	/*Returns the dot product of itself and another vector.*/
	public double dotProduct(double x1, double y1, double z1) {
		return x*x1 + y*y1 + z*z1;
	}
	
	/*Returns the dot product of itself and another vector.*/
	public double dotProduct(Vector v) {
		return x*v.x + y*v.y + z*v.z;
	}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public void setLength(double length) {
		multiply(length / getLength());
	}
	
	/*Returns the distance between the endpoints of itself and another vector, or the length of their difference.*/
	public double getDistance(double x1, double y1, double z1) {
		return Math.sqrt((x-x1)*(x-x1) + (y-y1)*(y-y1) + (z-z1)*(z-z1));
	}
	
	/*Returns the distance between the endpoints of itself and another vector, or the length of their difference.*/
	public double getDistance(Vector v) {
		return Math.sqrt((x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z));
	}
	
	// Adds the values to itself
	public void add(double x1, double y1, double z1) {
		x += x1;
		y += y1;
		z += z1;
	}
	
	// Adds the vector to itself
	public void add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	// Adds the scaled vector to itself
	public void add(Vector v, double scale) {
		x += v.x * scale;
		y += v.y * scale;
		z += v.z * scale;
	}
	
	// Adds the values to itself
	public void subtract(double x1, double y1, double z1) {
		x -= x1;
		y -= y1;
		z -= z1;
	}
	
	// Subtracts the vector from itself
	public void subtract(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	// Subtracts the scaled vector from itelf
	public void subtract(Vector v, double scale) {
		x -= v.x * scale;
		y -= v.y * scale;
		z -= v.z * scale;
	}
	
	// Multiplies itself by a scalar
	public void multiply(double l) {
		x *= l;
		y *= l;
		z *= l;
	}
	
	// Divides itself by a scalar
	public void divide(double l) {
		l = 1 / l;
		x *= l;
		y *= l;
		z *= l;
	}
	
	// Rescales itself by values lx, ly, lz
	public void stretch(double lx, double ly, double lz) {
		x *= lx;
		y *= ly;
		z *= lz;
	}
	
	public void set(double x1, double y1, double z1) {
		x = x1;
		y = y1;
		z = z1;
	}
	
	public void set(Vector v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public void setPositive() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
	}
	
	public void setNegative() {
		x = -Math.abs(x);
		y = -Math.abs(y);
		z = -Math.abs(z);
	}
	
	public void sign() {
		x = x > 0 ? 1 : -1;
		y = y > 0 ? 1 : -1;
		z = z > 0 ? 1 : -1;
	}
	
	public void copySign(double a, double b, double c) {
		x *= a > 0 ? 1 : -1;
		y *= b > 0 ? 1 : -1;
		z *= c > 0 ? 1 : -1;
	}
	
	public void copySign(Vector v) {
		x *= v.x > 0 ? 1 : -1;
		y *= v.y > 0 ? 1 : -1;
		z *= v.z > 0 ? 1 : -1;
	}
	
	private static double[] getSincos(Vector rotation) {
		double sincos[] = {Math.sin(rotation.x), Math.sin(rotation.y), Math.sin(rotation.z),
						   Math.cos(rotation.x), Math.cos(rotation.y), Math.cos(rotation.z)};
		
		return sincos;
	}
}
