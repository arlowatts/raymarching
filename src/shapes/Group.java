package src.shapes;

import java.lang.Math;
import java.util.ArrayList;

public class Group extends Shape {
	// Constants
	public static final String[] PARAMS = {"objectList", "smoothingVal"};
	
	// Member variables
	private ArrayList<Shape> shapes;
	private ArrayList<Character> modifiers;
	
	private double smoothing;
	
	// Constructors
	public Group(ArrayList<Shape> shapes, ArrayList<Character> modifiers, double smoothing, double[] args) {
		super(args);
		
		this.smoothing = Math.min(Math.max(smoothing, 0), 1);
		this.shapes = shapes;
		this.modifiers = modifiers;
		
		setBoundRadius();
	}
	
	// Methods
	public double getDistance(Vector v) {
		if (shapes.size() == 0) return getPos().getDistance(v);
		
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		double minDist = shapes.get(0).getDistance(v);
		
		for (int i = 1; i < shapes.size(); i++) {
			switch (modifiers.get(i)) {
				case '+':
				minDist = smoothUnion(minDist, shapes.get(i).getDistance(v));
				break;
				
				case '-':
				minDist = smoothDifference(minDist, shapes.get(i).getDistance(v));
				break;
				
				case '&':
				minDist = smoothIntersection(minDist, shapes.get(i).getDistance(v));
				break;
			}
		}
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return minDist;
	}
	
	public void add(Shape object, char modifier) {
		shapes.add(object);
		modifiers.add(modifier);
		setBoundRadius();
	}
	
	public Shape remove(int i) {
		modifiers.remove(i);
		setBoundRadius();
		return shapes.remove(i);
	}
	
	// Getters
	public Shape get(int i) {
		return shapes.get(i);
	}
	
	public int getMod(int i) {
		return modifiers.get(i);
	}
	
	// Setters
	public void setSmoothing(double smoothing) {this.smoothing = smoothing;}
	
	// Helpers
	private void setBoundRadius() {
		setBoundRadius(0);
		
		for (int i = 0; i < shapes.size(); i++) {
			if (modifiers.get(i) == '+') {
				setBoundRadius(Math.max(shapes.get(i).getPos().getLength() + shapes.get(i).getBoundRadius(), getBoundRadius()));
			}
		}
	}
	
	private double smoothUnion(double a, double b) {
		double h = Math.min(Math.max(0.5 + 0.5 * (a - b) / smoothing, 0), 1);
		return lerp(a, b, h) - smoothing * h * (1 - h);
	}
	
	private double smoothDifference(double a, double b) {
		double h = Math.min(Math.max(0.5 - 0.5 * (a + b) / smoothing, 0), 1);
		return lerp(a, -b, h) + smoothing * h * (1 - h);
	}
	
	private double smoothIntersection(double a, double b) {
		double h = Math.min(Math.max(0.5 - 0.5 * (a - b) / smoothing, 0), 1);
		return lerp(a, b, h) + smoothing * h * (1 - h);
	}
	
	private double lerp(double a, double b, double t) {
		return a + t * (b - a);
	}
}