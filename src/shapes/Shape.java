package src.shapes;

import java.lang.Math;

public class Shape {
	// Constants
	public static final String[] DEFAULT_PARAMS = {"x", "y", "z", "phi", "theta", "psi", "shine", "transparency", "refrIndex", "color"};
	
	public static final double MIN_LENGTH = 0.001;
	
	// Member variables
	private Vector pos, angle;
	
	private int color;
	private double shine, transparency, refrIndex;
	
	private double boundRadius;
	
	// Constructors
	public Shape(double... args) {
		pos = new Vector(args[0], args[1], args[2]);
		angle = new Vector(args[3], args[4], args[5]);
		
		shine = Math.min(Math.max(args[6], 0), 1);
		transparency = Math.min(Math.max(args[7], 0), 1);
		refrIndex = Math.max(args[8], 1);
		
		color = (int)args[9] & 0xffffff;
		
		boundRadius = -1;
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
	
	public double getTransparency() {return transparency;}
	public double getRefrIndex() {return refrIndex;}
	
	public double getBoundRadius() {
		if (boundRadius == -1) setBoundRadius();
		return boundRadius;
	}
	
	// Setters
	public void setColor(int color) {this.color = color;}
	public void setShine(double shine) {this.shine = shine;}
	
	public void setBoundRadius(double r) {boundRadius = r;}
	
	// Helpers
	protected void setBoundRadius() {boundRadius = 0;}
}