package display;

import data.XYVector;
import data.XYZVector;

/**
 * X-axis: + right and - left
 * Y-axis: + up and - down
 * Z-axis: + image and - at viewer
 * @author Matthew Zane
 *
 */
public class ThreeDCamera {
	public XYZVector position;
	
	/**
	 * angular field of view of the camera. Using human field of view.
	 * First value is horizontal field of view and Second value is vertical field of view.
	 */
	public XYVector fieldOfView;
	
	/**
	 * The angle above the xz-plane in which the camera is facing
	 */
	public double pitch;
	
	/**
	 * The angle from the positive x-axis in the xz-plane in which the
	 * camera is facing
	 */
	public double yaw;
	
	/**
	 * The angle from the positive y-axis in the yz-plane in which the 
	 * vertical axis of the camera is facing
	 */
	public double roll;
	
	public double scrollSpeed = 1;
	public double turnSpeed = 1;
	
	public XYZVector getPosition() { return position; }
	public XYVector getFieldOfView() { return fieldOfView; }
	public double getPitch() { return pitch; }
	public double getYaw() { return yaw; }
	public double getRoll() { return yaw; }
	public double getScrollSpeed() { return scrollSpeed; }
	public double getTurnSpeed() { return turnSpeed; }
	
	public void setPosition(XYZVector positionIn) { position = positionIn; }
	public void setFieldOfView(XYVector fieldOfViewIn) { fieldOfView = fieldOfViewIn; }
	public void setPitch(double pitchIn) { pitch = pitchIn; }
	public void setYaw(double yawIn) { yaw = yawIn; }
	public void setRoll(double rollIn) { roll = rollIn; }
	public void setScrollSpeed(double scrollSpeedIn) { scrollSpeed = scrollSpeedIn; }
	public void setTurnSpeed(double turnSpeedIn) { turnSpeed = turnSpeedIn; }
	
	public ThreeDCamera(XYZVector positionIn, XYVector fieldOfViewIn, double pitchIn, 
			double yawIn, double rollIn, double scrollSpeedIn, double turnSpeedIn) {
		position = positionIn;
		fieldOfView = fieldOfViewIn;
		pitch = pitchIn;
		yaw = yawIn;
		roll = rollIn;
		scrollSpeed = scrollSpeedIn;
		turnSpeed = turnSpeedIn;
	}
	
	public void moveUp() {
		position.add(new XYZVector(0, scrollSpeed, 0));
	}
	
	/**
	 * Moves cameraPosition down by 1m
	 */
	public void moveDown() {
		position.add(new XYZVector(0, -scrollSpeed, 0));
	}

	/**
	 * Moves cameraPosition left by 1m
	 */
	public void moveLeft() {
		position.add(new XYZVector(-scrollSpeed, 0, 0));
	}

	/**
	 * Moves cameraPosition right by 1m
	 */
	public void moveRight() {
		position.add(new XYZVector(scrollSpeed, 0, 0));
	}

	/**
	 * Moves cameraPosition down by 1m
	 */
	public void moveForward() {
		position.add(new XYZVector(0, 0, scrollSpeed));
	}

	/**
	 * Moves cameraPosition down by 1m
	 */
	public void moveBackward() {
		position.add(new XYZVector(0, 0, -scrollSpeed));
	}

	
}
