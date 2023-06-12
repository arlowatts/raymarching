package raymarching.shapes;

import java.lang.Math;

/**
A subclass of Polyhedron defining a regular 20-sided polyhedron.
*/
public class Icosahedron extends Polyhedron {
	// The ratio of the radius of the inscribed sphere to the radius of the circumscribed sphere
	private static final Double CIRCUMSCRIBED_SPHERE_RATIO = Math.sqrt((5.0 + 2.0 * Math.sqrt(5.0)) / 15.0);
	// The value of the golden ratio
	private static final Double GOLDEN_RATIO = (1.0 + Math.sqrt(5.0)) / 2.0;
	
	/**
	Creates a new Icosahedron from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Icosahedron.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Icosahedron.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Icosahedron(double[] args, double[] dargs) {
		super(20, dargs);
	}
	
	protected void initFaces() {
		for (int i = -1; i <= 1; i += 2)
			for (int j = -1; j <= 1; j += 2)
				for (int k = -1; k <= 1; k += 2)
					addFace(i, j, k);
		
		double[] coords = new double[3];
		
		for (int i = 0; i < 3; i++) {
			coords[0] = 0;
			coords[1] = 0;
			coords[2] = 0;
			
			for (int j = -1; j <= 1; j += 2) {
				coords[i] = j * (2 * GOLDEN_RATIO + 1);
				
				for (int k = -1; k <= 1; k += 2) {
					coords[(i+1) % 3] = k * GOLDEN_RATIO;
					
					addFace(coords[0], coords[1], coords[2]);
				}
			}
		}
	}
	
	protected double getCircumSphereRatio() {
		return CIRCUMSCRIBED_SPHERE_RATIO;
	}
}
