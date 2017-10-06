package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.LinkedList;

import static maze.Maze.*;


public class ModifiedPrimsGenerator implements MazeGenerator {

    @Override
    public void generateMaze(Maze maze) {
        Random random = new Random();
        ArrayList<Cell> frontiers = new ArrayList<>();
        LinkedList<Cell> visited = new LinkedList<>();
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(NORTH, EAST, SOUTH, WEST));
        if (maze.type == HEX) {
            directions.add(NORTHEAST);
            directions.add(NORTHWEST);
            directions.add(SOUTHEAST);
            directions.add(SOUTHWEST);
        }
        Cell startingPoint;
        if (maze.type == NORMAL) {
            startingPoint = maze.map[random.nextInt(maze.sizeR)][random.nextInt(maze.sizeC)];

        } else {
            //Temp fix to nullpointer issue. Not sure what happens but the starting point does not exists some times?
            while (true) {
                startingPoint = maze.map[random.nextInt(maze.sizeR)][random.nextInt((maze.sizeC + 1) / 2 + maze.sizeC)];
                try {
                    int test = startingPoint.c;
                    break;
                } catch (Exception e) { // ignore ArrayIndexOutOfBounds
                }
            }
        }
        visited.add(startingPoint);


        // TODO Auto-generated method stub
        System.out.println(startingPoint.r + "," + startingPoint.c);
        getTheNeighbours(directions, frontiers, maze, startingPoint, visited);

        while (visited.size() < maze.sizeR * maze.sizeC) {
            Cell c = frontiers.remove(random.nextInt(frontiers.size()));
            ArrayList<Cell> adjacent = adjacentFinder(directions, maze, c, visited);
            System.out.println("size is " + adjacent.size());
            Cell b = adjacent.get(random.nextInt(adjacent.size()));
            deleteWall(directions, maze, b, c);
            if (!visited.contains(c)) {
                visited.add(c);
            }
            getTheNeighbours(directions, frontiers, maze, c, visited);
        }
    }


    // end of generateMaze()
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

    public ArrayList<Cell> getTheNeighbours(ArrayList<Integer> directions, ArrayList<Cell> frontiers, Maze maze, Cell cell, LinkedList<Cell> visited) {
        //Get the neighbours!

        int i = 0;
        while (i < directions.size()) {
            try {
                if (maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]] != null) {
                    if ((frontiers.contains(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]))) {
                    } else {
                        frontiers.add(maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]]);
                        System.out.println("Adding cell " + maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]].r + "," + maze.map[cell.r + deltaR[directions.get(i)]][cell.c + deltaC[directions.get(i)]].c);

                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
                System.out.println("Cell to the " + directions.get(i) + " does not exist");
            }
            i++;
        }


        i = 0;
        while (i < frontiers.size()) {
            if (visited.contains(frontiers.get(i))) {
                frontiers.remove(i);
                continue;
            }
            System.out.println("Frontiers are: " + frontiers.get(i).r + "," + frontiers.get(i).c);
            i++;
        }
        return frontiers;
    }

    public ArrayList<Cell> adjacentFinder(ArrayList<Integer> directions, Maze maze, Cell startingPoint, LinkedList<Cell> visited) {
        //Get the neighbours!
        ArrayList<Cell> frontiers = new ArrayList<>();

        int i = 0;
        while (i < directions.size()) {

            try {
                if (maze.map[startingPoint.r + deltaR[directions.get(i)]][startingPoint.c + deltaC[directions.get(i)]] != null) {
                    if ((visited.contains(maze.map[startingPoint.r + deltaR[directions.get(i)]][startingPoint.c + deltaC[directions.get(i)]]) && !frontiers.contains(maze.map[startingPoint.r + deltaR[directions.get(i)]][startingPoint.c + deltaC[directions.get(i)]]))) {
                        frontiers.add(maze.map[startingPoint.r + deltaR[directions.get(i)]][startingPoint.c + deltaC[directions.get(i)]]);
                    }
                }
            } catch (Exception e) { // ignore ArrayIndexOutOfBounds
            }
            i++;
        }

        return frontiers;
    }

    public void stop() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
} // end of class ModifiedPrimsGenerator

