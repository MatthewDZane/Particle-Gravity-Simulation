package display;

import java.awt.Graphics;

import javax.swing.JLabel;

import data.XYVector;
import data.XYZVector;
import helper.Utility;
import world.World;

public class TotalMomentumLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private World world;
	
	private XYZVector momentum;
	
	public XYZVector getKineticEnergy() { return momentum; }
	
	public void setWorld(World worldIn) { world = worldIn; }
	
	public TotalMomentumLabel(World worldIn) {
		super();
		world = worldIn;
		setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		momentum = world.getTotalMomentum();
		setText("Total Momentum:   (" + 
		Utility.getEngineeringNotation(momentum.getX(), 3) + "Ns" + ", " +
		Utility.getEngineeringNotation(momentum.getY(), 3) + "Ns"+ ", " +
		Utility.getEngineeringNotation(momentum.getZ(), 3) + "Ns)");
		//TODO: make the 3 decimals places configurable and develop superclass
	}
}
