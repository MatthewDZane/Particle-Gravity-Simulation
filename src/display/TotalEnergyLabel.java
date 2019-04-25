package display;

import java.awt.Graphics;

import javax.swing.JLabel;

import helper.Utility;

public class TotalEnergyLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private KineticEnergyLabel kineticEnergyLabel;
	private PotentialEnergyLabel potentialEnergyLabel;
	
	public TotalEnergyLabel(KineticEnergyLabel kineticEnergyLabelIn, 
			PotentialEnergyLabel potentialEnergyLabelIn) {
		super();
		kineticEnergyLabel = kineticEnergyLabelIn;
		potentialEnergyLabel = potentialEnergyLabelIn;
		setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		double totalEnergy = kineticEnergyLabel.getKineticEnergy() 
				+ potentialEnergyLabel.getPotentialEnergy();
		setText("Total Energy:   " + Utility.getEngineeringNotation(totalEnergy, 3) + "J");
	}
}
