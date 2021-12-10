import java.lang.Math;

public class Vector {
	private double x, y, z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector v) {this(v.x, v.y, v.z);}
	
	public double getX() {return x;}
	
	public double getY() {return y;}
	
	public double getZ() {return z;}
	
	public void setX(double x) {this.x = x;}
	
	public void setY(double y) {this.y = y;}
	
	public void setZ(double z) {this.z = z;}
	
	public double getDistance(Vector v) {
		return Math.sqrt((v.x - x)*(v.x - x) + (v.y - y)*(v.y - y) + (v.z - z)*(v.z - z));
	}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public void setLength(double length) {
		multiply(length / getLength());
	}
	
	public void rotate(double sincos[], double originX, double originY, double originZ) {
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
	
	public void rotate(double sincos[], Vector origin) {
		rotate(sincos, origin.x, origin.y, origin.z);
	}
	
	public static double[] getSincos(double angleX, double angleY) {
		double sincos[] = {Math.sin(angleX), Math.sin(angleY), Math.cos(angleX), Math.cos(angleY)};
		
		return sincos;
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
}