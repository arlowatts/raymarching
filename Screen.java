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
	public void updateScene(Shape camera, ArrayList<Shape> shapes, ArrayList<Light> lights) {
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
	
	// The 'starter' method for the recursive castRay method
	public int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Light> lights) {
		return castRay(ray, shapes, lights, 1, 1, 1, 0);
	}
	
	// The recursive method to cast and reflect a light ray
	private int castRay(Ray ray, ArrayList<Shape> shapes, ArrayList<Light> lights, double rShade, double gShade, double bShade, int reflections) {
		// Marches a ray
		int hit = ray.march(shapes);
		if (hit == -1 || reflections++ >= maxReflections) return getBgnd(rShade * ambientLight, gShade * ambientLight, bShade * ambientLight);
		
		Shape shape = shapes.get(hit);
		Vector rayDir = ray.getDir();
		
		// Gets the surface normal of the hit object
		Vector normal = shape.getNormal(ray.getPos());
		double dot = normal.dotProduct(rayDir);
		
		// Assumes the object is at least partly reflective and reflects the ray
		ray.setSteps(0);
		rayDir.add(-2 * dot * normal.getX(), -2 * dot * normal.getY(), -2 * dot * normal.getZ());
		ray.step(2 * Main.MIN_LENGTH);
		
		double rBrightness = 1;
		double gBrightness = 1;
		double bBrightness = 1;
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < lights.size(); i++) {
			double colorFactor = 1.0 / 255.0;
			
			// If the ray is already at the light source, add its brightness
			if (shape == lights.get(i)) {
				rBrightness *= 1 - (lights.get(i).getColor(lights.get(i).getBrightness(), 0, 0) >> 16) * colorFactor;
				gBrightness *= 1 - ((lights.get(i).getColor(0, lights.get(i).getBrightness(), 0) >> 8) & 255) * colorFactor;
				bBrightness *= 1 - (lights.get(i).getColor(0, 0, lights.get(i).getBrightness()) & 255) * colorFactor;
				
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
				colorFactor *= Math.max(normal.dotProduct(lightRay.getDir()), 0);
				
				rBrightness *= 1 - (lights.get(i).getColor(lights.get(i).getBrightness(), 0, 0) >> 16) * colorFactor;
				gBrightness *= 1 - ((lights.get(i).getColor(0, lights.get(i).getBrightness(), 0) >> 8) & 255) * colorFactor;
				bBrightness *= 1 - (lights.get(i).getColor(0, 0, lights.get(i).getBrightness()) & 255) * colorFactor;
			}
		}
		
		// Multiply the brightness of the pixel by the total brightness and add ambient light
		rShade *= Math.min(1 - rBrightness + ambientLight, 1);
		gShade *= Math.min(1 - gBrightness + ambientLight, 1);
		bShade *= Math.min(1 - bBrightness + ambientLight, 1);
		
		// Casts the reflected ray - this is recursive
		int reflectionColor = castRay(ray, shapes, lights, rShade * shape.getShine(), gShade * shape.getShine(), bShade * shape.getShine(), reflections);
		
		return shape.getColor(rShade * (1 - shape.getShine()), gShade * (1 - shape.getShine()), bShade * (1 - shape.getShine())) + reflectionColor;
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
	
	public int getBgnd(double shadeR, double shadeG, double shadeB) {
		return ((int)((background >> 16) * Math.max(0, Math.min(1, Math.abs(shadeR)))) << 16) |
			   ((int)(((background >> 8) & 255) * Math.max(0, Math.min(1, Math.abs(shadeG)))) << 8) |
			   (int)((background & 255) * Math.max(0, Math.min(1, Math.abs(shadeB))));
	}
	
	public double getAmbientLight() {return ambientLight;}
	
	public BufferedImage getImage() {return image;}
	
	// Setters
	public void setDistance(int dist) {distance = dist;}
	
	public void setBgnd(int bgnd) {background = bgnd;}
	public void setAmbientLight(double ambientLight) {this.ambientLight = ambientLight;}
	
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