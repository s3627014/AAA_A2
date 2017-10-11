package mazeSolver;

import maze.Maze;

import maze.Maze;
import maze.Cell;
import maze.Wall;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implements WallFollowerSolver
 */

public class WallFollowerSolver implements MazeSolver {
	private ArrayList<Cell> visited = null;
	private boolean solved = false;
	
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
			if (!visited.contains(currentPosition))
				visited.add(currentPosition);
			// Ensure we haven't reached the exit
			if (currentPosition == maze.exit) {
				System.out.printf("Found!\n");
				solved = true;
				break;
			}
			// Update current position or backtrack
			if ((direction = nextDirection(maze, currentPosition)) != -1) {
				// Draw
				maze.drawFtPrt(currentPosition);
				System.out.printf("CurrR (%d), CurrC (%d), Next Direction (%d)\n", currentPosition.r, currentPosition.c, direction);
				currentPosition = maze.map[currentPosition.r + Maze.deltaR[direction]][currentPosition.c + Maze.deltaC[direction]];
				// Reset backtrack
				backtrack = 1;
			} else if ((visited.size() - backtrack) != -1) {
				System.out.printf("Backtrack\n");
				currentPosition = visited.get(visited.size() - backtrack++);
			}
		}
	} // end of solveMaze()

	/**
	 * Determine the next unvisited direction in the maze to progress with finding the exit in the maze
	 * @param maze
	 * @param currentPosition
	 * @return
	 */
	private int nextDirection(Maze maze, Cell currentPosition) {
		/** Local Variable Instantiation **/
		Random rand = new Random();
		/** Local Variable Initialisation **/
		int[] directions = (maze.type == Maze.HEX ? new int[]{ Maze.EAST, Maze.NORTHEAST, Maze.WEST, Maze.NORTHWEST } : new int[]{ Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST });
		/** Implementation **/
		for (int i = 0; i < directions.length; i++) {
			System.out.printf("Checking Direction (%d)\n", directions[i]);
			if (wallExists(currentPosition, directions[i]) && !isOutOfBounds(maze, currentPosition, directions[i]) && isCellUnvisited(maze, currentPosition, directions[i]))
				return directions[i];
		}
		return -1;
	} // end of nextDirection()

	/**
	 * Ensure a wall exists it's knocked down in the next direction we're heading.
	 * @param cell is our current position in the maze
	 * @param direction is our next position in the maze
	 * @return
	 */
	private boolean wallExists(Cell cell, int direction) {
		/** Implementation **/
		// Ensure the wall exists and isn't present
		if (cell.wall[direction] != null && cell.wall[direction].present == false)
			return true;
		return false;
	} // end of wallExists()

	/**
	 * Ensure the next cell hasn't already been visited when solving the maze.
	 * @param maze is the constructed maze we're solving
	 * @param cell is out current position in the maze
	 * @param direction is our next position in the maze
	 * @return
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
		if (maze.type != maze.HEX && deltaR >= maze.sizeR || deltaR < 0 || deltaC >= maze.sizeC || deltaC < 0)
			return true;
		else if (maze.type == maze.HEX && deltaR >= maze.sizeR || deltaR < 0 || deltaC > ((deltaC + 1)/2 + deltaC) || deltaC < 0)
			return true;
		return false;
	} // end of isOutOfBounds()

	@Override
	public boolean isSolved() {
		return solved;
	} // end if isSolved()
    
	@Override
	public int cellsExplored() {
		return visited == null ? 0 : visited.size();
	} // end of cellsExplored()

} // end of class WallFollowerSolver
