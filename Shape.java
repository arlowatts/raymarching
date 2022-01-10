public class Shape {
	// Constants
	private static final double NORMAL_PRECISION = 0.001;
	
	// Member variables
	private Vector pos;
	
	private double angleX, angleY;
	
	private double[] bounds;
	
	private int color;
	
	// Constructors
	public Shape(double x, double y, double z, double angleX, double angleY, int color) {
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
	
	public Shape(Vector pos, double angleX, double angleY, int color) {this(pos.getX(), pos.getY(), pos.getZ(), angleX, angleY, color);}
	
	//Methods
	public void rotate(double angleX, double angleY) {
		this.angleX += angleX;
		this.angleY += angleY;
	}
	
	public void setBounds(Shape camera, Screen screen) {}
	
	public double getDistance(Vector v) {return pos.getDistance(v);}
	
	public Vector getNormal(Vector v) {
		Vector v1 = new Vector(v);
		
		double distance = getDistance(v1);
		
		v1.add(NORMAL_PRECISION, 0, 0);
		double xDistance = getDistance(v1);
		
		v1.add(-NORMAL_PRECISION, NORMAL_PRECISION, 0);
		double yDistance = getDistance(v1);
		
		v1.add(0, -NORMAL_PRECISION, NORMAL_PRECISION);
		double zDistance = getDistance(v1);
		
		Vector normal = new Vector(xDistance - distance, yDistance - distance, zDistance - distance);
		normal.setLength(1);
		
		return normal;
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