package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a torus.
*/
public class Torus extends Shape {
	/**
	The list of parameters required by Torus's constructor.
	The parameters are "radiusRatio".
	*/
	public static final String[] PARAMS = {"radiusRatio"};
	
	private double radiusRatio;
	
	/**
	Creates a new Torus from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Torus.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Torus.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Torus(double[] args, double[] dargs) {
		super(dargs);
		
		radiusRatio = Math.max(args[0], MIN_LENGTH);
	}
	
	// Methods
	protected double getLocalDistance(Vector r) {
		double d = Math.sqrt(r.x*r.x + r.z*r.z) - 1;
		
		return Math.sqrt(d*d + r.y*r.y) - radiusRatio;
	}
	
	@Override
	protected int getLocalColor(Vector r) {
		if (getTexture() == null) return getColor();
		
		double w = 1 - Math.sqrt(r.x*r.x + r.z*r.z);
		
		int x = (int)(((r.z < 0 ? 0.25 : 0.75) - Math.atan(r.x / r.z) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)(((r.y < 0 ? 0.25 : 0.75) - Math.atan(w / r.y) / (Math.PI * 2)) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	protected double setBoundRadius() {
		return 1 + radiusRatio;
	}
}
