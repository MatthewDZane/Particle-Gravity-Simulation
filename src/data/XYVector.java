package data;
/**
 * Represents a 2D vector <x, y>
 * @author Matthew Zane
 *
 */
public class XYVector {
	private double x;
	private double y;
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public void setX(double xIn) { x = xIn; }
	public void setY(double yIn) { y = yIn; }
	
	/**
	 * Default Constructor instantiates a <0, 0> vector
	 */
	public XYVector() {
		x = 0.0;
		y = 0.0;
	}
	
	/**
	 * Constructor instantiates vector <xIn, yIn>
	 * @param xIn x coordinate of the vector
	 * @param yIn y coordinate of the vector
	 */
	public XYVector(double xIn, double yIn) {
		x = xIn;
		y = yIn;
	}
	
	/**
	 * Calculates magnitude of vector
	 * @return magnitude of vector
	 */
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Return the angle (in radians) of the vector with respect to the positive x-axis
	 * @return
	 */
	public double getAngle() {
		double angle = Math.atan(y / x);
		if (x < 0) {
			angle += Math.PI;
		}
		else if (x == 0) {
			if (y >= 0) {
				angle = Math.PI / 2;
			}
			else {
				angle = 3 * Math.PI / 2;
			}
		}
		return angle;
	}
	
	/**
	 * Translates the vector by adding the parameters to the coordinates
	 * @param xOffSet x coordinate translation
	 * @param yOffSet y coordinate translation
	 */
	public void translate(double xOffSet, double yOffSet) {
		x += xOffSet;
		y += yOffSet;
	}
	
	/**
	 * Translates the Vector by adding the input Vector to the this Vector
	 * @param translation
	 */
	public void add(XYVector translation) {
		translate(translation.getX(), translation.getY());
	}
	
	/**
	 * Scales Vector by multiplying Vector by scalar
	 * @param scalar constant to multiply vector with
	 */
	public void scale(double scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	/**
	 * Calculates the dot product of this Vector and another
	 * @param vectorIn
	 * @return
	 */
	public double dotProduct(XYVector vectorIn) {
		return x * vectorIn.getX() + y * vectorIn.getY();
	}
	
	/**
	 * Same as object.clone() but saves casting from Object to Vector on 
	 * each call
	 * @return
	 */
	public XYVector copy() { return new XYVector(x, y); }
	
	/**
	 * Returns the sum of the specified Vectors as a new Vector
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static XYVector add(XYVector v1, XYVector v2) {
		XYVector sum = v1.copy();
		sum.add(v2);
		return sum;
	}
	
	/**
	 * Returns the difference of the specified Vectors by subtracting the second
	 * Vector by the first as a new Vector 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static XYVector subtract(XYVector v1, XYVector v2) {
		XYVector negativeV2 = v2.copy();
		negativeV2.scale(-1);
		return add(v1, negativeV2);
	}
}
