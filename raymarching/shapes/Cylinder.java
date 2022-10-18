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
		
		radius = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		r.set(Math.sqrt(r.x*r.x + r.z*r.z) - radius, Math.abs(r.y) - height, 0);
		
		double dist = Math.min(Math.max(r.x, r.y), 0);
		
		r.x = Math.max(r.x, 0);
		r.y = Math.max(r.y, 0);
		
		return dist + r.getLength();
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
