package src;

import src.Scene;
import src.GifSequenceWriter;
import src.UndefinedException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

import javafx.scene.image.ImageView;

import javafx.concurrent.Task;

import java.awt.image.BufferedImage;

import java.util.List;

import javafx.scene.image.WritableImage;

public class Raymarching extends Application {
	// All of the objects being rendered are stored in Scene object
	private Scene scene;
	private String sceneName;
	
	private WritableImage img;
	private ImageView imgView;
	private javafx.scene.Scene jfxScene;
	
	private GifSequenceWriter gifWriter;
	
	// Main
	@Override
	public void start(Stage stage) throws IOException {
		getSceneName(getParameters().getRaw());
		loadScene();
		
		img = scene.getScreen().getWritableImage();
		
		imgView = new ImageView(img);
		
		jfxScene = new javafx.scene.Scene(new StackPane(imgView), 500, 500);
        stage.setScene(jfxScene);
		stage.show();
		
		scene.start();
		
		/*if (scene.getMaxFrames() != -1) setupGifWriter();
		
		// The thread to run the main loop
		Thread th = new Thread(new Task<Integer>() {
			@Override
			protected Integer call() throws IOException {
				// The main loop
				while (true) {
					// Update and display the current frame
					scene.next();
					System.out.println(scene.getFrames());
					
					if (scene.getMaxFrames() != -1) {
						gifWriter.writeToSequence(scene.getScreen().getBufferedImage());
						
						if (scene.getFrames() >= scene.getMaxFrames()) break;
					}
				}
				
				if (scene.getMaxFrames() != -1) gifWriter.close();
				System.out.println(sceneName + ".gif saved");
				
				return scene.getFrames();
			}
		});
		
		th.setDaemon(true);
		th.start();*/
	}
	
	// Helpers
	// Gets the scene name from the arguments the program was run with
	private void getSceneName(List<String> args) {
		sceneName = args.size() < 1 || args.get(0).equals("${scene}") ? "default" : args.get(0);
		
		if (sceneName.length() >= 4 && sceneName.substring(sceneName.length() - 4).equals(".txt"))
			sceneName = sceneName.substring(0, sceneName.length() - 4);
	}
	
	// Loads the scene from a file
	private void loadScene() {
		try {
			scene = new Scene(sceneName + ".txt");
		}
		catch (UndefinedException e) {
			System.out.println(e);
			return;
		}
	}
	
	// Creates am ImageView and initializes the javafx Scene to display the rendered image
	private void initScreen(Stage stage) {
		imgView = new ImageView(scene.getScreen().getWritableImage());
		
		jfxScene = new javafx.scene.Scene(new StackPane(imgView), scene.getScreen().getWidth(), scene.getScreen().getHeight());
        stage.setScene(jfxScene);
		stage.show();
	}
	
	// Initializes a Gif writer to write the rendered images to a .gif file
	private void setupGifWriter() throws IOException{
		File outputFile = new File("gifs\\" + sceneName + ".gif");
		gifWriter = new GifSequenceWriter(ImageIO.createImageOutputStream(outputFile), BufferedImage.TYPE_INT_RGB, 1000 / 60, true);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
