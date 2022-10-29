package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Tetrahedron extends Shape {
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	private static final double SQRT_3 = Math.sqrt(3);
	
	private double width, height, depth;
	
	private Vector[] normals;
	
	public Tetrahedron(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
		
		normals = new Vector[] {new Vector(-1, -1, -1), new Vector(1, -1, 1), new Vector(-1, 1, 1), new Vector(1, 1, -1)};
		
		for (int i = 0; i < 4; i++) normals[i].divide(SQRT_3);
	}
	
	// Not exact - bound
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		r.stretch(1 / width, 1 / height, 1 / depth);
		
		return r.dotProduct(normals[getNearestFace(r)]) - 1.0 / 3.0;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(width*width + height*height + depth*depth));
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
