public class Light extends Vector {
	// Member variables
	private double brightness;
	private int color;
	
	private Shape linkedShape;
	
	public Light(double x, double y, double z, double brightness, int color, Shape linkedShape) {
		super(x, y, z);
		this.brightness = brightness;
		this.color = color;
		
		this.linkedShape = linkedShape;
	}
	
	public void linkTo(Shape shape) {linkedShape = shape;}
	
	public void unlink() {linkedShape = null;}
	
	// Getters
	public double getBrightness() {return brightness;}
	public int getColor() {return color;}
	
	public int getColor(double shade) {
		shade = Math.max(0, Math.min(1, Math.abs(shade)));
		
		return ((int)((color >> 16) * shade) << 16) |
			   ((int)(((color >> 8) & 255) * shade) << 8) |
			   (int)((color & 255) * shade);
	}
	
	public Shape getLinkedShape() {return linkedShape;}
	
	// Setters
	public void setBrightness(double brightness) {this.brightness = brightness;}
	public void setColor(int color) {this.color = color;}
}