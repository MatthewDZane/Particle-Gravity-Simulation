package data;
/**
 * Represents a 3D vector <x, y, z>
 * @author Matthew Zane
 *
 */
public class XYZVector {
	private double x;
	private double y;
	private double z;
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }
	
	public void setX(double xIn) { x = xIn; }
	public void setY(double yIn) { y = yIn; }
	public void setZ(double zIn) { z = zIn; }
	
	/**
	 * Default Constructor instantiates a <0, 0, 0> vector
	 */
	public XYZVector() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	/**
	 * Constructor instantiates vector <xIn, yIn, zIn>
	 * @param xIn x coordinate of the vector
	 * @param yIn y coordinate of the vector
	 * @param zIn z coordinate of the vector
	 */
	public XYZVector(double xIn, double yIn, double zIn) {
		x = xIn;
		y = yIn;
		z = zIn;
	}
	
	/**
	 * Calculates magnitude of vector
	 * @return magnitude of vector
	 */
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns a 2D vector representing the angles (Radians) of the vector with
	 * x being the angle from the z-axis to the vector and
	 * y being the angle from the x-axis in the xy-plane.
	 * @return
	 */
	public XYVector getAngle() {
		double angleX = Math.acos(z / Math.sqrt(x * x + y * y + z * z));
		
		double angleY = Math.atan(y / x);
		if (x < 0) {
			angleY += Math.PI;
		}
		else if (x == 0) {
			if (y > 0) {
				angleY = Math.PI / 2;
			}
			else if (y == 0) {
				angleY = 0;
			}
			else {
				angleY = 3 * Math.PI / 2;
			}
		}
		
		return new XYVector(angleX, angleY);
	}
	
	/**
	 * Translates the vector by adding the parameters to the coordinates
	 * @param xOffSet x coordinate translation
	 * @param yOffSet y coordinate translation
	 */
	public void translate(double xOffSet, double yOffSet, double zOffSet) {
		x += xOffSet;
		y += yOffSet;
		z += zOffSet;
	}
	
	/**
	 * Translates the Vector by adding the input Vector to the this Vector
	 * @param translation
	 */
	public void add(XYZVector translation) {
		translate(translation.getX(), translation.getY(), translation.getZ());
	}
	
	/**
	 * Scales Vector by multiplying Vector by scalar
	 * @param scalar constant to multiply vector with
	 */
	public void scale(double scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	/**
	 * Calculates the dot product of this Vector and another
	 * @param vectorIn
	 * @return
	 */
	public double dotProduct(XYZVector vectorIn) {
		return x * vectorIn.getX() + y * vectorIn.getY() + z * vectorIn.getZ();
	}
	
	/**
	 * Same as object.clone() but saves casting from Object to Vector on 
	 * each call
	 * @return
	 */
	public XYZVector copy() { return new XYZVector(x, y, z); }
	
	/**
	 * Returns the sum of the specified Vectors as a new Vector
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static XYZVector add(XYZVector v1, XYZVector v2) {
		XYZVector sum = v1.copy();
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
	public static XYZVector subtract(XYZVector v1, XYZVector v2) {
		XYZVector negativeV2 = v2.copy();
		negativeV2.scale(-1);
		return add(v1, negativeV2);
	}
}
