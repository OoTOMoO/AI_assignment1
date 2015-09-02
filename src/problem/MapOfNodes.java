package problem;

import java.util.ArrayList;
import java.util.List;

import problem.ArmConfig;

public class MapOfNodes {
	
	
	Node current;
	List<Node> nodes;

	public MapOfNodes(List<Node> nodes) {
		this.nodes = nodes;
		this.current = current;
	}
	
	public Node getNode(Node n) {
		for (Node x : nodes) {
			if (x.getArm().equals(n.getArm())) {
				return x;
			}
		}
		return null;
	}
	
	public Node getNode(ArmConfig arm) {
		for (Node x : nodes) {
			if (x.getArm().equals(arm)) {
				return x;
			}
		}
		return null;
	}
	
	
}
