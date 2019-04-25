package display;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import world.World;
import helper.PlayBack.ReplayListener;
/**
 * Main JFrame from which graphics will be displayed. <br>
 * <br>
 * How to use:<br>
 * 	Display d = new Display(world);<br>
 * 	d.int();
 * @author Matthew Zane
 * @version 1.1
 * @since 2017-09-15
 */
public class Display extends JFrame {
	
	private World world;
	
	JPanel northPanel = new JPanel();
	JPanel southPanel = new JPanel();
	JPanel westPanel = new JPanel();
	JPanel eastPanel = new JPanel();
	
	//private WorldPanel panel;
	private ThreeDWorldPanel threeDPanel;
	ClockLabel clockLabel;
	KineticEnergyLabel keLabel;
	PotentialEnergyLabel peLabel;
	TotalEnergyLabel totalEnergyLabel;
	TotalMomentumLabel totalMomentumLabel;
	
	
	

	public World getWorld() { return world; }
	
	//public WorldPanel getWorldPanel() { return panel; }

	public void setWorld(World worldIn) { 
		world = worldIn; 
		threeDPanel.setWorld(world);
		clockLabel.setWorld(world);
		keLabel.setWorld(world);
		peLabel.setWorld(world);
		totalMomentumLabel.setWorld(world);
	}

	public Display() {
		super();
	}

	public Display(World worldIn) {
		super();
		world = worldIn;
	}

	public void init() throws Exception {
		//Display is utilizing the MigLayout
		setLayout(new MigLayout());

		//Panel from which the world will be displayed
		if (world == null) {
			throw new Exception("World was not instantiated");
		}
		
		threeDPanel = new ThreeDWorldPanel(world);
		clockLabel = new ClockLabel(world);
		keLabel = new KineticEnergyLabel(world);
		peLabel = new PotentialEnergyLabel(world);
		totalEnergyLabel = new TotalEnergyLabel(keLabel, peLabel);
		totalMomentumLabel = new TotalMomentumLabel(world);
		
		//panel.createCamera();
		clockLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		keLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		peLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		totalEnergyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		totalMomentumLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//WorldPanel centered in JFrame with a height and width
		//equal to a certain percent of the Display
		//add(panel, "w " + WorldPanel.PCT_OF_PARENT + "%, h " + 
				//WorldPanel.PCT_OF_PARENT + "%, align center");
		add(threeDPanel, "w " + WorldPanel.PCT_OF_PARENT + "%, h " + 
				WorldPanel.PCT_OF_PARENT + "%, align center");

		//Border Panels placed around World Panel
		//May have future use
		
		
		add(northPanel, "north, h " + (100 - WorldPanel.PCT_OF_PARENT) / 2 + "%");
		add(southPanel, "south, h " + (100 - WorldPanel.PCT_OF_PARENT) / 2 + "%");
		add(westPanel, "west, w " + (100 - WorldPanel.PCT_OF_PARENT) / 2 + "%");
		add(eastPanel, "east, w " + (100 - WorldPanel.PCT_OF_PARENT) / 2 + "%");
		
		northPanel.setLayout(new MigLayout());
		northPanel.add(clockLabel, "h " + 100 + "%, w " + 20 + "%");
		northPanel.add(keLabel, "h " + 100 + "%, w " + 20 + "%");
		northPanel.add(peLabel, "h " + 100 + "%, w " + 20 + "%");
		northPanel.add(totalEnergyLabel, "h " + 100 + "%, w " + 20 + "%");
		northPanel.add(totalMomentumLabel, "h " + 100 + "%, w " + 20 + "%");
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);

		threeDPanel.requestFocus();
		//panel.requestFocus();
	}
	
	public void addReplayButton(ReplayListener replayListenerIn) {
		JButton replayButton = new JButton("Replay");
		replayButton.addActionListener(replayListenerIn);
		southPanel.add(replayButton);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
