package nl.tudelft.contextproject.model.level.util;

/**
 * MST edge. 
 */
public class MSTEdge {
	
	public int startID;
	public int endID;
	public double weight;
	public int corridorID;
	
	/**
	 * Constructor.
	 * @param startID
	 * 					start node ID
	 * @param endID
	 * 					end node ID
	 * @param weight
	 * 					weight of edge
	 * @param corridorID
	 * 					id of old corridorID
	 */
	public MSTEdge(int startID, int endID, double weight, int corridorID) {
		this.startID = startID;
		this.endID = endID;
		this.weight = weight;
		this.corridorID = corridorID;		
	}

}
