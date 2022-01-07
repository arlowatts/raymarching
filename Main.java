import java.lang.Math;
import java.util.ArrayList;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	private static final int numShapes = 2;
	
	public static void main(String[] args) throws InterruptedException {
		Object camera = new Object(0, 0, -25, 0);
		Screen screen = new Screen(1500, 900, 500, 0xffffff, "Raymarching");
		
		ArrayList<Object> objects = new ArrayList<>();
		
		for (int i = 0; i < numShapes; i++) {
			Collection coll = new Collection(Math.random() * 10 - 5, Math.random() * 10 - 5, Math.random() * 10 - 5, (int)(Math.random() * 0xffffff));
			objects.add(coll);
			
			coll.add(new Sphere(Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 2, (int)(Math.random() * 0xffffff)));
			
			coll.add(new Box(Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 4, Math.random() * 4, Math.random() * 4, (int)(Math.random() * 0xffffff)));
		}
		
		while (true) {
			screen.update(camera, objects);
			
			for (int i = 0; i < objects.size(); i++) {
				objects.get(i).rotate(0.025, 0.05);
				objects.get(i).getPos().rotate(0.025, 0.05, 0, 0, 0);
			}
		}
	}
}