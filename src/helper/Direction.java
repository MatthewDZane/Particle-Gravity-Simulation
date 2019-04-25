package helper;

/**
 * Defines four directions.
 * @author matth
 *
 */
public enum Direction {
	UP, DOWN, LEFT, RIGHT, FORWARD, BACKWARD, INVALID; 
	
	public static final Direction[] DIRECTIONS = {UP, DOWN, LEFT, RIGHT, FORWARD, BACKWARD}; 
	
	public static Direction getRandomDirection() {
		int randomInt = (int)((Math.random()*6) + 1);
		switch (randomInt) {
		case 1: return Direction.UP;
		case 2: return Direction.DOWN;
		case 3: return Direction.LEFT;
		case 4: return Direction.RIGHT;
		case 5: return Direction.FORWARD;
		case 6: return Direction.BACKWARD;
		default: return Direction.INVALID;
		}
	}
	
	public static Direction[] getDirections() { return DIRECTIONS; }
}