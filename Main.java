import src.*;
import src.shapes.*;

import java.lang.Math;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
	// All of the objects being rendered are stored in Scene object
	private static Scene scene;
	
	// Main
	public static void main(String[] args) throws IOException {
		// Creates and loads the scene from a file
		String sceneName = args.length > 0 ? args[0] : "setup";
		
		if (sceneName.length() >= 4 && sceneName.substring(sceneName.length() - 4).equals(".txt"))
			sceneName = sceneName.substring(0, sceneName.length() - 4);
		
		try {
			scene = new Scene(sceneName + ".txt");
		}
		catch (UndefinedException e) {
			System.out.println(e);
			return;
		}
		
		// Creating the output file and GIF writer
		File outputFile = new File("gifs\\" + sceneName + ".gif");
		GifSequenceWriter gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
		
		// The main loop
		while (true) {
			// Update and save the current frame
			scene.updateScreen();
			scene.executeActions();
			
			if (scene.getMaxFrames() != -1) {
				gifWriter.writeToSequence(scene.getScreen().getImage());
				
				if (scene.getFrames() >= scene.getMaxFrames()) break;
			}
		}
		
		gifWriter.close();
		System.out.println(sceneName + ".gif saved");
		scene.getScreen().dispose();
	}
}