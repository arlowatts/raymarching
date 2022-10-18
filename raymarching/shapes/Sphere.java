package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Sphere extends Shape {
	// Constants
	public static final String[] PARAMS = {"radius"};

	// Member variables
	private double radius;
	
	// Constructors
	public Sphere(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(radius);
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
	
	// Getters
	public double getRadius() {return radius;}
	
	// Setters
	public void setRadius(double r) {
		radius = r;
		setBoundRadius(-1);
	}
}
