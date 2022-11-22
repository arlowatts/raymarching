package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its width, height, and depth scaling factors.
*/
public class Tetrahedron extends Shape {
	/**
	The list of parameters required by Tetrahedron's constructor.
	The parameters are "width", "height", "depth".
	*/
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	private static final double CIRCUMSCRIBED_SPHERE_RATIO = 1.0 / 3.0;
	
	private double width, height, depth;
	
	private Vector[] normals;
	
	/**
	Creates a new Tetrahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Tetrahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Tetrahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Tetrahedron(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
		
		normals = new Vector[] {new Vector(-1, -1, -1), new Vector(1, -1, 1), new Vector(-1, 1, 1), new Vector(1, 1, -1)};
		
		for (int i = 0; i < 4; i++) normals[i].setLength(1);
	}
	
	// Not exact - bound
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		r.stretch(1 / width, 1 / height, 1 / depth);
		
		return r.dotProduct(normals[getNearestFace(r)]) - CIRCUMSCRIBED_SPHERE_RATIO;
	}
	
	protected double setBoundRadius() {
		return Math.max(Math.max(width, height), depth);
	}
	
	public Vector getNormal(Vector v) {
		Vector r = toLocalFrame(v);
		r.stretch(1 / width, 1 / height, 1 / depth);
		
		Vector n = new Vector(normals[getNearestFace(r)]);
		n.stretch(width, height, depth);
		n.setLength(1);
		n.rotate(getAngle());
		
		return n;
	}
	
	private int getNearestFace(Vector r) {
		int nearest = 0;
		double nearestDist = r.getDistance(normals[nearest]);
		double dist;
		
		for (int i = 1; i < 4; i++) {
			dist = r.getDistance(normals[i]);
			
			if (dist < nearestDist) {
				nearest = i;
				nearestDist = dist;
			}
		}
		
		return nearest;
	}
}
