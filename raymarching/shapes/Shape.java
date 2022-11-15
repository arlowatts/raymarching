package raymarching.shapes;

import raymarching.Color;
import raymarching.Vector;

import java.lang.Math;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
A 3-dimensional volume defined by a signed distance function.
*/
public abstract class Shape {
	/**
	The list of parameters required by the class's constructor.
	*/
	public static final String[] DEFAULT_PARAMS = {"x", "y", "z", "phi", "theta", "psi", "reflectivity", "transparency", "refrIndex"};
	/**
	The minimum size of a shape. It depends on the subclasses of Shape to limit their size by this value.
	*/
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
	
	/**
	Creates a new Shape from the array <code>args</code>, which must match the parameters in <code>DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>DEFAULT_PARAMS</code>.
	*/
	public Shape(double... args) {
		pos = new Vector(args[0], args[1], args[2]);
		angle = new Vector(args[3], args[4], args[5]);
		
		reflectivity = Math.min(Math.max(args[6], 0), 1);
		transparency = Math.min(Math.max(args[7], 0), 1);
		refrIndex = Math.max(args[8], 1);
		
		color = 0;
		
		boundRadius = -1;
	}
	
	/**
	Computes the least distance from a point to the surface of the volume.
	
	@param v the point
	
	@return the least distance from <code>v</code> to the surface of the volume, which is a negative value if the point is inside the volume.
	*/
	public abstract double getDistance(Vector v);
	
	/**
	Computes the bound radius of the shape based on its dimensions. Called automatically as needed.
	
	@return the least radius of a sphere centered on the shape that completely contains it.
	*/
	protected abstract double setBoundRadius();
	
	/**
	Computes the surface normal of the shape near a point.
	Often can be overridden in a subclass by a more efficient and more accurate method.
	
	@param v the point
	
	@return the unit vector normal to the surface near <code>v</code>.
	*/
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
	
	/**
	Returns the color of the shape near a point.
	Must be overriden in a sublcass to implement texture mapping.
	
	@param v the point
	
	@return an integer representing the color in 32-bit RGB format.
	*/
	public int getColor(Vector v) {return color;}
	
	/**
	Loads an image into the texture of the shape.
	
	@param path a correct path to an image file
	*/
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
		if (boundRadius == -1) boundRadius = setBoundRadius();
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
	
	/**
	Triggers an update to the bound radius. Should be called whenever the shape's dimensions are modified.
	*/
	protected void updateBoundRadius() {boundRadius = -1;}
	
	/**
	Takes a vector in universal coordinates and transforms it into a vector in coordinates local to the shape.
	
	@return a vector in coordinates relative to the position and rotation of the shape.
	*/
	protected Vector toLocalFrame(Vector v) {
		Vector r = new Vector(v);
		
		r.subtract(pos);
		r.inverseRotate(angle);
		
		return r;
	}
	
	/**
	Takes a universal vector and transforms it into a local vector that is closer to the surface of the shape.
	
	@return a vector in coordinates relative to the shape and closer to the surface.
	*/
	protected Vector toSurface(Vector v) {
		Vector r = new Vector(v);
		
		r.subtract(getNormal(r), getDistance(r));
		r.subtract(pos);
		r.inverseRotate(angle);
		
		return r;
	}
}
