package src.shapes;

import java.lang.Math;
import java.util.ArrayList;

public class Group extends Shape {
	// Constants
	public static final String[] PARAMS = {"smoothing"};
	
	// Member variables
	private ArrayList<Shape> shapes;
	private ArrayList<Character> modifiers;
	
	private double smoothing;
	
	// Constructors
	public Group(double[] args, double[] dargs) {
		super(dargs);
		
		this.smoothing = Math.min(Math.max(args[0], Shape.MIN_LENGTH), 1);
		
		this.shapes = new ArrayList<>();
		this.modifiers = new ArrayList<>();
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
	
	protected void setBoundRadius() {
		setBoundRadius(0);
		
		for (int i = 0; i < shapes.size(); i++) {
			if (modifiers.get(i) == '+') {
				setBoundRadius(Math.max(shapes.get(i).getPos().getLength() + shapes.get(i).getBoundRadius(), getBoundRadius()));
			}
		}
	}
	
	public int getColor(Vector v) {
		if (shapes.size() == 0) return getColor();
		
		v.subtract(getPos());
		v.inverseRotate(getAngle());
		
		int pointColor = 0;
		
		double[] dists = new double[shapes.size()];
		double sumDists = 0;
		
		for (int i = 0; i < shapes.size(); i++) {
			dists[i] = smoothing / Math.abs(shapes.get(i).getDistance(v));
			sumDists += dists[i];
		}
		
		for (int i = 0; i < shapes.size(); i++) {
			pointColor += Color.shade(shapes.get(i).getColor(v), dists[i] / sumDists);
		}
		
		v.rotate(getAngle());
		v.add(getPos());
		
		return pointColor;
	}
	
	public void add(Shape object, char modifier) {
		shapes.add(object);
		modifiers.add(modifier);
		setBoundRadius(-1);
	}
	
	public void remove(int i) {
		shapes.remove(i);
		modifiers.remove(i);
		setBoundRadius(-1);
	}
	
	// Getters
	public double getSmoothing() {return smoothing;}
	
	public Shape getShape(int i) {
		return shapes.get(i);
	}
	
	public char getModifier(int i) {
		return modifiers.get(i);
	}
	
	// Setters
	public void setSmoothing(double smoothing) {this.smoothing = smoothing;}
	
	// Helpers
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
