package nl.tudelft.contextproject.model.level.util;

/**
 * MST edge. 
 */
public class MSTEdge {
	
	public MSTNode startNode;
	public MSTNode endNode;
	public double weight;
	public int corridorID;
	
	/**
	 * Constructor.
	 * @param startNode
	 * 					start node ID
	 * @param endNode
	 * 					end node ID
	 * @param weight
	 * 					weight of edge
	 * @param corridorID
	 * 					ID of corridorEdge
	 */
	public MSTEdge(MSTNode startNode, MSTNode endNode, double weight, int corridorID) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.weight = weight;
		this.corridorID = corridorID;		
	}

}
