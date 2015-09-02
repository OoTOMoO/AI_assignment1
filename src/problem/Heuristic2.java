package problem;


/**
 * A trivial admissible heuristic that always returns zero. This is the simplest
 * heuristic estimate - it always returns zero, and hence is also 100% useless
 * as a heuristic.
 * 
 * Notably, using this heuristic with A* causes it to function exactly the same
 * as a Uniform Cost Search, hence no separate implementation of UCS is
 * required.
 * 
 * @author lackofcheese
 * @param <S>
 *            the type of state used.
 */
public class Heuristic2 implements Heuristic {
	/**
	 * Returns an estimate (zero) of the cost to reach the goal from the given
	 * state.
	 * 
	 * @param s
	 *            the state.
	 * @return always estimates the cost as zero.
	 */
	
	double inf = Double.POSITIVE_INFINITY;
	
	@Override
	public double estimate(ArmConfig move, ArmConfig end) {
		return move.maxDistance(end);
	}
	
	@Override
	public double estimate(ArmConfig move, ProblemSpec problem) {
		if (a1.hitObject(problem, move) || a1.outofbounds(move)) {
			return inf;
		}
		return move.maxDistance(problem.getGoalState());
	}
	
	
}
