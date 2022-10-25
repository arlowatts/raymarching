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
	}
	
	// Not exact - bound
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		r.stretch(1 / width, 1 / height, 1 / depth);
		
		int nearest = 0;
		double nearestDist = r.getDistance(normals[nearest]);
		
		for (int i = 1; i < 4; i++) {
			if (r.getDistance(normals[i]) < nearestDist) {
				nearest = i;
				nearestDist = r.getDistance(normals[nearest]);
			}
		}
		
		return (r.dotProduct(normals[nearest]) - 1) / SQRT_3;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(3*Math.sqrt(width*width + height*height + depth*depth));
	}
	
	public Vector ggetNormal(Vector v) {
		Vector r = toLocalFrame(v);
		r.stretch(1 / width, 1 / height, 1 / depth);
		
		int nearest = 0;
		
		for (int i = 1; i < 4; i++)
			if (r.getDistance(normals[i]) < r.getDistance(normals[nearest]))
				nearest = i;
		
		Vector n = new Vector(normals[nearest]);
		n.stretch(width, height, depth);
		n.setLength(1);
		n.rotate(getAngle());
		
		return n;
	}
}