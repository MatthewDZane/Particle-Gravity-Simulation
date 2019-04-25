package runner;

import java.util.List;

import display.Display;
import helper.ParticleFileParser;
import helper.Timer;
import world.World;

public class GravitySimulator {
	private String pathname;

	public String getPathname() { return pathname; }

	public void setPathname(String pathnameIn) { pathname = pathnameIn; }

	public static void main(String [] args) {
		//parse command line arguments
		//for now just accept one parameter: config file

		//instantiate gravity simulator
		GravitySimulator gs = new GravitySimulator();
		gs.setPathname(args[0]);

		//configure gravity simulator

		//call run()
		gs.run();
	}

	/**
	 * Requires pathname to be set.
	 */
	public void run() {
		try {
			
			List<World> worlds = ParticleFileParser.parseUniverse(pathname);
			Display display = new Display(worlds.get(worlds.size() - 1));
			display.init();
			display.getWorld().start();

			worlds.clear();
			
			boolean isDone = false;

			double frameCap = 1.0 / 1.0;

			double frameTime = 0;
			int frames = 0;

			double time = Timer.getTime();
			double unprocessed = 0;
			while (!isDone) {
				boolean canRender = false;

				double time2 = Timer.getTime();
				double passed = time2 - time;
				unprocessed += passed;
				frameTime += passed;

				time = time2;

				while (unprocessed >= frameCap) {
					unprocessed -= frameCap;
					canRender = true;


					if (frameTime >= 1.0) {
						frameTime = 0;
						//System.out.println("FPS: " + frames);
						frames = 0;
					}
				}
				if (canRender) {
					display.repaint();
					frames++;
				}

			}
		} catch(Exception e) {
			System.out.println("There was an error: " + e.getMessage());
		}
	}
}
