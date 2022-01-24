import java.lang.Math;

public class Shape {
	// Member variables
	private Vector pos;
	
	private double angleX, angleY;
	
	private int[] bounds;
	private Vector[] boundCorners;
	
	private int color;
	private double shine;
	
	// Constructors
	public Shape(double x, double y, double z, double angleX, double angleY, double shine, int color) {
		pos = new Vector(x, y, z);
		
		this.angleX = angleX;
		this.angleY = angleY;
		
		bounds = new int[4];
		bounds[0] = 0;
		bounds[1] = 0;
		bounds[2] = 0;
		bounds[3] = 0;
		
		boundCorners = new Vector[8];
		for (int i = 0; i < 8; i++) boundCorners[i] = new Vector();
		
		this.color = color & 0xffffff;
		
		this.shine = Math.min(Math.max(shine, 0), 1);
	}
	
	//Methods
	public double getDistance(Vector v) {return pos.getDistance(v);}
	
	public Vector getNormal(Vector v) {
		double distance = getDistance(v);
		
		v.add(Main.MIN_LENGTH, 0, 0);
		double xDistance = getDistance(v);
		
		v.add(-Main.MIN_LENGTH, Main.MIN_LENGTH, 0);
		double yDistance = getDistance(v);
		
		v.add(0, -Main.MIN_LENGTH, Main.MIN_LENGTH);
		double zDistance = getDistance(v);
		
		v.add(0, 0, -Main.MIN_LENGTH);
		
		Vector normal = new Vector(xDistance - distance, yDistance - distance, zDistance - distance);
		normal.setLength(1);
		
		return normal;
	}
	
	public void setBounds(Shape camera, Screen screen) {
		bounds[0] = screen.getWidth();
		bounds[1] = screen.getHeight();
		bounds[2] = -screen.getWidth();
		bounds[3] = -screen.getHeight();
		
		int numInvalid = 0;
		
		for (int i = 0; i < 8; i++) {
			Vector newPos = new Vector(boundCorners[i]);
			
			newPos.rotate(getAngleX(), getAngleY());
			newPos.add(getPos());
			
			newPos.subtract(camera.getPos());
			newPos.inverseRotate(camera.getAngleX(), camera.getAngleY());
			
			if (newPos.getZ() < 1) numInvalid++;
			else {
				double ratio = screen.getDistance() / newPos.getZ();
				
				int screenX = (int)(newPos.getX() * ratio + screen.getWidth() / 2);
				int screenY = (int)(newPos.getY() * ratio + screen.getHeight() / 2);
				
				bounds[0] = Math.min(bounds[0], screenX);
				bounds[1] = Math.min(bounds[1], screenY);
				bounds[2] = Math.max(bounds[2], screenX);
				bounds[3] = Math.max(bounds[3], screenY);
			}
		}
		
		if (numInvalid == 8) {
			bounds[0] = 0;
			bounds[1] = 0;
			bounds[2] = 0;
			bounds[3] = 0;
		}
	}
	
	public void updateBoundCorners() {
		for (int i = 0; i < 8; i++) {
			boundCorners[i].set(0, 0, 0);
		}
	}
	
	public void rotate(double angleX, double angleY) {
		this.angleX += angleX;
		this.angleY += angleY;
	}
	
	// Getters
	public Vector getPos() {return pos;}
	
	public double getAngleX() {return angleX;}
	public double getAngleY() {return angleY;}
	
	public int[] getBounds() {return bounds;}
	public Vector[] getBoundCorners() {return boundCorners;}
	
	public int getColor() {return color;}
	public double getShine() {return shine;}
	
	// Setters
	public void setAngleX(double angleX) {this.angleX = angleX;}
	public void setAngleY(double angleY) {this.angleY = angleY;}
	
	public void setColor(int color) {this.color = color;}
	public void setShine(double shine) {this.shine = shine;}
}