package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defined by its width and length.
*/
public class Plane extends Shape {
	/**
	The list of parameters required by Plane's constructor.
	The parameters are "width", "length".
	*/
	public static final String[] PARAMS = {"width", "length"};

	// Member variables
	private double width, length;
	
	/**
	Creates a new Plane from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Plane.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Plane.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Plane(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		length = Math.max(args[1], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector r = toLocalFrame(v);
		
		r.add(-Math.min(Math.max(-width, r.x), width), 0, -Math.min(Math.max(-length, r.z), length));
		
		return r.getLength();
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(width*width + length*length);
	}
	
	public Vector getNormal(Vector v) {
		Vector n = toLocalFrame(v);
		
		n.set(0, n.y > 0 ? 1 : -1, 0);
		
		n.rotate(getAngle());
		
		return n;
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		int x = (int)(Math.min(Math.max(r.x / (width * 2) + 0.5, 0), 1) * (getTexture().getWidth() - 1));
		int y = (int)(Math.min(Math.max(r.z / (length * 2) + 0.5, 0), 1) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
}
