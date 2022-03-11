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
	
	private double lastStep;
	
	// Constructors
	public Ray(Vector pos, Vector dir) {
		this.pos = new Vector(pos);
		this.dir = new Vector(dir);
		
		this.dir.setLength(1);
		
		steps = 0;
		length = 0;
		
		lastStep = 0;
	}
	
	// Methods
	// The recursive method to cast and reflect a light ray
	public int cast(Scene scene, Vector shade, int medium, int reflections) {
		// March the ray through the scene
		int hit = march(scene.getShapes(), medium);
		
		if (hit == -1) return Color.shade(scene.getScreen().getBgnd(), shade.getX(), shade.getY(), shade.getZ());
		
		Vector normal = scene.getShape(hit).getNormal(pos);
		
		double transparency = scene.getShape(hit).getTransparency();
		int refractionColor = 0;
		
		// If the hit object is not opaque, create a new ray and refract it through the surface
		if (transparency > 0 && reflections > 0) {
			// Get the refractive index of the current medium, or 1 if the medium is -1 (no object)
			double n1 = medium == -1  ? 1 : scene.getShape(medium).getRefrIndex();
			// Get the refractive index of the object the ray is refracting into, or 1 if it hit the same surface (it is refracting into space)
			double n2 = medium == hit ? 1 : scene.getShape(hit).getRefrIndex();
			
			Ray refractedRay = new Ray(this.pos, this.dir);
			refractedRay.refract(normal, n1, n2);
			
			// Shade the current color by the refracted ray
			shade.multiply(transparency);
			refractionColor = refractedRay.cast(scene, shade, hit == medium ? -1 : hit, reflections - 1);
			shade.multiply(1 / transparency - 1);
		}
		
		double shine = scene.getShape(hit).getShine();
		int reflectionColor = 0;
		
		// If the hit object is reflective, create a new ray and reflect it
		if (shine > 0 && reflections > 0) {
			Ray reflectedRay = new Ray(this.pos, this.dir);
			reflectedRay.reflect(normal);
			
			// Stepping away from the current surface
			reflectedRay.pos.add(2 * MIN_LENGTH * normal.getX(), 2 * MIN_LENGTH * normal.getY(), 2 * MIN_LENGTH * normal.getZ());
			
			// Shade the current color by the reflected ray
			shade.multiply(shine);
			reflectionColor = reflectedRay.cast(scene, shade, medium, reflections - 1);
			shade.multiply(1 / shine - 1);
		}
		
		Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < scene.getLights().size(); i++) {
			if (scene.getShape(hit) == scene.getLight(i)) continue;
			
			getBrightness(scene, scene.getLight(i), normal, brightness);
		}
		
		// Multiply the shade of the pixel by the total brightness and add ambient light
		shade.stretch(Math.min(1 - brightness.getX() + (double)Color.getR(scene.getScreen().getBgnd()) * Color.RATIO, 1),
					  Math.min(1 - brightness.getY() + (double)Color.getG(scene.getScreen().getBgnd()) * Color.RATIO, 1),
					  Math.min(1 - brightness.getZ() + (double)Color.getB(scene.getScreen().getBgnd()) * Color.RATIO, 1));
		
		return Color.shade(scene.getShapes().get(hit).getColor(), shade.getX(), shade.getY(), shade.getZ()) + reflectionColor + refractionColor;
	}
	
	private void refract(Vector normal, double n1, double n2) {
		double dot = normal.dotProduct(dir);
		double mu = n1 / n2;
		
		double root = Math.sqrt(1 - mu*mu * (1 - dot*dot)) * (dot < 0 ? -1 : 1);
		
		/* 
		 * Implementing Snell's Law in vector form as t = sqrt(1 - (u^2)(1 - (n.i)^2)) * n + u(i - (n.i)n)
		 * i = the initial vector,
		 * t = the resultant vector,
		 * n = the normal vector,
		 * u = the ratio of refractive indices n1/n2
		 */
		dir.add(-dot * normal.getX(), -dot * normal.getY(), -dot * normal.getZ());
		dir.multiply(mu);
		dir.add(root * normal.getX(), root * normal.getY(), root * normal.getZ());
	}
	
	private void reflect(Vector normal) {
		double dot = -2 * normal.dotProduct(dir);
		
		/*
		 * Reflecting the vector by the formula t = i - 2(n.i)(n)
		 * i = the initial vector,
		 * t = the resultant vector,
		 * n = the normal vector
		 */
		dir.add(dot * normal.getX(), dot * normal.getY(), dot * normal.getZ());
	}
	
	private void getBrightness(Scene scene, Shape light, Vector normal, Vector brightness) {
		Ray lightRay = new Ray(this.pos, this.dir);
		
		// Stepping away from the current surface
		lightRay.pos.add(2 * MIN_LENGTH * normal.getX(), 2 * MIN_LENGTH * normal.getY(), 2 * MIN_LENGTH * normal.getZ());
		
		// Pointing it at the light
		lightRay.dir.set(light.getPos());
		lightRay.dir.subtract(lightRay.pos);
		lightRay.dir.setLength(1);
		
		int hit = lightRay.march(scene.getShapes(), -1);
		
		// If it hits the light, add brightness proportional to the light's brightness and the dot product of the normal
		if (hit != -1 && scene.getShape(hit) == light) {
			double lightShade = Math.max(normal.dotProduct(lightRay.dir), 0) * Color.RATIO;
			int lightColor = light.getColor();
			
			brightness.stretch(1 - Color.getR(lightColor) * lightShade,
							   1 - Color.getG(lightColor) * lightShade,
							   1 - Color.getB(lightColor) * lightShade);
		}
	}
	
	public int march(ArrayList<Shape> shapes, int medium) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = -1;
			double minDist = MAX_LENGTH;
			
			// Finding the smallest distance to a shape in the scene
			for (int i = 0; i < shapes.size(); i++) {
				// If the ray is traveling through an object, the distance is inverted
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
		lastStep = len;
		
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