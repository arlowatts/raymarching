package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a 12-sided polyhedron.
*/
public class Dodecahedron extends Shape {
	/**
	The list of parameters required by Dodecahedron's constructor.
	Dodecahedron has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	// The interior angle between two faces: 2atan((1 + sqrt(5)) / 2)
	private static final Vector INTERIOR_ANGLE = new Vector(2.0344439357957027, 0, 0);
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final Double CIRCUMSCRIBED_SPHERE_RATIO = 0.7946544722917661;
	
	private Vector[] normals;
	
	/**
	Creates a new Dodecahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Dodecahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Dodecahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Dodecahedron(double[] args, double[] dargs) {
		super(dargs);
		
		normals = new Vector[12];
		
		normals[0] = new Vector(0, 1, 0);
		normals[11] = new Vector(0, -1, 0);
		
		Vector rotation = new Vector();
		
		for (int i = 0; i < 5; i++) {
			rotation.set(-Math.PI / 2, i * Math.PI * 2 / 5, Math.PI / 2);
			
			normals[1 + i] = new Vector(normals[0]);
			normals[1 + i].rotate(INTERIOR_ANGLE);
			normals[1 + i].rotate(rotation);
			
			normals[10 - i] = new Vector(normals[11]);
			normals[10 - i].rotate(INTERIOR_ANGLE);
			normals[10 - i].rotate(rotation);
		}
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
		
		for (int i = 1; i < 12; i++) {
			dist = r.getDistance(normals[i]);
			
			if (dist < nearestDist) {
				nearest = i;
				nearestDist = dist;
			}
		}
		
		return nearest;
	}
}
