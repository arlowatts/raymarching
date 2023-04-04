package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a sphere
*/
public class Sphere extends Shape {
	/**
	Creates a new Sphere from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Sphere.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Sphere.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Sphere(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		return r.getLength() - 1;
	}
	
	@Override
	protected Vector getLocalNormal(Vector r) {
		Vector n = new Vector(r);
		n.setLength(1);
		return n;
	}
	
	@Override
	protected int getLocalColor(Vector r) {
		if (getTexture() == null) return getColor();
		
		int x = (int)(((r.z < 0 ? 0.25 : 0.75) - Math.atan(r.x / r.z) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)((Math.acos(r.y) / Math.PI) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	protected double setBoundRadius() {
		return 1;
	}
}
