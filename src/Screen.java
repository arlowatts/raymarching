package src;

import src.shapes.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.ArrayList;

/*
 * A Screen object extends the JFrame class and displays the rendered image each frame.
 */
public class Screen extends JFrame {
	// Member variables
	private int width, height, distance, background, maxReflections;
	
	private boolean initWaiting;
	
	private JLabel label;
	private BufferedImage image;
	private int[] pixels;
	
	// Constructors
	public Screen(String title, int[] args) {
		super(title);
		
		setSize(args[0], args[1]);
		distance = args[2];
		
		background = args[3];
		
		maxReflections = args[4];
		
		// Adding a listener to know when the user resizes the window
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Screen c = (Screen)e.getSource();
				
				c.setInitWaiting(true);
			}
		});
		
		// Initializing the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// Methods
	public void updateImage(Scene scene) {
		if (initWaiting) init();
		
		int k = 0;
		
		// Iterating through every pixel to cast a ray through it
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				// Create a ray starting at the camera and passing through the current pixel
				Ray ray = new Ray(scene.getCamera().getPos(), new Vector(x - width / 2, y - height / 2, distance));
				
				// Rotate it by the camera's rotation
				ray.getDir().rotate(scene.getCamera().getAngle());
				
				pixels[k++] = castRay(ray, scene);
			}
		}
		
		// Updating the screen
		image.setRGB(0, 0, width, height, pixels, 0, width);
		label.updateUI();
	}
	
	private int castRay(Ray ray, Scene scene) {
		return ray.cast(scene, new Vector(1, 1, 1), null, maxReflections);
	}
	
	// Getters
	public int getDistance() {return distance;}
	
	public int getBgnd() {return background;}
	
	public BufferedImage getImage() {return image;}
	
	// Setters
	public void setDistance(int dist) {distance = dist;}
	
	public void setBgnd(int bgnd) {background = bgnd;}
	
	// Helpers
	private void init() {
		initWaiting = false;
		
		width = getWidth();
		height = getHeight();
		
		// Creating the Buffered Image and a pixels array to update individual pixels faster
		image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = new int[getWidth() * getHeight()];
		
		// Creating the label
		label = new JLabel(new ImageIcon(image));
		getContentPane().removeAll();
		getContentPane().add(label);
	}
	
	private void setInitWaiting(boolean init) {initWaiting = init;}
}