package entity;

import data.XYZVector;
import world.World;

/**
 * Represents a particle of matter with a mass, velocity, position
 * @author Matthew Zane
 *
 */
public class Particle {
	/**
	 * The elasticity of the particle. The percentage of 
	 * kinetic energy in conserves in a collision. Measured from 0.0 to 1.
	 * 1 is perfectly elastic and 0.0 is completely inelastic
	 */
	private double elasticity = 1;

	/**
	 * Measured in kilograms (kg)
	 */
	private double mass;
	
	/**
	 * Measured in Coulombs (C)
	 */
	private double charge = 1 * 1.60217662e-19;

	/**
	 * Measured in meter (m)
	 */
	private double radius;

	/**
	 * Measured in meters per second (m/s)
	 */
	private XYZVector initialVelocity;

	/**
	 * Measured in meters per second (m/s)
	 */
	private XYZVector finalVelocity;

	/**
	 * Measured in meters (m)
	 */
	private XYZVector position;

	/**
	 * Measured in Newtons (N)
	 */
	private XYZVector netForce;

	public double getElasticity() { return elasticity; }
	public double getMass() { return mass; }
	public double getRadius() { return radius; }
	public double getCharge() { return charge; }

	/**
	 * Returns a Vector of the initial velocity. 
	 * Always returns a non-null value.
	 * @return initialVelocity of the Particle
	 */
	public XYZVector getInitialVelocity() {
		if (initialVelocity == null) {
			initialVelocity = new XYZVector();
		}
		return initialVelocity;
	}

	/**
	 * Returns a Vector of the final velocity. 
	 * Always returns a non-null value.
	 * @return finalVelocity of the Particle
	 */
	public XYZVector getFinalVelocity() { 
		if (finalVelocity == null) {
			finalVelocity = new XYZVector();
		}
		return finalVelocity; 
	}

	/**
	 * Returns a Vector of the current position. 
	 * Always returns a non-null value.
	 * @return position of the Particle
	 */
	public XYZVector getPosition() {
		if (position == null) {
			position = new XYZVector();
		}
		return position;
	}

	/**
	 * Returns a Vector of the net force currently acting on the Particle.
	 * Always returns a non-null value.
	 * @return netForce on the Particle
	 */
	public XYZVector getNetForce() {
		if (netForce == null) {
			netForce = new XYZVector();
		}
		return netForce; 
	}

	/**
	 * Sets mass of the Particle. Must be greater than zero.
	 * Silently ignores invalid values.
	 * @param massIn value to set mass of Particle 
	 */
	public void setMass(double massIn) { 
		if (massIn > 0.0) {
			mass = massIn; 
		}
	}

	/**
	 * Sets radius of the Particle. Must be greater that zero.
	 * Silently ignores invalid values.
	 * @param radiusIn value to set radius of Particle
	 */
	public void setRadius(double radiusIn) { 
		if (radiusIn > 0.0) {
			radius = radiusIn; 
		}
	}
	
	public void setInitialVelocity(XYZVector velocityIn) { initialVelocity = velocityIn; }
	public void setFinalVelocity(XYZVector velocityIn) { finalVelocity = velocityIn; }
	public void setPosition(XYZVector positionIn) { position = positionIn; }
	public void setNetForce(XYZVector netForceIn) { netForce = netForceIn; }

	/**
	 * Constructor instantiates mass, velocityMagnitude, and velocityAngle 
	 * with parameters
	 * @param massIn Mass of Particle measured in kilograms (kg)
	 * @param velocityIn Vector representing velocity measured in meter 
	 * per seconds (m/s)
	 * @param positionIn Vector representing position in a 3D space measured 
	 * in meters (m)
	 */
	public Particle(double massIn, double radiusIn, XYZVector velocityIn, 
			XYZVector positionIn) {
		mass = massIn;
		radius = radiusIn;
		initialVelocity = velocityIn;
		position = positionIn;
	}

	/**
	 * Constructor instantiates mass, velocityMagnitude, and velocityAngle 
	 * with parameters in form of base data types
	 * @param massIn Mass of Particle measured in kilograms (kg)
	 * @param velocityIn Vector representing velocity measured in meter 
	 * per seconds (m/s)
	 * @param positionIn Vector representing position in a 3D space measured 
	 * in meters (m)
	 */
	public Particle(double massIn, double radiusIn, double velocityX, 
			double velocityY, double velocityZ, double positionX, double positionY,
			double positionZ) {
		mass = massIn;
		radius = radiusIn;
		initialVelocity = new XYZVector(velocityX, velocityY, velocityZ);
		position = new XYZVector(positionX, positionY, positionZ);
	}

	/**
	 * Calculates final velocity using formula: Vf = (p + J) / m, 
	 * with p = momentum, j = impulse, and m = mass. 
	 */
	public void calcNewVelocity() {
		finalVelocity = XYZVector.add(getMomentum(), getImpulse());
		finalVelocity.scale(1 / mass);
	}

	/**
	 * Uses the average of the initial and final velocity, 
	 * using formula: d = (Vi + Vf) / 2 * t, where t - MIN_TIME_INTERVAL
	 * and then sets the initial velocity equal to final velocity
	 */
	public void moveParticle() {
		XYZVector displacement = XYZVector.add(initialVelocity, finalVelocity);
		displacement.scale(World.MIN_TIME_INTERVAL / 2);
		position.add(displacement);
		resetVelocity();
	}

	/**
	 * Sets initialVelocity to a copy of finalVelocity
	 */
	public void resetVelocity() {
		initialVelocity = finalVelocity.copy();
	}

	/**
	 * Returns the current momentum of the Particle, using formula: p = mv.
	 * @return momentum is calculated with the initialVelocity vector 
	 * scaled by the mass.
	 */
	public XYZVector getMomentum() {
		XYZVector v = initialVelocity.copy();
		v.scale(mass);
		return v;
	}

	/**
	 * Returns the net impulse the Particle is experiencing in the current time 
	 * interval, using formula: J = Ft.
	 * @return impulse is calculated with the netForce vector scaled by 
	 * MIN_TIME_INTERVAL.
	 */
	public XYZVector getImpulse() {
		XYZVector v = netForce.copy();
		v.scale(World.MIN_TIME_INTERVAL);
		return v;
	}

	/**
	 * Returns the kinetic energy of the Particle using the formula:
	 * KE = 0.5 * m * v^2;
	 * @return the kinetic energy of the Particle
	 */
	public double getKineticEnergy() {
		return .5 * mass * Math.pow(initialVelocity.getMagnitude(), 2);
	}
	
	public String toString() {
		return getMass() + "," + getRadius() + "," + 
				getInitialVelocity().getX() + "," + 
				getInitialVelocity().getY() + "," + 
				getInitialVelocity().getZ() + "," +
				getPosition().getX() + "," + 
				getPosition().getY() + "," +
				getPosition().getZ(); 
	}
}
