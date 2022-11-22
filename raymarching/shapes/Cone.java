package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its radius at the base and its height.
*/
public class Cone extends Shape {
	/**
	The list of parameters required by Cone's constructor.
	The parameters are "radius", "height".
	*/
	public static final String[] PARAMS = {"radius", "height"};
	
	// Member variables
	private double radius, height;
	
	/**
	Creates a new Cone from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Cone.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Cone.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Cone(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		r.set(Math.sqrt(r.x*r.x + r.z*r.z), r.y + height / 2, 0);
		
		double epsilon = Math.min(Math.max((r.dotProduct(-radius, height, 0) + radius*radius) / (radius*radius + height*height), 0), 1);
		
		double distance = Math.min(r.getDistance(radius * (1 - epsilon), height * epsilon, 0),
								   r.x < radius ? Math.abs(r.y) : r.getDistance(radius, 0, 0));
		
		if (r.y > 0 && r.y < height && r.x < radius * (height - r.y) / height) distance *= -1;
		
		return distance;
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(radius*radius + height*height / 4);
	}
	
	public Vector getNormal(Vector v) {
		Vector n = toLocalFrame(v);
		
		if (n.y < -height / 2) n.set(0, -1, 0);
		else {
			n.set(n.x, radius / height * Math.sqrt(n.x*n.x + n.z*n.z), n.z);
			n.setLength(1);
		}
		
		n.rotate(getAngle());
		
		return n;
	}
}
