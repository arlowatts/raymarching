import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

public class Screen {
	// Member variables
	private int width, height, distance, background;
	
	private JFrame frame;
	private JLabel label;
	private BufferedImage image;
	private int[] pixels;
	
	private static long marchTime = 0;
	private static long imageWritingTime = 0;
	private static long currentTime = 0;
	
	// Constructors
	public Screen(int w, int h, int dist, int bgnd, String title) {
		width = w;
		height = h;
		distance = dist;
		
		background = bgnd;
		
		frame = new JFrame(title);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = new int[width * height];
		
		label = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(label);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// Methods
	public void update(Object camera, ArrayList<Object> objects) {
		int centerX = width / 2;
		int centerY = height / 2;
		
		for (int i = 0; i < objects.size(); i++) objects.get(i).setBounds(camera, this);
		
		ArrayList<Object> nearObjects = new ArrayList<>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				nearObjects.clear();
				
				for (int i = 0; i < objects.size(); i++) {
					final double[] bounds = objects.get(i).getBounds();
					
					if (x >= bounds[0] && y >= bounds[1] && x <= bounds[2] && y <= bounds[3])
						nearObjects.add(objects.get(i));
				}
				
				if (nearObjects.size() == 0) {
					pixels[x + (height - y - 1) * width] = background;
					continue;
				}
				
				Ray ray = new Ray(camera.getPos(), new Vector(x - centerX, y - centerY, distance));
				
				ray.getDir().rotate(camera.getAngleX(), camera.getAngleY(), 0, 0, 0);
				
				int pixel = ray.march(nearObjects);
				
				if (pixel != -1) {
					double shade = nearObjects.get(pixel).getNormal(ray.getPos()).dotProduct(ray.getDir());
					
					pixels[x + (height - y - 1) * width] = nearObjects.get(pixel).getColor(shade);
				}
				else {
					pixels[x + (height - y - 1) * width] = background;
				}
			}
		}
		
		image.setRGB(0, 0, width, height, pixels, 0, width);
		
		label.updateUI();
	}
	
	// Getters
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getDistance() {return distance;}
	
	public int getBackground() {return background;}
	
	// toString
	public String toString() {
		return "Width: " + width +
			   "\nHeight: " + height + 
			   "\nDistance: " + distance + 
			   "\nBackground color: " + background;
	}
}