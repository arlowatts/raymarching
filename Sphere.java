import java.lang.Math;

public class Sphere extends Object{
	private double radius;
	
	public Sphere(double x, double y, double z, double r, int color) {
		super(x, y, z, color);
		radius = r;
	}
	
	public Sphere(Vector v, double r, int color) {this(v.getX(), v.getY(), v.getZ(), r, color);}
	
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	public Vector getNormal(Vector v) {
		Vector normal = new Vector(v);
		
		normal.subtract(getPos());
		
		normal.setLength(1);
		
		return normal;
	}
}