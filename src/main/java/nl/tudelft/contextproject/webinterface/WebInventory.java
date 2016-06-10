package nl.tudelft.contextproject.webinterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

/**
 * Class for providing a team shared inventory for the web clients.
 */
public class WebInventory {
	private Map<Team, Map<Action, Integer>> inventory = new HashMap<>();
	
	/**
	 * Creates a new WebInventory, with the global maximums set.
	 */
	public WebInventory() {
		reset();
	}
	
	/**
	 * Returns the amount of times the given team can still perform the given action.
	 * If the action is unlimited or not applicable for the team, this method returns -1.
	 * 
	 * @param team
	 * 		the team trying to perform the action
	 * @param action
	 * 		the action to check for
	 * @return
	 * 		the amount of times the given team can still perform the given action
	 */
	public int getActionCount(Team team, Action action) {
		Integer value = inventory.get(team).get(action);
		if (value == null) return -1;
		return value.intValue();
	}
	
	/**
	 * @param team
	 * 		the team attempting to perform the action
	 * @param action
	 * 		the action being performed
	 * @return
	 * 		true if the action was performed, false if the action could not be performed.
	 */
	public boolean performAction(Team team, Action action) {
		Map<Action, Integer> map = inventory.get(team);
		
		synchronized (map) {
			Integer old = map.get(action);
			if (old != null) {
				int oldVal = old.intValue();
				if (oldVal == 0) return false;
				
				if (oldVal > 0) map.put(action, oldVal - 1);
			}
		}
		return true;
	}
	
	/**
	 * Resets the inventory by re adding all action counts.
	 */
	public void reset() {
		for (Team team : Team.values()) {
			Map<Action, Integer> actions = new ConcurrentHashMap<>();
			for (Action action : team.getActions()) {
				if (action.getGlobalMaxAmount() == -1) continue;
				
				actions.put(action, action.getGlobalMaxAmount());
			}
			
			inventory.put(team, actions);
		}
	}
	
	/**
	 * Gets the json for the inventory of the given team.
	 * 
	 * @param team
	 * 		the team to get the json for
	 * @return
	 * 		the JSON encoded team actions
	 */
	public JSONObject toWebJson(Team team) {
		Map<Action, Integer> map = inventory.get(team);
		JSONObject json = new JSONObject();
		for (Entry<Action, Integer> entry : map.entrySet()) {
			json.put("" + entry.getKey().ordinal(), entry.getValue());
		}
		
		return json;
	}
}
