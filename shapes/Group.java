package shapes;

import java.lang.Math;
import java.util.ArrayList;

public class Group extends Shape {
	// Constants
	public static final String[] PARAMS = {"objectList", "smoothingVal"};
	
	// Member variables
	private ArrayList<Shape> objects;
	
	private double smoothing;
	
	// Constructors
	public Group(ArrayList<Shape> objects, double smoothing, double[] args) {
		super(args);
		this.smoothing = smoothing;
		this.objects = objects;
	}
	
	// Methods
	public double getDistance(Vector v) {
		if (objects.size() == 0) return getPos().getDistance(v);
		
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		double minDist = objects.get(0).getDistance(v);
		
		for (int i = 1; i < objects.size(); i++) {
			minDist = smoothMin(minDist, objects.get(i).getDistance(v), smoothing);
		}
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return minDist;
	}
	
	public void add(Shape object) {
		objects.add(object);
	}
	
	public void remove(int i) {
		objects.remove(i);
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