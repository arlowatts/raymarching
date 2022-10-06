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
		
		Vector q = new Vector(Math.sqrt(v.x*v.x + v.z*v.z), v.y + height / 2, 0);
		
		v.rotate(getAngle());
		v.add(getPos());
		
		Vector a = new Vector(radius, 0, 0);
		Vector b = new Vector(-radius, height, 0);
		
		double gamma = Math.min(Math.max((q.dotProduct(b) - a.dotProduct(b)) / b.dotProduct(b), 0), 1);
		
		b.multiply(gamma);
		a.add(b);
		
		Vector c = new Vector(Math.min(q.x, radius), 0, 0);
		
		double distance = Math.min(q.getDistance(a), q.getDistance(c));
		
		if (q.y > 0 && q.y < height && q.x < radius * (height - q.y) / height) distance *= -1;
		
		return distance;
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(radius*radius + height*height/4));
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
}
