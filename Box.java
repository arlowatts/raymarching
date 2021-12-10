import java.lang.Math;

public class Box extends Object {
	private double width, height, length;
	
	public Box(double x, double y, double z, double w, double h, double l, int color) {
		super(x, y, z, color);
		width = w;
		height = h;
		length = l;
	}
	
	public Box(Vector v, double w, double h, double l, int color) {this(v.getX(), v.getY(), v.getZ(), w, h, l, color);}
	
	public double getDistance(Vector v) {
		Vector nearest = new Vector(Math.max(0, Math.min(width,  getPos().getX() - v.getX())) + v.getX(),
									Math.max(0, Math.min(height, getPos().getY() - v.getY())) + v.getY(),
									Math.max(0, Math.min(length, getPos().getZ() - v.getZ())) + v.getZ());
		
		return getPos().getDistance(nearest);
	}
	
	public Vector getNormal(Vector v) {
		Vector normal = new Vector(v);
		
		normal.add(width / 2.0, height / 2.0, length / 2.0);
		
		normal.subtract(getPos());
		
		normal.setLength(1);
		
		return normal;
	}
}