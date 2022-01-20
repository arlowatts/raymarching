import java.lang.Math;
import java.util.ArrayList;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	// Constants
	public static final int MAX_STEPS = 1000;
	public static final int MAX_LENGTH = 100;
	public static final double MIN_LENGTH = 0.01;
	
	private static int frames = 0;
	
	// Main
	public static void main(String[] args) {
		Shape camera = new Shape(0, 0, -10, 0, 0, 0, 0);
		Screen screen = new Screen(500, 750, 500, 0xffffff, 0.25, 10, "Raymarching");
		
		ArrayList<Shape> shapes = new ArrayList<>();
		ArrayList<Light> lights = new ArrayList<>();
		
		// Add shapes
		Group coll = new Group(0, 0, 0, 0, 0, 0.9, 0xf4a0e6, 0.8);
		shapes.add(coll);
		
		coll.add(new Sphere(2, -1, 0, 1.5, 0, 0.4, 0, 0));
		coll.add(new Box(1, -0.5, 0.25, 2.1, 3.14, 1.23, 0.1, 0, 0, 0, 0));
		
		Box box = new Box(-2.5, -3, -2.5, 0.8, 2.9, 3.4, 1, 0.2, 0, 0x7bc4a8, 0.5);
		//shapes.add(box);
		
		Plane plane = new Plane(0, -5, 0, 4, 4, -1, 0, 0xe69a3b, 0.7);
		shapes.add(plane);
		
		Cylinder cylinder = new Cylinder(-1, -2, 3, 1, 3, 0.4, -2.9, 0, 0x4f7a42, 0.4);
		shapes.add(cylinder);
		
		Torus torus = new Torus(0, 0, 0, 2, 0.2, 0, 0, 0x554433, 0.2);
		coll.add(torus);
		
		Sphere sphere = new Sphere(0, -2, 21, 10, 0, 0, 0xf4a0e6, 0.5);
		//shapes.add(sphere);
		
		// Add lights
		Light lightA = new Light(-10, 0, 0, 1, 0xffffff);
		lights.add(lightA);
		
		Light lightB = new Light(3, -3, -3, 1, 0xffffff);
		lights.add(lightB);
		
		while (true) {
			screen.updateScene(camera, shapes, lights);
			
			torus.rotate(0.15, 0);
			
			lightA.rotate(0, 0.1);
			lightB.rotate(0, 0.1);
			
			camera.getPos().rotate(0, 0.1);
			camera.rotate(0, 0.1);
			
			frames++;
		}
	}
	
	// Getters
	public static int getFrames() {return frames;}
}