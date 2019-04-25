package display;

import java.awt.Graphics;
import javax.swing.JLabel;

import helper.Utility;
import world.World;

public class ClockLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private World world;

	public void setWorld(World worldIn) { world = worldIn; }
	
	public ClockLabel(World worldIn) {
		super();
		world = worldIn;
		setHorizontalAlignment(JLabel.CENTER);
	}

	public void paint(Graphics g) {
		super.paint(g);
		double time = world.getTime();
		if (time > 31556926.08) {
			time /= 31556926.08;
			setText("Time:   " + Utility.getEngineeringNotation(time, 3) + "Y");
		}
		else if (time > 86400) {
			time /= 86400;
			setText("Time:   " + Utility.getEngineeringNotation(time, 3) + "d");
		}
		else {
			setText("Time:   " + Utility.getEngineeringNotation(time, 3) + "s");
		}
	}
}
