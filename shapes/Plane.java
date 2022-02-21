package shapes;

import java.lang.Math;

public class Plane extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "length"};
	
	// Member variables
	private double width, length;
	
	// Constructors
	public Plane(double w, double l, double[] args) {
		super(args);
		
		width = w;
		length = l;
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngle());
		
		v1.add(-Math.min(Math.max(-width, v1.getX()), width), 0, -Math.min(Math.max(-length, v1.getZ()), length));
		
		return v1.getLength();
	}
	
	public Vector getNormal(Vector v) {
		v.inverseRotate(getAngle(), getPos());
		
		Vector normal = new Vector(0, v.getY() > getPos().getY() ? 1 : -1, 0);
		
		v.rotate(getAngle(), getPos());
		
		normal.rotate(getAngle());
		
		return normal;
	}
	
	// Getters
	public double getWidth() {return width;}
	public double getLength() {return length;}
	
	// Setters
	public void setWidth(double w) {width = w;}
	public void setLength(double l) {length = l;}
	
	public void setSize(double w, double l) {
		width = w;
		length = l;
	}
}