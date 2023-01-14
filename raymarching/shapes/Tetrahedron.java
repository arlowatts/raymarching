package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Polyhedron defining a 4-sided polyhedron.
*/
public class Tetrahedron extends Polyhedron {
	/**
	The list of parameters required by Tetrahedron's constructor.
	Tetrahedron has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final double CIRCUMSCRIBED_SPHERE_RATIO = 1.0 / 3.0;
	
	/**
	Creates a new Tetrahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Tetrahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Tetrahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Tetrahedron(double[] args, double[] dargs) {
		super(4, dargs);
	}
	
	protected void initFaces() {
		addFace(-1, -1, -1);
		addFace(1, -1, 1);
		addFace(-1, 1, 1);
		addFace(1, 1, -1);
	}
	
	protected double getCircumSphereRatio() {
		return CIRCUMSCRIBED_SPHERE_RATIO;
	}
}
