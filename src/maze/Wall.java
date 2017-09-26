package maze;

/**
 * Class of wall object used in Cell objects.
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * 
 * The solvers require that same wall object are shared by two cell objects next to the wall.
 */
public class Wall {
	/**
	 * True if the wall is present in the maze. Otherwise false.
	 */
	public boolean present = true;
	
	/**
	 * Used only for visualization functions.
	 */
	public boolean drawn = false;

} // end of class Wall
