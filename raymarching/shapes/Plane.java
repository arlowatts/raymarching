package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Plane extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "length"};

	// Member variables
	private double width, length;
	
	// Constructors
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
