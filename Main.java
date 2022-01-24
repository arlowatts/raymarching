import java.lang.Math;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
		Shape camera = new Shape(0, 0, -10, 0, 0, 0, 0);
		Screen screen = new Screen(500, 750, 500, 0x303030, 10, "Raymarching");
		
		// Creating the output file and GIF writer
		File outputFile = new File(".\\gifs\\Output.gif");
		GifSequenceWriter gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
		
		// Creating the arrays to contain the shapes and lights
		ArrayList<Shape> shapes = new ArrayList<>();
		ArrayList<Shape> lights = new ArrayList<>();
		
		loadSetup(".\\setup.txt", shapes, lights, camera, screen);
		
		// Add shapes
		Group coll = new Group(new ArrayList<Shape>(), 0, 0, 0, 0, 0, 0.5, 0.8, 0xf436a0);
		shapes.add(coll);
		
		coll.add(new Sphere(1.5, 2, -1, 0, 0, 0));
		coll.add(new Box(2.1, 3.14, 1.23, 0.1, 1, -0.5, 0.25, 0, 0, 0, 0));
		
		Box box = new Box(0.8, 1.9, 2.4, 0.4, -1.5, -2, -1.5, 0.2, 0, 0.5, 0x7bc4a8);
		shapes.add(box);
		
		Plane plane = new Plane(4, 4, 0, -5, 0, -1, 0, 0.3, 0xe69a3b);
		shapes.add(plane);
		
		Cylinder cylinder = new Cylinder(1, 3, 0.4, -1, -2, 3, -2.9, 0, 0.4, 0x4f7a42);
		shapes.add(cylinder);
		
		Torus torus = new Torus(2, 0.2, 0, 0, 0, 0, 0, 0.2, 0x554433);
		coll.add(torus);
		
		// Add lights
		Sphere lightA = new Sphere(0.1, 0, 3, 3, 0, 0xffff00);
		lights.add(lightA);
		
		Sphere lightB = new Sphere(0.1, 0, -3, -3, 0, 0xff00ff);
		lights.add(lightB);
		
		Sphere lightC = new Sphere(0.1, 0, -3, 3, 0, 0x00ffff);
		lights.add(lightC);
		
		Sphere lightD = new Sphere(0.1, 0, 3, -3, 0, 0xffffff);
		lights.add(lightD);
		
		shapes.addAll(lights);
		
		// The main loop
		while (true) {
			// Update and save the current frame
			screen.updateScene(camera, shapes, lights);
			
			gifWriter.writeToSequence(screen.getImage());
			
			torus.rotate(-0.15, 0);
			
			lightA.getPos().rotate(0.2, 0.1);
			lightB.getPos().rotate(0.1, 0.2);
			lightC.getPos().rotate(0.2, 0.1);
			lightD.getPos().rotate(0.1, 0.2);
			
			camera.getPos().rotate(0, 0.1);
			camera.rotate(0, 0.1);
			
			frames++;
			
			if (frames * 0.1 > Math.PI * 2) break;
		}
		
		gifWriter.close();
		System.out.println("GIF saved.");
		screen.dispose();
	}
	
	public static void loadSetup(String path, ArrayList<Shape> shapes, ArrayList<Shape> lights, Shape camera, Screen screen) throws FileNotFoundException {
		File setup = new File(path);
		
		Scanner scanner = new Scanner(setup);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(line);
		}
		
		scanner.close();
	}
	
	// Getters
	public static int getFrames() {return frames;}
}