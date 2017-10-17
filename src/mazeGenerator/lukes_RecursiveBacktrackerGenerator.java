package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static maze.Maze.*;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

    @Override
    public void generateMaze(Maze maze) {
        //Creates a random instance to be used to select random index from lists.
        Random random = new Random();
        //list of visited cells.
        LinkedList<Cell> visited = new LinkedList<>();
        //list of directions, if hex more are added.
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(NORTH, EAST, SOUTH, WEST));
        if (maze.type == HEX) {
            directions.add(NORTHEAST);
            directions.add(NORTHWEST);
            directions.add(SOUTHEAST);
            directions.add(SOUTHWEST);
        }
        //Starting point, if maze is normal then can be selected from bounds of maze. If a Hex maze, the Col value must be calculated after the row as each row has a different Col bound.
        Cell startingPoint;
        if (maze.type == NORMAL) {
            startingPoint = maze.map[random.nextInt(maze.sizeR)][random.nextInt(maze.sizeC)];

        } else {
            int r = random.nextInt(maze.sizeR);
            //cBoundValue is calculated after the row has been selected using the formula given in the FAQ notes.
            int cBoundValue = (int) Math.ceil((double) r / 2) + maze.sizeC;
            int c;
            //If row is not even then -1 to the bounds of C.
            if (r % 2 != 0) {
                cBoundValue = cBoundValue - 1;
            }
            //Select a c value from the calculated bounds
            c = random.nextInt(cBoundValue);
            startingPoint = maze.map[r][c];
        }
        //Add cell to visited.
        visited.add(startingPoint);
        //Get the neighbours of the starting cell.
        ArrayList<Cell> neighs = getTheNeighbours(directions, maze, startingPoint, visited);
        //Select a random neighbour
        Cell neighCell = neighs.get(random.nextInt(neighs.size()));
        visited.add(neighCell);
        //clear the neighbours list
        neighs.clear();
        //Delete the wall between the neighCell and startingPoint.
        deleteWall(directions, maze, neighCell, startingPoint);
        Cell current;
        //run the backtracker algorithm while there are still unvisited cells.
        while (visited.size() < maze.sizeC * maze.sizeR) {
            current = neighCell;
            //get the neighbours of the current Cell.
            neighs = getTheNeighbours(directions, maze, current, visited);
            //While current has no neighs, backtrack through the visited linkedlist until valid neighbours can be found
            int counter = visited.size() - 2;
            while (neighs.isEmpty()) {
                //Select the previous cell.
                current = visited.get(counter);
                //Increment the counter
                counter--;
                //Clear the neighbour list.
                neighs.clear();
                //get the neighbours of current cell.
                neighs = getTheNeighbours(directions, maze, current, visited);
            }
            //select a random neighbour of current and add it to visited.
            neighCell = neighs.get(random.nextInt(neighs.size()));
            visited.add(neighCell);
            //If neighcell is a tunnel, set the cell to the tunnel exit. Delete the wall between neighCell and current.
            if (neighCell.tunnelTo != null) {
                deleteWall(directions, maze, neighCell, current);
                neighCell = neighCell.tunnelTo;
                visited.add(neighCell);
            } else {
                deleteWall(directions, maze, neighCell, current);

            }

        }

    } // end of generateMaze()

    /**
     * Deletes the wall between cell a and b.
     *
     * @param maze       is the maze we are constructing
     * @param directions is the list of directions.
     * @param a          is a cell on the maze adjacent to b.
     * @param b          is a cell on the maze adjacent to a.
     */
    public void deleteWall(ArrayList<Integer> directions, Maze maze, Cell a, Cell b) {
        int i = 0;
        Cell tempCell;
        //Find the direction of the wall between a and c and remove it.
        while (i < directions.size()) {
            try {
                tempCell = maze.map[a.r + deltaR[directions.get(i)]][a.c + deltaC[directions.get(i)]];
                if (tempCell != null) {
                    if (tempCell.c == b.c && tempCell.r == b.r) {
                        a.wall[directions.get(i)].present = false;
                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
            }
            i++;
        }

    }

    /**
     * Find valid neighbours of a cell.
     *
     * @param maze       is the maze we are constructing
     * @param directions is the list of directions.
     * @param cell       is the cell in the maze for which we are finding neighbours.
     * @param visited    is the list of visited cells.
     */
    public ArrayList<Cell> getTheNeighbours(ArrayList<Integer> directions, Maze maze, Cell cell, LinkedList<Cell> visited) {
        ArrayList<Cell> neighs = new ArrayList<>();
        int i = 0;
        //Find all valid neighbours of the cell.
        while (i < directions.size()) {
            try {
                if (maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]] != null) {
                    if ((neighs.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]))) {
                    } else {
                        neighs.add(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]);
                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
            }

            i++;
        }

        //Remove neighs that are in visited.
        i = 0;
        while (i < neighs.size()) {
            if (visited.contains(neighs.get(i))) {
                neighs.remove(i);
                continue;
            }
            i++;
        }
        return neighs;
    }


} // end of class RecursiveBacktrackerGenerator