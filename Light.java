public class Light extends Vector {
	// Member variables
	private int brightness, color;
	
	public Light(double x, double y, double z, int brightness, int color) {
		super(x, y, z);
		this.brightness = brightness;
		this.color = color;
	}
	
	// Getters
	public int getBrightness() {return brightness;}
	public int getColor() {return color;}
	
	public int getColor(double shade) {
		shade = Math.max(0, Math.min(1, Math.abs(shade)));
		
		return ((int)((color >> 16) * shade) << 16) |
			   ((int)(((color >> 8) & 255) * shade) << 8) |
			   (int)((color & 255) * shade);
	}
	
	// Setters
	public void setBrightness(int brightness) {this.brightness = brightness;}
	public void setColor(int color) {this.color = color;}
}