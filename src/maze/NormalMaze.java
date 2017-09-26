package maze;

import java.util.*;

import maze.StdDraw;

/**
 * Class of a normal rectangular maze.
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * 
 */
public class NormalMaze extends Maze {

 	/**
 	 * Boolean matrix to record visited cells by drawFtPrt(Cell)
 	 */
 	protected boolean isRecorded[][];

	
	public NormalMaze() {
		type = NORMAL;
	} // end of NormalMaze()

	// auxiliary functions
	/**
	 * Check whether cell (r, c) is in the maze.
	 * @param r Row coordinate
	 * @param c Column coordinate
	 * @return True if in the maze. Otherwise false.
	 */
	protected boolean isIn(int r, int c) {
		return r >= 0 && r < sizeR && c >= 0 && c < sizeC;
	} // end of isIn()

	
	/**
	 * Check whether the cell is in the maze.
	 * @param cell The cell being checked. 
	 * @return True if in the maze. Otherwise false.
	 */
	protected boolean isIn(Cell cell) {
		if (cell == null)
			return false;
		return isIn(cell.r, cell.c);
	} // end of isIn()
	
	
	@Override
	public boolean isOnEdge(int r, int c) {
		if (isIn(r, c) && (r == 0 || r == sizeR - 1 || c == 0 || c == sizeC - 1)) 
			return true;
		return false;
	} // end of isOnEdge()
	
	
	@Override
	public void initMaze(int rs, int cs, int entR, int entC, int exitR, int exitC, List<int[]> tunnelList) {
		// set up maze constants
		sizeR = rs;
		sizeC = cs;
		sizeTunnel = tunnelList.size();
		
		// set up map matrix
		map = new Cell[sizeR][sizeC];
		for (int i = 0; i < sizeR; i++){
			for (int j = 0; j < sizeC; j++) {
				Cell cell = new Cell(i, j);
				map[i][j] = cell;
				for (int k = 0; k < 3; k++) {
					if (k == 1)
						continue;
					cell.wall[k] = new Wall();
				}
				for (int k = 3; k < NUM_DIR; k++) {
					if (k == 4)
						continue;
					if (isIn(i + deltaR[k], j + deltaC[k])) {
						Cell neigh = map[i + deltaR[k]][j + deltaC[k]];
						cell.wall[k] = neigh.wall[oppoDir[k]];
						cell.neigh[k] = neigh;
						neigh.neigh[oppoDir[k]] = cell;
					}
					else
						cell.wall[k] = new Wall();
				}
			}
		}
		
		// set up entrance and exit
		if (isIn(entR, entC))
			entrance = map[entR][entC];
		if (isIn(exitR, exitC))
			exit = map[exitR][exitC];

 		// set up recording matrix for validation
 		isRecorded = new boolean[sizeR][sizeC];
	} // end of initMaze()
	
	
	@Override
	public boolean isPerfect() {
		boolean visited[][] = new boolean[sizeR][sizeC];
		Queue<Cell> queue = new LinkedList<Cell>();
		
		queue.add(entrance);
		
		while (!queue.isEmpty()) {
			Cell currCell = queue.poll();
			visited[currCell.r][currCell.c] = true;
			int visitedNeigh = 0;
			for (int i = 0; i < NUM_DIR; i++) {
				Cell nextCell = currCell.neigh[i];
				if (!isIn(nextCell) || currCell.wall[i].present)
					continue;
				if (visited[nextCell.r][nextCell.c])
					visitedNeigh += 1; 
				else
					queue.add(nextCell);
			}
			
			if (visitedNeigh > 1)
				return false;
		}
		
		for (int i = 0; i < sizeR; i++)
			for (int j = 0; j < sizeC; j++)
				if (!visited[i][j])
					return false;
		
		return true;
	} // end of isPerfect()

	
	@Override
	public void draw() {
		// draw nothing if visualization is switched off
		if (!isVisu)
			return;
		
		for (int i = 0; i < sizeR; i++)
			for (int j = 0; j < sizeC; j++)
				for (int k = 0; k < NUM_DIR; k++) {
					if (map[i][j].wall[k] != null)
						map[i][j].wall[k].drawn = false;
				}
		
		for (int k = 0; k < NUM_DIR; k++) {
			if (k == 1 || k == 4)
				continue;
			if (entrance.neigh[k] == null) {
				entrance.wall[k].drawn = true;
				break;
			}
		}
		
		for (int k = 0; k < NUM_DIR; k++) {
			if (k == 1 || k == 4)
				continue;
			if (exit.neigh[k] == null) {
				exit.wall[k].drawn = true;
				break;
			}
		}
		
		StdDraw.setCanvasSize(900, 900);
		StdDraw.setXscale(-1, sizeC+1);
		StdDraw.setYscale(-1, sizeR+1);

		// draw entrance
		StdDraw.setPenColor(StdDraw.BLUE);
		if (entrance != null) {
			StdDraw.filledCircle(entrance.c + 0.5, entrance.r + 0.5, 0.375);
		}

		// draw exit
		StdDraw.setPenColor(StdDraw.RED);
		if (exit != null) {
			StdDraw.filledCircle(exit.c + 0.5, exit.r + 0.5, 0.375);
		}        


		// draw walls
		StdDraw.setPenColor(StdDraw.BLACK);
		for (int r = 0; r < sizeR; r++) {
			for (int c = 0; c < sizeC; c++) {
				if (map[r][c].wall[EAST].present && !map[r][c].wall[EAST].drawn) { StdDraw.line(c+1, r, c+1, r+1); map[r][c].wall[EAST].drawn = true; }
				if (map[r][c].wall[NORTH].present && !map[r][c].wall[NORTH].drawn) { StdDraw.line(c, r+1, c+1, r+1); map[r][c].wall[NORTH].drawn = true; }
				if (map[r][c].wall[WEST].present && !map[r][c].wall[WEST].drawn) { StdDraw.line(c, r, c, r+1); map[r][c].wall[WEST].drawn = true; }
				if (map[r][c].wall[SOUTH].present && !map[r][c].wall[SOUTH].drawn) { StdDraw.line(c, r, c+1, r); map[r][c].wall[SOUTH].drawn = true; }
			}
		}
	} // end of draw()

	
	@Override
	public void drawFtPrt(Cell cell) {
 		// record every cell drawn
 		isRecorded[cell.r][cell.c] = true;

		// draw nothing if visualization is switched off
		if (!isVisu)
			return;
		
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledCircle(cell.c + 0.5, cell.r + 0.5, 0.25);
	} // end of drawFtPrt()
	

        @Override
        public boolean validate() {
                boolean isValid = true;
                int pathLength = 0;
                int count = 0;

                int stepCount[][] = new int[sizeR][sizeC];
                Queue<Cell> queue = new LinkedList<Cell>();

                queue.add(entrance);
                stepCount[entrance.r][entrance.c] = 1;

                while (!queue.isEmpty()) {
                        Cell cell = queue.poll();
                        count++;
                        int step = stepCount[cell.r][cell.c];

                        for (int i = 0; i < Maze.NUM_DIR; i++) {
                                Cell next = cell.neigh[i];
                                if (next != null && !cell.wall[i].present && isRecorded[next.r][next.c] && stepCount[next.r][next.c] == 0) {
                                        stepCount[next.r][next.c] = step + 1;
                                        queue.add(next);
                                }
                        }
                }

                if (stepCount[exit.r][exit.c] == 0) {
                        isValid = false;
                        System.out.println("[Validation] Exit is not reached.");
                }
                else {
                        pathLength = stepCount[exit.r][exit.c];
                }

                for (int i = 0; i < sizeR; i++){
                        for (int j = 0; j < sizeC; j++) {
                                if (isValid && isRecorded[i][j] && stepCount[i][j] == 0) {
                                        isValid = false;
                                        System.out.println("[Validation] Visited cell not reachable.");
                                }
                        }
                }

                if (isValid) {
                        System.out.println("[Validation] Number of cells visited = " + count);
                        System.out.println("[Validation] Path length of the solution = " + pathLength);
                }

                return isValid;
        } // end of validate()

} // end of class NormalMaze
