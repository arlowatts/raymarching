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
	public void updateScene(Shape camera, ArrayList<Shape> shapes, ArrayList<Shape> lights) {
		if (initWaiting) init();
		
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
	}
	
	// The recursive method to cast and reflect a light ray
	private int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Shape> lights, Vector shade, int reflections) {
		// Marches a ray
		int hit = ray.march(shapes);
		if (hit == -1 || reflections++ >= maxReflections) return Color.shade(getBgnd(), shade.getX(), shade.getY(), shade.getZ());
		
		// Gets the surface normal of the hit object
		Vector normal = shapes.get(hit).getNormal(ray.getPos());
		double dot = normal.dotProduct(ray.getDir());
		
		// Assumes the object is at least partly reflective and reflects the ray
		ray.setSteps(0);
		ray.getDir().add(-2 * dot * normal.getX(), -2 * dot * normal.getY(), -2 * dot * normal.getZ());
		ray.step(2 * Main.MIN_LENGTH);
		
		Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < lights.size(); i++) {
			// If the ray is already at the light source, add its brightness
			if (shapes.get(hit) == lights.get(i)) {
				int lightColor = lights.get(i).getColor();
				
				brightness.stretch(1 - Color.getR(lightColor) * Color.SCALE,
								   1 - Color.getG(lightColor) * Color.SCALE,
								   1 - Color.getB(lightColor) * Color.SCALE);
				
				continue;
			}
			
			// Marches a new ray to the light
			Vector lightRayDir = new Vector(lights.get(i).getPos());
			lightRayDir.subtract(ray.getPos());
			
			Ray lightRay = new Ray(ray.getPos(), lightRayDir);
			lightRay.step(2 * Main.MIN_LENGTH);
			
			int lightRayHit = lightRay.march(shapes);
			
			// If it hits the light, add brightness proportional to the light's brightness and the dot product of the normal and the ray direction
			if (lightRayHit != -1 && shapes.get(lightRayHit) == lights.get(i)) {
				double lightShade = Math.max(normal.dotProduct(lightRay.getDir()), 0) * Color.SCALE;
				int lightColor = lights.get(i).getColor();
				
				brightness.stretch(1 - Color.getR(lightColor) * lightShade,
								   1 - Color.getG(lightColor) * lightShade,
								   1 - Color.getB(lightColor) * lightShade);
			}
		}
		
		// Multiply the shade of the pixel by the total brightness and add ambient light
		shade.stretch(Math.min(1 - brightness.getX() + Color.getR(getBgnd()) * Color.SCALE, 1),
					  Math.min(1 - brightness.getY() + Color.getG(getBgnd()) * Color.SCALE, 1),
					  Math.min(1 - brightness.getZ() + Color.getB(getBgnd()) * Color.SCALE, 1));
		
		double shine = shapes.get(hit).getShine();
		
		int reflectionColor = 0;
		
		// Casts the reflected ray
		if (shine > 0) {
			shade.multiply(shine);
			reflectionColor = castRay(ray, shapes, lights, shade, reflections);
			shade.multiply(1 / shine - 1);
		}
		
		return Color.shade(shapes.get(hit).getColor(), shade.getX(), shade.getY(), shade.getZ()) + reflectionColor;
	}
	
	// The 'starter' method for the recursive castRay method
	public int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Shape> lights) {
		return castRay(ray, shapes, lights, new Vector(1, 1, 1), 0);
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