package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static maze.Maze.*;


public class ModifiedPrimsGenerator implements MazeGenerator {

    @Override
    public void generateMaze(Maze maze) {
        //Creates a random variable to select random indexes from Lists.
        Random random = new Random();
        //ArrayList of all frontiers found during the generation.
        ArrayList<Cell> frontiers = new ArrayList<>();
        //LinkedList of visited cells during generation.
        LinkedList<Cell> visited = new LinkedList<>();
        //A list of directions for generation to select from.
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
        visited.add(startingPoint);
        //Find the neighbours of the starting point to begin generation
        getTheNeighbours(directions, frontiers, maze, startingPoint, visited);
        //While their are still unvisited cells, apply the primm algorithm.
        while (visited.size() < maze.sizeR * maze.sizeC) {
            //Select a random frontier from the frontiers list.
            Cell selectedFrontier = frontiers.remove(random.nextInt(frontiers.size()));
            //Find the unvisited adjacent cells and add them to the list adjacent.
            ArrayList<Cell> adjacent = adjacentFinder(directions, maze, selectedFrontier, visited);
            //Select an unvisited adjacent cell and then delete the wall between the selectedFrontier and adjacentCell
            Cell adjacentCell = adjacent.get(random.nextInt(adjacent.size()));
            deleteWall(directions, maze, adjacentCell, selectedFrontier);
            //If the selectedFrontier has not been visited, add it to the visited list.
            if (!visited.contains(selectedFrontier)) {
                visited.add(selectedFrontier);
            }
            //Get the neighbours of the selectedFrontier.
            getTheNeighbours(directions, frontiers, maze, selectedFrontier, visited);
        }
    }


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
        //Tests for valid directions which are within the bounds of the maze.
        while (i < directions.size()) {
            try {
                tempCell = maze.map[a.r + deltaR[directions.get(i)]][a.c + deltaC[directions.get(i)]];
                if (tempCell != null) {
                    if (tempCell.c == b.c && tempCell.r == b.r) {
                        //Removes the wall between the cells
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
     * @param frontiers  is the list of frontiers found during generation.
     * @param cell       is the cell in the maze for which we are finding neighbours.
     * @param visited    is the list of visited cells.
     */
    public ArrayList<Cell> getTheNeighbours(ArrayList<Integer> directions, ArrayList<Cell> frontiers, Maze maze, Cell cell, LinkedList<Cell> visited) {
        int i = 0;
        //Adds all the unvisited neighbouring cells to the frontier list.
        while (i < directions.size()) {
            try {
                if (maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]] != null) {
                    if ((frontiers.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]))) {
                    } else {
                        frontiers.add(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]);
                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
            }
            i++;
        }
        //Removes visited frontiers from frontier list.
        i = 0;
        while (i < frontiers.size()) {
            if (visited.contains(frontiers.get(i))) {
                frontiers.remove(i);
                continue;
            }
            i++;
        }
        return frontiers;
    }

    /**
     * Finds valid adjacent cells.
     *
     * @param maze       is the maze we are constructing
     * @param directions is the list of directions.
     * @param cell       is the cell which adjacent cells are being found for.
     * @param visited    is the list of visited cells.
     */
    public ArrayList<Cell> adjacentFinder(ArrayList<Integer> directions, Maze maze, Cell cell, LinkedList<Cell> visited) {
        ArrayList<Cell> adjacentCells = new ArrayList<>();
        int i = 0;
        //Loops to find all adjacent cells that have been visited
        while (i < directions.size()) {
            try {
                if (maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]] != null) {
                    if ((visited.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]) && !adjacentCells.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]))) {
                        adjacentCells.add(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]);
                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
            }
            i++;
        }

        return adjacentCells;
    }

} // end of class ModifiedPrimsGenerator

