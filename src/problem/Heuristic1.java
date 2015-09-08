package problem;


public class Heuristic1 implements Heuristic {
	double inf = Double.POSITIVE_INFINITY;
	
	@Override
	public double estimate(ArmConfig move, ArmConfig end) {
		return (move.totalDistance(end))/move.getJointCount();
	}

	public double estimate(ArmConfig move, ProblemSpec problem) {
		if (a1.hitObject(problem, move) || a1.outofbounds(move)) {
			return inf;
		}
		return (move.totalDistance(problem.getGoalState()))/move.getJointCount();
	}
	
	
}
