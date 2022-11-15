package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Octahedron extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	// Member variables
	private double width, height, depth;
	
	// Constructors
	public Octahedron(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		r.setPositive();
		r.x -= width;
		
		Vector n = new Vector(1 / width, 1 / height, 1 / depth);
		n.setLength(1);
		
		return r.dotProduct(n);
	}
	
	protected double setBoundRadius() {
		return Math.max(Math.max(width, height), depth);
	}
	
	public Vector getNormal(Vector v) {
		Vector n = toLocalFrame(v);
		
		n.sign();
		n.stretch(1 / width, 1 / height, 1 / depth);
		n.setLength(1);
		
		n.rotate(getAngle());
		
		return n;
	}
}
