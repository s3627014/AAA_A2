package mazeSolver;

import maze.Maze;
import maze.Cell;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {
    /**
     * Instance Variables
     */
    private ArrayList<Cell> entranceVisited = null, exitVisited = null;
    private boolean solved = false;

    /**
     * Using the wall follower algorithm solve a particularly generated maze instance.
     * @param maze The maze to solve
     */
	@Override
	public void solveMaze(Maze maze) {
        /** Variable Instantiation **/
        entranceVisited = new ArrayList<Cell>();
        exitVisited = new ArrayList<Cell>();
		/** Implementation **/
		biDirectionalBacktrack(maze, maze.entrance, 1, maze.exit, 1);
	} // end of solveMaze()

    /**
     * Recursively traverse a pseudorandom generated footprint through the maze using Depth First Search (DFS) algorithm.
     * @param maze is the maze we are constructing
     * @param fromEntrance is an unvisited cell starting from the mazes entrance cell
     * @param entranceBacktrack is the amount of backtrack positions to take from the entrance within the array list
     * @param fromExit is an unvisited cell starting from the mazes exit cell
     * @param exitBacktrack is the amount of backtrack positions to take from the entrance within the array list
     */
	private void biDirectionalBacktrack(Maze maze, Cell fromEntrance, int entranceBacktrack, Cell fromExit, int exitBacktrack) {
        /** Variable Initialisation */
        int direction;
        /** Implementation **/
        // Check whether we're visiting a new cell from the entrance
        if (!entranceVisited.contains(fromEntrance)) {
            maze.drawFtPrt(fromEntrance);
            entranceVisited.add(fromEntrance);
        }
        // Check whether we're visiting a new cell from the exit
        if (!entranceVisited.contains(fromEntrance)) {
            maze.drawFtPrt(fromExit);
            exitVisited.add(fromExit);
        }
        // Check whether the two paths have intersected
        if (entranceVisited.contains(fromExit) || exitVisited.contains(fromEntrance)) {
            solved = true;
            return;
        }
        // Move the fromEntrance cell
        if ((direction = nextDirection(maze, fromEntrance, entranceVisited)) != -1) {
            System.out.printf("[Entrance]: CurrR (%d), CurrC (%d), Next Direction (%d)\n", fromEntrance.r, fromEntrance.c, direction);
            fromEntrance = maze.map[fromEntrance.r + Maze.deltaR[direction]][fromEntrance.c + Maze.deltaC[direction]];
            entranceBacktrack = 1;
        } else if ((entranceVisited.size() - entranceBacktrack) != -1) {
            System.out.printf("Entrance Backtrack\n");
            fromEntrance = entranceVisited.get(entranceVisited.size() - entranceBacktrack++);
        } else {
            System.out.printf("Entrance Standoff\n");
            return;
        }
        // Move the fromExit cell
        if ((direction = nextDirection(maze, fromExit, exitVisited)) != -1) {
            System.out.printf("[Exit]: CurrR (%d), CurrC (%d), Next Direction (%d)\n", fromExit.r, fromExit.c, direction);
            fromExit = maze.map[fromExit.r + Maze.deltaR[direction]][fromExit.c + Maze.deltaC[direction]];
            exitBacktrack = 1;
        } else if ((exitVisited.size() - exitBacktrack) != -1) {
            System.out.printf("Exit Backtrack\n");
            fromExit = exitVisited.get(exitVisited.size() - exitBacktrack++);
        } else {
            System.out.printf("Exit Standoff\n");
            return;
        }
        // Reoccurence
        biDirectionalBacktrack(maze, fromEntrance, entranceBacktrack, fromExit, exitBacktrack);
	}

    /**
     * Determine the next unvisited direction in the maze to progress with finding the exit in the maze
     * @param maze is the constructed maze we're solving
     * @param position is our current position in the maze
     * @param visited is the container of currently visited cells
     * @return the next valid direction if one exists
     */
    private int nextDirection(Maze maze, Cell position, ArrayList<Cell> visited) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int[] directions = (maze.type == Maze.HEX ? new int[]{ Maze.WEST, Maze.NORTHWEST, Maze.EAST, Maze.NORTHEAST, Maze.SOUTHWEST, Maze.SOUTHEAST } : new int[]{ Maze.WEST, Maze.NORTH, Maze.EAST, Maze.SOUTH });
        /** Implementation **/
        for (int i = 0; i < directions.length; i++) {
            System.out.printf("Checking Direction (%d) [Iteration %d]\n", directions[i], i);
            if (checkDirection(maze, position, directions, i, visited))
                return directions[i];
        }
        return -1;
    } // end of nextDirection()

    /**
     * Check the validity of the next direction in the maze base off of our current position.
     * @param maze is the constructed maze we're solving
     * @param position is our current position in the maze
     * @param directions are the directions we're travelling within the maze
     * @param direction is the singular direction we're checking
     * @param visited is the container of currently visited cells
     * @return the evaluation whether a given direction is valid
     */
    private boolean checkDirection(Maze maze, Cell position, int[] directions, int direction, ArrayList<Cell> visited) {
        /** Implementation **/
        if (wallIsDown(position, directions[direction]) && !isOutOfBounds(maze, position, directions[direction]) && isCellUnvisited(maze, position, directions[direction], visited))
            return true;
        return false;
    } // end of checkDirection()

    /**
     * Ensure a wall exists it's knocked down in the next direction we're heading.
     * @param cell is our current position in the maze
     * @param direction is our next position in the maze
     * @return the evaluation whether the wall is down
     */
    private boolean wallIsDown(Cell cell, int direction) {
        /** Implementation **/
        // Ensure the wall exists and isn't present
        if (cell.wall[direction] != null && cell.wall[direction].present == false)
            return true;
        return false;
    } // end of wallIsDown()

    /**
     * Ensure the next cell hasn't already been visited when solving the maze.
     * @param maze is the constructed maze we're solving
     * @param cell is our current position in the maze
     * @param direction is our next position in the maze
     * @param visited is the container of currently visited cells
     * @return the unvisited status of the next maze
     */
    private boolean isCellUnvisited(Maze maze, Cell cell, int direction, ArrayList<Cell> visited) {
        /** Local Variable Initialisation **/
        Cell neighbour = maze.map[cell.r + Maze.deltaR[direction]][cell.c + Maze.deltaC[direction]];
        /** Implementation **/
        return !visited.contains(neighbour);
    } // end of isCellUnvisited()

    /**
     * Evaluate if the cell we would traverse to given a particular direction is outside the mazes boundaries.
     * @param maze is the maze we're constructing
     * @param cell is our current position in the maze
     * @param direction is the direction we want to evaluate
     * @return whether the direction leads out of bounds
     */
    private boolean isOutOfBounds(Maze maze, Cell cell, int direction) {
        /** Local Variable Initialisation **/
        int deltaR = cell.r + Maze.deltaR[direction], deltaC = cell.c + Maze.deltaC[direction];
        /** Implementation **/
        if (maze.type != maze.HEX && (deltaR >= maze.sizeR || deltaR < 0 || deltaC >= maze.sizeC || deltaC < 0))
            return true;
        else if (maze.type == maze.HEX && (deltaR >= maze.sizeR || deltaR < 0 || deltaC > ((deltaC + 1)/2 + deltaC) || deltaC < 0))
            return true;
        return false;
    } // end of isOutOfBounds()

    /**
     * Check whether the maze has been solved.
     * @return the status of the maze
     */
	@Override
	public boolean isSolved() {
        return solved;
	} // end if isSolved()

    /**
     * Find the amount of cells explored in the maze.
     * @return the amount of explored cells
     */
	@Override
	public int cellsExplored() {
		return (entranceVisited == null || exitVisited == null) ? 0 : entranceVisited.size() + exitVisited.size();
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
