import java.lang.Math;

public class Plane extends Shape {
	// Member variables
	private double width, length;
	
	// Constructors
	public Plane(double x, double y, double z, double w, double l, double angleX, double angleY, int color) {
		super(x, y, z, angleX, angleY, color);
		width = w;
		length = l;
	}
	
	public Plane(Vector v, double w, double l, double angleX, double angleY, int color) {this(v.getX(), v.getY(), v.getZ(), w, l, angleX, angleY, color);}
	
	// Methods
	public void setBounds(Shape camera, Screen screen) {
		double[] bounds = getBounds();
		
		bounds[0] = screen.getWidth();
		bounds[1] = screen.getHeight();
		bounds[2] = -screen.getWidth();
		bounds[3] = -screen.getHeight();
		
		int numInvalid = 0;
		
		for (int i = 0; i < 4; i++) {
			Vector newPos = new Vector(getPos());
			
			newPos.add(width * ((i & 1) * 2 - 1), 0, length * (((i >> 1) & 1) * 2 - 1));
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
		
		if (numInvalid == 4) {
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
		
		Vector v2 = new Vector(Math.min(Math.max(-width, v1.getX()), width), 0, Math.min(Math.max(-length, v1.getZ()), length));
		
		return v2.getDistance(v1);
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