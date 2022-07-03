package src.shapes;

import java.lang.Math;

public class Plane extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "length"};

	// Member variables
	private double width, length;
	
	// Constructors
	public Plane(double[] args, double[] dargs) {
		super(dargs);
		
		width = args[0];
		length = args[1];
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
	public void setWidth(double w) {
		width = w;
		setBoundRadius();
	}
	public void setLength(double l) {
		length = l;
		setBoundRadius();
	}
	
	public void setSize(double w, double l) {
		width = w;
		length = l;
		setBoundRadius();
	}
	
	// Helpers
	@Override
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(width*width + length*length));
	}
}