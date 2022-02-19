import java.lang.Math;

public class Color {
	public static final double RATIO = 1.0 / 255.0;
	
	public static int getR(int color) {return (color >> 16) & 0xff;}
	public static int getG(int color) {return (color >>  8) & 0xff;}
	public static int getB(int color) {return (color      ) & 0xff;}
	
	public static int toColor(int r, int g, int b) {return (r << 16) | (g << 8) | b;}
	public static int toColor(double r, double g, double b) {return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);}
	
	public static int shade(int color, double shade) {
		return ((int)((double)getR(color) * shade) << 16) |
			   ((int)((double)getG(color) * shade) << 8) |
			   (int)((double)getB(color) * shade);
	}
	
	public static int shade(int color, double shadeR, double shadeG, double shadeB) {
		return ((int)((double)getR(color) * shadeR) << 16) |
			   ((int)((double)getG(color) * shadeG) << 8) |
			   (int)((double)getB(color) * shadeB);
	}
	
	public static int shade(int color, int shade) {
		return ((int)((double)(getR(color) * getR(shade)) * RATIO) << 16) |
			   ((int)((double)(getG(color) * getG(shade)) * RATIO) << 8) |
			   (int)((double)(getB(color) * getB(shade)) * RATIO);
	}
	
	public static int invert(int color) {return 0xffffff - color;}
	
	public static int max(int colorA, int colorB) {
		return (Math.max(getR(colorA), getR(colorB)) << 16) |
			   (Math.max(getG(colorA), getG(colorB)) << 8) |
			   Math.max(getB(colorA), getB(colorB));
	}
	
	public static int min(int colorA, int colorB) {
		return (Math.min(getR(colorA), getR(colorB)) << 16) |
			   (Math.min(getG(colorA), getG(colorB)) << 8) |
			   Math.min(getB(colorA), getB(colorB));
	}
}