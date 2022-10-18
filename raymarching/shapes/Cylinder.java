package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Cylinder extends Shape {
	// Constants
	public static final String[] PARAMS = {"radius", "height"};

	// Member variables
	private double radius, height;
	
	// Constructors
	public Cylinder(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], Shape.MIN_LENGTH);
		height = Math.max(args[1], Shape.MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector v1 = new Vector(Math.sqrt(v.x*v.x + v.z*v.z) - radius, Math.abs(v.y) - height, 0);
		
		double dist = Math.min(Math.max(v1.x, v1.y), 0);
		
		v1.x = Math.max(v1.x, 0);
		v1.y = Math.max(v1.y, 0);
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return dist + v1.getLength();
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(radius*radius + height*height));
	}
	
	// Getters
	public double getRadius() {return radius;}
	public double getHeight() {return height;}
	
	// Setters
	public void setRadius(double r) {
		radius = r;
		setBoundRadius(-1);
	}
	
	public void setHeight(double h) {
		height = h;
		setBoundRadius(-1);
	}
	
	public void setSize(double r, double h) {
		radius = r;
		height = h;
		setBoundRadius(-1);
	}
}
