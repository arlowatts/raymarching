import shapes.*;

import java.util.ArrayList;

public class Ray {
	// Constants
	public static final int MAX_STEPS = 1000;
	public static final int MAX_LENGTH = 100;
	public static final double MIN_LENGTH = 0.01;
	
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
	public int cast(Scene scene, Vector shade, int reflections) {
		int hit = march(scene.getShapes());
		if (hit == -1) return Color.shade(scene.getScreen().getBgnd(), shade.getX(), shade.getY(), shade.getZ());
		
		// Gets the surface normal of the hit object
		Vector normal = scene.getShapes().get(hit).getNormal(pos);
		double dot = normal.dotProduct(dir);
		
		double transparency = scene.getShapes().get(hit).getTransparency();
		double refrIndex = scene.getShapes().get(hit).getRefrIndex();
		int refractionColor = 0;
		
		if (transparency > 0 && reflections > 0) {
			double mu = 1 / refrIndex;
			
			Vector refrRayDir = new Vector(dir);
			
			// Implementing Snell's Law in vector form as t = sqrt(1 - u^2(1 - (n.i)^2)) * n + u(i - (n.i)n)
			double root = -Math.sqrt(1 - mu*mu * (1 - dot*dot));
			refrRayDir.add(-dot * normal.getX(), -dot * normal.getY(), -dot * normal.getZ());
			refrRayDir.multiply(mu);
			refrRayDir.add(root * normal.getX(), root * normal.getY(), root * normal.getZ());
			
			Ray refrRay = new Ray(pos, refrRayDir);
			
			refrRay.marchThrough(scene.getShapes(), hit);
			
			// Refracting again on the other side
			Vector newNormal = scene.getShapes().get(hit).getNormal(refrRay.getPos());
			double newDot = newNormal.dotProduct(refrRayDir);
			
			// Implementing Snell's Law again
			root = Math.sqrt(1 - refrIndex*refrIndex * (1 - newDot*newDot));
			refrRayDir.add(-root * newNormal.getX(), -root * newNormal.getY(), -root * newNormal.getZ());
			refrRayDir.multiply(refrIndex);
			refrRayDir.add(newDot * newNormal.getX(), newDot * newNormal.getY(), newDot * newNormal.getZ());
			
			refrRay.setDir(refrRayDir);
			
			shade.multiply(transparency);
			refractionColor = refrRay.cast(scene, shade, reflections - 1);
			shade.multiply(1 / transparency - 1);
		}
		
		// Reflects the ray
		dir.add(-2 * dot * normal.getX(), -2 * dot * normal.getY(), -2 * dot * normal.getZ());
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
				
				int lightRayHit = lightRay.march(scene.getShapes());
				
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
			reflectionColor = cast(scene, shade, reflections - 1);
			shade.multiply(1 / shine - 1);
		}
		
		return Color.shade(scene.getShapes().get(hit).getColor(), shade.getX(), shade.getY(), shade.getZ()) + reflectionColor + refractionColor;
	}
	
	public int march(ArrayList<Shape> shapes) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = -1;
			double minDist = MAX_LENGTH;
			
			for (int i = 0; i < shapes.size(); i++) {
				double distance = shapes.get(i).getDistance(pos);
				
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
	
	public int marchThrough(ArrayList<Shape> shapes, int shape) {
		steps = 0;
		length = 0;
		
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = -1;
			double minDist = MAX_LENGTH;
			
			for (int i = 0; i < shapes.size(); i++) {
				double distance = shapes.get(i).getDistance(pos);
				
				if (i == shape) distance = 2 * MIN_LENGTH - distance;
				
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