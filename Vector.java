import java.lang.Math;

public class Vector {
	// Member variables
	private double x, y, z;
	
	// Constructors
	public Vector(double x, double y, double z) {set(x, y, z);}
	
	public Vector(Vector v) {set(v);}
	
	public Vector() {set(0, 0, 0);}
	
	// Methods
	public void rotate(double angleX, double angleY, double originX, double originY, double originZ) {
		double[] sincos = getSincos(angleX, angleY);
		
		x -= originX;
		y -= originY;
		z -= originZ;
		
		double t;
		
		t = y;
		y = y * sincos[2] - z * sincos[0];
		z = t * sincos[0] + z * sincos[2];
		
		t = x;
		x = x * sincos[3] - z * sincos[1];
		z = t * sincos[1] + z * sincos[3];
		
		x += originX;
		y += originY;
		z += originZ;
	}
	
	public void inverseRotate(double angleX, double angleY, double originX, double originY, double originZ) {
		double[] sincos = getSincos(-angleX, -angleY);
		
		x -= originX;
		y -= originY;
		z -= originZ;
		
		double t;
		
		t = x;
		x = x * sincos[3] - z * sincos[1];
		z = t * sincos[1] + z * sincos[3];
		
		t = y;
		y = y * sincos[2] - z * sincos[0];
		z = t * sincos[0] + z * sincos[2];
		
		x += originX;
		y += originY;
		z += originZ;
	}
	
	public void rotate(double angleX, double angleY, Vector origin) {
		rotate(angleX, angleY, origin.x, origin.y, origin.z);
	}
	
	public void inverseRotate(double angleX, double angleY, Vector origin) {
		inverseRotate(angleX, angleY, origin.x, origin.y, origin.z);
	}
	
	public void rotate(double angleX, double angleY) {
		rotate(angleX, angleY, 0, 0, 0);
	}
	
	public void inverseRotate(double angleX, double angleY) {
		inverseRotate(angleX, angleY, 0, 0, 0);
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
	
	public int toInt() {
		Vector v = new Vector(this);
		
		return ((Math.abs((int)v.x) & 255) << 16) |
			   ((Math.abs((int)v.y) & 255) << 8) |
			   (Math.abs((int)v.z) & 255);
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
	private static double[] getSincos(double angleX, double angleY) {
		double sincos[] = {Math.sin(angleX), Math.sin(angleY), Math.cos(angleX), Math.cos(angleY)};
		
		return sincos;
	}
	
	// toString
	public String toString() {
		return "X: " + (double)Math.round(x * 100000) / 100000D +
			   "\tY: " + (double)Math.round(y * 100000) / 100000D +
			   "\tZ: " + (double)Math.round(z * 100000) / 100000D;
	}
}