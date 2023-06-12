package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a cone.
*/
public class Cone extends Shape {
	/**
	Creates a new Cone from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Cone.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Cone.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Cone(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		Vector q = new Vector(r);
		
		q.set(Math.sqrt(q.x*q.x + q.z*q.z), q.y + 1, 0);
		
		double g = Math.min(Math.max((q.dotProduct(-1, 2, 0) + 1) / 5, 0), 1);
		
		double d = Math.min(q.getDistance(1 - g, 2 * g, 0),
							q.x < 1 ? Math.abs(q.y) : q.getDistance(1, 0, 0));
		
		if (q.y > 0 && q.y < 2 && q.x < 1 - q.y / 2) d *= -1;
		
		return d;
	}
	
	@Override
	protected Vector getLocalNormal(Vector r) {
		Vector n = new Vector(r);
		
		if (n.y < -1) n.set(0, -1, 0);
		else {
			n.y = Math.sqrt(n.x*n.x + n.z*n.z) / 2;
			n.setLength(1);
		}
		
		return n;
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(2);
	}
}
