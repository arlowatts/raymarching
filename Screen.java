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
				
				//pixels[x + (height - y - 1) * width] = background;
				
				for (int i = 0; i < objects.size(); i++) {
					final double[] bounds = objects.get(i).getBounds();
					
					if (bounds[0] != -1 && x - centerX >= bounds[0] && y - centerY >= bounds[1] && x - centerX <= bounds[2] && y - centerY <= bounds[3])
						//pixels[x + (height - y - 1) * width] = objects.get(i).getColor();
						nearObjects.add(objects.get(i));
				}
				
				if (nearObjects.size() == 0) {
					pixels[x + (height - y - 1) * width] = background;
					continue;
				}
				
				Ray ray = new Ray(camera.getPos(), new Vector(x - centerX, y - centerY, distance));
				
				ray.getDir().rotate(camera.getAngleX(), camera.getAngleY(), 0, 0, 0);
				
				//currentTime = System.nanoTime();
				int pixel = ray.march(nearObjects);
				//marchTime += System.nanoTime() - currentTime;
				
				if (pixel != -1) pixels[x + (height - y - 1) * width] = nearObjects.get(pixel).getColor();
				else pixels[x + (height - y - 1) * width] = background;
			}
		}
		
		//currentTime = System.nanoTime();
		image.setRGB(0, 0, width, height, pixels, 0, width);
		//imageWritingTime += System.nanoTime() - currentTime;
		
		label.updateUI();
		
		//long totalTime = marchTime + imageWritingTime;
		
		//System.out.println("\nMarch time:\t\t" + 100 * marchTime / totalTime + 
		//				   "\nImage Writing time:\t" + 100 * imageWritingTime / totalTime);
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