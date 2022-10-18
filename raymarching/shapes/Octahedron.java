package raymarching.shapes;

import java.lang.Math;

public class Octahedron extends Shape {
	public static final String[] PARAMS = {"width", "height", "depth"};
	
	private double width, height, depth;
	
	public Octahedron(double[] args, double[] dargs) {
		super(dargs);
		
		width = Math.max(args[0], MIN_LENGTH);
		height = Math.max(args[1], MIN_LENGTH);
		depth = Math.max(args[2], MIN_LENGTH);
	}
	
	public double getDistance(Vector v) {
		Vector r = new Vector(v);
		
		r.subtract(getPos());
		r.inverseRotate(getAngle());
		
		r.setPositive();
		r.x -= width;
		
		Vector n = new Vector(width, height, depth);
		n.setLength(1);
		
		return r.dotProduct(n);
	}
	
	public Vector getNormal(Vector v) {
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		Vector n = new Vector(width, height, depth);
		n.setLength(1);
		
		n.copySign(v);
		
		n.rotate(getAngle());
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return n;
	}
	
	public void setBoundRadius() {
		setBoundRadius(Math.max(Math.max(width, height), depth));
	}
	
	public double getWidth() {return width;}
	public double getHeight() {return height;}
	public double getDepth() {return depth;}
	
	public void setWidth(double w) {width = w;}
	public void setHeight(double h) {height = h;}
	public void setDepth(double d) {depth = d;}
}
