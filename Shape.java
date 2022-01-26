import java.lang.Math;

public class Shape {
	// Constants
	public static final int IS_ONIONED = 1;
	public static final int SURFACE_NOISE = 2;
	public static final int IS_REPEATING = 4;
	
	// Member variables
	private Vector pos;
	
	private double angleX, angleY;
	
	private int[] bounds;
	private Vector[] boundCorners;
	
	private int color;
	private double shine;
	
	private int modifiers;
	
	private double thickness;
	private double noiseAmplitude;
	private double noiseWavelength;
	private double repeatUnit;
	
	// Constructors
	public Shape(double x, double y, double z, double angleX, double angleY, double shine, int color, int modifiers) {
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
		
		this.modifiers = modifiers;
		
		thickness = 0;
		noiseAmplitude = 0;
		noiseWavelength = 0;
		repeatUnit = 0;
	}
	
	//Methods
	public double distanceTo(Vector v) {
		double distance;
		
		if ((modifiers & IS_REPEATING) == 0) {
			distance = getDistance(v);
		}
		else {
			Vector v1 = new Vector(v);
			v1.subtract(pos);
			
			//v1.setX(v1.getX() > 0 ? (v1.getX() + 0.5 * repeatUnit) % repeatUnit - 0.5 * repeatUnit : );
			//v1.setY((v1.getY() + 0.5 * repeatUnit) % repeatUnit - 0.5 * repeatUnit);
			//v1.setZ((v1.getZ() + 0.5 * repeatUnit) % repeatUnit - 0.5 * repeatUnit);
			
			v1.setX(Math.IEEEremainder((v1.getX() + 0.5 * repeatUnit), Math.copySign(repeatUnit, v1.getX() + 0.5 * repeatUnit)) - 0.5 * repeatUnit);
			v1.setY(((Math.abs(v1.getY() + 0.5 * repeatUnit)) % repeatUnit) - 0.5 * repeatUnit);
			v1.setZ(((Math.abs(v1.getZ() + 0.5 * repeatUnit)) % repeatUnit) - 0.5 * repeatUnit);
			
			v1.add(pos);
			
			distance = getDistance(v1);
		}
		
		if ((modifiers & IS_ONIONED) != 0)
			distance = Math.abs(distance) - thickness;
		
		//if ((modifiers & SURFACE_NOISE) != 0)
		//	distance += noiseAmplitude * Math.sin(noiseWavelength * v.getX()) * Math.sin(noiseWavelength * v.getY()) * Math.sin(noiseWavelength * v.getZ());
		
		return distance;
	}
	
	public double getDistance(Vector v) {return pos.getDistance(v);}
	
	public Vector getNormal(Vector v) {
		double distance = distanceTo(v);
		
		v.add(Main.MIN_LENGTH, 0, 0);
		double xDistance = distanceTo(v);
		
		v.add(-Main.MIN_LENGTH, Main.MIN_LENGTH, 0);
		double yDistance = distanceTo(v);
		
		v.add(0, -Main.MIN_LENGTH, Main.MIN_LENGTH);
		double zDistance = distanceTo(v);
		
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
	
	public double getThickness() {return thickness;}
	
	public double getNoiseAmplitude() {return noiseAmplitude;}
	
	public double getNoiseWavelength() {return noiseWavelength;}
	
	public double getRepeatUnit() {return repeatUnit;}
	
	// Setters
	public void setAngleX(double angleX) {this.angleX = angleX;}
	public void setAngleY(double angleY) {this.angleY = angleY;}
	
	public void setColor(int color) {this.color = color;}
	public void setShine(double shine) {this.shine = shine;}
	
	public void setThickness(double thickness) {this.thickness = thickness;}
	
	public void setNoiseAmplitude(double noiseAmplitude) {this.noiseAmplitude = noiseAmplitude;}
	
	public void setNoiseWavelength(double noiseWavelength) {this.noiseWavelength = noiseWavelength;}
	
	public void setRepeatUnit(double repeatUnit) {this.repeatUnit = repeatUnit;}
}