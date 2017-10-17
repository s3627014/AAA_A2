package mazeGenerator;

import java.io.IOException;
import java.util.*;

import maze.Maze;
import maze.Cell;

import static maze.Maze.*;
import static maze.Maze.SOUTHEAST;
import static maze.Maze.SOUTHWEST;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	//This value can be changed to determine how often a cell is picked randomly or most recently.
	double threshold = 0.1;

	@Override

	public void generateMaze(Maze maze) {
		//Creates a random instance to be used for selecting random indexes from a list
		Random random = new Random();
		//z is the list of Cells that will be used for growing tree to select from.
		ArrayList<Cell> z = new ArrayList<>();
		//visited is a list of cells that have been visited.
		ArrayList<Cell> visited = new ArrayList<>();
		//List of directions. If Hex map more directions are added.
		ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(NORTH, EAST, SOUTH, WEST));
		if (maze.type == HEX) {
			directions.add(NORTHEAST);
			directions.add(NORTHWEST);
			directions.add(SOUTHEAST);
			directions.add(SOUTHWEST);
		}
		//Calulcates a random starting position.
		Cell startingPoint;
		//If normal maze, select a random cell inside the bounds.
		if (maze.type == NORMAL) {
			startingPoint = maze.map[random.nextInt(maze.sizeR)][random.nextInt(maze.sizeC)];
		//If Hex maze, starting point is calulated according to the FAQ notes.
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
		z.add(startingPoint);
		visited.add(startingPoint);
		Cell current;
		//While z list is not empty, select a cell from the list and run it through the growing tree algorithm.
		while(!z.isEmpty()){
			if(random.nextDouble() > threshold){
				//Selects random cell from list.
				current = z.get(random.nextInt(z.size()));
			}
			else{
				//Selects the last cell from the list.
				current = z.get(z.size()-1);
			}
			//List of neighs of the current cell
			ArrayList<Cell> neighs = getTheNeighbours(directions,maze,current,visited);
			//If current has no neighbours then remove it from z.
			if(neighs.isEmpty()){
				z.remove(current);
			}
			//selects one of the found neighbours and deletes the wall between current and neighCell.
			else{
				Cell neighCell = neighs.get(random.nextInt(neighs.size()));
				deleteWall(directions,maze,neighCell,current);
				//Add the neighCell to both z and visited.
				z.add(neighCell);
				visited.add(neighCell);
			}
			//Clear the neigh list for next iteration.
			neighs.clear();
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
	public ArrayList<Cell> getTheNeighbours(ArrayList<Integer> directions, Maze maze, Cell cell, ArrayList<Cell> visited) {
		ArrayList<Cell> neighs = new ArrayList<>();
		int i = 0;
		//Find all valid neighbours of the cell.
		while (i < directions.size()) {
			try {
				if (maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]] != null) {
					if ((neighs.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]))) {
					} else {
						neighs.add(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]);
						//System.out.println("Adding cell " + maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]].r + "," + maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]].c);

					}
				}
			} catch (Exception e) { // ignore ArrayIndexOutOfBounds
			}

			i++;
		}

		//Remove all neighs that have been visited already
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
	/**
	 * Deletes the wall between cell a and b.
	 *
	 * @param maze       is the maze we are constructing
	 * @param directions is the list of directions.
	 * @param a          is a cell on the maze adjacent to b.
	 * @param b          is a cell on the maze adjacent to a.
	 */
	public void deleteWall(ArrayList<Integer> directions, Maze maze, Cell a, Cell b) {
		System.out.println("Deleting wall between " + a.r + "," + a.c + " and " + b.r + "," + b.c);
		int i = 0;
		Cell tempCell;
		//Finds the correct direction to delete a wall from.
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


}
