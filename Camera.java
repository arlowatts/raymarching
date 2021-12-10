public class Camera {
	private Vector pos;
	private double angleX, angleY;
	
	public Camera(double x, double y, double z) {
		pos = new Vector(x, y, z);
		angleX = angleY = 0;
	}
	
	public Camera(Vector v) {this(v.getX(), v.getY(), v.getZ());}
	
	public Vector getPos() {return pos;}
	
	public double getAngleX() {return angleX;}
	
	public double getAngleY() {return angleY;}
	
	public void setAngleX(double angleX) {this.angleX = angleX;}
	
	public void setAngleY(double angleY) {this.angleY = angleY;}
}