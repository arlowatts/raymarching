package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its width, height, and depth.
*/
public class Box extends Shape {
	/**
	The list of parameters required by Box's constructor.
	The parameters are "width", "height", "depth".
	*/
	public static final String[] PARAMS = {"width", "height", "depth"};

	// Member variables
	private double width, height, depth;
	
	/**
	Creates a new Box from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Box.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Box.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
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
