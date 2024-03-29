package raymarching;

/*
 * A class with static methods for manipulating colors stored as standard RGB integers
 */
public class Color {
	public static final double RATIO = 1.0 / 255.0;
	
	// Methods to split the color into its components
	public static int getR(int color) {return (color >> 16) & 0xff;}
	public static int getG(int color) {return (color >>  8) & 0xff;}
	public static int getB(int color) {return  color        & 0xff;}
	
	// Methods to merge three components into a color
	public static int toColor(int r, int g, int b) {
		return (r << 16) | (g << 8) | b;
	}

	public static int toColor(double r, double g, double b) {
		return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
	}

	public static int toColor(Vector v) {
		return toColor(v.x, v.y, v.z);
	}

	public static Vector toVector(int color) {
		return new Vector(getR(color) * RATIO, getG(color) * RATIO, getB(color) * RATIO);
	}

	// Returns the component-wise average of two colors
	public static int averageColor(int colorA, int colorB) {
		return toColor(
			(getR(colorA) + getR(colorB)) / 2,
			(getG(colorA) + getG(colorB)) / 2,
			(getB(colorA) + getB(colorB)) / 2
		);
	}

	// Converts the color to a vector and then adds it to the other vector
	public static void addToVector(Vector v, int color, double scale) {
		scale *= RATIO;
		v.add(getR(color) * scale, getG(color) * scale, getB(color) * scale);
	}
	
	// Shades a color by a value between 0 and 1
	public static int shade(int color, double shade) {
		return ((int)(getR(color) * shade) << 16) |
			   ((int)(getG(color) * shade) <<  8) |
			    (int)(getB(color) * shade);
	}
	
	// Shades the components of a color by values between 0 and 1
	public static int shade(int color, double shadeR, double shadeG, double shadeB) {
		return ((int)(getR(color) * shadeR) << 16) |
			   ((int)(getG(color) * shadeG) <<  8) |
			    (int)(getB(color) * shadeB);
	}
	
	// Shades the components of a color by corresponding components of a vector
	public static int shade(int color, Vector v) {
		return ((int)(getR(color) * v.x) << 16) |
			   ((int)(getG(color) * v.y) <<  8) |
			    (int)(getB(color) * v.z);
	}
	
	// Shades a color by another color
	public static int shade(int color, int shade) {
		return ((int)(getR(color) * getR(shade) * RATIO) << 16) |
			   ((int)(getG(color) * getG(shade) * RATIO) <<  8) |
			    (int)(getB(color) * getB(shade) * RATIO);
	}
	
	public static int invert(int color) {return 0xffffff - color;}
}
