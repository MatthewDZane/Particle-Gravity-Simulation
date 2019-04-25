package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import entity.Particle;
import world.World;

/**
 * Parses files of a certain format into lists of particle objects.
 * @author Matthew Zane
 *
 */
public class ParticleFileParser {
	
	/**
	 * A Universe is a list of worlds, which are just the same world in different time moments
	 * The end of a world is marked with a "*".
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public static List<World> parseUniverse(String pathname) throws Exception {
		File file = new File(pathname);
		
		//check for file existence
		if (!file.exists()) {
			throw new Exception("Config file " + pathname + " does not exist.");
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<World> worlds = new ArrayList<World>();
		
		String line;
		
		
		while ((line = br.readLine()) != null) {
			//add the time variable to the world
			String world = line + "\n";
			//add the particles to the world
			while ((line = br.readLine()) != null && !line.equals("*")) {
				world += line + "\n";
			}
			
			worlds.add(parseWorld(world));
		}
		
		br.close();
		
		return worlds;
	}
	
	public static World parseWorld(String line) throws Exception {
		String[] fields = line.split("\n");
		
		World world = new World();
		world.setTime(Double.parseDouble(fields[0]));
		
		ArrayList<Particle> particles = new ArrayList<Particle>();
		
		for (int i = 1; i < fields.length; i++) {
			Particle particle = parseParticle(fields[i]);
			
			if (particle != null) {
				particles.add(particle);
			}
		}
		
		world.setParticles(particles);
		
		return world;
	}
	/**
	 * Returns a list of particles from a comma-delimited file. 
	 * Ignores lines that start with "#". Skips any unparsable lines.
	 * Fields are (in order): <br>
	 * 	Mass (double)<br>
	 * 	Radius (double)<br>
	 * 	x-component of velocity (double)<br>
	 * 	y-component of velocity (double)<br>
	 * 	x-component of the particle's position (double)<br>
	 * 	y-component of the particle's position (double)
	 * @param pathname Full pathname of the config file.
	 * @return list of particles
	 * @exception Exception when file does not exist.
	 */
	public static List<Particle> parseParticles(String pathname) throws Exception {
		File file = new File(pathname);
		List<Particle> particles = new ArrayList<Particle>();
		//check for file existence
		if (!file.exists()) {
			throw new Exception("Config file " + pathname + " does not exist.");
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#") || line.startsWith("*")) {
				continue;
			}
			
			Particle particle = parseParticle(line);
			
			if (particle != null) {
				particles.add(particle);
			}
		}
		br.close();
		return particles;
	}

	/**
	 * Returns null if particle could not be instantiated
	 * @param particleText
	 * @return
	 */
	public static Particle parseParticle(String particleText) {
		// comma-delimited
		String[] fields = particleText.split(",");
		Particle particle = null;
		try {
			particle = new Particle(
					Double.parseDouble(fields[0]),
					Double.parseDouble(fields[1]),
					Double.parseDouble(fields[2]),
					Double.parseDouble(fields[3]),
					Double.parseDouble(fields[4]),
					Double.parseDouble(fields[5]),
					Double.parseDouble(fields[6]),
					Double.parseDouble(fields[7]));
		} catch (Exception e) {		
			System.out.println("Cannot instantiate Particle from " + particleText);
		}
		
		return particle;
	}
}
