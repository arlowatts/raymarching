package shapes;

import java.lang.Math;

public class Box extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "height", "length", "edgeRadius"};
	
	// Member variables
	private double width, height, length, radius;
	
	// Constructors
	public Box(double w, double h, double l, double r, double[] args) {
		super(args);
		
		width = Math.max(w, Shape.MIN_LENGTH);
		height = Math.max(h, Shape.MIN_LENGTH);
		length = Math.max(l, Shape.MIN_LENGTH);
		
		radius = r;
		
		setBoundRadius();
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngle());
		v1.set(Math.abs(v1.getX()) - width + radius, Math.abs(v1.getY()) - height + radius, Math.abs(v1.getZ()) - length + radius);
		
		Vector v2 = new Vector(Math.max(v1.getX(), 0), Math.max(v1.getY(), 0), Math.max(v1.getZ(), 0));
		
		return v2.getLength() + Math.min(Math.max(v1.getX(), Math.max(v1.getY(), v1.getZ())), 0) - radius;
	}
	
	// Getters
	public double getWidth() {return width;}
	public double getHeight() {return height;}
	public double getLength() {return length;}
	public double getRadius() {return radius;}
	
	// Setters
	public void setWidth(double w) {
		width = w;
		setBoundRadius();
	}
	
	public void setHeight(double h) {
		height = h;
		setBoundRadius();
	}
	
	public void setLength(double l) {
		length = l;
		setBoundRadius();
	}
	
	public void setRadius(double r) {radius = r;}
	
	public void setSize(double w, double h, double l) {
		width = w;
		height = h;
		length = l;
		setBoundRadius();
	}
	
	// Helpers
	private void setBoundRadius() {
		setBoundRadius(Math.sqrt(width*width + height*height + length*length));
	}
}