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
		
		List<Integer> t1 = new ArrayList<Integer>();
		
			  
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
			if (checkStraightPath(test.getInitialState(), test.getGoalState(), test)) {
				test = armMove(test.getInitialState(), test.getGoalState(), test);
			} else {
				
				
			}
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
	
	// A* Search algorithm
	public static void aStarSearch(ProblemSpec problem){
		
		
		// Change to whichever Heuristic
		Heuristic h = new Heuristic2();
		
		// Sample nodes
		List<ArmConfig> samples = randomSample(problem, 1000);
		
		MapOfNodes nodes = new MapOfNodes(sampleNodes(samples, problem, h));
		
		// Init List and add start node to open list
		List<ArmConfig> open = new ArrayList<ArmConfig>();
		List<ArmConfig> close = new ArrayList<ArmConfig>();
		open.add(problem.getInitialState());
		ArmConfig current;
		double cost;
		
		while (!open.isEmpty()) {
			// Get lowest g cost in open list
			current = open.get(0);
			for (int i = 1 ; i < open.size() ; i++) {
				if (nodes.getNode(current).getT() > nodes.getNode(open.get(i)).getT()) {
					current = open.get(i);
				}
			}
			// Take lowest out of open list and add to close
			close.add(current);
			open.remove(open.indexOf(current));
			for (Node neighbours : nodes.getNode(current).getPath()) {
				// Change cost
				cost = (maxMoves(neighbours.getArm(), current) + nodes.getNode(current).getG());
				if (cost > nodes.getNode(neighbours).getG()) {
					nodes.getNode(neighbours).setG(cost);
					nodes.getNode(neighbours).setParent(nodes.getNode(current));
				}
				if (!open.contains(neighbours.getArm()) && !close.contains(neighbours.getArm())) {
					open.add(neighbours.getArm());
					
				}
			}
		}
	
		//nodes.getNode(current)
		
	}
	
	public static List<Node> sampleNodes(List<ArmConfig> sample, ProblemSpec p, Heuristic h) {
		ArmConfig start = p.getInitialState();
		ArmConfig end = p.getGoalState();
		List<Node> list = new ArrayList<Node>();
		Node n;
		n = new Node(start, 0, p, h);
		n.setG(0);
		list.add(n);
		list.add(new Node(end, 0, p, h));
		for (ArmConfig arm : sample) {
			n = new Node(arm, 0, p, h);
			list.add(n);
		}
		for (int x = 0 ; x < list.size() ; x++) {
			int y = x;
			while (y<list.size()) { 
				if (checkStraightPath(list.get(x).getArm(), list.get(y).getArm(), p)) {
					list.get(x).addPath(list.get(y));
					list.get(y).addPath(list.get(x));
				}
				y++;
			}
		}
		return list;
	}
	
	
	// Check if path between start ArmConfig and end ArmConfig within the selected ProblemSpec
	// 	Return true if straight path is available
	// 	Return false if can't go straight
	public static boolean checkStraightPath(ArmConfig start, ArmConfig end, ProblemSpec problem) {
		
		int links = problem.getJointCount();
		List<Line2D> startPoints = start.getLinks();
		List<Line2D> endPoints = end.getLinks();
		List<Line2D> straightPath = new ArrayList<Line2D>();
		Line2D current;
		
		for (int i = 1; i < links ; i++) {
			current = new Line2D.Double(startPoints.get(i).getX2(), startPoints.get(i).getY2(),
					endPoints.get(i).getX2(), endPoints.get(i).getY2());
			if (!checkStraightPath(current, problem)) return false;
		}
		
		return true;
	}
	
	// Check if an individual line collides with any obstacle or is out of bounds in a ProblemSpec
	// Return false if any collision or out of bounds else True
	public static boolean checkStraightPath(Line2D line, ProblemSpec problem) {
		if (hitObject(problem, line) || outofbounds(line) ) {
			return false;
		}
		return true;
	}
	
	// Returns the moves required to move from start to end ArmConfig
	// Return in long variable.
	public static long maxMoves(ArmConfig start, ArmConfig end) {
		
		long steps = 0;
		Double max = new Double("0.001");
		steps = Math.round(start.getBase().distance(end.getBase())/max);
		long stepsNeeded = 0;
		
		List<Double> startLinks = start.getJointAngles();
		List<Double> endLinks = end.getJointAngles();
		
		for (int i = 0; i < startLinks.size();i++){
			//System.out.println("Angle diff " + (startLinks.get(i) - endLinks.get(i)));
			stepsNeeded = Math.round(Math.abs((startLinks.get(i) - endLinks.get(i))/maxAngleChange));
			//System.out.println("Steps needed "+ i + " " + steps);
			if (steps < stepsNeeded) steps = stepsNeeded;
		}
		
		//System.out.println("Steps needed " + steps);

		return steps + 1;
	}
	
	// Returns a List of double of angle change for each link within a certain amount of moves
	// This function assumes that the moves will allow the function to give a radian less max angle change
	// Each double is the angle change in Radian
	// This will cater for change from start to end ArmConfig
	public static List<Double> angleChange(ArmConfig start, ArmConfig end, long moves) {
		
		//System.out.println(moves + " used");
		
		List<Double> change = new ArrayList<Double>();
		Double current;
	
		List<Double> startLinks = start.getJointAngles();
		List<Double> endLinks = end.getJointAngles();
		
		for (int i = 0; i < startLinks.size();i++){
			current = (endLinks.get(i) - startLinks.get(i))/moves;
			change.add(current);
			//if (current > maxAngleChange || current < (maxAngleChange*-1)) System.err.println("Angle change too big");
		}

		return change;
	}
	
	// Adds path of move from start to end ArmConfig to problem spec and returns it
	public static ProblemSpec armMove(ArmConfig start, ArmConfig end, ProblemSpec problem) {
		ArmConfig current = start;
		Point2D nextPoint;
		ArmConfig nextArm;
		Double max = new Double("0.001");
		List<ArmConfig> path = problem.getPath();
		if (path == null) path = new ArrayList<ArmConfig>();
		double r = GetRadianOfLineBetweenTwoPoints(start.getBase(), end.getBase());
		long moves = maxMoves(start, end);
		List<Double> angleChange = angleChange(start, end, moves);
		Double speed = start.getBase().distance(end.getBase())/(moves);
		//for (Double d : angleChange) System.out.println(d.toString());
		List<Double> newLinks;
		// X change by cos
		// Y change by sin
		path.add(current);
		//System.out.println(current.toString());
		for (long i = 0; i < moves; i++) {
			nextPoint = new Point2D.Double(current.getBase().getX() + (Math.cos(r)*speed), current.getBase().getY() + (Math.sin(r)*speed)); 
			newLinks = new ArrayList<Double>();
			for (int y = 0; y < angleChange.size();y++) {
				newLinks.add(current.getJointAngles().get(y) + angleChange.get(y));
			}	
			current = new ArmConfig(nextPoint, newLinks);
			path.add(current);
		}
		
		//Final move
		if (!current.equals(end)){
			if (canMoveArm(current, end, problem)) {
				path.add(end);
			} else {
				System.err.println(current.toString());
				System.err.println(end.toString());
				System.err.println("Final move error");
			}
		}
		
		problem.setPath(path);
		return problem;
	}
	
	// Random Sample possible armconfig in problem space x amounts of time
	// This function returns a List of ArmConfig of size x
	public static List<ArmConfig> randomSample(ProblemSpec problem, int x) {
		
		List<ArmConfig> answer = new ArrayList<ArmConfig>();
		for (int i = 0; i < x; i++) {
			answer.add(randomArm(problem));
			//answer.add(randomArmCopy(problem));
		}
		return answer;
		
	}
	
	// Returns a random single ArmConfig
	public static ArmConfig randomArm(ProblemSpec problem) {
		
		Point2D base = new Point2D.Double(Math.random(), Math.random());
		List<Double> links = new ArrayList<Double>();
		for (int i = 0; i < problem.getJointCount(); i++) {
			links.add((Math.random() * 2 * radianLimit) - radianLimit);
		}
		
		ArmConfig answer = new ArmConfig(base, links);
		if (outofbounds(answer) || hitObject(problem, answer)) {
			answer = randomArm(problem);
		}
		return answer;
	}
	
	/*
	public static ArmConfig randomArmCopy(ProblemSpec problem) {
		
		ArmConfig copy = problem.getPath().get(problem.getPath().size());
		Point2D base = new Point2D.Double(Math.random(), Math.random());
		List<Double> links = copy.getJointAngles();
		
		ArmConfig answer = new ArmConfig(base, links);
		if (outofbounds(answer) || hitObject(problem, answer)) {
			answer = randomArmCopy(problem);
		}
		return answer;
	}
	*/
	
	// Return the radian of angle between 2 points
	public static double GetRadianOfLineBetweenTwoPoints(Point2D p1, Point2D p2) {
		double xDiff = p2.getX() - p1.getX();
		double yDiff = p2.getY() - p1.getY();
		//return Math.toDegrees(Math.atan2(yDiff, xDiff));
		return Math.atan2(yDiff, xDiff);
	} 
	
	// Check if next move has collided with objects within the problem spec
	// Return true if object is hit,  else Return False
	public static boolean hitObject(ProblemSpec problem, ArmConfig nextMove){
		
		List<Obstacle> objectList = problem.getObstacles();
		List<Line2D> lineList = nextMove.getLinks();
		for (Line2D l : lineList) {
			if (hitObject(problem, l)) {
				System.err.println("Hit obj");
				return true;
			}
		}
		return false;
	}
	
	// Check if Line has collided with objects in problem spec
	// Return true if object is hit,  else Return False
	public static boolean hitObject(ProblemSpec problem, Line2D line){
		
		List<Obstacle> objectList = problem.getObstacles();
		for (Obstacle o : objectList) {
		Rectangle2D rect = o.getRect();
			if (LineIntersectsRect(line, rect)) {
				return true;
			}
		}
		return false;
	}
	
	// Check if move has exit the bounds of 1,1
	// Return true if object is hit,  else Return False
	 public static boolean outofbounds(ArmConfig move) {
		 List<Line2D> lines = move.getLinks();
			int i = 0;
			for (Line2D line : lines) {
				if (outofbounds(line)) {
					return true;
				}
				/* for (int z = i ; z < lines.size() ; z++) {
					if ( LineIntersectsLine(lines.get(z).getP1(), lines.get(z).getP2(),line.getP1(),line.getP2())) {
						System.err.println("Here");
						return true;
					}
				} */
				i++;
			}
		return false;
	 }
	
	// Check if Line has exit the bounds of 1,1
	// Return true if object is hit,  else Return False
	 public static boolean outofbounds(Line2D line) {
		 	// Doesn't check for if Line is completely out of bounds
		 	Point2D.Double p1 = new Point2D.Double(line.getX1(), line.getY1());
		 	Point2D.Double p2 = new Point2D.Double(line.getX2(), line.getY2());
		 	Double zero = new Double("0");
		 	Double one = new Double("1");
		 	Rectangle2D rect = new Rectangle2D.Double(zero, zero, one, one);
		 	
		 	if (!(rect.contains(p1) && rect.contains(p2))) {
		 		System.err.println("Out of bounds: not in bounds completely");
		 		return true;
		 	}

		 	
		 	if (LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY()), new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight())) ||
	               LineIntersectsLine(p1, p2, new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight()), new Point2D.Double(rect.getX(), rect.getY()))
	               )  { System.err.println("Out of bounds: midway out of bounds"); return true; }
		 	
		 	return false;
	 }
	 
	 // Return boolean value of if line is within rect
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
	 
	 
	// Return boolean value of if line(l1p1 + l1p2) is intersect with another line(l2p1 + l2p2)
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
	 
	 // Checks if a current ArmConfig can move to another ArmConfig in a problem spec
	 // Return True if move is valid, else Return false;
	 public static boolean canMoveArm(ArmConfig current, ArmConfig move, ProblemSpec problem) {
		
		List<Double> clinks = current.getJointAngles();
		List<Double> mlinks = move.getJointAngles();
		List<Line2D> links = move.getLinks();
		Double angleDiff;
		int y = 0;
		
		if (current.getBase().distance(move.getBase()) > new Double("0.001")) {
			System.err.println("Distance check");
			return false;
		}
		
		// angle check
		for (int i = 0; i < problem.getJointCount(); i++) {
			angleDiff = clinks.get(i) - mlinks.get(i);
			if (angleDiff > maxAngleChange || angleDiff < (maxAngleChange * -1)) {
//				System.err.println(angleDiff.toString());
//				System.err.println(maxAngleChange);
				System.err.println("angle check");
				return false;
			}
		}
		
		if (hitObject(problem, move) || outofbounds(move)) {
			System.err.println("collision check");
			return false;
		}
		// check arms overlap
		/*for (Line2D l : links) {
			for (int z = 1 ; )
		}*/

		// Check for -150 and 150
		for (Double d : move.getJointAngles()) {
			if (d > radianLimit || d < (radianLimit * -1)) {
				System.err.println("angle limit check");
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