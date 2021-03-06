package mazeSolver;

import maze.Maze;
import maze.Cell;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	/**
	 * Instance Variables
	 */
	private List<Cell> visited = null;
	private boolean solved = false;

	/**
	 * Using the wall follower algorithm solve a particularly generated maze instance.
	 * @param maze The maze to solve
	 */
	@Override
	public void solveMaze(Maze maze) {
		/** Variable Instantiation **/
		visited = new ArrayList<Cell>();
		/** Variable Initialisation */
		int direction, backtrack = 1;
		Cell currentPosition = maze.entrance;
		/** Implementation **/
		for (;;) {
			// Check whether we're visiting a new cell
			if (!visited.contains(currentPosition)) {
				maze.drawFtPrt(currentPosition);
				visited.add(currentPosition);
			}
			//Checks to see if cell was a tunnel, if so tunnel through that bad boy!
			if(currentPosition.tunnelTo !=null && !visited.contains(currentPosition.tunnelTo)){
				currentPosition = currentPosition.tunnelTo;
				maze.drawFtPrt(currentPosition);
				visited.add(currentPosition);
			}
			// Check if we've reached the exit
			if (currentPosition == maze.exit) {
				solved = true;
				break;
			}
			// Update current position or backtrack
			if ((direction = nextDirection(maze, currentPosition)) != -1) {
				System.out.printf("CurrR (%d), CurrC (%d), Next Direction (%d)\n", currentPosition.r, currentPosition.c, direction);
				currentPosition = maze.map[currentPosition.r + Maze.deltaR[direction]][currentPosition.c + Maze.deltaC[direction]];
				backtrack = 1;
			} else if ((visited.size() - backtrack) != -1) {
				System.out.printf("Backtrack\n");
				currentPosition = visited.get(visited.size() - backtrack++);
			} else {
				System.out.printf("Standoff\n");
				break;
			}
		}
	} // end of solveMaze()

	/**
	 * Determine the next unvisited direction in the maze to progress with finding the exit in the maze
	 * @param maze is the constructed maze we're solving
	 * @param position is our current position in the maze
	 * @return the next valid direction if one exists
	 */
	private int nextDirection(Maze maze, Cell position) {
		/** Local Variable Instantiation **/
		Random rand = new Random();
		/** Local Variable Initialisation **/
		int[] directions = (maze.type == Maze.HEX ? new int[]{ Maze.WEST, Maze.NORTHWEST, Maze.EAST, Maze.NORTHEAST, Maze.SOUTHWEST, Maze.SOUTHEAST } : new int[]{ Maze.WEST, Maze.NORTH, Maze.EAST, Maze.SOUTH });
		/** Implementation **/
		for (int i = 0; i < directions.length; i++) {
			System.out.printf("Checking Direction (%d) [Iteration %d]\n", directions[i], i);
			if (checkDirection(maze, position, directions, i))
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
	 * @return the evaluation whether a given direction is valid
	 */
	private boolean checkDirection(Maze maze, Cell position, int[] directions, int direction) {
		/** Implementation **/
		if (wallIsDown(position, directions[direction]) && !isOutOfBounds(maze, position, directions[direction]) && isCellUnvisited(maze, position, directions[direction]))
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
	 * @return the unvisited status of the next maze
	 */
	private boolean isCellUnvisited(Maze maze, Cell cell, int direction) {
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
		return visited == null ? 0 : visited.size();
	} // end of cellsExplored()

} // end of class WallFollowerSolver
