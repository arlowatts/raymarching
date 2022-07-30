package src;

import src.Scene;

import src.GifSequenceWriter;

import src.UndefinedException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class Raymarching {
	// All of the objects being rendered are stored in Scene object
	private static Scene scene;
	private static String sceneName;
	
	private static JFrame frame;
	private static JLabel label;
	
	private static GifSequenceWriter gifWriter;
	
	// Main
	public static void main(String[] args) throws IOException {
		getSceneName(args);
		loadScene();
		
		createJFrame();
		
		if (scene.getMaxFrames() != -1) setupGifWriter();
		
		// The main loop
		while (true) {
			// Update and display the current frame
			scene.tick();
			label.updateUI();
			
			if (scene.getMaxFrames() != -1) {
				gifWriter.writeToSequence(scene.getScreen().getImage());
				
				if (scene.getFrames() >= scene.getMaxFrames()) break;
			}
		}
		
		if (scene.getMaxFrames() != -1) gifWriter.close();
		System.out.println(sceneName + ".gif saved");
		frame.dispose();
	}
	
	// Helpers
	// Gets the scene name from the arguments the program was run with
	private static void getSceneName(String[] args) {
		sceneName = args[0].equals("${setup}") ? "default" : args[0];
		
		if (sceneName.length() >= 4 && sceneName.substring(sceneName.length() - 4).equals(".txt"))
			sceneName = sceneName.substring(0, sceneName.length() - 4);
	}
	
	// Loads the scene from a file
	private static void loadScene() {
		try {
			scene = new Scene(sceneName + ".txt");
		}
		catch (UndefinedException e) {
			System.out.println(e);
			return;
		}
	}
	
	// Creates a JFrame and a JLabel to display the rendered image
	private static void createJFrame() {
		frame = new JFrame(sceneName);
		frame.setSize(scene.getScreen().getWidth(), scene.getScreen().getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		label = new JLabel(new ImageIcon(scene.getScreen().getImage()));
		frame.getContentPane().add(label);
	}
	
	// Initializes a Gif writer to write the rendered images to a .gif file
	private static void setupGifWriter() throws IOException{
		File outputFile = new File("gifs\\" + sceneName + ".gif");
		gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
	}
}