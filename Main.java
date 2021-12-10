import java.lang.Math;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	public static void main(String[] args) throws InterruptedException {
		Camera c = new Camera(0, 0, -50);
		Screen s = new Screen(500, 300, 500, 0xffffff, "Raymarching");
		
		Object objects[] = new Object[5];
		objects[0] = new Box(-5, 0, -20, 3, 3, 5, 0x00ffff);
		objects[1] = new Sphere(2, -6, 20, 2, 0xff00ff);
		objects[2] = new Sphere(0, 0, 10, 1, 0xffff00);
		objects[3] = new Box(-1, 3, 0, 2, 2, 4, 0x0000ff);
		objects[4] = new Sphere(8, 9, 10, 4, 0xff0000);
		
		while (true) {
			s.update(c, objects);
			
			//Thread.sleep(10);
			
			//c.getPos().add(-Math.cos(c.getAngleY()), 0, -Math.sin(c.getAngleY()));
			
			//c.setAngleY(c.getAngleY() - 0.025);
		}
	}
}