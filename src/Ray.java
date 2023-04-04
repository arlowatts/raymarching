package raymarching;

import raymarching.shapes.*;

import java.util.ArrayList;

/*
 * A Ray object has a position and a direction and can be cast through a scene.
 */
public class Ray {
	// Constants
	public static final int MAX_STEPS = 1000;
	public static final int MAX_LENGTH = 100;
	public static final double MIN_LENGTH = Shape.MIN_LENGTH;
	
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
		this.pos = new Vector(ray.pos);
		this.dir = new Vector(ray.dir);
		
		steps = 0;
		length = 0;
	}
	
	// Methods
	// The recursive method to cast and reflect a light ray
	public int cast(Scene scene, Vector shade, Shape medium, int reflections) {
		// March the ray through the scene
		int hitIndex = march(scene, medium);
		if (hitIndex == -1) return Color.shade(scene.getScreen().getBgnd(), shade);
		
		Shape hit = scene.getShape(hitIndex);
		
		Vector normal = hit.getNormal(pos);
		
		double transparency = hit.getTransparency();
		int refractionColor = 0;
		
		// If the hit object is not opaque, create a new ray and refract it through the surface
		if (transparency > 0 && reflections > 0) {
			// Get the refractive index of the current medium, or 1 if the medium is null (empty space)
			double n1 = medium == null ? 1 : medium.getRefrIndex();
			// Get the refractive index of the object the ray is refracting into, or 1 if it hit the same surface (it is refracting into space)
			double n2 = medium == hit ? 1 : hit.getRefrIndex();
			
			Ray refractedRay = new Ray(this);
			refractedRay.refract(normal, n1, n2);
			
			// Shade the current color by the refracted ray
			Vector tempShade = new Vector(shade);
			tempShade.multiply(transparency);
			shade.multiply(1 - transparency);
			
			refractionColor = refractedRay.cast(scene, tempShade, medium == hit ? null : hit, reflections - 1);
		}
		
		double reflectivity = hit.getSmoothness();
		int reflectionColor = 0;
		
		// If the hit object is reflective, create a new ray and reflect it
		if (reflectivity > 0 && reflections > 0) {
			Ray reflectedRay = new Ray(this);
			reflectedRay.reflect(normal);
			
			// Stepping away from the current surface
			reflectedRay.pos.add(2 * MIN_LENGTH * normal.x, 2 * MIN_LENGTH * normal.y, 2 * MIN_LENGTH * normal.z);
			
			// Shade the current color by the reflected ray
			Vector tempShade = new Vector(shade);
			tempShade.multiply(reflectivity);
			shade.multiply(1 - reflectivity);
			
			reflectionColor = reflectedRay.cast(scene, tempShade, medium, reflections - 1);
		}
		
		Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < scene.numLights(); i++) {
			if (hit == scene.getLight(i)) continue;
			
			getBrightness(scene, scene.getLight(i), normal, brightness);
		}
		
		// Multiply the shade of the pixel by the total brightness and add ambient light
		shade.stretch(
			Math.min(1 - brightness.x + (double)Color.getR(scene.getScreen().getBgnd()) * Color.RATIO, 1),
			Math.min(1 - brightness.y + (double)Color.getG(scene.getScreen().getBgnd()) * Color.RATIO, 1),
			Math.min(1 - brightness.z + (double)Color.getB(scene.getScreen().getBgnd()) * Color.RATIO, 1)
		);
		
		// Add together all of the colors and return the result
		return Color.shade(hit.getColor(pos), shade) + refractionColor + reflectionColor;
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
		dir.add(-dot * normal.x, -dot * normal.y, -dot * normal.z);
		dir.multiply(mu);
		dir.add(root * normal.x, root * normal.y, root * normal.z);
	}
	
	private void reflect(Vector normal) {
		double dot = normal.dotProduct(dir);
		
		/*
		 * Reflecting the ray by the formula t = i - 2(n.i)(n)
		 * i = the initial vector,
		 * t = the resultant vector,
		 * n = the normal vector
		 */
		dir.add(-2 * dot * normal.x, -2 * dot * normal.y, -2 * dot * normal.z);
	}
	
	// March rays from the current point to each light source in the scene to find the total brightness of the hit point
	private void getBrightness(Scene scene, Shape light, Vector normal, Vector brightness) {
		Ray lightRay = new Ray(this.pos, this.dir);
		
		// Stepping away from the current surface
		lightRay.pos.add(2 * MIN_LENGTH * normal.x, 2 * MIN_LENGTH * normal.y, 2 * MIN_LENGTH * normal.z);
		
		// Pointing it at the light
		lightRay.dir.set(light.getPos());
		lightRay.dir.subtract(lightRay.pos);
		lightRay.dir.setLength(1);
		
		int hit = lightRay.march(scene, null);
		
		// If it hits the light, add brightness proportional to the light's brightness and the dot product of the normal
		if (hit != -1 && scene.getShape(hit) == light) {
			double lightShade = Math.max(normal.dotProduct(lightRay.dir), 0) * Color.RATIO;
			int lightColor = light.getColor();
			
			brightness.stretch(1 - Color.getR(lightColor) * lightShade,
							   1 - Color.getG(lightColor) * lightShade,
							   1 - Color.getB(lightColor) * lightShade);
		}
	}
	
	// Finding the distance along the ray's path to the closest point to v
	public double lengthToPoint(Vector v) {
		return v.dotProduct(dir) - pos.dotProduct(dir);
	}
	
	// Marching the ray through the scene until it hits a shape, exceeds the maximum number of steps, or exceeds the maximum render distance
	public int march(Scene scene, Shape medium) {
		steps = 0;
		length = 0;
		
		double[] bounds = new double[scene.numShapes() * 2];
		
		for (int i = 0; i < scene.numShapes(); i++) {
			Vector shapePos = scene.getShape(i).getPos();
			double shapeRadius = scene.getShape(i).getBoundRadius();
			
			double distance = lengthToPoint(shapePos);
			
			// Find the value equal to the square of half the distance between the two points of intersection of the ray's path and the bounding sphere
			// Negative if there are no points of intersection
			double range = (shapeRadius + MIN_LENGTH)*(shapeRadius + MIN_LENGTH) -
				(shapePos.x - pos.x - distance*dir.x)*(shapePos.x - pos.x - distance*dir.x) -
				(shapePos.y - pos.y - distance*dir.y)*(shapePos.y - pos.y - distance*dir.y) -
				(shapePos.z - pos.z - distance*dir.z)*(shapePos.z - pos.z - distance*dir.z);
			
			if (range > 0 && distance > 0) {
				range = Math.sqrt(range);
				bounds[2*i] = distance - range;
				bounds[2*i+1] = distance + range;
			}
			else {
				bounds[2*i] = 0;
				bounds[2*i+1] = 0;
			}
		}
		
		// Iteratively step my the largest distance possible without passing through a shape
		while (steps < MAX_STEPS && length < MAX_LENGTH) {
			int nearest = -1;
			double minDist = MAX_LENGTH;
			
			// Finding the smallest distance to a shape in the scene
			for (int i = 0; i < scene.numShapes(); i++) {
				if (bounds[2*i] == bounds[2*i+1]) continue;
				
				double distance;
				
				if (length < bounds[2*i])
					distance = bounds[2*i] - length;
				
				else if (length < bounds[2*i+1])
					distance = scene.getShape(i).getDistance(pos) - MIN_LENGTH;
				
				else continue;
				
				// If the ray is traveling through an object, the distance is inverted
				distance = distance * (scene.getShape(i) == medium ? -1 : 1) + MIN_LENGTH;
				
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
		
		pos.add(dir.x * len, dir.y * len, dir.z * len);
	}
	
	// Getters
	public Vector getPos() {return pos;}
	public Vector getDir() {return dir;}
	
	public int getSteps() {return steps;}
	public double getLength() {return length;}
}
