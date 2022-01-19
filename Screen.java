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
	private int width, height, distance, background, maxReflections;
	
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
	public Screen(int w, int h, int dist, int bgnd, int maxReflections, String title) {
		super(title);
		
		setSize(w, h);
		distance = dist;
		
		background = bgnd;
		
		this.maxReflections = maxReflections;
		
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
	
	// Methods
	public void updateScene(Shape camera, ArrayList<Shape> shapes) {
		frameStart = System.nanoTime();
		int k = 0;
		
		// Updating the bounding boxes for all the shapes in the scene
		//for (int i = 0; i < shapes.size(); i++) shapes.get(i).setBounds(camera, this);
		
		//ArrayList<Shape> validShapes = new ArrayList<>();
		
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				// Clearing the previous pixel's valid Shapes to reuse the ArrayList
				//validShapes.clear();
				/*
				startTime = System.nanoTime();
				for (int i = 0; i < shapes.size(); i++) {
					int[] bounds = shapes.get(i).getBounds();
					
					// If the object's bounding box includes the current pixel, add it to the list
					if (x >= bounds[0] && y >= bounds[1] && x <= bounds[2] && y <= bounds[3])
						validShapes.add(shapes.get(i));
				}
				checkBoundsTime += System.nanoTime() - startTime;
				
				// If there are no shapes, don't create a ray
				if (validShapes.size() == 0) {
					pixels[k++] = background;
					continue;
				}
				//*/
				// Create a ray starting at the camera and passing through the current pixel
				Ray ray = new Ray(camera.getPos(), new Vector(x - width / 2, y - height / 2, distance));
				
				// Rotate it by the camera's rotation
				ray.getDir().rotate(camera.getAngleX(), camera.getAngleY());
				
				pixels[k++] = castRay(ray, shapes);
				
				/*
				startTime = System.nanoTime();
				// March the ray
				int pixel = ray.march(validShapes);
				marchTime += System.nanoTime() - startTime;
				
				// If it hits something, calculate the surface normal at that point and shade the object's color by the dot product of the ray's angle and the normal
				if (pixel != -1) {
					Shape shape = validShapes.get(pixel);
					
					Vector rayDir = ray.getDir();
					
					startTime = System.nanoTime();
					Vector normal = shape.getNormal(ray.getPos());
					
					double dot = normal.dotProduct(rayDir);
					normalTime += System.nanoTime() - startTime;
					
					ray.setSteps(0);
					ray.setLength(0);
					rayDir.set(rayDir.getX() - 2 * dot * normal.getX(), rayDir.getY() - 2 * dot * normal.getY(), rayDir.getZ() - 2 * dot * normal.getZ());
					
					ray.step(2 * Main.MIN_LENGTH);
					pixel = ray.march(shapes);
					
					int secondColor = getBgnd(dot * shape.getShine());
					
					if (pixel != -1) {
						secondColor = shapes.get(pixel).getColor(dot * shape.getShine());
					}
					
					pixels[k++] = shape.getColor(dot * (1 - shape.getShine())) + secondColor;
				}
				else {
					pixels[k++] = background;
				}
				//*/
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
		
		if (initWaiting) init();
	}
	
	public int castRay(Ray ray, ArrayList<Shape> shapes) {
		return castRay(ray, shapes, 1, 0);
	}
	
	private int castRay(Ray ray, ArrayList<Shape> shapes, double shade, int reflections) {
		int hit = ray.march(shapes);
		if (hit == -1 || reflections++ >= maxReflections) return getBgnd(shade);
		
		Shape shape = shapes.get(hit);
		Vector rayDir = ray.getDir();
		
		Vector normal = shape.getNormal(ray.getPos());
		double dot = normal.dotProduct(rayDir);
		
		ray.setSteps(0);
		rayDir.set(rayDir.getX() - 2 * dot * normal.getX(), rayDir.getY() - 2 * dot * normal.getY(), rayDir.getZ() - 2 * dot * normal.getZ());
		ray.step(2 * Main.MIN_LENGTH);
		
		int reflectionColor = castRay(ray, shapes, shade * dot * shape.getShine(), reflections);
		
		return shape.getColor(shade * dot * (1 - shape.getShine())) + reflectionColor;
	}
	
	// Getters
	public int getDistance() {return distance;}
	
	public int getBgnd() {return background;}
	
	public int getBgnd(double shade) {
		shade = Math.max(0, Math.min(1, Math.abs(shade)));
		
		return ((int)((background >> 16) * shade) << 16) |
			   ((int)(((background >> 8) & 255) * shade) << 8) |
			   (int)((background & 255) * shade);
	}
	
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