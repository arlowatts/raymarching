package raymarching;

import raymarching.shapes.Shape;

/*
 * An Action object stores an action that can be performed on a shape.
 */
public class Action {
	// Constants
	public static final String[] ACTION_TYPES = {"translate", "rotate", "rotateabout"};
	public static final int[] NUM_VECS_BY_TYPE = {1, 1, 2};
	
	// Member variables
	private int type;
	private Shape shape;
	private Vector[] vals;
	
	// Constructor
	public Action(int type, Shape shape, Vector[] vals) {
		this.type = type;
		this.shape = shape;
		this.vals = vals;
	}
	
	// Methods
	public void execute() {
		switch (type) {
			// Translation
			case 0:
			shape.getPos().add(vals[0]);
			break;
			
			// Rotation
			case 1:
			shape.getRot().add(vals[0]);
			break;
			
			// Rotation about a point
			case 2:
			shape.getPos().rotate(vals[0], vals[1]);
			break;
		}
	}
}
