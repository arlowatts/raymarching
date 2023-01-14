package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Polyhedron defining a 12-sided polyhedron.
*/
public class Dodecahedron extends Polyhedron {
	/**
	The list of parameters required by Dodecahedron's constructor.
	Dodecahedron has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	// The interior angle between two faces: 2atan((1 + sqrt(5)) / 2)
	private static final Vector INTERIOR_ANGLE = new Vector(2.0344439357957027, 0, 0);
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final Double CIRCUMSCRIBED_SPHERE_RATIO = 0.7946544722917661;
	
	/**
	Creates a new Dodecahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Dodecahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Dodecahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Dodecahedron(double[] args, double[] dargs) {
		super(12, dargs);
	}
	
	protected void initFaces() {
		addFace(0, 1, 0);
		addFace(0, -1, 0);
		
		Vector n, rotation = new Vector(-Math.PI / 2, 0, Math.PI / 2);
		
		for (int i = 0; i < 5; i++) {
			rotation.y = i * Math.PI * 2 / 5;
			
			n = new Vector(0, 1, 0);
			n.rotate(INTERIOR_ANGLE);
			n.rotate(rotation);
			addFace(n);
			
			n = new Vector(0, -1, 0);
			n.rotate(INTERIOR_ANGLE);
			n.rotate(rotation);
			addFace(n);
		}
	}
	
	protected double getCircumSphereRatio() {
		return CIRCUMSCRIBED_SPHERE_RATIO;
	}
}
