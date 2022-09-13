package src;

import src.shapes.Vector;

import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelFormat;

import java.awt.image.BufferedImage;

/*
 * A Screen object creates the rendered image each frame.
 */
public class Screen {
	// Member variables
	private int width, height, distance, background, maxReflections;
	
	private WritableImage wImage;
	private BufferedImage bImage;
	private int[] pixels;
	
	// Constructors
	public Screen(int[] args) {
		width = args[0];
		height = args[1];
		distance = args[2];
		
		background = args[3];
		
		maxReflections = args[4];
		
		// Creating the Buffered Image and a pixels array to update individual pixels faster
		wImage = new WritableImage(width, height);
		bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = new int[width * height];
	}
	
	// Methods
	public void updateImage(Scene scene) {
		int k = 0;
		
		// Iterating through every pixel to cast a ray through it
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				// Create a ray starting at the camera and passing through the current pixel
				Ray ray = new Ray(scene.getCamera().getPos(), new Vector(x - width / 2, y - height / 2, distance));
				
				// Rotate it by the camera's rotation
				ray.getDir().rotate(scene.getCamera().getAngle());
				
				pixels[k++] = ray.cast(scene, new Vector(1, 1, 1), null, maxReflections);
			}
		}
		
		// Updating the screen
		bImage.setRGB(0, 0, width, height, pixels, 0, width);
		wImage.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
	}
	
	// Getters
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getDistance() {return distance;}
	public int getBgnd() {return background;}
	public WritableImage getWritableImage() {return wImage;}
	public BufferedImage getBufferedImage() {return bImage;}
	
	// Setters
	public void setWidth(int w) {width = w;}
	public void setHeight(int h) {height = h;}
	public void setDistance(int d) {distance = d;}
	public void setBgnd(int b) {background = b;}
}
