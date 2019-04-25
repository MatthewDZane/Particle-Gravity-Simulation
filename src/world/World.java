package world;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import data.XYVector;
import data.XYZVector;
import entity.Particle;
import helper.Recorder;

public class World extends Thread {
	/**
	 * Gravitational Constant = 6.67408 x 10^-11 m^3 kg^-1 s^-2
	 */
	public static final double GRAVITATIONAL_CONSTANT = 6.67408e-11;

	/**
	 * Coulomb's Law Constant = 8.9875517873681764 x 10^9 N m^2 C^-2
	 */
	public static final double COULOMBS_LAW_CONSTANT = 8.9875517873681764e9;
	
	/**
	 * Smallest possible length in the world in meters (m)
	 */
	public static final double MIN_LENGTH = 1;

	/**
	 * Shortest possible time length in the world in seconds (s)
	 */
	public static final double MIN_TIME_INTERVAL = 100;

	/**
	 * List of all particles in the world
	 */
	private List<Particle> particles = new ArrayList<Particle>();

	private double time = 0;

	public List<Particle> getParticles() { return particles; }
	public double getTime() { return time; }

	public void setParticles(List<Particle> particlesIn) { particles = particlesIn; }
	
	public void setTime(double timeIn) { time = timeIn; }

	public void run() {
		while(true) {
			runSingleCycle();
			time += MIN_TIME_INTERVAL;
		}
	}

	/**
	 * One cycle includes calculating net force in the moment, then calculate 
	 * acceleration, then calculate velocity, then move each particle based on 
	 * velocity, and then checks for and handles collisions.
	 */
	private void runSingleCycle() {
		calculateForces();
		calculateDisplacement();
		calculateCollisions(); 

		if (time % (10000 * MIN_TIME_INTERVAL) == 0) {
			Recorder.recordTurn(this);
		}
	}

	/**
	 * Calculates the net forces on each particle
	 */
	private void calculateForces() {
		//reset all net forces
		for (Particle tempParticle : particles) {
			tempParticle.setNetForce(new XYZVector());
		}

		for (int i = 0; i < particles.size() - 1; i++) {
			for (int j = i + 1; j < particles.size(); j++) {
				addForces(particles.get(i), particles.get(j));
			}
		}
	}

	/**
	 * Calculates the force of gravity and electromagnetism between the two specified particles and 
	 * then adds the force vector to the particle1 and then scales it to the 
	 * negative before adding it to particle2
	 * @param particle1 Particle in which the force vector will be calculated in 
	 * reference to
	 * @param particle2 Other particle to calculate  force
	 */
	private void addForces(Particle particle1, Particle particle2) {
		XYZVector distance = getDistance(particle1, particle2);

		//calculate gravity magnitude
		double centerDistanceMagnitude = distance.getMagnitude();
		double gravityForceMagnitude = calcGravityForceMagnitude(particle1.getMass(),	
				particle2.getMass(), centerDistanceMagnitude);
		
		//calculate electric force
		double surfaceDistanceMagnitude = centerDistanceMagnitude - 
				particle1.getRadius() - particle2.getRadius();
		double electromagneticForceMagnitude = calcElectricForceMagnitude(particle1.getCharge(),
				particle2.getCharge(), surfaceDistanceMagnitude);
		
		double totalForceMagnitude = gravityForceMagnitude - electromagneticForceMagnitude;

		if (Math.abs(totalForceMagnitude) > Integer.MAX_VALUE / 1000) {
			System.out.println("particle had a force of: " + totalForceMagnitude + " N");
		}
		
		XYVector vectorAngle = distance.getAngle();

		double forceX = totalForceMagnitude * Math.sin(vectorAngle.getX())
				* Math.cos(vectorAngle.getY());
		double forceY = totalForceMagnitude * Math.sin(vectorAngle.getX())
				* Math.sin(vectorAngle.getY());
		double forceZ = totalForceMagnitude * Math.cos(vectorAngle.getX()) ;

		XYZVector force = new XYZVector(forceX, forceY, forceZ);
		XYZVector oppositeForce = force.copy();
		oppositeForce.scale(-1);

		particle1.getNetForce().add(force);
		particle2.getNetForce().add(oppositeForce);
	}

	/**
	 * Calculates the distance vector between two objects centers
	 * @param particle1
	 * @param particle2
	 * @return The vector of the distance between the two objects are the 
	 * direction needed
	 * to get from particle1 to particle2
	 */
	private XYZVector getDistance(Particle particle1, Particle particle2) {
		return XYZVector.subtract(particle2.getPosition(), particle1.getPosition());
	}

	/**
	 * Calculates the force magnitude using the formula: F = GMm / r^2
	 * @param mass1
	 * @param mass2
	 * @param distanceMagnitude
	 * @return
	 */
	private double calcGravityForceMagnitude(double mass1, double mass2, 
			double centerDistanceMagnitude) {
		return World.GRAVITATIONAL_CONSTANT * mass1 * mass2 / 
				Math.pow(centerDistanceMagnitude, 2);
	}

	/**
	 * Calculates the force magnitude using the formula: F = kQq / r^2
	 * @param charge1
	 * @param charge2
	 * @param surfaceDistanceMagnitude
	 * @return
	 */
	private double calcElectricForceMagnitude(double charge1, double charge2,
			double surfaceDistanceMagnitude) {
		return World.COULOMBS_LAW_CONSTANT * charge1 * charge2 /
				Math.pow(surfaceDistanceMagnitude, 2);
	}
	/**
	 * Calculates acceleration due to net force, then calculates velocity, 
	 * and then calculates movement all for the SHORTEST_TIME
	 */
	private void calculateDisplacement() {
		for (Particle tempParticle : particles) {
			tempParticle.calcNewVelocity();
			tempParticle.moveParticle();
		}
	}


	/**
	 * TODO correct overlap in collision
	 */
	private void calculateCollisions() {
		for (Particle particle : particles) {
			particle.setFinalVelocity(new XYZVector());
		}
		boolean[] particlesInCollision = new boolean[particles.size()];

		for (int i = 0; i < particles.size() - 1; i++) {
			Particle tempParticle = particles.get(i);
			for (int j = i + 1; j < particles.size(); j++) {
				Particle otherParticle = particles.get(j);
				if (tempParticle != otherParticle && getOverlapDistance(tempParticle, otherParticle) >= 0) {
					particlesInCollision[i] = true;
					particlesInCollision[j] = true;
					XYZVector[] velocityChanges = handleCollision(tempParticle, otherParticle);
					tempParticle.getFinalVelocity().add(velocityChanges[0]);
					otherParticle.getFinalVelocity().add(velocityChanges[1]);

				}
			}
		}

		for (int i = 0; i < particles.size(); i++) {
			if (particlesInCollision[i]) {
				particles.get(i).resetVelocity();
			}
		}
	}

	private double getOverlapDistance(Particle particle1, Particle particle2) {
		double centerDistance = getDistance(particle1, particle2).getMagnitude();
		double radiiSum = particle1.getRadius() + particle2.getRadius();
		return radiiSum - centerDistance;
	}



	/**
	 * 
	 * @param particle1
	 * @param particle2
	 */
	private XYZVector[] handleCollision(Particle particle1, Particle particle2) {
		//normalize everything relative to particle2
		//particle2 vectors are zero
		//calculate relative velocities
		XYZVector collisionVelocity = particle2.getInitialVelocity().copy();
		XYZVector particle1relativeVelocity = XYZVector.subtract(particle1.getInitialVelocity(), 
				particle2.getInitialVelocity());

		//calculate incident directions
		XYZVector incident1 = particle1relativeVelocity;
		XYZVector incident2 = incident1.copy();
		incident2.scale(-1);

		//calculate final velocities magnitudes
		XYZVector particle1FinalVelocity = getParticle1FinalVelocity(particle1, particle2, 
				particle1relativeVelocity);
		XYZVector particle2FinalVelocity = getParticle2FinalVelocity(particle1, particle2, 
				particle1relativeVelocity);

		particle1FinalVelocity.add(collisionVelocity);
		particle2FinalVelocity.add(collisionVelocity);

		//for non perfect elastic collisions
		//don't know if this is how the physics works
		particle1FinalVelocity.scale(particle1.getElasticity());
		particle1FinalVelocity.scale(particle2.getElasticity());

		return new XYZVector[]{particle1FinalVelocity, particle2FinalVelocity};
	}

	private XYZVector getParticle1FinalVelocity(Particle particle1, Particle particle2, XYZVector particle1relativeVelocity) {
		return getParticleFinalVelocity(particle1.getMass(), particle2.getMass(), particle1relativeVelocity,
				new XYZVector(), particle1.getPosition(), particle2.getPosition());
	}

	private XYZVector getParticle2FinalVelocity(Particle particle1, Particle particle2, XYZVector particle1relativeVelocity) {
		return getParticleFinalVelocity(particle2.getMass(), particle1.getMass(), new XYZVector(),
				particle1relativeVelocity, particle2.getPosition(), particle1.getPosition());
	}

	/**
	 * Calculates and returns the resulting velocity of the first mass using the
	 * formula for 2D collisions with two moving objects:<br>
	 * Vf = V1 - 2 * M2 / (M1 + M2) <V1 - V2, X1 - X2> / ||X1 - X2||^2 * (X1 - X2)
	 * @param mass1
	 * @param mass2
	 * @param velocity1
	 * @param velocity2
	 * @return
	 */
	private XYZVector getParticleFinalVelocity(double mass1, double mass2, XYZVector velocity1, 
			XYZVector velocity2, XYZVector position1, XYZVector position2) {
		XYZVector posDif = XYZVector.subtract(position1, position2);
		double c = XYZVector.subtract(velocity1, velocity2).dotProduct(posDif) / Math.pow(posDif.getMagnitude(), 2) *
				2 * mass2 / (mass1 + mass2);
		posDif.scale(c);
		return XYZVector.subtract(velocity1, posDif);
	}

	/**
	 * Calculate and returns the position of the Center of Mass of the
	 * World using the formula: X = (M1X1 + M2X2 + ...) / (M1 + M2 ...) and
	 * Y = (M1Y1 + M2Y2 + ...) / (M1 + M2 ...). (For graphical uses)
	 * @return the position of the center of mass
	 */
	public static XYZVector getCenterMass(List<Particle> particles) {
		double xDividend = 0;
		double yDividend = 0;
		double zDividend = 0;
		double divisor = 0;

		for (Particle temp : particles) {
			double mass = temp.getMass();
			double xPos = temp.getPosition().getX();
			double yPos = temp.getPosition().getY();
			double zPos = temp.getPosition().getZ();
			xDividend += mass * xPos;
			yDividend += mass * yPos;
			zDividend += mass * zPos;
			divisor += mass;
		}

		double x;
		double y;
		double z;

		try {
			x = xDividend / divisor;
			y = yDividend / divisor;
			z = zDividend / divisor;
		} catch (Exception e) { return null; }

		return new XYZVector(x, y, z);
	}

	/**
	 * Calculates and returns the kinetic energy of the whole system
	 * @return value of the kinetic energy
	 */
	public double getKineticEnergy() {
		double energy = 0;

		for (Particle temp : particles) {
			energy += temp.getKineticEnergy();
		}

		return energy;
	}

	/**
	 * Adds the potential energy between every possible pairs of particles
	 * @return
	 */
	public double getTotalPotentialEnergy() {
		double energy = 0;

		for (int i = 0; i < particles.size(); i++) {
			for (int j = i + 1; j < particles.size(); j++) {
				energy += getPotentialEnergy(particles.get(i), particles.get(j));
			}
		}

		return energy;
	}

	public double getPotentialEnergy(Particle particle1, Particle particle2) {
		return -GRAVITATIONAL_CONSTANT * particle1.getMass() * particle2.getMass()
				/ getDistance(particle1, particle2).getMagnitude();
	}


	public XYZVector getTotalMomentum() {
		XYZVector momentum = new XYZVector();

		for (int i = 0; i < particles.size(); i++) {
			momentum.add(particles.get(i).getMomentum());
		}
		return momentum;
	}
	/**
	 * Just something to get starting camera view. Need to improve with algorithm that
	 * centers around starting objects.
	 * @return
	 */
	public Rectangle getBounds() {
		return new Rectangle((int)(-3000),
				(int) (-3000),
				(int) (6000),
				(int) (6000));
	}

	public String toString() {
		String string = time + "\n";

		for (Particle temp : particles) {
			string += temp.toString() + "\n";
		}

		string += "*\n";
		
		return string;
	}
}
