package maze;

import java.util.*;

import maze.StdDraw;

/**
 * Class of hexagon maze.
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * 
 */
public class HexMaze extends NormalMaze {
	
	public HexMaze() {
		type = HEX;
	} // end of HexMaze()
	
	
	@Override
	protected boolean isIn(int r, int c) {
		return r >= 0 && r < sizeR && c >= (r + 1) / 2 && c < sizeC + (r + 1) / 2;
	} // end of isIn()
	
	
	@Override
	public boolean isOnEdge(int r, int c) {
		if (isIn(r, c + (r + 1) / 2) && (r == 0 || r == sizeR - 1 || c == 0 || c == sizeC - 1)) 
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
		map = new Cell[sizeR][sizeC + (sizeR + 1) / 2];
		for (int i = 0; i < sizeR; i++){
			for (int j = (i + 1) / 2; j < sizeC + (i + 1) / 2; j++) {
				if (!isIn(i, j))
					continue;
				Cell cell = new Cell(i, j);
				map[i][j] = cell;
				for (int k = 0; k < 3; k++) {
					cell.wall[k] = new Wall();
				}
				for (int k = 3; k < NUM_DIR; k++) {
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
		if (isIn(entR, entC + (entR + 1) / 2))
			entrance = map[entR][entC + (entR + 1) / 2];
		if (isIn(exitR, exitC + (exitR + 1) / 2))
			exit = map[exitR][exitC + (exitR + 1) / 2];

                // set up recording matrix for validation
                isRecorded = new boolean[sizeR][sizeC + (sizeR + 1) / 2];
	} // end of initMaze()
	
	
	@Override
	public boolean isPerfect() {
		boolean visited[][] = new boolean[sizeR][sizeC];
		Queue<Cell> queue = new LinkedList<Cell>();
		
		queue.add(entrance);
		
		while (!queue.isEmpty()) {
			Cell currCell = queue.poll();
			visited[currCell.r][currCell.c - (currCell.r + 1) / 2] = true;
			int visitedNeigh = 0;
			for (int i = 0; i < NUM_DIR; i++) {
				Cell nextCell = currCell.neigh[i];
				if (!isIn(nextCell) || currCell.wall[i].present)
					continue;
				if (visited[nextCell.r][nextCell.c - (nextCell.r + 1) / 2])
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
			for (int j = (i + 1) / 2; j < sizeC + (i + 1) / 2; j++)
				for (int k = 0; k < NUM_DIR; k++) {
					if (map[i][j].wall[k] != null)
						map[i][j].wall[k].drawn = false;
				}
		

		for (int k = 0; k < NUM_DIR; k++) {
			if (entrance.neigh[k] == null) {
				entrance.wall[k].drawn = true;
				break;
			}
		}
		

		for (int k = 0; k < NUM_DIR; k++) {
			if (exit.neigh[k] == null) {
				exit.wall[k].drawn = true;
				break;
			}
		}
		
		StdDraw.setCanvasSize(900, 900);
		StdDraw.setXscale(-1, sizeC + 1.5);
		StdDraw.setYscale(-1, sizeR+1);
		

		// draw entrance
		StdDraw.setPenColor(StdDraw.BLUE);
		if (entrance != null) {
			StdDraw.filledCircle(entrance.r % 2 * 0.5 + entrance.c - (entrance.r + 1) / 2 + 0.5, entrance.r + 0.5, 0.375);
		}

		// draw exit
		StdDraw.setPenColor(StdDraw.RED);
		if (exit != null) {
			StdDraw.filledCircle(exit.r % 2 * 0.5 + exit.c - (exit.r + 1) / 2 + 0.5, exit.r + 0.5, 0.375);
		}        


		// draw walls
		double halfEdge = 1.0 / 3; // Math.sqrt(3) / 6; 
		StdDraw.setPenColor(StdDraw.BLACK);
		for (int r = 0; r < sizeR; r++) {
			for (int c = 0; c < sizeC; c++) {
				double shift = r % 2 * 0.5;
				int cc = c + (r + 1) / 2;
				if (map[r][cc].wall[EAST].present && !map[r][cc].wall[EAST].drawn) { StdDraw.line(c+1+shift, r+0.5-halfEdge, c+1+shift, r+0.5+halfEdge); map[r][cc].wall[EAST].drawn = true; }
				if (map[r][cc].wall[NORTHEAST].present && !map[r][cc].wall[NORTHEAST].drawn) { StdDraw.line(c+0.5+shift, r+0.5+2*halfEdge, c+1+shift, r+0.5+halfEdge); map[r][cc].wall[NORTHEAST].drawn = true; }
				if (map[r][cc].wall[NORTHWEST].present && !map[r][cc].wall[NORTHWEST].drawn) { StdDraw.line(c+0.5+shift, r+0.5+2*halfEdge, c+shift, r+0.5+halfEdge); map[r][cc].wall[NORTHWEST].drawn = true; }
				if (map[r][cc].wall[WEST].present && !map[r][cc].wall[WEST].drawn) { StdDraw.line(c+shift, r+0.5-halfEdge, c+shift, r+0.5+halfEdge); map[r][cc].wall[WEST].drawn = true; }
				if (map[r][cc].wall[SOUTHWEST].present && !map[r][cc].wall[SOUTHWEST].drawn) { StdDraw.line(c+shift, r+0.5-halfEdge, c+0.5+shift, r+0.5-2*halfEdge); map[r][cc].wall[SOUTHWEST].drawn = true; }
				if (map[r][cc].wall[SOUTHEAST].present && !map[r][cc].wall[SOUTHEAST].drawn) { StdDraw.line(c+1+shift, r+0.5-halfEdge, c+0.5+shift, r+0.5-2*halfEdge); map[r][cc].wall[SOUTHEAST].drawn = true; }
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
		StdDraw.filledCircle(cell.r % 2 * 0.5 + cell.c - (cell.r + 1) / 2 + 0.5, cell.r + 0.5, 0.25);
	} // end of drawFtPrt()
	

        @Override
        public boolean validate() {
                boolean isValid = true;
                int pathLength = 0;
                int count = 0;

                int stepCount[][] = new int[sizeR][sizeC + (sizeR + 1) / 2];
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
                        for (int j = 0; j < sizeC + (sizeR + 1) / 2; j++) {
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


} // end of class HexMaze
