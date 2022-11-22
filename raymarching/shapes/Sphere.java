package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its radius.
*/
public class Sphere extends Shape {
	/**
	The list of parameters required by Sphere's constructor.
	The parameters are "radius".
	*/
	public static final String[] PARAMS = {"radius"};

	// Member variables
	private double radius;
	
	/**
	Creates a new Sphere from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Sphere.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Sphere.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Sphere(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	protected double setBoundRadius() {
		return radius;
	}
	
	public Vector getNormal(Vector v) {
		Vector n = new Vector(v);
		
		n.subtract(getPos());
		n.setLength(1);
		
		return n;
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		int x = (int)(((r.z < 0 ? 0.25 : 0.75) - Math.atan(r.x / r.z) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)((Math.acos(r.y / radius) / Math.PI) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
}
