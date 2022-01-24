import java.lang.Math;

public class Torus extends Shape{
	// Member variables
	private double largeRadius, smallRadius;
	
	// Constructors
	public Torus(double R, double r, double x, double y, double z, double angleX, double angleY, double shine, int color) {
		super(x, y, z, angleX, angleY, shine, color);
		largeRadius = Math.max(R, Main.MIN_LENGTH);
		smallRadius = Math.max(r, Main.MIN_LENGTH);
		
		updateBoundCorners();
	}
	
	// Methods
	public double getDistance(Vector v) {
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		v1.inverseRotate(getAngleX(), getAngleY());
		
		double a = Math.sqrt(v1.getX()*v1.getX() + v1.getZ()*v1.getZ()) - largeRadius;
		
		return Math.sqrt(a*a + v1.getY()*v1.getY()) - smallRadius;
	}
	
	public void updateBoundCorners() {
		Vector[] boundCorners = getBoundCorners();
		
		for (int i = 0; i < 8; i++) {
			boundCorners[i].set((largeRadius + smallRadius) * (((i << 1) & 2) - 1), smallRadius * ((i & 2) - 1), (largeRadius + smallRadius) * (((i >> 1) & 2) - 1));
		}
	}
	
	// Getters
	public double getLargeRadius() {return largeRadius;}
	public double getSmallRadius() {return smallRadius;}
	
	// Setters
	public void setLargeRadius(double r) {largeRadius = r;}
	public void setSmallRadius(double r) {smallRadius = r;}
}