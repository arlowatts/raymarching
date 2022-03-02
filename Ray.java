import shapes.*;

import java.util.ArrayList;

public class Ray {
	// Constants
	public static final int MAX_STEPS = 1000;
	public static final int MAX_LENGTH = 100;
	public static final double MIN_LENGTH = 0.001;
	
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
	
	public Ray(Ray ray) {
		this(ray.pos, ray.dir);
		
		steps = ray.steps;
		length = ray.length;
	}
	
	// Methods
	// The recursive method to cast and reflect a light ray
	public int cast(Scene scene, Vector shade, int medium, int reflections) {
		int hit = march(scene.getShapes(), medium);
		
		if (hit == -1) return Color.shade(scene.getScreen().getBgnd(), shade.getX(), shade.getY(), shade.getZ());
		
		Shape hitShape = scene.getShapes().get(hit);
		Shape mediumShape = medium == -1 ? null : scene.getShapes().get(medium);
		
		Vector normal = hitShape.getNormal(pos);
		
		double transparency = hitShape.getTransparency();
		int refractionColor = 0;
		
		if (transparency > 0 && reflections > 0) {
			double n1 = medium == -1 ? 1 : scene.getShapes().get(medium).getRefrIndex();
			double n2 = medium == hit ? 1 : scene.getShapes().get(hit).getRefrIndex();
			
			Ray refractedRay = new Ray(this);
			refractedRay.refract(normal, n1, n2);
			
			//if (medium == hit) System.out.println(hitShape.getDistance(pos) + " " + hitShape.getDistance(refractedRay.pos) + " " + medium + " " + hit);
			
			shade.multiply(transparency);
			refractionColor = refractedRay.cast(scene, shade, hit == medium ? -1 : hit, reflections - 1);
			shade.multiply(1 / transparency - 1);
		}
		
		// Reflects the ray
		reflect(normal);
		step(2 * MIN_LENGTH);
		
		Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < scene.getLights().size(); i++) {
			// If the ray is already at the light source, add its brightness
			if (scene.getShapes().get(hit) == scene.getLights().get(i)) {
				int lightColor = scene.getLights().get(i).getColor();
				
				brightness.stretch(1 - Color.getR(lightColor) * Color.RATIO,
								   1 - Color.getG(lightColor) * Color.RATIO,
								   1 - Color.getB(lightColor) * Color.RATIO);
			}
			else {
				// Marches a new ray to the light
				Vector lightRayDir = new Vector(scene.getLights().get(i).getPos());
				lightRayDir.subtract(pos);
				
				Ray lightRay = new Ray(pos, lightRayDir);
				lightRay.step(2 * MIN_LENGTH);
				
				int lightRayHit = lightRay.march(scene.getShapes(), -1);
				
				// If it hits the light, add brightness proportional to the light's brightness and the dot product of the normal
				if (lightRayHit != -1 && scene.getShapes().get(lightRayHit) == scene.getLights().get(i)) {
					double lightShade = Math.max(normal.dotProduct(lightRay.getDir()), 0) * Color.RATIO;
					int lightColor = scene.getLights().get(i).getColor();
					
					brightness.stretch(1 - Color.getR(lightColor) * lightShade,
									   1 - Color.getG(lightColor) * lightShade,
									   1 - Color.getB(lightColor) * lightShade);
				}
			}
		}
		
		// Multiply the shade of the pixel by the total brightness and add ambient light
		shade.stretch(Math.min(1 - brightness.getX() + Color.getR(scene.getScreen().getBgnd()) * Color.RATIO, 1),
					  Math.min(1 - brightness.getY() + Color.getG(scene.getScreen().getBgnd()) * Color.RATIO, 1),
					  Math.min(1 - brightness.getZ() + Color.getB(scene.getScreen().getBgnd()) * Color.RATIO, 1));
		
		double shine = scene.getShapes().get(hit).getShine();
		int reflectionColor = 0;
		
		// Casts the reflected ray
		if (shine > 0 && reflections > 0) {
			shade.multiply(shine);
			reflectionColor = cast(scene, shade, medium, reflections - 1);
			shade.multiply(1 / shine - 1);
		}
		
		return Color.shade(scene.getShapes().get(hit).getColor(), shade.getX(), shade.getY(), shade.getZ()) + reflectionColor + refractionColor;
	}
	
	private void reflect(Vector normal) {
		double dot = normal.dotProduct(dir);
		
		dir.add(-2 * dot * normal.getX(), -2 * dot * normal.getY(), -2 * dot * normal.getZ());
	}
	
	private void refract(Vector normal, double n1, double n2) {
		double dot = normal.dotProduct(dir);
		double mu = n1 / n2;
		
		double root = Math.sqrt(1 - mu*mu * (1 - dot*dot)) * (n1 < n2 ? -1 : 1);
		
		// Implementing Snell's Law in vector form as t = sqrt(1 - u^2(1 - (n.i)^2)) * n + u(i - (n.i)n)
		dir.add(-dot * normal.getX(), -dot * normal.getY(), -dot * normal.getZ());
		dir.multiply(mu);
		dir.add(root * normal.getX(), root * normal.getY(), root * normal.getZ());
	}
	
	public int march(ArrayList<Shape> shapes, int medium) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = -1;
			double minDist = MAX_LENGTH;
			
			for (int i = 0; i < shapes.size(); i++) {
				double distance = (shapes.get(i).getDistance(pos) - MIN_LENGTH) * (i == medium ? -1 : 1) + MIN_LENGTH;
				
				if (distance < minDist) {
					nearest = i;
					minDist = distance;
				}
			}
			
			if (nearest == -1) return -1;
			
			step(minDist);
			
			if (minDist < MIN_LENGTH) return nearest;
		}
		
		return -1;
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