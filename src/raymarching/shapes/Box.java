package raymarching.shapes;

import raymarching.Vector;

import java.lang.Math;

/**
A subclass of Shape defining a rectangular box.
*/
public class Box extends Shape {
	/**
	Creates a new Box from <code>args</code> and <code>dargs</code>.
	<code>args</code> must match the parameters in <code>Box.PARAMS</code>.
	<code>dargs</code> must match the parameters in <code>Shape.DEFAULT_PARAMS</code>.
	
	@param args an array of doubles representing the paramaters described in <code>Box.PARAMS</code>.
	@param dargs an array of doubles representing the paramaters described in <code>Shape.DEFAULT_PARAMS</code>.
	*/
	public Box(double[] args, double[] dargs) {
		super(dargs);
	}
	
	protected double getLocalDistance(Vector r) {
		Vector q = new Vector(r);
		
		q.positive();
		q.subtract(1, 1, 1);
		
		double d = Math.min(Math.max(q.x, Math.max(q.y, q.z)), 0);
		
		q.set(Math.max(q.x, 0), Math.max(q.y, 0), Math.max(q.z, 0));
		
		return q.getLength() + d;
	}
	
	@Override
	protected int getLocalColor(Vector r) {
		if (getTexture() == null) return getColor();

		int x = 0, y = 0;

		Vector q = new Vector(r);
		q.positive();

		if (q.x > q.y && q.x > q.z) {
			if (r.x < 0) {
				x = (int)(Math.min(Math.max(-r.z / 2 + 0.5, 0), 1) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(r.y / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
			else {
				x = (int)((Math.min(Math.max(r.z / 2 + 0.5, 0), 1) + 1) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(r.y / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
		}
		else if (q.z > q.y && q.z > q.x) {
			if (r.z < 0) {
				x = (int)((Math.min(Math.max(-r.x / 2 + 0.5, 0), 1) + 2) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(r.y / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
			else {
				x = (int)((Math.min(Math.max(r.x / 2 + 0.5, 0), 1) + 3) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(r.y / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
		}
		else if (q.y > q.z && q.y > q.x) {
			if (r.y < 0) {
				x = (int)((Math.min(Math.max(-r.x / 2 + 0.5, 0), 1) + 4) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(-r.z / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
			else {
				x = (int)((Math.min(Math.max(r.x / 2 + 0.5, 0), 1) + 5) * (getTexture().getWidth() - 1) / 6.0);
				y = (int)(Math.min(Math.max(r.z / 2 + 0.5, 0), 1) * (getTexture().getHeight() - 1));
			}
		}
		else {
			return getColor();
		}
		
		return getTexture().getRGB(x, y);
	}
	
	protected double setBoundRadius() {
		return Math.sqrt(3);
	}
}
