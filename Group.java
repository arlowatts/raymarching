import java.lang.Math;
import java.util.ArrayList;

public class Group extends Shape {
	// Member variables
	private ArrayList<Shape> objects;
	
	private double smoothing;
	
	// Constructors
	public Group(ArrayList<Shape> objects, double x, double y, double z, double angleX, double angleY, double smoothing, double shine, int color) {
		super(x, y, z, angleX, angleY, shine, color);
		this.smoothing = smoothing;
		this.objects = objects;
		
		updateBoundCorners();
	}
	
	// Methods
	public double getDistance(Vector v) {
		if (objects.size() == 0) return getPos().getDistance(v);
		
		v.subtract(getPos());
		v.inverseRotate(getAngleX(), getAngleY());
		
		double minDist = objects.get(0).getDistance(v);
		
		for (int i = 1; i < objects.size(); i++) {
			minDist = smoothMin(minDist, objects.get(i).getDistance(v), smoothing);
		}
		
		v.rotate(getAngleX(), getAngleY());
		v.add(getPos());
		
		return minDist;
	}
	
	public void updateBoundCorners() {
		for (int i = 0; i < objects.size(); i++) {
			for (int j = 0; j < 8; j++) {
				Vector groupCorner = getBoundCorners()[j];
				Vector shapeCorner = objects.get(i).getBoundCorners()[j];
				
				groupCorner.setX(((j & 1) == 0) ? Math.min(groupCorner.getX(), shapeCorner.getX()) : Math.max(groupCorner.getX(), shapeCorner.getX()));
				
				groupCorner.setY(((j & 2) == 0) ? Math.min(groupCorner.getY(), shapeCorner.getY()) : Math.max(groupCorner.getY(), shapeCorner.getY()));
				
				groupCorner.setZ(((j & 4) == 0) ? Math.min(groupCorner.getZ(), shapeCorner.getZ()) : Math.max(groupCorner.getZ(), shapeCorner.getZ()));
			}
		}
	}
	
	public void add(Shape object) {
		objects.add(object);
		updateBoundCorners();
	}
	
	public void remove(int i) {
		objects.remove(i);
		updateBoundCorners();
	}
	
	// Getters
	public Shape get(int i) {
		return objects.get(i);
	}
	
	// Setters
	public void setSmoothing(double smoothing) {this.smoothing = smoothing;}
	
	// Helpers
	private double smoothMin(double a, double b, double k) {
		double h = Math.max(k - Math.abs(a - b), 0) / k;
		
		return Math.min(a, b) - h * h * k * 0.25;
	}
}