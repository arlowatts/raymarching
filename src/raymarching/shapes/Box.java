package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a rectangular box.
*/
public class Box extends Shape {
	/**
	Creates a new Box from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Box.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Box.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Box(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		Vector q = new Vector(r);
		
		q.positive();
		q.subtract(1, 1, 1);
		
		double d = Math.min(Math.max(q.x, Math.max(q.y, q.z)), 0);
		
		q.set(Math.max(q.x, 0), Math.max(q.y, 0), Math.max(q.z, 0));
		
		return q.getLength() + d;
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(3);
	}
}
