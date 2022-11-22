package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its large radius and its small radius.
*/
public class Torus extends Shape {
	/**
	The list of parameters required by Torus's constructor.
	The parameters are "largeRadius", "smallRadius".
	*/
	public static final String[] PARAMS = {"largeRadius", "smallRadius"};

	// Member variables
	private double largeRadius, smallRadius;
	
	/**
	Creates a new Torus from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Torus.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Torus.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Torus(double[] args, double[] dargs) {
		super(dargs);
		
		largeRadius = Math.max(args[0], MIN_LENGTH);
		smallRadius = Math.max(args[1], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		double a = Math.sqrt(r.x*r.x + r.z*r.z) - largeRadius;
		double distance = Math.sqrt(a*a + r.y*r.y) - smallRadius;
		
		return distance;
	}
	
	protected double setBoundRadius() {
		return largeRadius + smallRadius;
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		double w = largeRadius - Math.sqrt(r.x*r.x + r.z*r.z);
		
		int x = (int)(((r.z < 0 ? 0.25 : 0.75) - Math.atan(r.x / r.z) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)(((r.y < 0 ? 0.25 : 0.75) - Math.atan(w / r.y) / (Math.PI * 2)) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
}
