import shapes.*;

import java.lang.Math;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
	// Variables
	private static int frames = 0;
	
	private static Scene scene;
	
	// Main
	public static void main(String[] args) throws IOException, InvalidSetupException {
		// Creates and loads the scene
		scene = new Scene("setup.txt");
		
		// Creating the output file and GIF writer
		File outputFile = new File("gifs\\Output.gif");
		GifSequenceWriter gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
		
		// The main loop
		while (true) {
			// Update and save the current frame
			scene.updateScreen();
			gifWriter.writeToSequence(scene.getScreen().getImage());
			
			scene.getCamera().getPos().rotate(-Math.PI / 2, -0.1, Math.PI / 2);
			scene.getCamera().getAngle().add(0, -0.1, 0);
			
			scene.getShapes().get(4).getPos().rotate(-Math.PI / 2, -0.1, Math.PI / 2);
			scene.getShapes().get(4).getAngle().add(0, -0.1, 0);
			
			frames++;
			
			if (frames * 0.1 > Math.PI * 2) break;
		}
		
		gifWriter.close();
		System.out.println("GIF saved.");
		scene.getScreen().dispose();
	}
	
	// Getters
	public static int getFrames() {return frames;}
}