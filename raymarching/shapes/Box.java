package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Box extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "height", "depth"};

	// Member variables
	private double width, height, depth;
	
	// Constructors
	public Box(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		r.setPositive();
		r.subtract(width, height, depth);
		
		double dval = Math.min(Math.max(r.x, Math.max(r.y, r.z)), 0);
		
		r.set(Math.max(r.x, 0), Math.max(r.y, 0), Math.max(r.z, 0));
		
		return r.getLength() + dval;
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(width*width + height*height + depth*depth);
	}
}
