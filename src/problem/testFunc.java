package problem;

//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.NoSuchElementException;
//import java.util.Scanner;
//import java.awt.Point;
import java.lang.Double;
import java.awt.geom.*;
import java.util.List;

public abstract class testFunc {
	
	public static void collisionTest() {
		
		//Double d = new Double("0.1");
	 	Point2D.Double tp1 = new Point2D.Double(new Double("0"), new Double("0"));
	 	Point2D.Double tp2 = new Point2D.Double(new Double("1"), new Double("1"));
		Line2D testL1 = new Line2D.Double(tp1, tp2);
		Rectangle2D testR1 = new Rectangle2D.Double(new Double("1"), new Double("1"), new Double("8"), new Double("8"));
		
		System.out.println(a1.LineIntersectsRect(testL1, testR1));
		//System.out.println(a1.outofbounds(testL1));
	}
	
	public static void angleTest() {
		
	 	Point2D.Double tp1 = new Point2D.Double(new Double("0"), new Double("0"));
	 	Point2D.Double tp2 = new Point2D.Double(new Double("0"), new Double("1"));
	 	
	 	System.out.println(a1.GetRadianOfLineBetweenTwoPoints(tp1, tp2));
	 	System.out.println(Math.toDegrees(a1.GetRadianOfLineBetweenTwoPoints(tp1, tp2))+" in degrees");

	}
	
	public static void moveTest(ProblemSpec test) {
		
		ProblemSpec problem = test;
	 	
		ArmConfig start = problem.getInitialState();
	 	ArmConfig end = problem.getGoalState();
		ProblemSpec answer = a1.armMove(start, end, problem);
	 	
	 	List<ArmConfig> path = answer.getPath();
	 	for (ArmConfig a : path) {
	 		System.out.println(a.toString());
	 	}

	}
	
	public static void randomTest(ProblemSpec test) {
		
		List<ArmConfig> list = a1.randomSample(test, 100);
	 	
		for (ArmConfig a : list) {
	 		System.out.println(a.toString());
	 	}

	}
	
	
	
}
