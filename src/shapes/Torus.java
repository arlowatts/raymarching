package src.shapes;

import java.lang.Math;

public class Torus extends Shape{
	// Constants
	public static final String[] PARAMS = {"largeRadius", "smallRadius"};

	// Member variables
	private double largeRadius, smallRadius;
	
	// Constructors
	public Torus(double[] args, double[] dargs) {
		super(dargs);
		
		largeRadius = Math.max(args[0], Shape.MIN_LENGTH);
		smallRadius = Math.max(args[1], Shape.MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		double a = Math.sqrt(v.getX()*v.getX() + v.getZ()*v.getZ()) - largeRadius;
		double distance = Math.sqrt(a*a + v.getY()*v.getY()) - smallRadius;
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return distance;
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		double w = largeRadius - Math.sqrt(r.getX()*r.getX() + r.getZ()*r.getZ());
		
		int x = (int)(((r.getZ() < 0 ? 0.25 : 0.75) - Math.atan(r.getX() / r.getZ()) / (Math.PI * 2)) * (getTexture().getWidth() - 1));
		
		int y = (int)(((r.getY() < 0 ? 0.75 : 0.25) - (r.getY() < 0 ? -1 : 1) * Math.asin(w / smallRadius) / (Math.PI * 2)) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	// Getters
	public double getLargeRadius() {return largeRadius;}
	public double getSmallRadius() {return smallRadius;}
	
	// Setters
	public void setLargeRadius(double r) {
		largeRadius = r;
		setBoundRadius();
	}
	
	public void setSmallRadius(double r) {
		smallRadius = r;
		setBoundRadius();
	}
	
	// Helpers
	@Override
	protected void setBoundRadius() {
		setBoundRadius(largeRadius + smallRadius);
	}
}