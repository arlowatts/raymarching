package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a flat rectangle.
*/
public class Plane extends Shape {
	/**
	The list of parameters required by Plane's constructor.
	Plane has no unique parameters.
	*/
	public static final String[] PARAMS = {};
	
	/**
	Creates a new Plane from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Plane.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Plane.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Plane(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		Vector q = new Vector(r);
		
		q.add(-Math.min(Math.max(-1, q.x), 1), 0, -Math.min(Math.max(-1, q.z), 1));
		
		return q.getLength();
	}
	
	@Override
	protected Vector getLocalNormal(Vector r) {
		Vector n = new Vector(r);
		
		n.set(0, n.y > 0 ? 1 : -1, 0);
		
		return n;
	}
	
	@Override
	protected int getLocalColor(Vector r) {
		if (getTexture() == null) return getColor();
		
		int x = (int)(Math.min(Math.max(r.x / 2 + 0.5, 0), 1) * (getTexture().getWidth() - 1));
		int y = (int)(Math.min(Math.max(r.z / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(2);
	}
}
