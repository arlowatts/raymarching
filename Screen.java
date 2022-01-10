import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.ArrayList;

public class Screen extends JFrame {
	// Member variables
	private int width, height, distance, background;
	
	private boolean initWaiting;
	
	private JLabel label;
	private BufferedImage image;
	private int[] pixels;
	
	private static long startTime;
	private static long programStart = System.nanoTime();
	private static long checkBoundsTime = 0;
	private static long marchTime = 0;
	private static long normalTime = 0;
	private static long timeTime = 0;
	private static long frameStart;
	private static int frames = 0;
	
	// Constructors
	public Screen(int w, int h, int dist, int bgnd, String title) {
		super(title);
		
		setSize(w, h);
		distance = dist;
		
		background = bgnd;
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Screen c = (Screen)e.getSource();
				
				c.setInitWaiting(true);
			}
		});
		
		// Initializing the JLabel and BufferedImage
		init();
		
		// Initializing the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void init() {
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
	
	// Methods
	public void updateScene(Shape camera, ArrayList<Shape> objects) {
		frameStart = System.nanoTime();
		int k = 0;
		
		// Updating the bounding boxes for all the shapes in the scene
		for (int i = 0; i < objects.size(); i++) objects.get(i).setBounds(camera, this);
		
		ArrayList<Shape> validShapes = new ArrayList<>();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Clearing the previous pixel's valid Shapes to reuse the ArrayList
				validShapes.clear();
				
				startTime = System.nanoTime();
				for (int i = 0; i < objects.size(); i++) {
					double[] bounds = objects.get(i).getBounds();
					
					// If the object's bounding box includes the current pixel, add it to the list
					if (x > bounds[0] && y > bounds[1] && x < bounds[2] && y < bounds[3])
						validShapes.add(objects.get(i));
				}
				checkBoundsTime += System.nanoTime() - startTime;
				
				// If there are no objects, don't create a ray
				if (validShapes.size() == 0) {
					pixels[k++] = background;
					continue;
				}
				
				// Create a ray starting at the camera and passing through the current pixel
				Ray ray = new Ray(camera.getPos(), new Vector(x - width / 2, y - height / 2, distance));
				
				// Rotate it by the camera's rotation
				ray.getDir().rotate(camera.getAngleX(), camera.getAngleY(), 0, 0, 0);
				
				startTime = System.nanoTime();
				// March the ray
				int pixel = ray.march(validShapes);
				marchTime += System.nanoTime() - startTime;
				
				// If it hits something, calculate the surface normal at that point and shade the object's color by the dot product of the ray's angle and the normal
				if (pixel != -1) {
					startTime = System.nanoTime();
					double shade = validShapes.get(pixel).getNormal(ray.getPos()).dotProduct(ray.getDir());
					normalTime += System.nanoTime() - startTime;
					
					pixels[k++] = validShapes.get(pixel).getColor(shade);
				}
				else {
					pixels[k++] = background;
				}
			}
		}
		
		// Updating the screen
		image.setRGB(0, 0, width, height, pixels, 0, width);
		label.updateUI();
		
		startTime = System.nanoTime() - programStart;
		timeTime += System.nanoTime() - frameStart;
		/*
		System.out.println("\nCheck bounds:   " + (100 * checkBoundsTime) / startTime +
						   "\nMarch:          " + (100 * marchTime) / startTime +
						   "\nNormals:        " + (100 * normalTime) / startTime +
						   "\nTime:           " + timeTime / (100000 * ++frames));
		//*/
		
		if (initWaiting) {
			init();
			initWaiting = false;
		}
	}
	
	// Getters
	public int getDistance() {return distance;}
	
	public int getBgnd() {return background;}
	
	// Setters
	public void setDistance(int dist) {distance = dist;}
	
	public void setBgnd(int bgnd) {background = bgnd;}
	
	public void setInitWaiting(boolean init) {initWaiting = init;}
}