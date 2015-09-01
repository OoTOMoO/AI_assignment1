package problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import problem.State;

public abstract class StateMap implements State {
	/** A mapping holding the successors and their costs */
	Map<StateMap, Double> successsorMap;

	/**
	 * Constructor; initializes the mapping.
	 */
	public StateMap() {
		successsorMap = new HashMap<StateMap, Double>();
	}

	/**
	 * Adds the given state as a successor of the current state, with the given
	 * cost.
	 * 
	 * @param succ
	 *            the successor state.
	 * @param cost
	 *            the edge cost to the successor state.
	 */
	public void addSuccessor(StateMap succ, double cost) {
		this.successsorMap.put(succ, cost);
	}

	@Override
	public List<State> getSuccessors() {
		return new ArrayList<State>(successsorMap.keySet());
	}

	@Override
	public double getCost(State successor) {
		return successsorMap.get(successor);
	}
}
