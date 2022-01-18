import java.lang.Math;

public class Sphere extends Shape{
	// Member variables
	private double radius;
	
	// Constructors
	public Sphere(double x, double y, double z, double r, double angleX, double angleY, int color, double shine) {
		super(x, y, z, angleX, angleY, color, shine);
		radius = r;
		
		updateBoundCorners();
	}
	
	public Sphere(Vector v, double r, double angleX, double angleY, int color, double shine) {this(v.getX(), v.getY(), v.getZ(), r, angleX, angleY, color, shine);}
	
	// Methods
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	public void updateBoundCorners() {
		Vector[] boundCorners = getBoundCorners();
		
		for (int i = 0; i < 8; i++) {
			boundCorners[i].set(getPos());
			boundCorners[i].add(radius * (((i << 1) & 2) - 1), radius * ((i & 2) - 1), radius * (((i >> 1) & 2) - 1));
		}
	}
	
	// Getters
	public double getRadius() {return radius;}
	
	// Setters
	public void setRadius(double r) {radius = r;}
}