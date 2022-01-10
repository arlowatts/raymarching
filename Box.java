import java.lang.Math;

public class Box extends Shape {
	// Member variables
	private double width, height, length, radius;
	
	// Constructors
	public Box(double x, double y, double z, double w, double h, double l, double r, double angleX, double angleY, int color) {
		super(x, y, z, angleX, angleY, color);
		width = w;
		height = h;
		length = l;
		
		radius = r;
	}
	
	public Box(Vector v, double w, double h, double l, double r, double angleX, double angleY, int color) {this(v.getX(), v.getY(), v.getZ(), w, h, l, r, angleX, angleY, color);}
	
	// Methods
	public void setBounds(Shape camera, Screen screen) {
		double[] bounds = getBounds();
		
		bounds[0] = screen.getWidth();
		bounds[1] = screen.getHeight();
		bounds[2] = -screen.getWidth();
		bounds[3] = -screen.getHeight();
		
		int numInvalid = 0;
		
		for (int i = 0; i < 8; i++) {
			Vector newPos = new Vector(getPos());
			
			newPos.add(width * ((i & 1) * 2 - 1), height * (((i >> 1) & 1) * 2 - 1), length * (((i >> 2) & 1) * 2 - 1));
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
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngleX(), getAngleY(), 0, 0, 0);
		v1.set(Math.abs(v1.getX()) - width + radius, Math.abs(v1.getY()) - height + radius, Math.abs(v1.getZ()) - length + radius);
		
		Vector v2 = new Vector(Math.max(v1.getX(), 0), Math.max(v1.getY(), 0), Math.max(v1.getZ(), 0));
		
		return v2.getLength() + Math.min(Math.max(v1.getX(), Math.max(v1.getY(), v1.getZ())), 0) - radius;
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