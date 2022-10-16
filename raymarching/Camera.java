package raymarching;

import raymarching.shapes.Shape;
import raymarching.shapes.Vector;

public class Camera extends Shape {
	// Constructors
	public Camera(double[] args) {
		super(args[0], args[1], args[2], args[3], args[4], args[5], 0, 0, 0);
	}
	
	public double getDistance(Vector v) {return getPos().getDistance(v);}
	
	public void setBoundRadius() {setBoundRadius(0);}
}
