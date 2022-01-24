import java.lang.Math;

public class Box extends Shape {
	// Member variables
	private double width, height, length, radius;
	
	// Constructors
	public Box(double w, double h, double l, double r, double x, double y, double z, double angleX, double angleY, double shine, int color) {
		super(x, y, z, angleX, angleY, shine, color);
		width = Math.max(w, Main.MIN_LENGTH);
		height = Math.max(h, Main.MIN_LENGTH);
		length = Math.max(l, Main.MIN_LENGTH);
		
		radius = r;
		
		updateBoundCorners();
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngleX(), getAngleY());
		v1.set(Math.abs(v1.getX()) - width + radius, Math.abs(v1.getY()) - height + radius, Math.abs(v1.getZ()) - length + radius);
		
		Vector v2 = new Vector(Math.max(v1.getX(), 0), Math.max(v1.getY(), 0), Math.max(v1.getZ(), 0));
		
		return v2.getLength() + Math.min(Math.max(v1.getX(), Math.max(v1.getY(), v1.getZ())), 0) - radius;
	}
	
	public void updateBoundCorners() {
		Vector[] boundCorners = getBoundCorners();
		
		for (int i = 0; i < 8; i++) {
			boundCorners[i].set(width * (((i << 1) & 2) - 1), height * ((i & 2) - 1), length * (((i >> 1) & 2) - 1));
		}
	}
	
	// Getters
	public double getWidth() {return width;}
	public double getHeight() {return height;}
	public double getLength() {return length;}
	public double getRadius() {return radius;}
	
	// Setters
	public void setWidth(double w) {width = w;}
	public void setHeight(double h) {height = h;}
	public void setLength(double l) {length = l;}
	public void setRadius(double r) {radius = r;}
	
	public void setSize(double w, double h, double l) {
		width = w;
		height = h;
		length = l;
	}
}