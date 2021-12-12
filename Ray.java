import java.util.ArrayList;

public class Ray {
	// Constants
	private static final int MAX_STEPS = 100;
	private static final int MAX_LENGTH = 100;
	private static final double MIN_LENGTH = 0.01;
	
	// Member variables
	private Vector pos;
	private Vector dir;
	
	private int steps;
	private double length;
	
	// Constructors
	public Ray(Vector pos, Vector dir) {
		this.pos = new Vector(pos);
		this.dir = new Vector(dir);
		
		this.dir.setLength(1);
		
		steps = 0;
		length = 0;
	}
	
	//Methods
	public int march(ArrayList<Object> objects) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = getNearest(objects);
			
			if (nearest == -1) return -1;
			
			double stepSize = objects.get(nearest).getDistance(pos);
			
			if (stepSize < MIN_LENGTH) {
				return nearest;
				//Vector normal = objects.get(nearest).getNormal(pos);
				//normal.setLength(0xff);
				
				//return (Math.abs((int)normal.getX()) & 0xff) << 16 +
				//	   (Math.abs((int)normal.getY()) & 0xff) << 8 +
				//	   (Math.abs((int)normal.getZ()) & 0xff);
			}
			
			step(stepSize);
		}
		
		return -1;
	}
	
	public int getNearest(ArrayList<Object> objects) {
		int index = -1;
		double minDist = MAX_LENGTH;
		
		for (int i = 0; i < objects.size(); i++) {
			double distance = objects.get(i).getDistance(pos);
			
			if (distance < minDist) {
				if (distance < MIN_LENGTH) return i;
				
				index = i;
				minDist = distance;
			}
		}
		
		return index;
	}
	
	public void step(double len) {
		steps++;
		length += len;
		
		pos.add(dir.getX() * len, dir.getY() * len, dir.getZ() * len);
	}
	
	// Getters
	public Vector getPos() {return pos;}
	
	public Vector getDir() {return dir;}
	
	// toString
	public String toString() {
		return "Position: " + pos.toString() + "\nDirection: " + dir.toString();
	}
}