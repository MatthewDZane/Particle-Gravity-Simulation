package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.XYVector;
import data.XYZVector;
import entity.Particle;

public class WorldGenerator {
	private static final String FILENAME = "C:\\Users\\matth\\eclipse-workspace\\GravitySimulation1.3\\src\\config\\record.txt";
	private static final int NUM_PARTICLES = 100;
	private static final int bounds = 200;
	private static final Random rand = new Random();

	/**
	 * Writes to a specified text file in the proper format.
	 * Uses method generateWorld to populate world with entities.
	 */
	public static void main(String [] args) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			String content = "0.0\n" + convertToString() + "*\n";

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(content);
			
		} catch (IOException e) { e.printStackTrace(); } 
		finally {
			try {

				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();

			} catch (IOException ex) {	ex.printStackTrace();	}
		}
		System.out.print("World Generator Done!");
	}

	/**
	 * Converts a list of Particles to the proper text format for a 
	 * config file.
	 * @return
	 */
	public static String convertToString() {
		ArrayList<Particle> particles = (ArrayList<Particle>) generateWorld();
		String text = "";

		for (Particle temp : particles) {
			text += temp.toString() + "\n"; 
		}

		return text;
	}

	/**
	 * Generates an ArrayList of Particles to populate a world using an algorithm.
	 * @return List of Particles
	 */
	public static List<Particle> generateWorld() {
		ArrayList<Particle> particles = new ArrayList<Particle>();
		for (int i = 0; i < NUM_PARTICLES; i++) {
			Particle particle;
			double mass = getRandomDouble(.1, 1);

			do {
				double radius = getRandomDouble(1, 10);

				XYZVector center;
				double angleX = getRandomDouble(0, 2 * Math.PI);
				double angleY = getRandomDouble(0, 2 * Math.PI);
				double distance = getRandomDouble(0, bounds);
				double x = distance * Math.sin(angleX) * Math.cos(angleY);
				double y = distance * Math.sin(angleX) * Math.sin(angleY);
				double z = distance * Math.cos(angleX);

				center = new XYZVector(x, y, z);

				particle = new Particle(mass, radius, 0, 0, 0, center.getX(), center.getY(), center.getZ() + 300);
			} while (isOverlapping(particle, particles));
			//uniform distribution
			/*

		do {
			center = new XYVector(getRandomDouble(-bounds, bounds),
								  getRandomDouble(-bounds, bounds);
		} while (center.getMagnitude() > bounds);
			 */
			particles.add(particle);
		}


		return particles;
	}

	public static double getRandomDouble(double min, double max) {
		return min + (max - min) * rand.nextDouble();
	}

	public static boolean isOverlapping(Particle particle, ArrayList<Particle> particles) {
		for (int i = 0; i < particles.size(); i++) {
			Particle otherParticle = particles.get(i);
			if (getOverlapDistance(particle, otherParticle) >= 0) {
				return true;
			}
		}
		return false;
	}

	private static double getOverlapDistance(Particle particle1, Particle particle2) {
		double centerDistance = getDistance(particle1, particle2).getMagnitude();
		double radiiSum = particle1.getRadius() + particle2.getRadius();
		return radiiSum - centerDistance;
	}

	private static XYZVector getDistance(Particle particle1, Particle particle2) {
		return XYZVector.subtract(particle2.getPosition(), particle1.getPosition());
	}
}
