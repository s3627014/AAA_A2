package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.Wall;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Random;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
        /** Local Variable Instantiation **/
        Random rand = new Random();
        /** Local Variable Initialisation **/
        int row = rand.nextInt(maze.sizeR), col = rand.nextInt(maze.sizeC);
        int rValue = (int) Math.ceil((double) row / 2) + maze.sizeC;
        //int row = rand.nextInt(maze.sizeR), col = rand.nextInt((maze.type == Maze.HEX ? (maze.sizeC + 1) / 2 + maze.sizeC : maze.sizeC));
        /** Implementation **/
        System.out.printf("Start Col: %d, Hex Col: %d\n", col, hexStartCol(row, col,rValue));
        if (maze.type != Maze.HEX)
            recursiveBacktrack(maze, maze.map[row][col], row, col, new ArrayDeque<Integer>());
        else
            recursiveBacktrack(maze, maze.map[row][(col = hexStartCol(row, col, rValue))], row, col, new ArrayDeque<Integer>());
	} // end of generateMaze()

    /**
     * Ensure the starting column is contained within the hex maze accordingly as the row increases.
     * @param row is the pseudorandom generated starting row
     * @param col is the pseudorandom generated starting col
     * @return the appropriate starting col
     */
    private int hexStartCol(int row, int col,int rValue ) {
        if (row == 0)
	        return col;
	    else
	        if(row %2 ==0){
                return rValue-1;
            }
            else{

                return rValue-2;
            }
    }

    /**
     * Recursively pave a pseudorandom generated pathway through the maze using Depth First Search (DFS) algorithm.
     * @param maze is the maze we are constructing
     * @param neighbour is an unvisited cell within the maze
     * @param row holds what row in the maze the neighbour is at
     * @param col holds what column in the maze the neighbour is at
     */
    private void recursiveBacktrack(Maze maze, Cell neighbour, int row, int col, Deque<Integer> directions) {
        /** Local Variable Initialisation **/
        int direction;
        /** Implementation **/
        System.out.printf("Row: %d, Col: %d\n", row, col);
        if ((direction = nextDirection(maze, neighbour)) != -1) {
            directions.push(direction);
            System.out.printf("Backtrack: r(%d) + dR(%d) = %d, c(%d) + dC(%d) = %d\n", row, Maze.deltaR[direction], row + Maze.deltaR[direction], col, Maze.deltaC[direction], col + Maze.deltaC[direction]);
            neighbour.wall[direction].present = false;
            recursiveBacktrack(maze, (neighbour = maze.map[row + Maze.deltaR[direction]][col + Maze.deltaC[direction]]),
                    neighbour.r, neighbour.c, directions);
        } else if (directions.size() != 0) {
            direction = directions.pop();
            recursiveBacktrack(maze, (neighbour = maze.map[row + Maze.deltaR[Maze.oppoDir[direction]]][col + Maze.deltaC[Maze.oppoDir[direction]]]),
                    neighbour.r, neighbour.c, directions);
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
        int[] directions = (maze.type == Maze.HEX ? new int[]{ Maze.NORTHEAST, Maze.EAST, Maze.SOUTHEAST, Maze.SOUTHWEST, Maze.WEST, Maze.NORTHWEST } : new int[]{ Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST });
        int value = rand.nextInt(directions.length);
        /** Implementation **/
        // Check all walls to determine whether there is an unvisited neighbour starting from random
        for (int i = value; i < directions.length; i++) {
            System.out.printf("Direction: %d\n", i);
            if (cell.wall[directions[i]] != null && !isOutOfBounds(maze, cell, directions[i]) && isCellUnvisited(maze, cell, directions[i]))
                return directions[i];
         }for (int i = 0; i < value; i++)
            if (cell.wall[directions[i]] != null && !isOutOfBounds(maze, cell, directions[i]) && isCellUnvisited(maze, cell, directions[i]))
                return directions[i];
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
        /** Local Variable Initialisation **/
        Cell neighbourCell;
        /** Implementation **/
        // Ensure the neighbouring cell isn't null
        if ((neighbourCell = maze.map[cell.r + Maze.deltaR[direction]][cell.c + Maze.deltaC[direction]]) == null)
            return false;
	    // Check whether a neighbouring cell is unvisited
        for (Wall wall : neighbourCell.wall)
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
	    if (maze.type != maze.HEX && (deltaR >= maze.sizeR || deltaR < 0 || deltaC >= maze.sizeC || deltaC < 0))
	        return true;
	    else if (maze.type == maze.HEX && (deltaR >= maze.sizeR || deltaR < 0 || deltaC >= ((maze.sizeC + 1)/2 + maze.sizeC) || deltaC < 0))
            return true;
	    return false;
    } // end of isOutOfBounds()

} // end of class RecursiveBacktrackerGenerator