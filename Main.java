import java.lang.Math;
import java.util.ArrayList;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	private static int frames = 0;
	
	public static void main(String[] args) {
		Shape camera = new Shape(0, 0, -10, 0, 0, 0);
		Screen screen = new Screen(1500, 900, 500, 0xffffff, "Raymarching");
		
		ArrayList<Shape> objects = new ArrayList<>();
		
		Group coll = new Group(0, 0, 0, 0, 0, 0.1, 0xf4a0e6);
		objects.add(coll);
		
		for (int i = 0; i < 2; i++) {
			coll.add(new Sphere(2, -1, 0, 1.5, 0, 0, 0));
			
			coll.add(new Box(1, -0.5, 0.25, 2.1, 3.14, 1.23, 0, 0, 0, 0));
		}
		
		Box box = new Box(-2.5, -3, -2.5, 0.8, 2.9, 3.4, 0.4, 0, 0, 0x7bc4a8);
		objects.add(box);
		
		while (true) {
			screen.updateScene(camera, objects);
			
			camera.getPos().rotate(0, 0.1, 0, 0, 0);
			camera.rotate(0, 0.1);
			
			frames++;
		}
	}
}