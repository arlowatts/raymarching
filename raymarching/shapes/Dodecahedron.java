package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Dodecahedron extends Shape {
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	private static final Vector INTERIOR_ANGLE = new Vector(2.0344439357957027, 0, 0); // The interior angle between two faces: 2atan((1 + sqrt(5)) / 2)
	private static final Double CIRCUMSCRIBED_SPHERE_RATIO = 0.7946544722917661; // The ratio of the radius of the circumscribed sphere to the radius of the inscribed sphere
	
	private double width, height, depth;
	
	private Vector[] normals;
	
	public Dodecahedron(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
		
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
		
		for (int i = 0; i < 12; i++) normals[i].setLength(1);
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
