package src.shapes;

import java.lang.Math;

public class Cylinder extends Shape {
	// Constants
	public static final String[] PARAMS = {"radius", "height", "edgeRadius"};

	// Member variables
	private double radius, height, edgeRadius;
	
	// Constructors
	public Cylinder(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], Shape.MIN_LENGTH);
		height = Math.max(args[1], Shape.MIN_LENGTH);
		
		edgeRadius = args[2];
	}
	
	// Methods
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector v1 = new Vector(Math.sqrt(v.x*v.x + v.z*v.z) - radius + edgeRadius, Math.abs(v.y) - height + edgeRadius, 0);
		
		double dist = Math.min(Math.max(v1.x, v1.y), 0);
		
		v1.x = Math.max(v1.x, 0);
		v1.y = Math.max(v1.y, 0);
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return dist + v1.getLength() - edgeRadius;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(radius*radius + height*height));
	}
	
	// Getters
	public double getRadius() {return radius;}
	public double getHeight() {return height;}
	public double getEdgeRadius() {return edgeRadius;}
	
	// Setters
	public void setRadius(double r) {
		radius = r;
		setBoundRadius(-1);
	}
	
	public void setHeight(double h) {
		height = h;
		setBoundRadius(-1);
	}
	
	public void setEdgeRadius(double r) {edgeRadius = r;}
	
	public void setSize(double r, double h) {
		radius = r;
		height = h;
		setBoundRadius(-1);
	}
}
