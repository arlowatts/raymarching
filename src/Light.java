package src;

import src.shapes.Shape;
import src.shapes.Color;

public class Light extends Shape {
	// Constructors
	public Light(double[] args) {
		super(args[0], args[1], args[2], 0, 0, 0, 0, 0, 0);
		setColor(Color.toColor(args[3], args[4], args[5]));
	}
}
