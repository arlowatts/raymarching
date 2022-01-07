import java.lang.Math;
import java.util.ArrayList;

public class Collection extends Object {
	// Member variables
	private ArrayList<Object> objects;
	
	// Constructors
	public Collection(double x, double y, double z, double angleX, double angleY, ArrayList<Object> objects, int color) {
		super(x, y, z, angleX, angleY, color);
		
		this.objects = objects;
	}
	
	public Collection(Vector v, double angleX, double angleY, ArrayList<Object> objects, int color) {this(v.getX(), v.getY(), v.getZ(), angleX, angleY, objects, color);}
	
	public Collection(double x, double y, double z, ArrayList<Object> objects, int color) {this(x, y, z, 0, 0, objects, color);}
	
	public Collection(Vector v, ArrayList<Object> objects, int color) {this(v.getX(), v.getY(), v.getZ(), 0, 0, objects, color);}
	
	public Collection(double x, double y, double z, int color) {this(x, y, z, 0, 0, new ArrayList<Object>(), color);}
	
	// Methods
	public void setBounds(Object camera, Screen screen) {
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
			Object object = objects.get(i);
			
			object.getPos().rotate(getAngleX(), getAngleY(), 0, 0, 0);
			object.getPos().add(getPos());
			object.rotate(getAngleX(), getAngleY());
			
			object.setBounds(camera, screen);
			
			double[] shapeBound = object.getBounds();
			
			object.rotate(-getAngleX(), -getAngleY());
			object.getPos().subtract(getPos());
			object.getPos().inverseRotate(getAngleX(), getAngleY(), 0, 0, 0);
			
			if (shapeBound[0] == 0 && shapeBound[1] == 0 && shapeBound[2] == 0 && shapeBound[3] == 0) continue;
			
			bounds[0] = Math.min(bounds[0], shapeBound[0]);
			bounds[1] = Math.min(bounds[1], shapeBound[1]);
			bounds[2] = Math.max(bounds[2], shapeBound[2]);
			bounds[3] = Math.max(bounds[3], shapeBound[3]);
		}
	}
	
	public double getDistance(Vector v) {
		if (objects.size() == 0) return getPos().getDistance(v);
		
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		
		v1.inverseRotate(getAngleX(), getAngleY(), 0, 0, 0);
		
		double minDist = objects.get(0).getDistance(v1);
		
		for (int i = 1; i < objects.size(); i++) {
			minDist = Math.min(minDist, objects.get(i).getDistance(v1));
		}
		
		return minDist;
	}
	
	public Vector getNormal(Vector v) {
		if (objects.size() == 0) return new Vector(0, 0, 0);
		
		Vector v1 = new Vector(v);
		
		v1.subtract(getPos());
		
		v1.inverseRotate(getAngleX(), getAngleY(), 0, 0, 0);
		
		int index = 0;
		double minDist = objects.get(0).getDistance(v1);
		
		for (int i = 1; i < objects.size(); i++) {
			double distance = objects.get(i).getDistance(v1);
			
			if (distance < minDist) {
				index = i;
				minDist = distance;
			}
		}
		
		Vector normal = objects.get(index).getNormal(v1);
		
		normal.rotate(getAngleX(), getAngleY(), 0, 0, 0);
		
		return normal;
	}
	
	public void add(Object object) {
		objects.add(object);
	}
	
	public void remove(Object object) {
		objects.remove(object);
	}
}