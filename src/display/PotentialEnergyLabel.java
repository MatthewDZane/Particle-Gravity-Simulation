package display;

import java.awt.Graphics;

import javax.swing.JLabel;

import helper.Utility;
import world.World;

public class PotentialEnergyLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private World world;
	
	private double potentialEnergy;
	
	public double getPotentialEnergy() { return potentialEnergy; }
	
	public void setWorld(World worldIn) { world = worldIn; }

	public PotentialEnergyLabel(World worldIn) {
		super();
		world = worldIn;
		setHorizontalAlignment(JLabel.CENTER);
	}

	public void paint(Graphics g) {
		super.paint(g);
		potentialEnergy = world.getTotalPotentialEnergy();
		setText("Potential Energy:   " + Utility.getEngineeringNotation(potentialEnergy, 3) + "J");
	}
}

