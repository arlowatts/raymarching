public abstract class Object {
	private Vector pos;
	private int color;
	
	public Object(double x, double y, double z, int color) {
		pos = new Vector(x, y, z);
		this.color = color;
	}
	
	public Object(Vector v, int color) {this(v.getX(), v.getY(), v.getZ(), color);}
	
	public Vector getPos() {return pos;}
	
	public int getColor() {return color;}
	
	public abstract double getDistance(Vector v);
	
	public abstract Vector getNormal(Vector v);
}