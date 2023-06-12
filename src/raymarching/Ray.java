package raymarching;

import raymarching.shapes.*;

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
	public int cast(Scene scene, Shape medium, int reflections) {
		// March the ray through the scene
		int hitIndex = march(scene, medium);
		if (hitIndex == -1) return scene.getScreen().getBgnd();
		
		Shape surface = scene.getShape(hitIndex);
		int surfaceColor = surface.getColor(pos);
		
		// If the ray has reached the reflection limit, simply return the surface color
		if (reflections <= 0) return surfaceColor;
		
		Vector normal = surface.getNormal(pos);
		
		int diffuseColor = 0, reflectColor = 0, refractColor = 0;
		
		double smoothness = surface.getSmoothness();
		double transparency = surface.getTransparency();
		
		// If the hit surface is not smooth, estimate the diffusion color from nearby surfaces
		if (smoothness < 1)
			diffuseColor = Color.shade(getDiffuseColor(scene, normal), 1 - smoothness);
		
		// If the hit surface is smooth, create a reflection ray and march it into the scene
		if (smoothness > 0 && transparency < 1) {
			Ray reflectedRay = new Ray(this);
			reflectedRay.reflect(normal);
			
			// Stepping away from the current surface
			reflectedRay.pos.add(normal, 2 * MIN_LENGTH);
			
			reflectColor = Color.shade(reflectedRay.cast(scene, medium, reflections - 1), smoothness * (1 - transparency));
		}
		
		// If the hit surface is smooth and transparent, create a refraction ray and march it through the surface
		if (smoothness > 0 && transparency > 0) {
			// Get the refractive index of the current medium, or 1 if the medium is null (empty space)
			double n1 = medium == null ? 1 : medium.getRefrIndex();
			// Get the refractive index of the object the ray is refracting into, or 1 if it hit the same surface (it is refracting into space)
			double n2 = medium == surface ? 1 : surface.getRefrIndex();
			
			Ray refractedRay = new Ray(this);
			refractedRay.refract(normal, n1, n2);
			
			refractColor = Color.shade(refractedRay.cast(scene, medium == surface ? null : surface, reflections - 1), smoothness * transparency);
		}
		
		/*Vector brightness = new Vector(1, 1, 1);
		
		// Iterates over all the lights and marches to them
		for (int i = 0; i < scene.numLights(); i++) {
			if (surface == scene.getLight(i)) continue;
			
			getBrightness(scene, scene.getLight(i), normal, brightness);
		}
		
		brightness.multiply(-1);
		brightness.add(1, 1, 1);
		
		int light = Color.safeAdd(Color.toColor(brightness), scene.getScreen().getBgnd());*/
		
		//return Color.shade(Color.shade(diffuseColor + reflectColor + refractColor, surfaceColor), light);
		return Color.shade(diffuseColor + reflectColor + refractColor, surfaceColor);
	}
	
	/*
	 * Approximate the diffusion color at the current point on the surface of an object
	 * Consider light emitted from light sources in the calculation
	 * Approximate each shape as a disk facing the target point
	 */
	private int getDiffuseColor(Scene scene, Vector normal) {
		// Start with the background color, weighted with full area (surface area of half sphere)
		double totalShapeArea = 2 * Math.PI;
		Vector diffuseColor = Color.toVector(scene.getScreen().getBgnd());
		diffuseColor.multiply(totalShapeArea);
		
		// Project each shape onto the unit sphere
		for (int i = scene.numShapes() - 1; i >= 0; i--) {
			Shape currShape = scene.getShape(i);
			
			// Get the position of the shape relative to the current point
			Vector relativePos = new Vector(currShape.getPos());
			relativePos.subtract(pos);
			
			// Find the arc length of the projected radius on the unit sphere, and the area of the region
			// See https://mathworld.wolfram.com/SphericalCap.html
			double radius = Math.atan(currShape.getBoundRadius() / relativePos.getLength());
			double area = 2 * Math.PI * (1 - 1 / Math.sqrt(1 + radius * radius));
			
			// Shade the color by the area it fills and add to the total color
			Color.addToVector(diffuseColor, currShape.getColor(), area);
			
			// Track the total area that has been contributed by each shape
			totalShapeArea += area;
		}
		
		diffuseColor.divide(totalShapeArea);
		
		return Color.toColor(diffuseColor);
	}
	
	// March rays from the current point to each light source in the scene to find the total brightness of the hit point
	/*private void getBrightness(Scene scene, Shape light, Vector normal, Vector brightness) {
		Ray lightRay = new Ray(this);
		
		// Stepping away from the current surface
		lightRay.pos.add(normal, 2 * MIN_LENGTH);
		
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
	}*/
	
	/* 
	 * Implementing Snell's Law in vector form as t = sqrt(1 - (u^2)(1 - (n.i)^2)) * n + u(i - (n.i)n)
	 * i = the initial vector,
	 * t = the resultant vector,
	 * n = the normal vector,
	 * u = the ratio of refractive indices n1/n2
	 */
	private void refract(Vector normal, double n1, double n2) {
		double dot = normal.dotProduct(dir);
		double mu = n1 / n2;
		
		double root = Math.sqrt(1 - mu*mu * (1 - dot*dot)) * (dot < 0 ? -1 : 1);
		
		dir.add(normal, -dot);
		dir.multiply(mu);
		dir.add(normal, root);
	}
		
	/*
	 * Reflecting the ray by the formula t = i - 2(n.i)(n)
	 * i = the initial vector,
	 * t = the resultant vector,
	 * n = the normal vector
	 */
	private void reflect(Vector normal) {
		double dot = normal.dotProduct(dir);
		dir.add(normal, -2 * dot);
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
		
		// Iteratively step by the largest distance possible without passing through a shape
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
		
		pos.add(dir, len);
	}
	
	// Getters
	public Vector getPos() {return pos;}
	public Vector getDir() {return dir;}
	
	public int getSteps() {return steps;}
	public double getLength() {return length;}
}
