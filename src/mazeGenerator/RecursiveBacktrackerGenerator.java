package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.Wall;

import java.util.Random;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int row = rand.nextInt(maze.sizeR), col = rand.nextInt(maze.sizeC);
        /** Implementation **/
        recursiveBacktrack(maze, maze.map[row][col], row, col);
	} // end of generateMaze()

    /**
     * Recursively pave a pseudorandom generated pathway through the maze using Depth First Search (DFS) algorithm.
     * @param maze is the maze we are constructing
     * @param neighbour is an unvisited cell within the maze
     * @param row holds what row in the maze the neighbour is at
     * @param col holds what column in the maze the neighbour is at
     */
    private void recursiveBacktrack(Maze maze, Cell neighbour, int row, int col) {
        /** Local Variable Initialisation **/
        int direction;
        /** Implementation **/
        // Get a direction of an unvisited cell if any are available
        while ((direction = nextDirection(maze, neighbour)) != -1) {
            // Knock down the wall towards the unvisited cell
            neighbour.wall[direction].present = false;
            recursiveBacktrack(maze, (neighbour = maze.map[row + Maze.deltaR[direction]][col + Maze.deltaC[direction]]),
                    neighbour.r, neighbour.c);
        }
    } // end of recursiveBacktrack()

    /**
     * Determine the next unvisited direction in the maze to progress with wall deconstruction.
     * @param maze is the maze we are constructing
     * @param cell is our current position in the maze
     * @return the unvisited direction if any remain
     */
    private int nextDirection(Maze maze, Cell cell) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int value = rand.nextInt(Maze.NUM_DIR);
        /** Implementation **/
        // Check all walls to determine whether there is an unvisited neighbour starting from random
        for (int i = value; i < Maze.NUM_DIR; i++)
            if (cell.wall[i] != null && !isOutOfBounds(maze, cell, i) && isCellUnvisited(maze, cell, i))
                return i;
        for (int i = 0; i < value; i++)
            if (cell.wall[i] != null && !isOutOfBounds(maze, cell, i) && isCellUnvisited(maze, cell, i))
                return i;
        return -1;
    } // end of nextDirection()

    /**
     * Evaluate if the cell we would traverse to given a particular direction is currently unvisited.
     * @param maze is the maze we are constructing
     * @param cell is our current position in the maze
     * @param direction is the direction we want to evaluate
     * @return whether the cell is unvisited
     */
    private boolean isCellUnvisited(Maze maze, Cell cell, int direction) {
        /** Implementation **/
	    // Check whether a neighbouring cell is unvisited
        for (Wall wall : maze.map[cell.r + Maze.deltaR[direction]][cell.c + Maze.deltaC[direction]].wall)
            if (wall != null && wall.present == false)
                return false;
        return true;
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
	    if (deltaR >= maze.sizeR || deltaR < 0 || deltaC >= maze.sizeC || deltaC < 0)
	        return true;
	    return false;
    } // end of isOutOfBounds()

} // end of class RecursiveBacktrackerGenerator