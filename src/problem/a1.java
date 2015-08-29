package problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.awt.geom.*;


public class a1 {
	
	public static void main(String args[]) throws IOException {
		
		if (args.length != 2) {
			System.err.println("Invalid command line arguments\n");
			System.exit(0);
		}
		
		ProblemSpec test = new ProblemSpec();
		
		
		/*

		The number of joints 
			private int jointCount;
		
		The initial configuration 
			private ArmConfig initialState;
		
		The goal configuration 
			private ArmConfig goalState;
		
		The obstacles
			private List<Obstacle> obstacles;

		The path taken in the solution 
			private List<ArmConfig> path;
		
		*/

		ArmConfig currentState;
		
		//System.err.println(test.solutionLoaded());
		
		try {
			//Start
			System.err.println("Load problem file");
			test.loadProblem(args[0]);
			
			currentState = test.getInitialState();
			
			//Search
			aStarSearch(test);
			
			//Finish
			createEmptySolution(test, args[1]);
		} catch (IOException e) {
			System.err.println(e);
		}


	}
	
	public static void createEmptySolution(ProblemSpec test, String filename) throws IOException {
		
		String ls = System.getProperty("line.separator");
		FileWriter output = new FileWriter(filename);
		//output.write(String.format("%d %f%s", test.get - 1, ls));
		//output.write(Integer.toString(test.getJointCount()));
		output.write(test.getInitialState() + ls);
		output.close();
		
	}
	
	public static void aStarSearch(ProblemSpec problem){
		
		// Change to whichever Heuristic
		Heuristic h = new ZeroHeuristic();
		
		problem.getPath();
		List<ArmConfig> finalPath = new ArrayList<ArmConfig>();
		problem.setPath(finalPath);
		
	}
	
	// Check if next move has collided with objects in problem spec
	public static boolean hitObject(ProblemSpec problem, ArmConfig nextMove){
		
		List<Obstacle> objectList = problem.getObstacles();
		List<Line2D> lineList = nextMove.getLinks();
		for (Obstacle o : objectList) {
			Rectangle2D rect = o.getRect();
			for (Line2D l : lineList) {
				if (l.contains(rect)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}