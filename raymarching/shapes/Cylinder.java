package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its radius and height.
*/
public class Cylinder extends Shape {
	/**
	The list of parameters required by Cylinder's constructor.
	The parameters are "radius", "height".
	*/
	public static final String[] PARAMS = {"radius", "height"};

	// Member variables
	private double radius, height;
	
	/**
	Creates a new Cylinder from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Cylinder.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Cylinder.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
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
	
	protected double setBoundRadius() {
		return Math.sqrt(radius*radius + height*height);
	}
}
