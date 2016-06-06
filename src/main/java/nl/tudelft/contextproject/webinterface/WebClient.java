package nl.tudelft.contextproject.webinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Class to represent connected web clients.
 */
public class WebClient {
	private Boolean team;
	private Map<Action, List<Long>> performedActions;

	/**
	 * Constructor for a WebClient.
	 */
	public WebClient() {
		team = null;
		performedActions = new HashMap<>();
	}

	/**
	 * @return
	 * 		if this client is an elf
	 */
	public boolean isElf() {
		if (team == null) {
			return false;
		}
		
		return team;
	}
	
	/**
	 * @return
	 * 		if this client is a dwarf
	 */
	public boolean isDwarf() {
		if (team == null) {
			return false;
		}
		
		return !team;
	}
	
	/**
	 * Team names are None, Elves and Dwarfs.
	 * 
	 * @return
	 * 		the name of the team of this player
	 */
	public String getTeam() {
		if (team == null) {
			return "None";
		} else if (team) {
			return "Elves";
		} else {
			return "Dwarfs";
		}
	}
	
	/**
	 * Sets the team of this client.
	 * Also resets the performedActions hashmap.
	 * 
	 * <ul>
	 * <li><code>null</code> = no team</li>
	 * <li><code>true</code> = Elves</li>
	 * <li><code>false</code> = Dwarves</li>
	 * </ul>
	 * 
	 * @param team
	 * 		the team of this client
	 */
	public void setTeam(Boolean team) {
		this.team = team;
		resetPerformed();
	}

	/**
	 * Reset the performed actions for the web client.
	 */
	public void resetPerformed() {
		this.performedActions.clear();
		if (team == null) return;
		if (team) {
			setUpPerformedElves();
		} else {
			setUpPerformedDwarfs();
		}
	}

	/**
	 * Create sets in the HashMap for all elves actions.
	 */
	private void setUpPerformedElves() {
		performedActions.put(Action.DROPBAIT, new ArrayList<>());
		performedActions.put(Action.PLACETILE, new ArrayList<>());
	}

	/**
	 * Create sets in the HashMap for all dwarfs actions.
	 */
	private void setUpPerformedDwarfs() {
		performedActions.put(Action.PLACEMINE, new ArrayList<>());
		performedActions.put(Action.PLACEBOMB, new ArrayList<>());
		performedActions.put(Action.PLACEPITFALL, new ArrayList<>());
		performedActions.put(Action.SPAWNENEMY, new ArrayList<>());
	}

	/**
	 * @return
	 * 		the map containing the actions performed by this client
	 */
	public Map<Action, List<Long>> getPerformedActions() {
		return performedActions;
	}

	@Override
	public String toString() {
		return "WebClient<team=" + getTeam() + ">";
	}
}
