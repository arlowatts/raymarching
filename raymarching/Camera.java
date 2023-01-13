package raymarching;

import raymarching.shapes.Shape;

public class Camera extends Shape {
	// Constructors
	public Camera(double[] args) {
		super(args[0], args[1], args[2], 1, 1, 1, args[3], args[4], args[5], 0, 0, 0);
	}
	
	protected double getLocalDistance(Vector r) {return r.getLength();}
	
	protected double setBoundRadius() {return 0;}
}
