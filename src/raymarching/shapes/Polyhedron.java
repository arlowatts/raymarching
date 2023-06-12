package raymarching.shapes;

import raymarching.Vector;

/**
An abstract subclass of Shape defining a regular polyhedron.
*/
public abstract class Polyhedron extends Shape {
	private int numFaces;
	private Vector[] normals;
	
	/**
	Creates a new Polyhedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Polyhedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Polyhedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Polyhedron(int numFaces, double[] dargs) {
		super(dargs);
		
		this.numFaces = 0;
		this.normals = new Vector[numFaces];
		
		initFaces();
	}
	
	protected abstract void initFaces();
	
	protected abstract double getCircumSphereRatio();
	
	protected void addFace(Vector v) {
		if (numFaces < normals.length) {
			v.setLength(1);
			normals[numFaces++] = v;
		}
	}
	
	protected void addFace(double x, double y, double z) {addFace(new Vector(x, y, z));}
	
	// Not exact - bound
	protected double getLocalDistance(Vector r) {
		return r.dotProduct(normals[getNearestFace(r)]) - getCircumSphereRatio();
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
		
		for (int i = 1; i < normals.length; i++) {
			dist = r.getDistance(normals[i]);
			
			if (dist < nearestDist) {
				nearest = i;
				nearestDist = dist;
			}
		}
		
		return nearest;
	}
}
