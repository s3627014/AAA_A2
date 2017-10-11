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
	
	double threshold = 0.1;

	@Override
	public void generateMaze(Maze maze) {
		Random random = new Random();
		ArrayList<Cell> z = new ArrayList<>();
		ArrayList<Cell> visited = new ArrayList<>();
		ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(NORTH, EAST, SOUTH, WEST));
		if (maze.type == HEX) {
			directions.add(NORTHEAST);
			directions.add(NORTHWEST);
			directions.add(SOUTHEAST);
			directions.add(SOUTHWEST);
			directions.remove(NORTH);
			directions.remove(SOUTH);
		}
		Cell startingPoint;
		if (maze.type == NORMAL) {
			startingPoint = maze.map[random.nextInt(maze.sizeR)][random.nextInt(maze.sizeC)];

		} else {
			int r = random.nextInt(maze.sizeR);
			int rValue = (int) Math.ceil((double) r / 2) + maze.sizeC;
			int c =0;



			if(r==0){
				c= random.nextInt(maze.sizeC);
			}
			else {
				if(r %2 ==0){
					c = rValue-1;
				}
				else{
					c = rValue-2;
				}

			}
			System.out.println("r is " + r + "  c is " + c);
			startingPoint = maze.map[r][c];

		}
		z.add(startingPoint);
		visited.add(startingPoint);
		Cell current;
		while(!z.isEmpty()){
			if(random.nextDouble() > threshold){
				current = z.get(random.nextInt(z.size()));
			}
			else{
				current = z.get(z.size()-1);
			}
			System.out.println("picked from z: " + current.r + "," + current.c);
			ArrayList<Cell> neighs = getTheNeighbours(directions,maze,current,visited);
			if(neighs.isEmpty()){
				z.remove(current);
			}
			else{
				Cell neighCell = neighs.get(random.nextInt(neighs.size()));
				deleteWall(directions,maze,neighCell,current);
				z.add(neighCell);
				visited.add(neighCell);
			}
		System.out.println(z.size());
			neighs.clear();
		}

	}
	public ArrayList<Cell> getTheNeighbours(ArrayList<Integer> directions, Maze maze, Cell cell, ArrayList<Cell> visited) {
		//Get the neighbours!
		ArrayList<Cell> neighs = new ArrayList<>();
		int i = 0;
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


		i = 0;
		while (i < neighs.size()) {
			if (visited.contains(neighs.get(i))) {
				neighs.remove(i);
				continue;
			}
			System.out.println("Neighs are: " + neighs.get(i).r + "," + neighs.get(i).c);
			i++;
		}
		return neighs;
	}

	public void deleteWall(ArrayList<Integer> directions, Maze maze, Cell a, Cell b) {
		System.out.println("Deleting wall between " + a.r + "," + a.c + " and " + b.r + "," + b.c);
		int i = 0;
		Cell tempCell;
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
	public void stop() {
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
