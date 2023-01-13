package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining an 8-sided polyhedron.
*/
public class Octahedron extends Shape {
	/**
	The list of parameters required by Octahedron's constructor.
	Octahedron has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final double CIRCUMSCRIBED_SPHERE_RATIO = 1.0 / Math.sqrt(3);
	
	private Vector[] normals;
	
	/**
	Creates a new Octahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Octahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Octahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Octahedron(double[] args, double[] dargs) {
		super(dargs);
		
		normals = new Vector[] {new Vector(-1, -1, -1), new Vector(-1, -1, 1), new Vector(-1, 1, -1), new Vector(1, -1, -1),
								new Vector(1, 1, 1), new Vector(1, 1, -1), new Vector(1, -1, 1), new Vector(-1, 1, 1)};
		
		for (int i = 0; i < 8; i++) normals[i].setLength(1);
	}
	
	// Not exact - bound
	protected double getLocalDistance(Vector r) {
		return r.dotProduct(normals[getNearestFace(r)]) - CIRCUMSCRIBED_SPHERE_RATIO;
	}
	
	@Override
	protected Vector getLocalNormal(Vector r) {
		Vector n = new Vector(normals[getNearestFace(r)]);
		
		return n;
	}
	
	protected double setBoundRadius() {
		return 1;
	}
	
	private int getNearestFace(Vector r) {
		int nearest = 0;
		double nearestDist = r.getDistance(normals[nearest]);
		double dist;
		
		for (int i = 1; i < 8; i++) {
			dist = r.getDistance(normals[i]);
			
			if (dist < nearestDist) {
				nearest = i;
				nearestDist = dist;
			}
		}
		
		return nearest;
	}
}
