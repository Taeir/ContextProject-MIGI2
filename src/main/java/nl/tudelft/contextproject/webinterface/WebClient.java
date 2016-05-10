package nl.tudelft.contextproject.webinterface;

/**
 * Class to represent connected web clients.
 */
public class WebClient {
	private Boolean team;
	
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
		System.out.println("[DEBUG] setteam to " + team);
		this.team = team;
	}
	
	@Override
	public String toString() {
		return "WebClient[team=" + getTeam() + "]";
	}
}
