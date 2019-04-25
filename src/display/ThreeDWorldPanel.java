package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.XYVector;
import data.XYZVector;
import entity.Particle;
import helper.Circle;
import helper.Direction;
import world.World;

public class ThreeDWorldPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Percentage of the height and width of the parent component
	 * Panel height and width equal parent component height and width
	 * times PCT_OF_PARENT divided by 100, respectively.
	 */
	public static final int PCT_OF_PARENT = 85;

	private World world;

	private ThreeDCamera camera = new ThreeDCamera(new XYZVector(0, 0, 0), new XYVector(Math.PI * 3 / 4, Math.PI * 3 / 4), 1, 1, 1, 1, 1);

	public ThreeDCamera getCamera() { return camera; }
	
	public void setWorld(World worldIn) { world = worldIn; }

	/**
	 * The current directions that the camera is moving in
	 */
	//private ArrayList<Direction> directions = new ArrayList<Direction>();
	
	//Units in m / sec
	private int scrollSpeed = 2000;
	
	private CameraMovementHandler cameraMovementHandler = new CameraMovementHandler();
	private Timer cameraMovementTimer = new Timer(60000 / scrollSpeed, cameraMovementHandler);

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean forward;
	private boolean backward;

	/**
	 * Initializes a JPanel with a line border and initializes textures
	 * @param worldIn - World object the panel will be displaying from
	 */
	public ThreeDWorldPanel(World worldIn) {
		super();

		//add listeners
		addKeyListener(new KeyHandler());
		addMouseListener(new MouseHandler());

		world = worldIn;

		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		cameraMovementTimer.start();
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintParticles(g);
		paintCenterMass(g);
	}

	/**
	 * Paints all of the particles in the world
	 * @param g
	 */
	private void paintParticles(Graphics g) {
		for (Particle tempParticle : world.getParticles()) {
			paintParticle(tempParticle, g);
		}
	}

	private void paintParticle(Particle particle, Graphics g) {
		if (isInView(particle)) {
			XYZVector distance = 
					XYZVector.subtract(particle.getPosition(), camera.getPosition());

			double apparentWidth = 2 * Math.atan(particle.getRadius() / Math.sqrt(
					Math.pow(distance.getX(), 2) + Math.pow(distance.getZ(), 2)));
			double apparentHeight = 2 * Math.atan(particle.getRadius() / Math.sqrt(
					Math.pow(distance.getY(), 2) + Math.pow(distance.getZ(), 2)));
			double apparentPositionX = Math.atan(distance.getX() / distance.getZ());
			double apparentPositionY = Math.atan(distance.getY() / distance.getZ());

			int width = (int) (getWidth() * apparentWidth / camera.getFieldOfView().getX());
			int height = (int) (getHeight() * apparentHeight / camera.getFieldOfView().getY());
			int positionX = (int)(getWidth() * apparentPositionX / camera.getFieldOfView().getX());
			int positionY = (int)(getHeight() * apparentPositionY / camera.getFieldOfView().getY());

			g.fillOval(getWidth() / 2 + positionX - width / 2, getHeight() / 2 - positionY - height / 2, width, height);
		}
	}

	private boolean isInView(Particle particle) {
		return particle.getPosition().getZ() - camera.getPosition().getZ() >= 0;
	}

	private void paintCenterMass(Graphics g) {
		g.setColor(Color.RED);
		XYZVector position = World.getCenterMass(world.getParticles());
		if (position != null) {
			XYZVector distance = 
					XYZVector.subtract(position, camera.getPosition());
			if (position.getZ() - camera.getPosition().getZ() >= 0) {
				double apparentPositionX = Math.atan(distance.getX() / distance.getZ());
				double apparentPositionY = Math.atan(distance.getY() / distance.getZ());

				int width = (int) (getWidth() * .025);
				int height = (int) (getHeight() * .025);
				int positionX = (int)(getWidth() * apparentPositionX / camera.getFieldOfView().getX());
				int positionY = (int)(getHeight() * apparentPositionY / camera.getFieldOfView().getY());

				g.fillOval(getWidth() / 2 + positionX - width / 2, getHeight() / 2 - positionY - height / 2, width, height);
			}
		}
	}

	/**
	 * Moves the camera either up, down, left, right, forward, or backward
	 * @author Matthew Zane
	 * @version 1.1
	 * @since 2017-09-15
	 */
	private class KeyHandler implements KeyListener {

		/**
		 * Adds new direction to direction list if not already
		 * in it using the arrow keys or center the camera, if 
		 * the space bar is pressed
		 * @Override
		 */
		public void keyPressed(KeyEvent e) {

			int keyCode = e.getKeyCode();
			switch (keyCode) { 
			case KeyEvent.VK_UP:
				forward = true;
				break;
			case KeyEvent.VK_DOWN:
				backward = true;
				break;
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				break;
			case KeyEvent.VK_SPACE:
				up = true;
				break;
			case KeyEvent.VK_SHIFT:
				down = true;
				break;
			}

			//cameraMovementHandler.actionPerformed(null);
		}

		/**
		 * Removes direction from list once key has been released
		 * @Override
		 */
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			//System.out.println("released");
			switch(keyCode) { 
			case KeyEvent.VK_UP:
				forward = false;
				//System.out.println("released " + Direction.FORWARD);
				break;
			case KeyEvent.VK_DOWN:
				backward = false;
				//System.out.println("released " + Direction.BACKWARD);
				break;
			case KeyEvent.VK_LEFT:
				left = false;
				//System.out.println("released " + Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT :
				right = false;
				//System.out.println("released " + Direction.RIGHT);
				break;
			case KeyEvent.VK_SPACE :
				up = false;
				//System.out.println("released " + Direction.UP);
				break;
			case KeyEvent.VK_SHIFT :
				down = false;
				//System.out.println("released " + Direction.DOWN);
				break;
			}

			//cameraMovementHandler.actionPerformed(null);
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}

	/**
	 * Moves the camera at a set speed using a Timer
	 * @author Matthew Zane
	 * @version 1.1
	 * @since 2017-09-15
	 */
	private class CameraMovementHandler implements ActionListener {

		/**
		 * Moves and updates Camera
		 */
		public void actionPerformed(ActionEvent arg0) {
			//System.out.println("Checking");
			move();
		}

		/**
		 * Moves camera depending on directions
		 */
		public void move() {
			//System.out.println("Moving");
			if (up) {
				moveUp();
				//System.out.println(Direction.UP);
			}
			if (down) {
				moveDown();
				//System.out.println(Direction.DOWN);
			}
			if (left) {
				moveLeft();
				//System.out.println(Direction.LEFT);
			}
			if (right) {
				moveRight();
				//System.out.println(Direction.RIGHT);
			}
			if (forward) {
				moveForward();
				//System.out.println(Direction.FORWARD);
			}
			if (backward) {
				moveBackward();
				//System.out.println(Direction.BACKWARD);
			}
			//if (directions.isEmpty()) {
			//	System.out.println("Empty");
			//}
		}

		/**
		 * Moves camera up
		 */
		public void moveUp()  {
			camera.moveUp();
		}

		/**
		 * Moves camera down
		 */
		public void moveDown() {
			camera.moveDown();
		}

		/**
		 * Moves camera left
		 */
		public void moveLeft() {
			camera.moveLeft();
		}

		/**
		 * Moves camera right
		 */
		public void moveRight() {
			camera.moveRight();
		}

		/**
		 * Moves camera right
		 */
		public void moveForward() {
			camera.moveForward();
		}
		/**
		 * Moves camera right
		 */
		public void moveBackward() {
			camera.moveBackward();
		}
	}

	/**
	 * Handles mouse clicks on panel
	 * @author Matthew Zane
	 * @version 1.1
	 * @since 2017-09-15
	 */
	public class MouseHandler implements MouseListener {

		/**
		 * Panel requests focus when clicked on
		 * @Override
		 */
		public void mouseClicked(MouseEvent e) {
			if (getMousePosition() != null) {
				requestFocus();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}
}
