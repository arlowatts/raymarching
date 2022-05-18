package src.shapes;

import java.lang.Math;

public class Sphere extends Shape{
	// Constants
	public static final String[] PARAMS = {"radius"};
	
	// Member variables
	private double radius;
	
	// Constructors
	public Sphere(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], Shape.MIN_LENGTH);
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
	public void setRadius(double r) {
		radius = r;
		setBoundRadius();
	}
	
	// Helpers
	@Override
	protected void setBoundRadius() {
		setBoundRadius(radius);
	}
}