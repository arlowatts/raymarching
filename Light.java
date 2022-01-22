public class Light extends Sphere {
	// Member variables
	private double brightness;
	
	// Constructors
	public Light(double x, double y, double z, double r, double brightness, int color) {
		super(x, y, z, r, color, 0);
		this.brightness = Math.min(Math.max(brightness, 0), 1);
	}
	
	// Getters
	public double getBrightness() {return brightness;}
	
	// Setters
	public void setBrightness(double brightness) {this.brightness = brightness;}
}