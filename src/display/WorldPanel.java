package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.XYVector;
import data.XYZVector;
import entity.Particle;
import helper.Circle;
import helper.Direction;
import helper.Sphere;
import world.World;

/**
 * JPanel from which graphics of the World will be drawn 
 * directly on. Uses keyboard and mouse for input.
 * @author Matthew Zane
 * @version 1.1
 * @since 2017-09-30
 */
public class WorldPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Percentage of the height and width of the parent component
	 * Panel height and width equal parent component height and width
	 * times PCT_OF_PARENT divided by 100, respectively.
	 */
	public static final int PCT_OF_PARENT = 85;

	private World world;

	private Camera camera;
	
	public Camera getCamera() { return camera; }
		
	/**
	 * The current directions that the camera is moving in
	 */
	private ArrayList<Direction> directions = new ArrayList<Direction>();

	private CameraMovementHandler cameraMovementHandler = new CameraMovementHandler();
	private Timer scrollTimer;
	
	public static final double SPACE_SCALE = .5;
	
	//Units in dm / sec
	private int scrollSpeed = 20;

	//Don't know what the units are
	private int zoomSpeed = 2;
	
	public int getScrollSpeed() { return scrollSpeed; }
	
	/**
	 * Initializes a JPanel with a line border and initializes textures
	 * @param worldIn - World object the panel will be displaying from
	 */
	public WorldPanel(World worldIn) {
		super();
				
		//add listeners
		addMouseWheelListener(new MouseWheelActionListener());
		addKeyListener(new KeyHandler());
		addMouseListener(new MouseHandler());

		world = worldIn;

		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		scrollTimer = new Timer(1000 / scrollSpeed, cameraMovementHandler);

		world.start();
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
			XYZVector position = tempParticle.getPosition().copy();
			Circle circle = new Circle(new XYVector(position.getX(), position.getY()), 
					tempParticle.getRadius());
			Circle scaledCircle = scaleCircle(circle);
			paintCircle(g, scaledCircle.getPosition(), scaledCircle.getRadius());
		}
	}

	/**
	 * Paint an oval with equal width and length
	 * @param g Graphics of the panel
	 * @param position center of the circle
	 * @param radius length of radius of the circle
	 */
	private void paintCircle(Graphics g, XYVector position, double radius) {
		g.fillOval((int) Math.round(position.getX() - radius), 
				(int) Math.round(position.getY() - radius),
				(int) Math.round(2 * radius), (int) Math.round(2 * radius));
	}

	/**
	 * Returns a scaled position so that it will fit in the camera view
	 * @param position position to be scaled
	 * @return scaled position
	 */
	private Circle scaleCircle(Circle circle) {
		XYVector position = circle.getPosition();
		int xCoord = (int)(camera.getScaleX() * (position.getX() * SPACE_SCALE - camera.getXalign()));
		int yCoord = (int)(camera.getScaleY() * (-position.getY() * SPACE_SCALE - camera.getYalign()));
		int radius = (int)(camera.getScaleY() * (circle.getRadius() * SPACE_SCALE));
		return new Circle(new XYVector(xCoord, yCoord), radius);
	}

	private void paintCenterMass(Graphics g) {
		g.setColor(Color.RED);
		XYZVector position = World.getCenterMass(world.getParticles());
		if (position != null) {
			Circle circle = new Circle(new XYVector(position.getX(), position.getY()), 1);
			Circle scaledCircle = scaleCircle(circle);
			paintCircle(g, scaledCircle.getPosition(), 4);
		}
		//paintCircle(g, new XYVector(408, 408),82);
	}

	/**
	 * Used to create the camera after the WorldPanel has been created
	 */
	public void createCamera() {
		camera = new Camera(world, this);
		camera.center();
		scrollTimer.start();
	}

	/**
	 * Zooms the camera in or out depending scroll direction
	 * @author Matthew Zane
	 * @version 1.1
	 * @since 2017-09-15
	 */
	private class MouseWheelActionListener implements MouseWheelListener {

		/**
		 * Uses mouseWheelEvent to either zoom in or out
		 * @Override
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			if (notches < 0) {
				camera.zoom(-zoomSpeed);
			}
			else {
				camera.zoom(zoomSpeed);
			}
		}
	}

	/**
	 * Moves the camera either up, down, left, or right
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
				if (!directions.contains(Direction.UP)) {
					directions.add(Direction.UP);
				}
				break;
			case KeyEvent.VK_DOWN:
				if (!directions.contains(Direction.DOWN)) {
					directions.add(Direction.DOWN);
				}
				break;
			case KeyEvent.VK_LEFT:
				if (!directions.contains(Direction.LEFT)) {
					directions.add(Direction.LEFT);
				}
				break;
			case KeyEvent.VK_RIGHT :
				if (!directions.contains(Direction.RIGHT)) {
					directions.add(Direction.RIGHT);
				}
				break;
			case KeyEvent.VK_SPACE:
				camera.center();
				break;
			}

			cameraMovementHandler.actionPerformed(null);
		}

		/**
		 * Removes direction from list once key has been released
		 * @Override
		 */
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			//System.out.println("released");
			switch( keyCode ) { 
			case KeyEvent.VK_UP:
				directions.remove(Direction.UP);
				break;
			case KeyEvent.VK_DOWN:
				directions.remove(Direction.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				directions.remove(Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT :
				directions.remove(Direction.RIGHT);
				break;
			}

			cameraMovementHandler.actionPerformed(null);
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
			move();
			camera.updateCamera();
		}

		/**
		 * Moves camera depending on directions
		 */
		public void move() {
			//System.out.println("Moving");
			if (directions.contains(Direction.UP)) {
				moveUp();
			//	System.out.println(Direction.UP);
			}
			if (directions.contains(Direction.DOWN)) {
				moveDown();
			//	System.out.println(Direction.DOWN);
			}
			if (directions.contains(Direction.LEFT)) {
				moveLeft();
			//	System.out.println(Direction.LEFT);
			}
			if (directions.contains(Direction.RIGHT)) {
				moveRight();
			//	System.out.println(Direction.RIGHT);
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
