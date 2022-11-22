package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its width, height, and depth scaling factors.
*/
public class Octahedron extends Shape {
	/**
	The list of parameters required by Octahedron's constructor.
	The parameters are "width", "height", "depth".
	*/
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	// Member variables
	private double width, height, depth;
	
	/**
	Creates a new Octahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Octahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Octahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
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
