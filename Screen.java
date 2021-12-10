import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class Screen {
	private int width, height, distance, background;
	
	private JFrame frame;
	private JLabel label;
	private BufferedImage image;
	private int[] pixels;
	
	private static long marchTime = 0;
	private static long imageWritingTime = 0;
	private static long currentTime = 0;
	
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
	
	public void update(Camera c, Object objects[]) {
		double sincos[] = Vector.getSincos(c.getAngleX(), c.getAngleY());
		
		int centerX = width / 2;
		int centerY = height / 2;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Ray ray = new Ray(new Vector(c.getPos()), new Vector(x - centerX, centerY - y, distance));
				
				ray.getDir().rotate(sincos, 0, 0, 0);
				
				currentTime = System.nanoTime();
				int pixel = ray.march(objects);
				marchTime += System.nanoTime() - currentTime;
				
				if (pixel != -1) pixels[x + y * width] = pixel;//objects[pixel].getColor();
				else pixels[x + y * width] = background;
			}
		}
		
		currentTime = System.nanoTime();
		image.setRGB(0, 0, width, height, pixels, 0, width);
		imageWritingTime += System.nanoTime() - currentTime;
		
		label.updateUI();
		
		long totalTime = marchTime + imageWritingTime;
		
		//System.out.println("\nMarch time:\t\t" + 100 * marchTime / totalTime + 
		//				   "\nImage Writing time:\t" + 100 * imageWritingTime / totalTime);
	}

	/*public void update(Camera c, Object objects[]) {
		double sincos[] = Vector.getSincos(c.getAngleX(), c.getAngleY());

		char output[] = new char[(width * 2 + 3) * (height + 2) + 1];
		int pixels[] = new int[(width * 2) * height];
		int k = 0;
		int m = 0;

		output[k++] = '+';
		for (int i = 0; i < width * 2; i++) output[k++] = '-';
		output[k++] = '+';
		output[k++] = '\n';

		for (int j = -(height / 2); j < height / 2; j++) {
			output[k++] = '|';

			for (double i = -(width / 2); i < width / 2; i += 0.5) {
				Ray ray = new Ray(new Vector(c.getPos()), new Vector(i, -j, distance));

				ray.getDir().rotate(sincos, 0, 0, 0);

				ray.getDir().setLength(1);
				
				//System.out.println(ray);

				int hit = ray.march(objects);

				pixels[m] = hit;

				if (m >= width * 2 && pixels[m - width * 2] != hit) output[k++] = 'X';
				
				else if (m % (width * 2) >= 1 && pixels[m - 1] != hit) output[k++] = 'X';

				else output[k++] = ' ';

				m++;
			}

			output[k++] = '|';
			output[k++] = '\n';
		}

		output[k++] = '+';
		for (int i = 0; i < width * 2; i++) output[k++] = '-';
		output[k++] = '+';

		output[k++] = '\n';
		output[k] = '\0';

		//System.out.println(output);
	}*/
}