package raymarching.shapes;

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
	The list of parameters required by Shape's constructor.
	The parameters are "x", "y", "z", "scalex", "scaley", "scalez", "phi", "theta", "psi", "smoothness", "transparency", "refrIndex".
	*/
	public static final String[] DEFAULT_PARAMS = {"x", "y", "z", "scalex", "scaley", "scalez", "phi", "theta", "psi", "smoothness", "transparency", "refrIndex"};
	/**
	The list of additional parameters required by subclass's constructor.
	Defaults to none.
	*/
	public static final String[] PARAMS = {};
	/**
	The minimum size of a shape. It depends on the subclasses of Shape to limit their size by this value.
	*/
	public static final double MIN_LENGTH = 0.001;
	
	// Member variables
	private Vector position, dimension, rotation;
	
	// Reflectivity, transparency, and refractive index are all universal properties of the surface
	private double smoothness, transparency, refrIndex;
	
	// The subclass of Shape should define how the texture maps to the surface
	// If texture is null, color is used instead
	private BufferedImage texture;
	private int color;
	
	// boundRadius represents the size of the smallest sphere centered at position that can completely contain the shape
	// The subclass of Shape must define how it is calculated
	private double boundRadius;
	
	/**
	Creates a new Shape from <code>args</code>, which must match the parameters in <code>DEFAULT_PARAMS</code>.
	
	@param args an array or sequence of doubles representing the paramaters described in <code>DEFAULT_PARAMS</code>.
	*/
	public Shape(double... args) {
		position = new Vector(args[0], args[1], args[2]);
		dimension = new Vector(args[3], args[4], args[5]);
		rotation = new Vector(args[6], args[7], args[8]);
		
		smoothness = Math.min(Math.max(args[9], 0), 1);
		transparency = Math.min(Math.max(args[10], 0), 1);
		refrIndex = Math.max(args[11], 1);
		
		color = -1;
		
		boundRadius = -1;
	}
	
	/**
	Computes the least distance from a point to the surface of the volume.
	
	@param v the point
	
	@return the least distance from <code>v</code> to the surface of the volume, which is a negative value if the point is inside the volume.
	*/
	public double getDistance(Vector v) {
		return getLocalDistance(toLocalFrame(v));
	}
	
	/**
	Computes the least distance from a point given in coordinates local to the shape to the surface of the volume.
	
	@param r the point
	
	@return the least distance from <code>v</code> to the surface of the volume, which is a negative value if the point is inside the volume.
	*/
	protected abstract double getLocalDistance(Vector r);
	
	/**
	Computes the surface normal of the shape near a point.
	
	@param v the point
	
	@return the unit vector normal to the surface near <code>v</code>.
	*/
	public Vector getNormal(Vector v) {
		Vector normal = getLocalNormal(toLocalFrame(v));
		
		normal.compress(dimension);
		normal.rotate(rotation);
		normal.setLength(1);
		
		return normal;
	}
	
	/**
	Computes the surface normal of the shape near a point given in coordinates local to the shape.
	This method can be overridden in a subclass by a more efficient and more accurate method.
	
	@param r the point in local coordinates
	
	@return the unit vector normal to the surface near <code>v</code>.
	*/
	protected Vector getLocalNormal(Vector r) {
		double d = getLocalDistance(r);
		
		r.add(MIN_LENGTH, 0, 0);
		double dx = getLocalDistance(r);
		
		r.add(-MIN_LENGTH, MIN_LENGTH, 0);
		double dy = getLocalDistance(r);
		
		r.add(0, -MIN_LENGTH, MIN_LENGTH);
		double dz = getLocalDistance(r);
		
		r.add(0, 0, -MIN_LENGTH);
		
		Vector normal = new Vector(dx - d, dy - d, dz - d);
		normal.setLength(1);
		
		return normal;
	}
	
	/**
	Returns the color of the shape near a point.
	
	@param v the point
	
	@return an integer representing the color in 32-bit RGB format.
	*/
	public int getColor(Vector v) {
		if (texture == null && color != -1) return color;
		
		Vector r = toLocalFrame(v);
		r.subtract(getLocalNormal(r), getLocalDistance(r));
		
		return getLocalColor(r);
	}
	
	/**
	Returns the color of the shape near a point given in coordinates local to the shape.
	This method must be overriden in a subclass to implement texture mapping.
	
	@param r the point
	
	@return an integer representing the color in 32-bit RGB format.
	*/
	protected int getLocalColor(Vector r) {
		return color;
	}
	
	/**
	Computes the bound radius of the shape based on its dimensions. Called automatically as needed.
	
	@return the least radius of a sphere centered on the shape that completely contains it.
	*/
	protected abstract double setBoundRadius();
	
	/**
	Loads an image into the texture of the shape.
	
	@param path a correct path to an image file
	*/
	public void loadTexture(String path) throws IOException {
		texture = ImageIO.read(new File(path));
	}
	
	// Getters
	public Vector getPos() {return position;}
	public Vector getDim() {return dimension;}
	public Vector getRot() {return rotation;}
	
	public double getSmoothness() {return smoothness;}
	public double getTransparency() {return transparency;}
	public double getRefrIndex() {return refrIndex;}
	
	public double getBoundRadius() {
		if (boundRadius == -1) boundRadius = Math.max(Math.max(dimension.x, dimension.y), dimension.z) * setBoundRadius();
		return boundRadius;
	}
	
	public int getColor() {return color;}
	public BufferedImage getTexture() {return texture;}
	
	// Setters
	public void setSmoothness(double smoothness) {this.smoothness = smoothness;}
	public void setTransparency(double transparency) {this.transparency = transparency;}
	public void setRefrIndex(double refrIndex) {this.refrIndex = refrIndex;}
	
	public void setColor(int color) {this.color = color;}
	public void setTexture(BufferedImage texture) {this.texture = texture;}
	
	/**
	Triggers an update to the bound radius. This method should be called whenever the shape's dimensions are modified.
	*/
	protected void updateBoundRadius() {boundRadius = -1;}
	
	/**
	Takes a vector in scene coordinates and transforms it into a vector in coordinates local to the shape.
	
	@return a vector in coordinates relative to the position and rotation of the shape.
	*/
	private Vector toLocalFrame(Vector v) {
		Vector r = new Vector(v);
		
		r.subtract(position);
		r.inverseRotate(rotation);
		r.compress(dimension);
		
		return r;
	}
}
