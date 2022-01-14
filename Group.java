import java.lang.Math;
import java.util.ArrayList;

public class Group extends Shape {
	// Member variables
	private ArrayList<Shape> objects;
	
	private double smoothing;
	
	// Constructors
	public Group(double x, double y, double z, double angleX, double angleY, double smoothing, ArrayList<Shape> objects, int color) {
		super(x, y, z, angleX, angleY, color);
		
		this.smoothing = smoothing;
		
		this.objects = objects;
	}
	
	public Group(Vector v, double angleX, double angleY, double smoothing, ArrayList<Shape> objects, int color) {this(v.getX(), v.getY(), v.getZ(), angleX, angleY, smoothing, objects, color);}
	
	public Group(double x, double y, double z, double angleX, double angleY, double smoothing, int color) {this(x, y, z, angleX, angleY, smoothing, new ArrayList<Shape>(), color);}
	
	// Methods
	public void setBounds(Shape camera, Screen screen) {
		double[] bounds = getBounds();
		
		if (objects.size() == 0) {
			bounds[0] = 0;
			bounds[1] = 0;
			bounds[2] = 0;
			bounds[3] = 0;
		}
		
		bounds[0] = screen.getWidth();
		bounds[1] = screen.getHeight();
		bounds[2] = -screen.getWidth();
		bounds[3] = -screen.getHeight();
		
		for (int i = 0; i < objects.size(); i++) {
			Shape object = objects.get(i);
			
			object.getPos().rotate(getAngleX(), getAngleY());
			object.getPos().add(getPos());
			object.rotate(getAngleX(), getAngleY());
			
			object.setBounds(camera, screen);
			
			double[] shapeBound = object.getBounds();
			
			object.rotate(-getAngleX(), -getAngleY());
			object.getPos().subtract(getPos());
			object.getPos().inverseRotate(getAngleX(), getAngleY());
			
			if (shapeBound[0] == 0 && shapeBound[1] == 0 && shapeBound[2] == 0 && shapeBound[3] == 0) continue;
			
			bounds[0] = Math.min(bounds[0], shapeBound[0]);
			bounds[1] = Math.min(bounds[1], shapeBound[1]);
			bounds[2] = Math.max(bounds[2], shapeBound[2]);
			bounds[3] = Math.max(bounds[3], shapeBound[3]);
		}
	}
	
	public double getDistance(Vector v) {
		if (objects.size() == 0) return getPos().getDistance(v);
		
		v.subtract(getPos());
		v.inverseRotate(getAngleX(), getAngleY());
		
		double minDist = objects.get(0).getDistance(v);
		
		for (int i = 1; i < objects.size(); i++) {
			minDist = smoothMin(minDist, objects.get(i).getDistance(v), smoothing);
		}
		
		v.rotate(getAngleX(), getAngleY());
		v.add(getPos());
		
		return minDist;
	}
	
	private double smoothMin(double a, double b, double k) {
		double h = Math.max(k - Math.abs(a - b), 0) / k;
		
		return Math.min(a, b) - h * h * k * 0.25;
	}
	
	public void add(Shape object) {
		objects.add(object);
	}
	
	public void remove(int i) {
		objects.remove(i);
	}
	
	public Shape get(int i) {
		return objects.get(i);
	}
	
	public void setSmoothing(double smoothing) {this.smoothing = smoothing;}
}