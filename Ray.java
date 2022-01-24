import java.util.ArrayList;

public class Ray {
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
	// The recursive method to cast and reflect a light ray
	public int cast(Screen screen, ArrayList<Shape> shapes, ArrayList<Shape> lights, Vector shade, int reflections) {
		int hit = march(shapes);
		if (hit == -1 || reflections-- == 0) return Color.shade(screen.getBgnd(), shade.getX(), shade.getY(), shade.getZ());
		
		// Gets the surface normal of the hit object
		Vector normal = shapes.get(hit).getNormal(pos);
		double dot = normal.dotProduct(dir);
		
		// Assumes the object is at least partly reflective and reflects the ray
		setSteps(0);
		getDir().add(-2 * dot * normal.getX(), -2 * dot * normal.getY(), -2 * dot * normal.getZ());
		step(2 * Main.MIN_LENGTH);
		
		Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < lights.size(); i++) {
			// If the ray is already at the light source, add its brightness
			if (shapes.get(hit) == lights.get(i)) {
				int lightColor = lights.get(i).getColor();
				
				brightness.stretch(1 - Color.getR(lightColor) * Color.SCALE,
								   1 - Color.getG(lightColor) * Color.SCALE,
								   1 - Color.getB(lightColor) * Color.SCALE);
				
				continue;
			}
			
			// Marches a new ray to the light
			Vector lightRayDir = new Vector(lights.get(i).getPos());
			lightRayDir.subtract(pos);
			
			Ray lightRay = new Ray(pos, lightRayDir);
			lightRay.step(2 * Main.MIN_LENGTH);
			
			int lightRayHit = lightRay.march(shapes);
			
			// If it hits the light, add brightness proportional to the light's brightness and the dot product of the normal and the ray direction
			if (lightRayHit != -1 && shapes.get(lightRayHit) == lights.get(i)) {
				double lightShade = Math.max(normal.dotProduct(lightRay.getDir()), 0) * Color.SCALE;
				int lightColor = lights.get(i).getColor();
				
				brightness.stretch(1 - Color.getR(lightColor) * lightShade,
								   1 - Color.getG(lightColor) * lightShade,
								   1 - Color.getB(lightColor) * lightShade);
			}
		}
		
		// Multiply the shade of the pixel by the total brightness and add ambient light
		shade.stretch(Math.min(1 - brightness.getX() + Color.getR(screen.getBgnd()) * Color.SCALE, 1),
					  Math.min(1 - brightness.getY() + Color.getG(screen.getBgnd()) * Color.SCALE, 1),
					  Math.min(1 - brightness.getZ() + Color.getB(screen.getBgnd()) * Color.SCALE, 1));
		
		double shine = shapes.get(hit).getShine();
		
		int reflectionColor = 0;
		
		// Casts the reflected ray
		if (shine > 0) {
			shade.multiply(shine);
			reflectionColor = cast(screen, shapes, lights, shade, reflections);
			shade.multiply(1 / shine - 1);
		}
		
		return Color.shade(shapes.get(hit).getColor(), shade.getX(), shade.getY(), shade.getZ()) + reflectionColor;
	}
	
	public int march(ArrayList<Shape> shapes) {
		steps = 0;
		length = 0;
		
		while (steps < Main.MAX_STEPS && length < Main.MAX_LENGTH) {
			int nearest = getNearest(shapes);
			
			if (nearest == -1) return -1;
			
			double stepSize = shapes.get(nearest).getDistance(pos);
			
			step(stepSize);
			
			if (stepSize < Main.MIN_LENGTH) return nearest;
		}
		
		return -1;
	}
	
	public int getNearest(ArrayList<Shape> shapes) {
		int index = -1;
		double minDist = Main.MAX_LENGTH;
		
		for (int i = 0; i < shapes.size(); i++) {
			double distance = shapes.get(i).getDistance(pos);
			
			if (distance < minDist) {
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
	
	public int getSteps() {return steps;}
	public double getLength() {return length;}
	
	// Setters
	public void setPos(Vector v) {pos = v;}
	public void setDir(Vector v) {dir = v; dir.setLength(1);}
	
	public void setSteps(int n) {steps = n;}
	public void setLength(double l) {length = l;}
	
	// toString
	public String toString() {
		return "Position: " + pos.toString() + "\nDirection: " + dir.toString();
	}
}