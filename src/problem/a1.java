package problem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.*;
import java.lang.Double;


public class a1 {
	
	static double radianLimit = new Double("2.61799388"); 
	static double maxAngleChange = new Double("0.00174532925");
	
	public static void main(String args[]) throws IOException {

		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
			  
			  
		//Tests
		//testFunc.collisionTest();
		//testFunc.angleTest();
		
		if (args.length != 2) {
			System.err.println("Invalid command line arguments\n");
			System.exit(0);
		}
		
		ProblemSpec test = new ProblemSpec();
		
		try {
			//Start
			//System.err.println("Load problem file");
			test.loadProblem(args[0]);
			
			//testFunc.randomTest(test);
			//testFunc.moveTest(test);
			
			//Check if direct path is available
			test = armMove(test.getInitialState(), test.getGoalState(), test);
			// else Sample
			
			
			//Search
			//aStarSearch(test);
			
			test.saveSolution(args[1]);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		// Finish
		try {
			//test.saveSolution(args[1]);
			//createEmptySolution(test, args[1]);
		} catch (Exception e) {
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
	
	public static ProblemSpec armMove(ArmConfig start, ArmConfig end, ProblemSpec problem) {
		ArmConfig current = start;
		Point2D nextPoint;
		ArmConfig nextArm;
		Double max = new Double("0.001");
		List<ArmConfig> path = problem.getPath();
		if (path == null) path = new ArrayList<ArmConfig>();
		double r = GetRadianOfLineBetweenTwoPoints(start.getBase(), end.getBase());
		// X change by cos
		// Y change by sin
		path.add(current);
		//System.out.println(current.toString());
		while(true){
			nextPoint = new Point2D.Double(current.getBase().getX() + (Math.cos(r)*max), current.getBase().getY() + (Math.sin(r)*max)); 
			current = new ArmConfig(nextPoint, current.getJointAngles());
			path.add(current);
			//System.out.println(current.toString());
			if (current.getBase().distance(end.getBase()) <= max) {
				nextPoint = end.getBase(); 
				current = new ArmConfig(nextPoint, current.getJointAngles());
				path.add(current);
				//System.out.println(current.toString());
				break;
			}
		}
		problem.setPath(path);
		return problem;
		
	}
	
	public static List<ArmConfig> randomSample(ProblemSpec problem, int x) {
		
		List<ArmConfig> answer = new ArrayList<ArmConfig>();
		
		for (int i = 0; i < x; i++) {
			answer.add(randomArm(problem));
		}
		return answer;
		
	}
	
	public static ArmConfig randomArm(ProblemSpec problem) {
		
		
		Point2D base = new Point2D.Double(Math.random(), Math.random());
		List<Double> links = new ArrayList<Double>();
		
		for (int i = 0; i < problem.getJointCount(); i++) {
			links.add((Math.random() * 2 * radianLimit) - radianLimit);
		}
		
		ArmConfig answer = new ArmConfig(base, links);
		return answer;
	}
	
	
	public static double GetRadianOfLineBetweenTwoPoints(Point2D p1, Point2D p2) {
		double xDiff = p2.getX() - p1.getX();
		double yDiff = p2.getY() - p1.getY();
		//return Math.toDegrees(Math.atan2(yDiff, xDiff));
		return Math.atan2(yDiff, xDiff);
	} 
	
	public static void aStarSearch(ProblemSpec problem){
		
		// Change to whichever Heuristic
		Heuristic h = new ZeroHeuristic();
		
		List<ArmConfig> closedSet = new ArrayList<ArmConfig>();
		List<ArmConfig> openSet = new ArrayList<ArmConfig>();
		List<ArmConfig> currentPath = new ArrayList<ArmConfig>();
		List<ArmConfig> finalPath = new ArrayList<ArmConfig>();
		ArmConfig currentArm = problem.getInitialState();
		Double currentCost = new Double("0");
		Double inf = Double.POSITIVE_INFINITY;
		boolean found = false;
		
		openSet.add(problem.getInitialState());
		
		while(!found) {
			if (currentArm.equals(problem.getGoalState())) {
				// change answer to finalPath
				found = true;
				problem.setPath(finalPath);
			} else {
				//Search
				
			}
		}
		
		//While openset is not empty
		//	current = node in openset that has the lowest total score
		//	if current = goal
		
		
		
	}
	
	// Check if next move has collided with objects in problem spec
	public static boolean hitObject(ProblemSpec problem, ArmConfig nextMove){
		
		List<Obstacle> objectList = problem.getObstacles();
		List<Line2D> lineList = nextMove.getLinks();
		for (Obstacle o : objectList) {
			Rectangle2D rect = o.getRect();
			for (Line2D l : lineList) {
				if (LineIntersectsRect(l, rect)) {
					return true;
				}
			}
		}
		return false;
	}
	
	 public static boolean outofbounds(ArmConfig move) {
		 List<Line2D> lines = move.getLinks();
			int i = 0;
			for (Line2D line : lines) {
				if (outofbounds(line)) {
					return true;
				}
				for (int z = i ; z < lines.size() ; z++) {
					if ( LineIntersectsLine(lines.get(z).getP1(), lines.get(z).getP2(),line.getP1(),line.getP2())) {
						return true;
					}
				}
				i++;
			}
		return false;
	 }
	
	 public static boolean outofbounds(Line2D line) {
		 	// Doesn't check for if Line is completely out of bounds
		 	Point2D.Double p1 = new Point2D.Double(line.getX1(), line.getY1());
		 	Point2D.Double p2 = new Point2D.Double(line.getX2(), line.getY2());
		 	Double zero = new Double("0");
		 	Double one = new Double("1");
		 	Rectangle2D rect = new Rectangle2D.Double(zero, zero, one, one);
		 	
		 	if (!(rect.contains(p1) && rect.contains(p2))) {
		 		return true;
		 	}

		 	return LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY()))
	               ;
	 }
	 
	 public static boolean LineIntersectsRect(Line2D line, Rectangle2D rect) {
		 	
		 	Point2D.Double p1 = new Point2D.Double(line.getX1(), line.getY1());
		 	Point2D.Double p2 = new Point2D.Double(line.getX2(), line.getY2());

		 	return (rect.contains(p1) || rect.contains(p2));
		 	/*||
		 			LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY())) ||
	               (rect.contains(p1) && rect.contains(p2)); */
	 }
	 
	 

	 private static boolean LineIntersectsLine(Point2D l1p1, Point2D l1p2, Point2D l2p1, Point2D l2p2){
	 
		 double q = (l1p1.getY() - l2p1.getY()) * (l2p2.getX() - l2p1.getX()) - (l1p1.getX() - l2p1.getX()) * (l2p2.getY() - l2p1.getY());
		 double d = (l1p2.getX() - l1p1.getX()) * (l2p2.getY() - l2p1.getY()) - (l1p2.getY() - l1p1.getY()) * (l2p2.getX() - l2p1.getX());
		
		 if( d == 0 ){
			 return false;
		 }

		 double r = q / d;

		 q = (l1p1.getY() - l2p1.getY()) * (l1p2.getX() - l1p1.getX()) - (l1p1.getX() - l2p1.getX()) * (l1p2.getY() - l1p1.getY());
		 double s = q / d; 

		 if( r < 0 || r > 1 || s < 0 || s > 1 ){
			 return false;
		 }
		 return true;
	 }
	 
	public static boolean canMoveArm(ArmConfig current, ArmConfig move, ProblemSpec problem) {
		
		List<Double> clinks = current.getJointAngles();
		List<Double> mlinks = move.getJointAngles();
		List<Line2D> links = move.getLinks();
		Double angleDiff;
		int y = 0;
		
		if (current.getBase().distance(move.getBase()) > new Double("0.001")) {
			return false;
		}
		
		// angle check
		for (int i = 0; i < problem.getJointCount(); i++) {
			angleDiff = clinks.get(i) - mlinks.get(i);
			if (angleDiff > maxAngleChange || angleDiff < (maxAngleChange * -1)) {
				return false;
			}
		}
		
		if (hitObject(problem, move) || outofbounds(move)) {
			return false;
		}
		// check arms overlap
		/*for (Line2D l : links) {
			for (int z = 1 ; )
		}*/

		// Check for -150 and 150
		for (Double d : move.getJointAngles()) {
			if (d > radianLimit || d < (radianLimit * -1)) {
				return false;
			}
		}
	
		return true;
	}
	
	
	/*
	public boolean outOfbounds(ArmConfig x) {
		List<Line2D> lines = x.getLinks();
		for (Line2D l : lines) {
			l.
		}
		List<Point2D> points = line
		
	} */	
	

}