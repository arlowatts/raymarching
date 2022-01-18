import java.lang.Math;

public class Plane extends Shape {
	// Member variables
	private double width, length;
	
	// Constructors
	public Plane(double x, double y, double z, double w, double l, double angleX, double angleY, int color, double shine) {
		super(x, y, z, angleX, angleY, color, shine);
		width = w;
		length = l;
		
		updateBoundCorners();
	}
	
	public Plane(Vector v, double w, double l, double angleX, double angleY, int color, double shine) {this(v.getX(), v.getY(), v.getZ(), w, l, angleX, angleY, color, shine);}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngleX(), getAngleY());
		
		Vector v2 = new Vector(Math.min(Math.max(-width, v1.getX()), width), 0, Math.min(Math.max(-length, v1.getZ()), length));
		
		return v2.getDistance(v1);
	}
	
	public void updateBoundCorners() {
		Vector[] boundCorners = getBoundCorners();
		
		for (int i = 0; i < 8; i++) {
			boundCorners[i].set(getPos());
			boundCorners[i].add(width * (((i << 1) & 2) - 1), 0, length * ((i & 2) - 1));
		}
	}
	
	// Getters
	public double getWidth() {return width;}
	public double getLength() {return length;}
	
	// Setters
	public void setWidth(double w) {width = w;}
	public void setLength(double l) {length = l;}
	
	public void setSize(double w, double l) {
		width = w;
		length = l;
	}
}