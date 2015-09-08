package problem;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import problem.ArmConfig;

public class MapOfNodes {
	
	
	Node current;
	List<Node> nodes;

	public MapOfNodes(List<Node> nodes) {
		this.nodes = nodes;
		//this.current = current;
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
	
	public void addSample(List<ArmConfig> samples, ProblemSpec p, Heuristic h) {

		Node k;
		List<Node> toadd = new ArrayList<Node>();
		for (ArmConfig arm : samples) {
			k = new Node(arm, 0, p, h);
			toadd.add(k);
		}
		
		List<Node> answer = this.nodes;
			
		for (int x = 0 ; x < toadd.size() ; x++) {
			Node current = toadd.get(x);
			for (Node n2 : answer) {
				if (a1.completePathCheck(current.getArm(), n2.getArm(), p)) {
					current.addPath(n2);
					n2.addPath(current);
				}
			}
			answer.add(current);
		}
		
		this.nodes = answer;
		System.out.println("New samples added");
		
	}
	
	
}
