package src.shapes;

import java.lang.Math;

public class Plane extends Shape {
	// Constants
	public static final String[] PARAMS = {"width", "length"};

	// Member variables
	private double width, length;
	
	// Constructors
	public Plane(double[] args, double[] dargs) {
		super(dargs);
		
		width = args[0];
		length = args[1];
		
		setBoundRadius();
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngle());
		
		v1.add(-Math.min(Math.max(-width, v1.x), width), 0, -Math.min(Math.max(-length, v1.z), length));
		
		return v1.getLength();
	}
	
	protected void setBoundRadius() {
		setBoundRadius(Math.sqrt(width*width + length*length));
	}
	
	public Vector getNormal(Vector v) {
		v.inverseRotate(getAngle(), getPos());
		
		Vector normal = new Vector(0, v.y > getPos().y ? 1 : -1, 0);
		
		v.rotate(getAngle(), getPos());
		
		normal.rotate(getAngle());
		
		return normal;
	}
	
	public int getColor(Vector v) {
		if (getTexture() == null) return getColor();
		
		Vector r = toSurface(v);
		
		int x = (int)(Math.min(Math.max(r.x / (width * 2) + 0.5, 0), 1) * (getTexture().getWidth() - 1));
		int y = (int)(Math.min(Math.max(r.z / (length * 2) + 0.5, 0), 1) * (getTexture().getHeight() - 1));
		
		return getTexture().getRGB(x, y);
	}
	
	// Getters
	public double getWidth() {return width;}
	public double getLength() {return length;}
	
	// Setters
	public void setWidth(double w) {
		width = w;
		setBoundRadius();
	}
	public void setLength(double l) {
		length = l;
		setBoundRadius();
	}
	
	public void setSize(double w, double l) {
		width = w;
		length = l;
		setBoundRadius();
	}
}
