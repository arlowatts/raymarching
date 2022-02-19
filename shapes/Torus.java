package shapes;

import java.lang.Math;

public class Torus extends Shape{
	// Member variables
	private double largeRadius, smallRadius;
	
	// Constructors
	public Torus(double R, double r, double x, double y, double z, double phi, double theta, double psi, double shine, int color) {
		super(x, y, z, phi, theta, psi, shine, color);
		largeRadius = Math.max(R, Shape.MIN_LENGTH);
		smallRadius = Math.max(r, Shape.MIN_LENGTH);
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngle());
		
		double a = Math.sqrt(v1.getX()*v1.getX() + v1.getZ()*v1.getZ()) - largeRadius;
		
		return Math.sqrt(a*a + v1.getY()*v1.getY()) - smallRadius;
	}
	
	// Getters
	public double getLargeRadius() {return largeRadius;}
	public double getSmallRadius() {return smallRadius;}
	
	// Setters
	public void setLargeRadius(double r) {largeRadius = r;}
	public void setSmallRadius(double r) {smallRadius = r;}
}