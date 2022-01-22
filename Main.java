import java.lang.Math;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
	private Ray forSomeReasonTheRayClassDoesntRecompileOnItsOwn;
	
	// Constants
	public static final int MAX_STEPS = 1000;
	public static final int MAX_LENGTH = 100;
	public static final double MIN_LENGTH = 0.01;
	
	private static int frames = 0;
	
	// Main
	public static void main(String[] args) throws IOException {
		// Creating the main camera and screen
		Shape camera = new Shape(-10, 0, 0, 0, -Math.PI / 2, 0, 0);
		Screen screen = new Screen(500, 750, 500, 0xffffff, 0.2, 10, "Raymarching");
		
		// Creating the output file and GIF writer
		File outputFile = new File(".\\gifs\\Output.gif");
		GifSequenceWriter gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
		
		// Creating the arrays to contain the shapes and lights
		ArrayList<Shape> shapes = new ArrayList<>();
		ArrayList<Light> lights = new ArrayList<>();
		
		// Add shapes
		Group coll = new Group(0, 0, 0, 0, 0, 0.9, 0xf4a0e6, 0.8);
		shapes.add(coll);
		
		coll.add(new Sphere(2, -1, 0, 1.5, 0, 0));
		coll.add(new Box(1, -0.5, 0.25, 2.1, 3.14, 1.23, 0.1, 0, 0, 0, 0));
		
		Box box = new Box(-1.5, -2, -1.5, 0.8, 1.9, 2.4, 0.4, 0.2, 0, 0x7bc4a8, 0.5);
		shapes.add(box);
		
		Plane plane = new Plane(0, -5, 0, 4, 4, 1, 0, 0xe69a3b, 0.7);
		shapes.add(plane);
		
		Cylinder cylinder = new Cylinder(-1, -2, 3, 1, 3, 0.4, -2.9, 0, 0x4f7a42, 0.4);
		shapes.add(cylinder);
		
		Torus torus = new Torus(0, 0, 0, 2, 0.2, 0, 0, 0x554433, 0.2);
		coll.add(torus);
		
		// Add lights
		Light lightA = new Light(0, 3, 3, 0.1, 0.8, 0xff0000);
		lights.add(lightA);
		shapes.add(lightA);
		
		Light lightB = new Light(0, -3, -3, 0.1, 0.8, 0x0000ff);
		lights.add(lightB);
		shapes.add(lightB);
		
		Light lightC = new Light(0, -3, 3, 0.1, 0.8, 0x00ff00);
		lights.add(lightC);
		shapes.add(lightC);
		
		Light lightD = new Light(0, 3, -3, 0.1, 0.8, 0xffffff);
		lights.add(lightD);
		shapes.add(lightD);
		
		// The main loop
		while (true) {
			// Update and save the current frame
			screen.updateScene(camera, shapes, lights);
			
			gifWriter.writeToSequence(screen.getImage());
			
			torus.rotate(-0.15, 0);
			
			plane.setAngleY(Math.sin(frames / 10.0));
			
			lightA.getPos().rotate(0.1, 0);
			lightB.getPos().rotate(0.1, 0);
			lightC.getPos().rotate(0.1, 0);
			lightD.getPos().rotate(0.1, 0);
			
			camera.getPos().rotate(0, 0.1);
			camera.rotate(0, 0.1);
			
			frames++;
			
			if (frames * 0.1 > Math.PI * 2) break;
		}
		
		gifWriter.close();
		System.out.println("GIF saved.");
		screen.dispose();
	}
	
	// Getters
	public static int getFrames() {return frames;}
}