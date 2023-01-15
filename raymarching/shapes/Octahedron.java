package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Polyhedron defining a regular 8-sided polyhedron.
*/
public class Octahedron extends Polyhedron {
	/**
	The list of parameters required by Octahedron's constructor.
	Octahedron has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final double CIRCUMSCRIBED_SPHERE_RATIO = 1.0 / Math.sqrt(3.0);
	
	/**
	Creates a new Octahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Octahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Octahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Octahedron(double[] args, double[] dargs) {
		super(8, dargs);
	}
	
	protected void initFaces() {
		for (int i = -1; i <= 1; i += 2)
			for (int j = -1; j <= 1; j += 2)
				for (int k = -1; k <= 1; k += 2)
					addFace(i, j, k);
	}
	
	protected double getCircumSphereRatio() {
		return CIRCUMSCRIBED_SPHERE_RATIO;
	}
}
