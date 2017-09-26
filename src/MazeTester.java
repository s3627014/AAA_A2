import java.io.*;
import java.util.*;

import maze.*;
import mazeGenerator.*;
import mazeSolver.*;


/**
 * 
 * @author Youhan Xia
 * @author Jeffrey Chan
 * @author Yongli Ren
 * Main class for testing the maze generators and solvers. 
 */
class MazeTester {
	
	/** 
	 * Name of class, used in error messages. 
	 */
	protected static final String progName = "MazeTester";

	/** 
	 * Standard outstream.
	 */
	protected static final PrintStream outStream = System.out;

	/**
	 * Print help/usage message.
	 */
	public static void usage(String progName) {
		System.err.println(progName + ": [input fileName] <visualize maze>");
		System.err.println("<visualize maze> = <y | n>");
		System.exit(1);
	} // end of usage()

	/**
	 * Main function of tester.
	 * @param args Two arguments which are input filename and "y/n" indicating whether to visualize the maze. 
	 */
	public static void main(String[] args) {
		// read command line arguments
		if (args.length != 2) {
			System.err.println("Incorrect number of arguments.");
			usage(progName);
		}
		
		String fName = args[0];
		
		// flag to indicate whether we visualise maze or not
		boolean isVisu = false;
		// note that drawFtPrt(Cell) need to be called for validating the solution
		switch (args[1]) {
		case "y":
			isVisu = true;
			break;
		case "n":
			isVisu = false;
			break;
		default:
			System.err.println("Incorrect argument value.");
			usage(progName);
		}
		
		// default values for parameters 
		String mazeType = "normal";
		String mazeGeneratorName = "recurBack";
		String mazeSolverName = "wallFollower";
		int rowNum = 30;
		int colNum = 30;
		int entR = 0;
		int entC = 0;
		int exitR = 0;
		int exitC = 1;
		List<int[]> tunnelList = new ArrayList<int[]>();
		
		File fin = new File(fName);
		
		// read input parameter file
		try {
			Scanner scanner = new Scanner(fin);
			mazeType = scanner.next();
			
			mazeGeneratorName = scanner.next();
			mazeSolverName = scanner.next();
			
			rowNum = Integer.parseInt(scanner.next());
			colNum = Integer.parseInt(scanner.next());
			
			entR = Integer.parseInt(scanner.next());
			entC = Integer.parseInt(scanner.next());
			exitR = Integer.parseInt(scanner.next());
			exitC = Integer.parseInt(scanner.next());

			// add tunnels
			while (scanner.hasNext()) {
				int temp[]= {Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next())};
				tunnelList.add(temp);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Input file doesn't exist.");
			usage(progName);
		}


                // check rowNum and colNum
                if (rowNum < 1 || colNum < 1) {
                        System.err.println("Row or column number of maze must be at least 1.");
                        usage(progName);
                }

		
		// construct maze object 
		Maze maze = null;
		switch (mazeType) {
		case "normal":
			maze = new NormalMaze();
			break;
		case "tunnel":
			maze = new TunnelMaze();
			break;
		case "hex":
			maze = new HexMaze();
			break;
		default:
			System.err.println("Unknown maze type.");
			usage(progName);
		}
		
		// initialise maze
		maze.initMaze(rowNum, colNum, entR, entC, exitR, exitC, tunnelList);

		// check if maze entrance or exit is valid
		if (!maze.isOnEdge(entR, entC) || !maze.isOnEdge(exitR, exitC)) {
			System.err.println("Incorrect maze entrance or exit position.");
			usage(progName);
		}
		
		// determine which implementation to test
		MazeGenerator mazeGen = null;
		switch (mazeGeneratorName) {
		case "modiPrim":
			mazeGen = new ModifiedPrimsGenerator();
			break;
		case "recurBack":
			mazeGen = new RecursiveBacktrackerGenerator();
			break;
		case "growingTree":
			mazeGen = new GrowingTreeGenerator();
			break;
		default:
			System.err.println("Unknown maze generator name.");
			usage(progName);
		}

		outStream.println(mazeGen.getClass().getSimpleName() + " is generating the maze.");

		// generate maze
		mazeGen.generateMaze(maze);

		// update whether maze should be visualised
		maze.isVisu = isVisu;

		// check if maze is perfect
		boolean isPerfectMaze = maze.isPerfect();
		
		outStream.println("The maze is " + (isPerfectMaze ? "" : "not ") + "perfect!");
		
		// draw maze (this depends on maze.isVisu)
		maze.draw();

		// solve the generateed maze if perfect
		if (isPerfectMaze) {
			MazeSolver mazeSolver = null;
			switch (mazeSolverName) {
			case "wallFollower":
				mazeSolver = new WallFollowerSolver();
				break;
			case "biDirrecurBack":
				mazeSolver = new BiDirectionalRecursiveBacktrackerSolver();
				break;
            // sample solver to help you get started
            case "sample":
                mazeSolver = new SampleSolver();
                break;
			// no solver
			case "none":
				break;
			default:
				System.err.println("Unknown maze solver name.");
				usage(progName);
			}

			if (mazeSolver != null) {
				mazeSolver.solveMaze(maze);
				outStream.println(mazeSolver.getClass().getSimpleName() + " is solving the maze.");
				// check if solver can get out of maze
				if (mazeSolver.isSolved()) {
					outStream.println("The maze has been solved!");
					// display number of cells visited for solver
					outStream.println("Number of cells visited = " + mazeSolver.cellsExplored());
					// show results from validation
					outStream.println("Validation result:");
					boolean isValid = maze.validate();
					outStream.println("The solution is " + (isValid ? "" : "not ") + "valid!");
				} else {
					outStream.println("Solver was failed!");
				}
			}
		}
	} // end of main()
} // end of class MazeTester
