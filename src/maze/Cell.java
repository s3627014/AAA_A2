package maze;
/**
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * 
 * Object used in map matrix in maze.Maze. 
 * The solvers require that all neighbor cell objects are assigned correctly
 * and same wall object are shared by two cell objects next to the wall.
 */
public class Cell {
	/**
	 * row coordinate
	 */
	public int r;
	
	/**
	 * column coordinate
	 */
	public int c;
	
	/**
	 * wall[i] is the wall on direction i of the cell
	 */
	public Wall wall[] = {null, null, null, null, null, null};
	
	/**
	 * neigh[i] is the neighbor cell of direction i
	 */
	public Cell neigh[] = {null, null, null, null, null, null};
	
	/**
	 * the other end of a tunnel connected to it, null if not or not a tunnel maze
	 */
	public Cell tunnelTo = null;
	
	/**
	 * construct cell of position (r, c) in the maze
	 * @param r	Row coordinate in the maze
	 * @param c Column coordinate in the maze
	 */
	public Cell(int r, int c) {
		this.r = r;
		this.c = c;
	} // end of Cell()
	
	/**
	 * default constructor
	 */
	public Cell() {
		this(0, 0);
	} // end of Cell()
	
} // end of class Cell
