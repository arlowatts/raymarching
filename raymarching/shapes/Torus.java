package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Torus extends Shape {
	// Constants
	public static final String[] PARAMS = {"largeRadius", "smallRadius"};

	// Member variables
	private double largeRadius, smallRadius;
	
	// Constructors
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
	
	protected void setBoundRadius() {
		setBoundRadius(largeRadius + smallRadius);
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		double w = largeRadius - Math.sqrt(r.x*r.x + r.z*r.z);
		
		int x = (int)(((r.z < 0 ? 0.25 : 0.75) - Math.atan(r.x / r.z) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)(((r.y < 0 ? 0.25 : 0.75) - Math.atan(w / r.y) / (Math.PI * 2)) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	// Getters
	public double getLargeRadius() {return largeRadius;}
	public double getSmallRadius() {return smallRadius;}
	
	// Setters
	public void setLargeRadius(double r) {
		largeRadius = r;
		setBoundRadius(-1);
	}
	
	public void setSmallRadius(double r) {
		smallRadius = r;
		setBoundRadius(-1);
	}
}
