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
	private double ambientLight;
	
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
	public Screen(int w, int h, int dist, int bgnd, double ambientLight, int maxReflections, String title) {
		super(title);
		
		setSize(w, h);
		distance = dist;
		
		background = bgnd;
		this.ambientLight = ambientLight;
		
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
	public void updateScene(Shape camera, ArrayList<Shape> shapes, ArrayList<Light> lights) {
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
				
				pixels[k++] = castRay(ray, shapes, lights);
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
	
	public int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Light> lights) {
		return castRay(ray, shapes, lights, 1, 0);
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
	
	public double getAmbientLight() {return ambientLight;}
	
	public BufferedImage getImage() {return image;}
	
	// Setters
	public void setDistance(int dist) {distance = dist;}
	
	public void setBgnd(int bgnd) {background = bgnd;}
	public void setAmbientLight(double ambientLight) {this.ambientLight = ambientLight;}
	
	// Helpers
	private int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Light> lights, double shade, int reflections) {
		int hit = ray.march(shapes);
		if (hit == -1 || reflections++ >= maxReflections) return getBgnd(shade * ambientLight);
		
		Shape shape = shapes.get(hit);
		Vector rayDir = ray.getDir();
		
		Vector normal = shape.getNormal(ray.getPos());
		double dot = normal.dotProduct(rayDir);
		
		ray.setSteps(0);
		rayDir.set(rayDir.getX() - 2 * dot * normal.getX(), rayDir.getY() - 2 * dot * normal.getY(), rayDir.getZ() - 2 * dot * normal.getZ());
		ray.step(2 * Main.MIN_LENGTH);
		
		double totalBrightness = 1;
		
		for (int i = 0; i < lights.size(); i++) {
			Vector lightRayDir = new Vector(lights.get(i));
			lightRayDir.subtract(ray.getPos());
			
			Ray lightRay = new Ray(ray.getPos(), lightRayDir);
			
			lightRay.step(2 * Main.MIN_LENGTH);
			int lightRayHit = shape == lights.get(i).getLinkedShape() ? hit : lightRay.march(shapes);
			
			if (lightRayHit != -1 && shapes.get(lightRayHit) == lights.get(i).getLinkedShape())
				totalBrightness *= (1 - lights.get(i).getBrightness() * Math.max(normal.dotProduct(lightRay.getDir()), 0));
		}
		
		shade *= Math.min(1 + ambientLight - totalBrightness, 1);
		
		int reflectionColor = castRay(ray, shapes, lights, shade * shape.getShine(), reflections);
		
		return shape.getColor(shade * (1 - shape.getShine())) + reflectionColor;
	}
	
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