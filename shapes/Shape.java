package shapes;

import java.lang.Math;

public class Shape {
	// Constants
	public static final double MIN_LENGTH = 0.01;
	
	// Member variables
	private Vector pos, angle;
	
	private int color;
	private double shine;
	
	// Constructors
	public Shape(double x, double y, double z, double phi, double theta, double psi, double shine, int color) {
		pos = new Vector(x, y, z);
		angle = new Vector(phi, theta, psi);
		
		this.color = color & 0xffffff;
		
		this.shine = Math.min(Math.max(shine, 0), 1);
	}
	
	//Methods
	public double getDistance(Vector v) {return pos.getDistance(v);}
	
	public Vector getNormal(Vector v) {
		double distance = getDistance(v);
		
		v.add(MIN_LENGTH, 0, 0);
		double xDistance = getDistance(v);
		
		v.add(-MIN_LENGTH, MIN_LENGTH, 0);
		double yDistance = getDistance(v);
		
		v.add(0, -MIN_LENGTH, MIN_LENGTH);
		double zDistance = getDistance(v);
		
		v.add(0, 0, -MIN_LENGTH);
		
		Vector normal = new Vector(xDistance - distance, yDistance - distance, zDistance - distance);
		normal.setLength(1);
		
		return normal;
	}
	
	// Getters
	public Vector getPos() {return pos;}
	
	public Vector getAngle() {return angle;}
	
	public int getColor() {return color;}
	public double getShine() {return shine;}
	
	// Setters
	public void setColor(int color) {this.color = color;}
	public void setShine(double shine) {this.shine = shine;}
}