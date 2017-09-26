package maze;

import java.util.List;

/**
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 *
 * Abstract class of Maze defines the interface 
 * and a set of constants common for every type of maze in the assignment.
 * solvers require that:
 * all cells are stored in matrix map[][], 
 * map[r][c].r == r and map[r][c].c == c,  
 * all neighbor cell objects are assigned correctly for each cell, 
 * and same wall object are shared by two cell objects next to the wall
 */
public abstract class Maze {
	
	/**
	 * constants which are common to any type of mazes
	 */
	// types of maze;
	public final static int NORMAL = 0;
	public final static int TUNNEL = 1;
	public final static int HEX = 2;
	//	directions used for indices
	public final static int EAST = 0;
	public final static int NORTHEAST = 1;
	public final static int NORTHWEST = 2;
	public final static int NORTH = 2;
	public final static int WEST = 3;
	public final static int SOUTHWEST = 4;
	public final static int SOUTHEAST = 5;
	public final static int SOUTH = 5;
	public final static int NUM_DIR = 6;
	// used for move along a deriction, for both square and hexagon
	// e.g., the northeast neighbor of map[r][c] would be map[r + deltaR[NORTHEAST][c + deltaC[NORTHEAST]]
	public final static int deltaR[] = { 0, 1, 1, 0, -1, -1 };
	public final static int deltaC[] = { 1, 1, 0, -1, -1, 0 };
	public final static int oppoDir[] = { 3, 4, 5, 0, 1, 2 };
		
	/**
	 * maze properties
	 */
	public int type;
	public int sizeR;
	public int sizeC;
	public int sizeTunnel;
	public Cell map[][] = null;
	public Cell entrance;
	public Cell exit;
	public boolean isVisu = true;
	
	/**
	 * To test whether the input entrance and exit locations are valid.
	 * @param r Row of a cell.
	 * @param c Column of a cell.
	 * @return true if the cell is on any of the four sides of the maze. Otherwise false.
	 */
	public abstract boolean isOnEdge(int r, int c);
	
	/**
	 * Initialize the basic structure of an all-wall maze.
	 * @param rs Number of rows to build. 
	 * @param cs Number of columns to build.
	 * @param entR Row coordinate of entrance.
	 * @param entC Column coordinate of entrance.
	 * @param exitR Row coordinate of exit.
	 * @param exitC Column coordinate of exit.
	 * @param tunnelList A list tunnels, each in the form of {p1Row, p1Col, p2Row,p2Col}.
	 */
	public abstract void initMaze(int rs, int cs, int entR, int entC, int exitR, int exitC, List<int[]> tunnelList);

	/**
	 * Check whether the maze is a perfect maze
	 * @return true if it is perfect maze, i.e. every cell can be visited and there is no loop.
	 */
	public abstract boolean isPerfect();
	
	/**
	 * Function to draw the maze in a window.
	 */
	public abstract void draw();

	/**
	 * Draw a foot print at cell's position, for testing solution
	 * @param cell Cell to be drawn.
	 */
	public abstract void drawFtPrt(Cell cell);


        /**
         * Show validation result of whether the maze is solved
         */
        public abstract boolean validate();

}
