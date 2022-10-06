package src.shapes;

import java.lang.Math;

public class Cone extends Shape {
	public static final String[] PARAMS = {"radius", "height"};
	
	private double radius, height;
	
	public Cone(double[] args, double[] dargs) {
		super(dargs);
		
		radius = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		
		setBoundRadius();
	}
	
	public double getDistance(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector q = new Vector(Math.sqrt(v.x*v.x + v.z*v.z), v.y, 0);
		
		Vector a = new Vector(radius, 0, 0);
		Vector b = new Vector(-radius, height, 0);
		b.setLength(1);
		
		double gamma = q.dotProduct(b) - a.dotProduct(b);
		
		b.multiply(gamma);
		a.add(b);
		a.y = Math.min(Math.max(a.y, 0), height);
		
		double distance = q.getDistance(a);
		
		if (q.y > 0 && q.y < height && q.x < radius * (height - q.y) / height) distance *= -1;
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return distance;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(height);
	}
}
