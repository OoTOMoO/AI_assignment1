package main;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import problem.*;
import visualiser.*;

public class Solution {
	
	
	public static void main(String [] args)
	{
		Container container = new Container();
		//Visualiser v = new Visualiser(container);
		
		VisualisationPanel vp; 
		ProblemSpec spec = new ProblemSpec();
		try {
			spec.loadProblem(args[1]);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		if(spec.problemLoaded()) {
			// if the problem is loaded, start to get the obstacles and joints
			
			
		}
		//problem is loaded, do searching here and generate a solution text document	
		if(spec.solutionLoaded()) {
			//initANimation(); here
		}
		String[] files = new String[2];
		for(int i=0; i<args.length; i++) {
			if(i!=0){
				files[i-1] = args[i];
			}	
		}
		//Visualiser.main(files);
		//vp = new VisualisationPanel(v);	
	}
	private String searching(ProblemSpec spec){
		ArmConfig initalState = spec.getInitialState();
		ArmConfig goalState = spec.getGoalState();
		List<Obstacle> obs = spec.getObstacles();
		return "";
	}
	
	

}
