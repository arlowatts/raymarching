public class Ray {
	private static final int MAX_STEPS = 100;
	private static final int MAX_LENGTH = 100;
	private static final double MIN_LENGTH = 0.1;
	
	private Vector pos;
	private Vector dir;
	
	private int steps;
	private double length;
	
	public Ray(Vector pos, Vector dir) {
		this.pos = pos;
		this.dir = dir;
		
		dir.setLength(1);
		
		steps = 0;
		length = 0;
	}
	
	public Vector getPos() {return pos;}
	
	public Vector getDir() {return dir;}
	
	public void step(double len) {
		steps++;
		length += len;
		
		pos.add(dir.getX() * len, dir.getY() * len, dir.getZ() * len);
	}
	
	public int march(Object objects[]) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = getNearest(objects);
			
			if (nearest == -1) return -1;
			
			double stepSize = objects[nearest].getDistance(pos);
			
			if (stepSize < MIN_LENGTH) {
				//return nearest;
				Vector normal = objects[nearest].getNormal(pos);
				normal.setLength(0xff);
				
				return Math.abs((int)normal.getX()) << 16 +
					   Math.abs((int)normal.getY()) << 8 +
					   Math.abs((int)normal.getZ());
			}
			
			step(stepSize);
		}
		
		return -1;
	}
	
	public int getNearest(Object objects[]) {
		int index = -1;
		double minDist = MAX_LENGTH;
		
		for (int i = 0; i < objects.length; i++) {
			double distance = objects[i].getDistance(pos);
			
			if (distance < minDist) {
				if (distance < MIN_LENGTH) return i;
				
				index = i;
				minDist = distance;
			}
		}
		
		return index;
	}
}