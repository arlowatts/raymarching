package raymarching.shapes;

import raymarching.Color;
import raymarching.Vector;

import java.lang.Math;
import java.util.ArrayList;

/**
A subclass of Shape to contain multiple Shapes and perform additional operations on them.
*/
public class Group extends Shape {
	/**
	The list of parameters required by Group's constructor.
	The parameters are "smoothing".
	*/
	public static final String[] PARAMS = {"smoothing"};
	
	// Member variables
	private ArrayList<Shape> shapes;
	private ArrayList<Character> modifiers;
	
	private double smoothing;
	
	/**
	Creates a new Group from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Group.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Group.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Group(double[] args, double[] dargs) {
		super(dargs);
		
		this.smoothing = Math.min(Math.max(args[0], Shape.MIN_LENGTH), 1);
		
		this.shapes = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}
	
	// Methods
	protected double getLocalDistance(Vector r) {
		if (shapes.size() == 0) return r.getLength();
		
		double minDist = shapes.get(0).getDistance(r);
		double dist;
		
		for (int i = 1; i < shapes.size(); i++) {
			dist = shapes.get(i).getDistance(r);
			
			switch (modifiers.get(i)) {
				case '+':
				minDist = smoothUnion(minDist, dist);
				break;
				
				case '-':
				minDist = smoothDifference(minDist, dist);
				break;
				
				case '&':
				minDist = smoothIntersection(minDist, dist);
				break;
			}
		}
		
		return minDist;
	}
	
	protected double setBoundRadius() {
		double radius = 0;
		
		for (int i = 0; i < shapes.size(); i++) {
			if (modifiers.get(i) == '+') {
				radius = Math.max(shapes.get(i).getPos().getLength() + shapes.get(i).getBoundRadius(), radius);
			}
		}
		
		return radius;
	}
	
	@Override
	protected int getLocalColor(Vector r) {
		if (shapes.size() == 0) return getColor();
		
		int pointColor = 0;
		
		double[] dists = new double[shapes.size()];
		double sumDists = 0;
		
		for (int i = 0; i < shapes.size(); i++) {
			dists[i] = smoothing / Math.abs(shapes.get(i).getDistance(r));
			sumDists += dists[i];
		}
		
		for (int i = 0; i < shapes.size(); i++) {
			pointColor += Color.shade(shapes.get(i).getColor(r), dists[i] / sumDists);
		}
		
		return pointColor;
	}
	
	/**
	Add a new shape to the group.
	<code>modifier</code> is the modifier of the new shape and should be one of '+' (union), '-' (difference) and '&amp;' (intersection).
	A shapes modifier defines how its distance function is compared with the distance functions of the other shapes in the group.
	
	@param shape the new shape.
	@param modifier the modifier of the new shape.
	
	@return the index that the shape was added at.
	*/
	public int add(Shape shape, char modifier) {
		shapes.add(shape);
		modifiers.add(modifier);
		updateBoundRadius();
		
		return shapes.size() - 1;
	}
	
	/**
	Remove a shape from the group by its index.
	
	@param index the index of the shape.
	*/
	public void remove(int index) {
		shapes.remove(index);
		modifiers.remove(index);
		updateBoundRadius();
	}
	
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
