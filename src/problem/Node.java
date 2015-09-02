package problem;

import java.util.ArrayList;
import java.util.List;

import problem.ArmConfig;

public class Node {
	
	private Node parent = null;
	private ArmConfig arm;
	private List<Node> pathTo;
//	boolean closed = false;
	private double gcost;
	private double hcost;

	public Node(ArmConfig arm, double cost, ProblemSpec p, Heuristic h) {
		this.arm = arm;
		this.gcost = Double.POSITIVE_INFINITY;
		this.hcost = h.estimate(arm, p.getGoalState());
	}
	
	public double getG() {
		return this.gcost;
	}
	
	public double getH() {
		return this.hcost;
	}
	
	public double getT() {
		return this.hcost + this.gcost;
	}

	public void setG(double set) {
		this.gcost = set;
	}
	
	public void setH(double set) {
		this.hcost = set;
	}
	
	public ArmConfig getArm() {
		return this.arm;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public void setParent(Node p) {
		this.parent = p;
	}
	
	public void addPath(Node n) {
		this.pathTo.add(n);
	}
	
	public List<Node> getPath() {
		return this.pathTo;
	}
	
	/*
	public void closeNode() {
		this.closed = true;
	}
	
	public void addPath(Node n) {
		this.pathTo.add(n);
	}
	*/
	
	public boolean equals(Node n) {
		if (this.arm == n.arm) {
			return true;
		}
		return false;
	}

}
