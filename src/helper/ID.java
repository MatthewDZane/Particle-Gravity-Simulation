package helper;

/**
 * Provide an unique ID.
 * @author matth
 *
 */
public class ID {
	public static int idCount = 0;
	
	public static int getID() { return idCount++; }
}
