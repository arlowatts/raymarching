package shapes;

import java.lang.Math;

public class Torus extends Shape{
	// Constants
	public static final String[] PARAMS = {"largeRadius", "smallRadius"};
	
	// Member variables
	private double largeRadius, smallRadius;
	
	// Constructors
	public Torus(double R, double r, double[] args) {
		super(args);
		
		largeRadius = Math.max(R, Shape.MIN_LENGTH);
		smallRadius = Math.max(r, Shape.MIN_LENGTH);
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
	
	// Getters
	public double getLargeRadius() {return largeRadius;}
	public double getSmallRadius() {return smallRadius;}
	
	// Setters
	public void setLargeRadius(double r) {largeRadius = r;}
	public void setSmallRadius(double r) {smallRadius = r;}
}