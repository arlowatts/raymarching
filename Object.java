public class Object {
	// Member variables
	private Vector pos;
	
	private double angleX, angleY;
	
	private double[] bounds;
	
	private int color;
	
	// Constructors
	public Object(double x, double y, double z, double angleX, double angleY, int color) {
		pos = new Vector(x, y, z);
		
		this.angleX = angleX;
		this.angleY = angleY;
		
		bounds = new double[4];
		bounds[0] = 0;
		bounds[1] = 0;
		bounds[2] = 0;
		bounds[3] = 0;
		
		this.color = color;
	}
	
	public Object(Vector pos, double angleX, double angleY, int color) {this(pos.getX(), pos.getY(), pos.getZ(), angleX, angleY, color);}
	
	public Object(double x, double y, double z, int color) {this(x, y, z, 0, 0, color);}
	
	public Object(Vector v, int color) {this(v.getX(), v.getY(), v.getZ(), 0, 0, color);}
	
	//Methods
	public void rotate(double angleX, double angleY) {
		this.angleX += angleX;
		this.angleY += angleY;
	}
	
	public void setBounds(Object camera, Screen screen) {
		bounds[0] = 0;
		bounds[1] = 0;
		bounds[2] = 0;
		bounds[3] = 0;
	}
	
	public double getDistance(Vector v) {
		return pos.getDistance(v);
	}
	
	public Vector getNormal(Vector v) {
		return new Vector(0, 0, 0);
	}
	
	// Getters
	public Vector getPos() {return pos;}
	
	public double getAngleX() {return angleX;}
	public double getAngleY() {return angleY;}
	
	public double[] getBounds() {return bounds;}
	
	public int getColor() {return color;}
	
	public int getColor(double shade) {
		shade = Math.max(0, Math.min(1, Math.abs(shade)));
		
		return ((int)((color >> 16) * shade) << 16) |
			   ((int)(((color >> 8) & 255) * shade) << 8) |
			   (int)((color & 255) * shade);
	}
	
	// Setters
	public void setAngleX(double angleX) {this.angleX = angleX;}
	public void setAngleY(double angleX) {this.angleY = angleY;}
	
	public void setColor(int color) {this.color = color;}
}