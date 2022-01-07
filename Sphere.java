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
		
		bounds[0] = screen.getWidth();
		bounds[1] = screen.getHeight();
		bounds[2] = -screen.getWidth();
		bounds[3] = -screen.getHeight();
		
		int numInvalid = 0;
		
		for (int i = 0; i < 8; i++) {
			Vector newPos = new Vector(getPos());
			
			newPos.add(radius * (((i << 1) & 2) - 1), radius * ((i & 2) - 1), radius * (((i >> 1) & 2) - 1));
			
			newPos.rotate(getAngleX(), getAngleY(), getPos());
			
			newPos.subtract(camera.getPos());
			
			newPos.inverseRotate(camera.getAngleX(), camera.getAngleY(), 0, 0, 0);
			
			if (newPos.getZ() < 1) numInvalid++;
			
			else {
				double ratio = screen.getDistance() / newPos.getZ();
				
				double screenX = newPos.getX() * ratio + screen.getWidth() / 2;
				double screenY = newPos.getY() * ratio + screen.getHeight() / 2;
				
				bounds[0] = Math.min(bounds[0], screenX);
				bounds[1] = Math.min(bounds[1], screenY);
				bounds[2] = Math.max(bounds[2], screenX);
				bounds[3] = Math.max(bounds[3], screenY);
			}
		}
		
		if (numInvalid == 8) {
			bounds[0] = 0;
			bounds[1] = 0;
			bounds[2] = 0;
			bounds[3] = 0;
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