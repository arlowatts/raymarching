import java.lang.Math;

public class Sphere extends Object{
	// Member variables
	private double radius;
	
	// Constructors
	public Sphere(double x, double y, double z, double r, double angleX, double angleY, int color) {
		super(x, y, z, angleX, angleY, color);
		radius = r;
	}
	
	public Sphere(Vector v, double r, double angleX, double angleY, int color) {this(v.getX(), v.getY(), v.getZ(), r, angleX, angleY, color);}
	
	public Sphere(double x, double y, double z, double r, int color) {this(x, y, z, r, 0, 0, color);}
	
	public Sphere(Vector v, double r, int color) {this(v.getX(), v.getY(), v.getZ(), r, 0, 0, color);}
	
	// Methods
	public void setBounds(Object camera, Screen screen) {
		double[] bounds = getBounds();
		
		Vector newPos = new Vector(getPos());
		
		newPos.subtract(camera.getPos());
		
		newPos.rotate(-camera.getAngleX(), -camera.getAngleY(), 0, 0, 0);
		
		if (newPos.getZ() < 1) {
			bounds[0] = -1;
			bounds[1] = -1;
			bounds[2] = -1;
			bounds[3] = -1;
		}
		else {
			double ratio = screen.getDistance() / newPos.getZ();
			
			bounds[0] = (newPos.getX() - radius) * ratio;
			bounds[1] = (newPos.getY() - radius) * ratio;
			bounds[2] = (newPos.getX() + radius) * ratio;
			bounds[3] = (newPos.getY() + radius) * ratio;
		}
	}
	
	public double getDistance(Vector v) {
		return getPos().getDistance(v) - radius;
	}
	
	public Vector getNormal(Vector v) {
		Vector normal = new Vector(v);
		
		normal.subtract(getPos());
		
		normal.setLength(1);
		
		return normal;
	}
	
	// Getters
	public double getRadius() {return radius;}
	
	// Setters
	public void setRadius(double r) {radius = r;}
}