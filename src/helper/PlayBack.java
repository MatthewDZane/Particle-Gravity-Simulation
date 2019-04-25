package helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import display.Display;
import world.World;

public class PlayBack {
	private static final String FILENAME = "C:\\Users\\matth\\eclipse-workspace\\GravitySimulation1.4\\src\\config\\record.txt";
	
	private ReplayListener replayListener = new ReplayListener();
	private boolean canReplay = false;
	
	public ReplayListener getReplayListener() { return replayListener; }
	public boolean getCanReplay() { return canReplay; }
	
	public void setCanReplay(boolean canReplayIn) { canReplay = canReplayIn; }
	
	public static void main(String[] args) {
		PlayBack playBack = new PlayBack();
		
		Display display = null;
		List<World> universe = null;
		try {
			System.out.println("Loading");
			universe = ParticleFileParser.parseUniverse(FILENAME);


			display = new Display(universe.get(0));
			display.init();
			display.addReplayButton(playBack.getReplayListener());

			//to do: make button to quit (change the boolean value in the while loop)
			while (true) {
				for (World moment: universe) {
					display.setWorld(moment);
					display.repaint();
					Thread.sleep(100);
				}
				System.out.println("Recording Done!");
				playBack.setCanReplay(false);
				
				//don't know if this is how you do it
				while (!playBack.getCanReplay()) {
					Thread.sleep(1000);
				}
				System.out.println("Restarting");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Something went wrong!");
		}


	}

	public class ReplayListener implements ActionListener {
		public ReplayListener() {}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			canReplay = true;
		}
	}
}
