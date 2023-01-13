package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a cylinder.
*/
public class Cylinder extends Shape {
	/**
	The list of parameters required by Cylinder's constructor.
	Cylinder has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	/**
	Creates a new Cylinder from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Cylinder.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Cylinder.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Cylinder(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		Vector q = new Vector(r);
		
		q.set(Math.sqrt(q.x*q.x + q.z*q.z) - 1, Math.abs(q.y) - 1, 0);
		
		double d = Math.min(Math.max(q.x, q.y), 0);
		
		q.x = Math.max(q.x, 0);
		q.y = Math.max(q.y, 0);
		
		return d + q.getLength();
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(2);
	}
}
