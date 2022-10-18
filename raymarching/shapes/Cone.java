package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

public class Cone extends Shape {
	public static final String[] PARAMS = {"radius", "height"};
	
	private double radius, height;
	
	public Cone(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
	}
	
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector q = new Vector(Math.sqrt(v.x*v.x + v.z*v.z), v.y + height / 2, 0);
		
		v.rotate(getAngle());
		v.add(getPos());
		
		double epsilon = Math.min(Math.max((q.dotProduct(-radius, height, 0) + radius*radius) / (radius*radius + height*height), 0), 1);
		
		double distance = Math.min(q.getDistance(radius * (1 - epsilon), height * epsilon, 0),
								   q.x < radius ? Math.abs(q.y) : q.getDistance(radius, 0, 0));
		
		if (q.y > 0 && q.y < height && q.x < radius * (height - q.y) / height) distance *= -1;
		
		return distance;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(radius*radius + height*height / 4));
	}
	
	public Vector getNormal(Vector v) {
		Vector n = new Vector();
		
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		if (v.y < -height / 2) n.set(0, -1, 0);
		else {
			n.set(v.x, radius / height * Math.sqrt(v.x*v.x + v.z*v.z), v.z);
			n.setLength(1);
		}
		
		n.rotate(getAngle());
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return n;
	}
	
	public double getRadius() {return radius;}
	public double getHeight() {return height;}
	
	public void setRadius(double r) {
		radius = r;
		setBoundRadius(-1);
	}
	
	public void setHeight(double h) {
		height = h;
		setBoundRadius(-1);
	}
}
