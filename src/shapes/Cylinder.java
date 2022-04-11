package src.shapes;

import java.lang.Math;

public class Cylinder extends Shape {
	// Constants
	public static final String[] PARAMS = {"radius", "height", "edgeRadius"};
	
	// Member variables
	private double radius, height, edgeRadius;
	
	// Constructors
	public Cylinder(double r, double h, double eR, double[] args) {
		super(args);
		
		radius = Math.max(r, Shape.MIN_LENGTH);
		height = Math.max(h, Shape.MIN_LENGTH);
		
		edgeRadius = eR;
		
		setBoundRadius();
	}
	
	// Methods
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector v1 = new Vector(Math.sqrt(v.getX()*v.getX() + v.getZ()*v.getZ()) - radius + edgeRadius, Math.abs(v.getY()) - height + edgeRadius, 0);
		
		double dist = Math.min(Math.max(v1.getX(), v1.getY()), 0);
		
		v1.setX(Math.max(v1.getX(), 0));
		v1.setY(Math.max(v1.getY(), 0));
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return dist + v1.getLength() - edgeRadius;
	}
	
	// Getters
	public double getRadius() {return radius;}
	public double getHeight() {return height;}
	
	// Setters
	public void setRadius(double r) {
		radius = r;
		setBoundRadius();
	}
	
	public void setHeight(double h) {
		height = h;
		setBoundRadius();
	}
	
	public void setSize(double r, double h) {
		radius = r;
		height = h;
		setBoundRadius();
	}
	
	// Helpers
	private void setBoundRadius() {
		setBoundRadius(Math.sqrt(radius*radius + height*height));
	}
}