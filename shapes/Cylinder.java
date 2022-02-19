package shapes;

import java.lang.Math;

public class Cylinder extends Shape {
	// Member variables
	private double radius, height, edgeRadius;
	
	// Constructors
	public Cylinder(double r, double h, double eR, double x, double y, double z, double phi, double theta, double psi, double shine, int color) {
		super(x, y, z, phi, theta, psi, shine, color);
		
		radius = Math.max(r, Shape.MIN_LENGTH);
		height = Math.max(h, Shape.MIN_LENGTH);
		edgeRadius = eR;
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngle());
		
		Vector v2 = new Vector(Math.sqrt(v1.getX()*v1.getX() + v1.getZ()*v1.getZ()) - radius + edgeRadius, Math.abs(v1.getY()) - height + edgeRadius, 0);
		
		double dist = Math.min(Math.max(v2.getX(), v2.getY()), 0);
		
		v2.setX(Math.max(v2.getX(), 0));
		v2.setY(Math.max(v2.getY(), 0));
		
		return dist + v2.getLength() - edgeRadius;
	}
	
	// Getters
	public double getRadius() {return radius;}
	public double getHeight() {return height;}
	
	// Setters
	public void setRadius(double r) {radius = r;}
	public void setHeight(double h) {height = h;}
	
	public void setSize(double r, double h) {
		radius = r;
		height = h;
	}
}