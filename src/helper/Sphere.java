package helper;

import data.XYVector;
import data.XYZVector;

/**
 * Defines a circle within an X/Y coordinate system.
 * @author Matthew Zane
 *
 */
public class Sphere {
	
	/**
	 * Position of center of circle
	 */
	private XYZVector position;
	
	/**
	 * Length of radius of circle
	 */
	private double radius;
	
	public XYZVector getPosition() { return position; }
	public double getRadius() { return radius; }
	
	public Sphere() {
		position = new XYZVector();
		radius = 0;
	}
	
	public Sphere(XYZVector positionIn, double radiusIn) {
		position = positionIn;
		radius = radiusIn;
	}
}
