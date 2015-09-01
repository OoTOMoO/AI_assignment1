package problem;

import java.util.List;

import problem.ArmConfig;

public interface State {
	/**
	 * Returns the successors of this state.
	 * 
	 * @return the successors of this state.
	 */
	public List<State> getSuccessors();

	public double getCost(State successor);

	public List<ArmConfig> getState();
}
