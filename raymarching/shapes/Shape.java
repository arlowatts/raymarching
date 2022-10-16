package raymarching.shapes;

import java.lang.Math;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*
 * Contains methods and variables a 3D shape will need, such as position and surface properties
 */
public abstract class Shape {
	// Constants
	// The default parameters that must be passed to the constructor
	public static final String[] DEFAULT_PARAMS = {"x", "y", "z", "phi", "theta", "psi", "reflectivity", "transparency", "refrIndex"};
	// The minimum size of a shape - it depends on the shape to limit its size to this value
	public static final double MIN_LENGTH = 0.001;
	
	// Member variables
	private Vector pos, angle;
	
	// Reflectivity, transparency, and refractive index are all universal properties of the surface
	private double reflectivity, transparency, refrIndex;
	
	// The subclass of Shape should define how the texture maps to the surface
	private BufferedImage texture;
	// If the texture is null, a color will be used instead
	private int color;
	
	// boundRadius represents the size of the smallest sphere centered at pos that can completely contain the shape
	// The subclass of Shape must define how it is calculated
	private double boundRadius;
	
	// Constructors
	public Shape(double... args) {
		pos = new Vector(args[0], args[1], args[2]);
		angle = new Vector(args[3], args[4], args[5]);
		
		reflectivity = Math.min(Math.max(args[6], 0), 1);
		transparency = Math.min(Math.max(args[7], 0), 1);
		refrIndex = Math.max(args[8], 1);
		
		color = 0;
		
		boundRadius = -1;
	}
	
	// Abstract methods
	// Should return the least distance from a point v to the surface of the shape
	public abstract double getDistance(Vector v);
	// Should call setBoundRadius(double r) to set the bound radius to the radius
	// of the smallest sphere centered at pos that completely contains the shape
	protected abstract void setBoundRadius();
	
	// Methods
	// Returns the surface normal at the point v
	// Can be overridden by a more efficient and more precise function in many cases
	public Vector getNormal(Vector v) {
		double distance = getDistance(v);
		
		v.add(MIN_LENGTH, 0, 0);
		double xDistance = getDistance(v);
		
		v.add(-MIN_LENGTH, MIN_LENGTH, 0);
		double yDistance = getDistance(v);
		
		v.add(0, -MIN_LENGTH, MIN_LENGTH);
		double zDistance = getDistance(v);
		
		v.add(0, 0, -MIN_LENGTH);
		
		Vector normal = new Vector(xDistance - distance, yDistance - distance, zDistance - distance);
		normal.setLength(1);
		
		return normal;
	}
	
	// Returns the color of the surface at a point v
	// Must be overriden in a subclass to implement texture mapping
	public int getColor(Vector v) {return color;}
	
	// Loads a texture (image) from a file into the shape
	public void loadTexture(String path) throws IOException {
		texture = ImageIO.read(new File(path));
	}
	
	// Getters
	public Vector getPos() {return pos;}
	public Vector getAngle() {return angle;}
	
	public double getReflectivity() {return reflectivity;}
	public double getTransparency() {return transparency;}
	public double getRefrIndex() {return refrIndex;}
	
	public double getBoundRadius() {
		if (boundRadius == -1) setBoundRadius();
		return boundRadius;
	}
	
	public int getColor() {return color;}
	public BufferedImage getTexture() {return texture;}
	
	// Setters
	public void setReflectivity(double reflectivity) {this.reflectivity = reflectivity;}
	public void setTransparency(double transparency) {this.transparency = transparency;}
	public void setRefrIndex(double refrIndex) {this.refrIndex = refrIndex;}
	
	public void setColor(int color) {this.color = color;}
	public void setTexture(BufferedImage texture) {this.texture = texture;}
	
	// Helpers
	protected void setBoundRadius(double r) {boundRadius = r;}
	
	// Returns a new vector much closer to the surface of the shape and in coordinates relative to the shape
	protected Vector toSurface(Vector v) {
		Vector r = new Vector(v);
		
		r.subtract(getNormal(r), getDistance(r));
		r.subtract(pos);
		r.inverseRotate(angle);
		
		return r;
	}
}
