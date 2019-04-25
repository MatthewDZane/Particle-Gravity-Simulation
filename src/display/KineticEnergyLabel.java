package display;

import java.awt.Graphics;

import javax.swing.JLabel;

import helper.Utility;
import world.World;

public class KineticEnergyLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private World world;
	
	private double kineticEnergy;
	
	public double getKineticEnergy() { return kineticEnergy; }
	
	public void setWorld(World worldIn) { world = worldIn; }
	
	public KineticEnergyLabel(World worldIn) {
		super();
		world = worldIn;
		setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		kineticEnergy = world.getKineticEnergy();
		setText("Kinetic Energy:   " + Utility.getEngineeringNotation(kineticEnergy, 3) + "J");
		//TODO: make the 3 decimals places configurable and develop superclass
	}
}
