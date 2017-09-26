package maze;
import java.awt.Color;
import java.util.*;

/**
 * Class of a rectangular maze contains several tunnels each of which connecting a pair of cells.
 * 
 * @author Jeffrey Chan
 * @author Youhan Xia
 *
 */
public class TunnelMaze extends NormalMaze {
	public TunnelMaze() {
		type = TUNNEL;
	} // end of TunnelMaze()

	@Override
	public void initMaze(int rs, int cs, int entR, int entC, int exitR, int exitC, List<int[]> tunnelList) {
		super.initMaze(rs, cs, entR, entC, exitR, exitC, tunnelList);
		for (int i = 0; i < tunnelList.size(); i++) {
			map[tunnelList.get(i)[0]][tunnelList.get(i)[1]].tunnelTo = map[tunnelList.get(i)[2]][tunnelList.get(i)[3]];
			map[tunnelList.get(i)[2]][tunnelList.get(i)[3]].tunnelTo = map[tunnelList.get(i)[0]][tunnelList.get(i)[1]];
		}
	} // end of initMaze()
	
	@Override
	public boolean isPerfect() {
		boolean visited[][] = new boolean[sizeR][sizeC];
		Queue<Cell> queue = new LinkedList<Cell>();
		
		queue.add(entrance);
		
		while (!queue.isEmpty()) {
			Cell cell = queue.poll();
			Cell next = null;
			visited[cell.r][cell.c] = true;
			int visitedNeigh = 0;
			if (cell.tunnelTo != null) {
				next = cell.tunnelTo;
				if (visited[next.r][next.c])
					visitedNeigh += 1; 
				else
					queue.add(next);
			}
			for (int i = 0; i < NUM_DIR; i++) {
				next = cell.neigh[i];
				if (!isIn(next) || cell.wall[i].present)
					continue;
				if (visited[next.r][next.c])
					visitedNeigh += 1; 
				else
					queue.add(next);
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
		
		Color[] colors = {Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, 
							Color.PINK, Color.YELLOW, StdDraw.BOOK_BLUE, StdDraw.BOOK_LIGHT_BLUE, 
							StdDraw.BOOK_RED};
		
		// draw the maze
		super.draw();
		
		List<Cell> drawnTunnels = new ArrayList<Cell>();
		
		int numTunnelDrawn = 0;
		
		// draw the tunnels
		for (int r = 0; r < sizeR; r++) {
			for (int c = 0; c < sizeC; c++) {
				if (map[r][c].tunnelTo != null && !drawnTunnels.contains(map[r][c])) {
					StdDraw.setPenColor(colors[numTunnelDrawn % colors.length]);
					StdDraw.setPenRadius(0.005);
					StdDraw.line(c+0.9, r+0.1, c+0.9, r+0.9);
					StdDraw.line(c+0.1, r+0.9, c+0.9, r+0.9);
					StdDraw.line(c+0.1, r+0.1, c+0.1, r+0.9);
					StdDraw.line(c+0.1, r+0.1, c+0.9, r+0.1);
					int rr = map[r][c].tunnelTo.r;
					int cc = map[r][c].tunnelTo.c;
					StdDraw.line(cc+0.9, rr+0.1, cc+0.9, rr+0.9);
					StdDraw.line(cc+0.1, rr+0.9, cc+0.9, rr+0.9);
					StdDraw.line(cc+0.1, rr+0.1, cc+0.1, rr+0.9);
					StdDraw.line(cc+0.1, rr+0.1, cc+0.9, rr+0.1);
					drawnTunnels.add(map[r][c].tunnelTo);
					StdDraw.setPenRadius();
					numTunnelDrawn++;
				}
			}
		}
	} // end of draw()


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

                        if (cell.tunnelTo != null && isRecorded[cell.tunnelTo.r][cell.tunnelTo.c] && stepCount[cell.tunnelTo.r][cell.tunnelTo.c] == 0) {
                                stepCount[cell.tunnelTo.r][cell.tunnelTo.c] = step + 1;
                                queue.add(cell.tunnelTo);
                        }

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

} // end of class TunnelMaze
