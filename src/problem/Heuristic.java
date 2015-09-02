package problem;

public interface Heuristic {
	/**
	 * Returns an estimate of the cost of reaching the goal state from the given
	 * state.
	 * 
	 * @param n
	 *            the node.
	 * @return an estimate of the cost to the goal.
	 */
	public double estimate(ArmConfig start, ArmConfig end);
	
	public double estimate(ArmConfig start, ProblemSpec p);
}
