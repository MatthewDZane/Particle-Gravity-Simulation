package helper;

import data.XYVector;

/**
 * Defines a circle within an X/Y coordinate system.
 * @author Matthew Zane
 *
 */
public class Circle {
	
	/**
	 * Position of center of circle
	 */
	private XYVector position;
	
	/**
	 * Length of radius of circle
	 */
	private double radius;
	
	public XYVector getPosition() { return position; }
	public double getRadius() { return radius; }
	
	public Circle() {
		position = new XYVector();
		radius = 0;
	}
	
	public Circle(XYVector positionIn, double radiusIn) {
		position = positionIn;
		radius = radiusIn;
	}
}
