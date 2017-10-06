package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.Wall;

import java.util.Arrays;
import java.util.Random;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int row = rand.nextInt(maze.sizeR), col = rand.nextInt(maze.sizeC);
        /** Implementation **/
        recursiveBacktrack(maze, null, row, col);
	} // end of generateMaze()

    private void recursiveBacktrack(Maze maze, Cell neighbour, int row, int col) {
        /** Local Variable Initialisation **/
        Cell start = null;
        int direction;
        /** Implementation **/
        // Verify at least one wall is currently present.
        for (Wall wall : (neighbour == null ? (start = maze.map[row][col]).wall : neighbour.wall)) {
            if (wall != null && wall.present == true)
                break;
            else if (wall == (neighbour == null ? start.wall[Maze.NUM_DIR - 1] : neighbour.wall[Maze.NUM_DIR - 1]))
                return;
        }
        for (;;) {
            direction = nextDirection(maze, neighbour == null ? start : neighbour);
            if (direction == -1)
                break;
            // Knock down the walls for a particular direction
            if (neighbour != null) {
                neighbour.wall[direction].present = false;
                System.out.printf("Backtrack: r(%d) + dR(%d) = %d, c(%d) + dC(%d) = %d\n", row, Maze.deltaR[direction], row + Maze.deltaR[direction], col, Maze.deltaC[direction], col + Maze.deltaC[direction]);
                recursiveBacktrack(maze, (neighbour = maze.map[row + Maze.deltaR[direction]][col + Maze.deltaC[direction]]),
                        neighbour.r, neighbour.c);
            } else {
                start.wall[direction].present = false;
                recursiveBacktrack(maze, (neighbour = maze.map[row + Maze.deltaR[direction]][col + Maze.deltaC[direction]]),
                        neighbour.r, neighbour.c);
            }
        }
    } // end of recursiveBacktrack()

    private int nextDirection(Maze maze, Cell cell) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int value = rand.nextInt(Maze.NUM_DIR);
        /** Implementation **/
        // Check all walls to determine whether there is an unvisited cell
        for (int i = value; i < Maze.NUM_DIR; i++)
            if (cell.wall[i] != null && !isOutOfBounds(maze, cell, i) && isCellUnvisited(maze, cell, i))
                return i;
        for (int i = 0; i < value; i++)
            if (cell.wall[i] != null && !isOutOfBounds(maze, cell, i) && isCellUnvisited(maze, cell, i))
                return i;
        return -1;
    } // end of nextDirection()

    private boolean isCellUnvisited(Maze maze, Cell cell, int direction) {
        /** Implementation **/
	    // Check whether a neighbouring cell is unvisited
        for (Wall wall : maze.map[cell.r + Maze.deltaR[direction]][cell.c + Maze.deltaC[direction]].wall)
            if (wall != null && wall.present == false)
                return false;
        return true;
    } // end of isCellUnvisited()

    private boolean isOutOfBounds(Maze maze, Cell cell, int direction) {
        /** Local Variable Initialisation **/
	    int deltaR = cell.r + Maze.deltaR[direction], deltaC = cell.c + Maze.deltaC[direction];
        /** Implementation **/
	    if (deltaR >= maze.sizeR || deltaR < 0 || deltaC >= maze.sizeC || deltaC < 0)
	        return true;
	    return false;
    } // end of isOutOfBounds()

} // end of class RecursiveBacktrackerGenerator