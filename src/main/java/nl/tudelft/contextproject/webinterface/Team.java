package nl.tudelft.contextproject.webinterface;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum for the different teams.
 */
public enum Team {
	DWARFS,
	ELVES,
	NONE;
	
	private Set<Action> actions = initActions();
	
	/**
	 * Initializes the actions of a team.
	 * 
	 * @return
	 * 		the set of actions for this team
	 */
	private Set<Action> initActions() {
		Set<Action> actions = new HashSet<Action>();
		switch (this.name()) {
			case "DWARFS":
				actions.add(Action.PLACEBOMB);
				actions.add(Action.PLACEMINE);
				actions.add(Action.PLACEPITFALL);
				actions.add(Action.SPAWNENEMY);
				return Collections.unmodifiableSet(actions);
			case "ELVES":
				actions.add(Action.DROPBAIT);
				actions.add(Action.DROPCRATE);
				actions.add(Action.OPENGATE);
				actions.add(Action.PLACETILE);
				return Collections.unmodifiableSet(actions);
			case "NONE":
				return Collections.emptySet();
			default:
				throw new IllegalArgumentException("Not a known team");
		}
	}
	
	/**
	 * @return
	 * 		an unmodifiable set of actions for this team
	 */
	public Set<Action> getActions() {
		return actions;
	}
}
