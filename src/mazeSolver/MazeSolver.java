package mazeSolver;

import maze.Maze;

/**
 * Interface of a maze solveer.
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * @author Yongli Ren
 */
public interface MazeSolver
{
	/**
	 * Find the solusion on given maze.
	 * @param maze The maze to solve.
	 */
	public abstract void solveMaze(Maze maze);
	

	/**
	 * Use after solveMaze(maze), to check whether the maze is solved.
	 * @return True if solved. Otherwise false.
	 */
	public abstract boolean isSolved();

	
	/**
	 * Use after solveMaze(maze), counting the number of cells explored in solving process.
	 * @return The number of cells explored.
	 * It is not required to be accurate and no marks are given (or lost) on it. 
	 */
	public abstract int cellsExplored();
} // end of interface mazeGenerator
