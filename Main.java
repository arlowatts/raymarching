import java.lang.Math;
import java.util.ArrayList;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	public static void main(String[] args) throws InterruptedException {
		Object camera = new Object(0, 0, -25, 0);
		Screen screen = new Screen(1500, 900, 500, 0xffffff, "Raymarching");
		
		ArrayList<Object> objects = new ArrayList<>();
		
		objects.add(new Box(-1, -2, -1, 2, 4, 2, 0x0088ff));
		objects.add(new Box(1, -2.5, -0.5, 1, 4, 1, 0x0077ee));
		objects.add(new Box(-2, -2.5, -0.5, 1, 4, 1, 0x0077ee));
		objects.add(new Box(0.5, -6, -0.5, 1, 4, 1, 0x0077ee));
		objects.add(new Box(-1.5, -6, -0.5, 1, 4, 1, 0x0077ee));
		objects.add(new Sphere(0, 3.5, 0, 1.5, 0xffddbb));
		
		while (true) {
			screen.update(camera, objects);
			
			for (int i = 0; i < objects.size(); i++) {
				objects.get(i).rotate(0.08, 0);
				objects.get(i).getPos().rotate(0.08, 0, 0, 0, 0);
			}
			
			//Thread.sleep(10);
		}
	}
}