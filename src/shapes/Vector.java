package src.shapes;

import java.lang.Math;

public class Vector {
	// Member variables
	private double x, y, z;
	
	// Constructors
	public Vector(double x, double y, double z) {set(x, y, z);}
	
	public Vector(Vector v) {set(v);}
	
	public Vector() {set(0, 0, 0);}
	
	// Methods
	public void rotate(double phi, double theta, double psi, double cx, double cy, double cz) {
		double[] sincos = getSincos(phi, theta, psi);
		
		x -= cx;
		y -= cy;
		z -= cz;
		
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
		
		x += cx;
		y += cy;
		z += cz;
	}
	
	public void inverseRotate(double phi, double theta, double psi, double cx, double cy, double cz) {
		double[] sincos = getSincos(-phi, -theta, -psi);
		
		x -= cx;
		y -= cy;
		z -= cz;
		
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
		
		x += cx;
		y += cy;
		z += cz;
	}
	
	public void rotate(Vector angle, Vector origin) {
		rotate(angle.x, angle.y, angle.z, origin.x, origin.y, origin.z);
	}
	
	public void inverseRotate(Vector angle, Vector origin) {
		inverseRotate(angle.x, angle.y, angle.z, origin.x, origin.y, origin.z);
	}
	
	public void rotate(Vector angle) {
		rotate(angle.x, angle.y, angle.z, 0, 0, 0);
	}
	
	public void inverseRotate(Vector angle) {
		inverseRotate(angle.x, angle.y, angle.z, 0, 0, 0);
	}
	
	public void rotate(double phi, double theta, double psi) {
		rotate(phi, theta, psi, 0, 0, 0);
	}
	
	public void inverseRotate(double phi, double theta, double psi) {
		inverseRotate(phi, theta, psi, 0, 0, 0);
	}
	
	public double dotProduct(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public double getDistance(Vector v) {
		return Math.sqrt((v.x - x)*(v.x - x) + (v.y - y)*(v.y - y) + (v.z - z)*(v.z - z));
	}
	
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void subtract(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public void multiply(double l) {
		x *= l;
		y *= l;
		z *= l;
	}
	
	public void stretch(double lx, double ly, double lz) {
		x *= lx;
		y *= ly;
		z *= lz;
	}
	
	// Getters
	public double getX() {return x;}
	public double getY() {return y;}
	public double getZ() {return z;}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	// Setters
	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	public void setZ(double z) {this.z = z;}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vector v) {
		set(v.x, v.y, v.z);
	}
	
	public void setLength(double length) {
		multiply(length / getLength());
	}
	
	// Helpers
	private static double[] getSincos(double phi, double theta, double psi) {
		double sincos[] = {Math.sin(phi), Math.sin(theta), Math.sin(psi),
						   Math.cos(phi), Math.cos(theta), Math.cos(psi)};
		
		return sincos;
	}
	
	// toString
	public String toString() {
		return "X: " + (double)Math.round(x * 100000) / 100000D +
			   "\tY: " + (double)Math.round(y * 100000) / 100000D +
			   "\tZ: " + (double)Math.round(z * 100000) / 100000D;
	}
}