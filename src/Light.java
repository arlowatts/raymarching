package raymarching;

import raymarching.shapes.Shape;

public class Light extends Shape {
	// Constructors
	public Light(double[] args) {
		super(args[0], args[1], args[2], 1, 1, 1, 0, 0, 0, 0, 0, 0);
		setColor(Color.toColor(args[3], args[4], args[5]));
	}
	
	protected double getLocalDistance(Vector r) {return r.getLength();}
	
	protected double setBoundRadius() {return 0;}
}
