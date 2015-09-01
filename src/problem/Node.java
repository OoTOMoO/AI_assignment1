package problem;

import java.util.ArrayList;
import java.util.List;

import problem.ArmConfig;

public class Node {
	
	ArmConfig arm;
	List<Node> pathTo;
	boolean closed = false;

	public Node(ArmConfig arm) {
		this.arm = arm;
	}
	
	public void closeNode() {
		this.closed = true;
	}
	
	public void addPath(Node n) {
		this.pathTo.add(n);
	}

}
