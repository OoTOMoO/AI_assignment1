package problem;

public class Heuristic2 implements Heuristic {

	double inf = Double.POSITIVE_INFINITY;
	
	@Override
	public double estimate(ArmConfig move, ArmConfig end) {
		return move.maxDistance(end)*10000;
	}
	
	@Override
	public double estimate(ArmConfig move, ProblemSpec problem) {
		if (a1.hitObject(problem, move) || a1.outofbounds(move)) {
			return inf;
		}
		return move.maxDistance(problem.getGoalState())*10000;
	}
	
	
}
