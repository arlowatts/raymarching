package raymarching;

import java.lang.Math;

/**
A 3-dimensional vector.
*/
public class Vector {
	/**
	The x coordinate of the vector.
	*/
	public double x;
	/**
	The y coordinate of the vector.
	*/
	public double y;
	/**
	The z coordinate of the vector.
	*/
	public double z;
	
	/**
	Creates a new vector with the coordinates <code>x</code>, <code>y</code>, <code>z</code>.
	
	@param x the x coordinate of the vector
	@param y the y coordinate of the vector
	@param z the z coordinate of the vector
	*/
	public Vector(double x, double y, double z) {set(x, y, z);}
	
	/**
	Creates a new vector from the values of an array of length 3.
	
	@param v an array of three doubles to be the coordinates of the vector
	*/
	public Vector(double[] v) {set(v[0], v[1], v[2]);}
	
	/**
	Creates a copy of a vector.
	
	@param v the vector to copy
	*/
	public Vector(Vector v) {set(v);}
	
	/**
	Creates a new instance of the zero vector.
	*/
	public Vector() {set(0, 0, 0);}
	
	/**
	Rotates the vector by the angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>).
	
	@param rotation a vector whose coordinates represent the Euler angles phi, theta, and psi by which to rotate
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
	Rotates the vector by the angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>) about <code>origin</code>.
	
	@param rotation a vector whose coordinates represent the Euler angles phi, theta, and psi by which to rotate
	@param origin a vector representing the point about which to rotate
	*/
	public void rotate(Vector rotation, Vector origin) {
		subtract(origin);
		rotate(rotation);
		add(origin);
	}
	
	/**
	Reverses a rotation by the angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>).
	
	@param rotation a vector whose coordinates represent the Euler angles phi, theta, and psi by which to rotate
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
	Reverses a rotation by the angles (<code>rotation.x</code>, <code>rotation.y</code>, <code>rotation.z</code>) about <code>origin</code>.
	
	@param rotation a vector whose coordinates represent the Euler angles phi, theta, and psi by which to rotate
	@param origin a vector representing the point about which to rotate
	*/
	public void inverseRotate(Vector rotation, Vector origin) {
		subtract(origin);
		inverseRotate(rotation);
		add(origin);
	}
	
	/**
	Computes the dot product of this vector and another.
	
	@param x the x coordinate of the other vector
	@param y the y coordinate of the other vector
	@param z the z coordinate of the other vector
	*/
	public double dotProduct(double x, double y, double z) {
		return this.x*x + this.y*y + this.z*z;
	}
	
	/**
	Computes the dot product of this vector and another.
	
	@param v the other vector
	*/
	public double dotProduct(Vector v) {
		return x*v.x + y*v.y + z*v.z;
	}
	
	/**
	Computes the length of the vector.
	*/
	public double getLength() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	Scales the vector to the specified length.
	
	@param length the target length of the vector
	*/
	public void setLength(double length) {
		multiply(length / getLength());
	}
	
	/**
	Computes the length of the difference between this vector and another.
	
	@param x the x coordinate of the other vector
	@param y the y coordinate of the other vector
	@param z the z coordinate of the other vector
	*/
	public double getDistance(double x, double y, double z) {
		return Math.sqrt((this.x-x)*(this.x-x) + (this.y-y)*(this.y-y) + (this.z-z)*(this.z-z));
	}
	
	/**
	Computes the length of the difference between this vector and another.
	
	@param v the other vector
	*/
	public double getDistance(Vector v) {
		return Math.sqrt((x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z));
	}
	
	/**
	Adds the components of another vector to this vector.
	
	@param x the x coordinate of the other vector
	@param y the y coordinate of the other vector
	@param z the z coordinate of the other vector
	*/
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	/**
	Adds the components of another vector to this vector.
	
	@param v the other vector
	*/
	public void add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	/**
	Adds the components of another vector to this vector, multiplied by a scaling value.
	
	@param v the other vector
	@param scale the multiplicative scaling value
	*/
	public void add(Vector v, double scale) {
		x += v.x * scale;
		y += v.y * scale;
		z += v.z * scale;
	}
	
	/**
	Subtracts the components of another vector from this vector.
	
	@param x the x coordinate of the other vector
	@param y the y coordinate of the other vector
	@param z the z coordinate of the other vector
	*/
	public void subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	
	/**
	Subtracts the components of another vector from this vector.
	
	@param v the other vector
	*/
	public void subtract(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	/**
	Subtracts the components of another vector from this vector, multiplied by a scaling value.
	
	@param v the other vector
	@param scale the multiplying value
	*/
	public void subtract(Vector v, double scale) {
		x -= v.x * scale;
		y -= v.y * scale;
		z -= v.z * scale;
	}
	
	/**
	Multiplies the vector by a scalar value.
	
	@param scale the multiplying value
	*/
	public void multiply(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}
	
	/**
	Divides the vector by a scalar value.
	
	@param scale the dividing value
	*/
	public void divide(double scale) {
		scale = 1 / scale;
		x *= scale;
		y *= scale;
		z *= scale;
	}
	
	/**
	Multiplies the x, y, and z coordinates of the vector by <code>scalex</code>, <code>scaley</code>, and <code>scalez</code> respectively.
	
	@param scalex the multiplicative value of the x coordinate
	@param scaley the multiplicative value of the y coordinate
	@param scalez the multiplicative value of the z coordinate
	*/
	public void stretch(double scalex, double scaley, double scalez) {
		x *= scalex;
		y *= scaley;
		z *= scalez;
	}
	
	/**
	Multiplies the x, y, and z coordinates of the vector by the x, y, and z coordinates of <code>v</code>, respectively.
	
	@param v the other vector
	*/
	public void stretch(Vector v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}
	
	/**
	Sets the coordinates of the vector.
	
	@param x the x coordinate of the vector
	@param y the y coordinate of the vector
	@param z the z coordinate of the vector
	*/
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	Copies the coordinates of another vector.
	
	@param v the other vector
	*/
	public void set(Vector v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	/**
	Takes the positive absolute value of each component individually.
	*/
	public void setPositive() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
	}
	
	/**
	Takes the negative absolute value of each component individually.
	*/
	public void setNegative() {
		x = -Math.abs(x);
		y = -Math.abs(y);
		z = -Math.abs(z);
	}
	
	/**
	Sets the absolute value of each component to 1, but preserves sign.
	*/
	public void sign() {
		x = x > 0 ? 1 : -1;
		y = y > 0 ? 1 : -1;
		z = z > 0 ? 1 : -1;
	}
	
	/**
	Copies the signs of <code>a</code>, <code>b</code>, and <code>c</code> onto the coordinates x, y, and z.
	
	@param a the sign to copy onto the x coordinate
	@param b the sign to copy onto the y coordinate
	@param c the sign to copy onto the z coordinate
	*/
	public void copySign(double a, double b, double c) {
		x *= a > 0 ? 1 : -1;
		y *= b > 0 ? 1 : -1;
		z *= c > 0 ? 1 : -1;
	}
	
	/**
	Copies the signs of the coordinates of <code>v</code> onto the coordinates of this vector.
	
	@param v the other vector
	*/
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
