package shapes;

import java.lang.Math;

public class Sphere extends Shape{
	// Member variables
	private double radius;
	
	// Constructors
	public Sphere(double r, double x, double y, double z, double shine, int color) {
		super(x, y, z, 0, 0, 0, shine, color);
		radius = Math.max(r, Shape.MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	public Vector getNormal(Vector v) {
		Vector normal = new Vector(v);
		
		normal.subtract(getPos());
		
		normal.setLength(1);
		
		return normal;
	}
	
	// Getters
	public double getRadius() {return radius;}
	
	// Setters
	public void setRadius(double r) {radius = r;}
}