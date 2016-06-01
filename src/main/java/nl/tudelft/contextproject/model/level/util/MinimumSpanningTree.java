package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import nl.tudelft.contextproject.model.level.MSTBasedLevelFactory;



/**
 * Minimum spanning tree (MST) algorithm class. 
 * Takes a graph and turns it into a minimum spanning tree,
 * thus this class will remove edges.
 */
public class MinimumSpanningTree {

	public List<RoomNode> roomNodes;
	public List<CorridorEdge> corridorEdges;
	public HashSet<MSTNode> graphNodes;
	public HashSet<MSTEdge> resultMST;

	/**
	 * Constructor.
	 * @param roomNodes
	 * 					RoomNodes of graph
	 * @param corridorEdges
	 * 					CorridorEdges of graph
	 */
	public MinimumSpanningTree(List<RoomNode> roomNodes, List<CorridorEdge> corridorEdges) {
		this.roomNodes = roomNodes;
		this.corridorEdges = corridorEdges;
		this.graphNodes = new HashSet<MSTNode>();
		this.resultMST = new HashSet<MSTEdge>();
	}

	/**
	 * Run Prim's algorithm.
	 * A greedy algorithm to find the minimum spanning tree.
	 * 
	 * First generates a new graph that deals with multiple exits and entrances per room.
	 */
	public void runPimAlgorithm() {
		createTransformedGraph();
		createMST();
	}

	/**
	 * Generate a new graph that deals better with rooms.
	 * 
	 */
	protected void createTransformedGraph() {
		HashMap<DoorLocation, MSTNode> treeNodes = new HashMap<DoorLocation, MSTNode>();
		MSTNode startNode, endNode;
		MSTEdge currentEdge;
		for (CorridorEdge corridorEdge : corridorEdges) {
			//Create RoomNode exit node equivalent, in other words, the start node of the corridor
			startNode = new MSTNode(MSTNodeType.EXIT_NODE, 
					corridorEdge.start, 
					corridorEdge.start.node.id);

			//Create RoomNode entrance node equivalent, in other words, the end node the corridor
			endNode = new MSTNode(MSTNodeType.ENTRANCE_NODE,
					corridorEdge.end,
					corridorEdge.end.node.id);

			//Check if there nodes already existed.
			startNode = treeNodes.getOrDefault(startNode.originalDoor, startNode);
			endNode = treeNodes.getOrDefault(endNode.originalDoor, endNode);

			//Create edge MSTEdge
			currentEdge = new MSTEdge(startNode, endNode, corridorEdge.weight, corridorEdge.id);
			startNode.addEdge(currentEdge);
		}
		int roomID;
		MSTNode currentNode, connectorNode;
		MSTEdge newEdge;
		for (RoomNode roomNode : roomNodes) {
			roomID = roomNode.id;
			Iterator<MSTNode> itMSTNodes = getMSTNodesWithSameRoomID(treeNodes, roomID);
			connectorNode = new MSTNode(MSTNodeType.CONNECTOR_NODE, null, roomID);
			while (itMSTNodes.hasNext()) {
				currentNode = itMSTNodes.next();
				/*
				 * If the current Node is an entrance node:
				 * 		a connection from the entrance node to the connector node has to be created.
				 * If the current node is a exit node:
				 * 		a connection from the connector node to the exit node has to be created.
				 */
				if (currentNode.nodeType == MSTNodeType.ENTRANCE_NODE) {
					newEdge = new MSTEdge(currentNode, connectorNode, 0, -1);
					currentNode.addEdge(newEdge);
				} else {
					newEdge = new MSTEdge(connectorNode, currentNode, 0, -1);
					connectorNode.addEdge(newEdge);
				}
				graphNodes.add(currentNode);
			}
			graphNodes.add(connectorNode);
		}
	}

	/**
	 * Run the actual algorithm.
	 * This method uses Kruskal's algorithm.
	 * First, a priority queue of MSTEdges is constructed with smallest weight first. Since the MSTNode graph is connected,
	 * this means that n - 1 edges from the original MSTNode graph will create a minimum spanning tree.
	 * Then a cluster structure is created. This structure is a HashMap&lt;MSTNode, Set&lt;MSTEdge&gt;&gt;. This means that clusters is
	 * represented by a set of Edges. After that the algorithms start. Kruskal's algorithm checks for the first edge in the 
	 * priority queue: if the clusters of that edges are not equal, or either cluster is still empty, then it means that 
	 * the clusters aren't equal and must be combined. Since they are sets, simply an addall and the adding of the current edge
	 * is enough to define the new clusters. This is done until the MSTNode graph number of nodes minus one edges have been created.
	 */
	protected void createMST() {
		PriorityQueue<MSTEdge> edgesPriorityQueue = new PriorityQueue<>(new MSTEdgeWeightComparator());
		HashMap<MSTNode, HashSet<MSTEdge>> clusterStructure = new HashMap<MSTNode, HashSet<MSTEdge>>(4 * graphNodes.size());
		
		//Add all edges to priority queue and create a cluster for each node
		for (MSTNode node : graphNodes) {
			edgesPriorityQueue.addAll(node.edges);
			clusterStructure.put(node, new HashSet<MSTEdge>());
		}
		
		//Run Kruskal's algorithm
		int numberOfEdgesInCluster = 0;
		MSTEdge currentEdge;
		HashSet<MSTEdge> startCluster, endCluster;
		while (numberOfEdgesInCluster < (graphNodes.size() - 1)) {
			currentEdge = edgesPriorityQueue.poll();
			startCluster = clusterStructure.get(currentEdge.startNode);
			endCluster = clusterStructure.get(currentEdge.endNode);
			
			if (!startCluster.equals(endCluster) || startCluster.isEmpty() || endCluster.isEmpty()) {
				resultMST.add(currentEdge);
				startCluster.addAll(endCluster);
				startCluster.add(currentEdge);
				clusterStructure.put(currentEdge.endNode, startCluster);
			}
			numberOfEdgesInCluster++;
		}
	}

	/**
	 * Find the start node in the new graph.
	 * @return
	 * 		the start node
	 */
	protected MSTNode findStartNode() {
		Iterator<MSTNode> nodes = graphNodes.iterator();
		MSTNode currentNode;
		while (nodes.hasNext()) {
			currentNode = nodes.next();
			if (currentNode.roomNodeID == MSTBasedLevelFactory.START_ROOM_ID 
					&& currentNode.nodeType == MSTNodeType.CONNECTOR_NODE) {
				return currentNode;
			}
		}
		return null; 
	}

	/**
	 * Scan treeNodes HashMap to find nodes that have the same RoomNode ID.
	 * @param treeNodes
	 * 		treeNodes HashMap
	 * @param roomID
	 * 		id of the room
	 * @return
	 * 		iterator of nodes with same ID
	 */
	public Iterator<MSTNode> getMSTNodesWithSameRoomID(HashMap<DoorLocation, MSTNode> treeNodes, int roomID) {
		ArrayList<MSTNode> list = new ArrayList<MSTNode>();
		Iterator<Entry<DoorLocation, MSTNode>> hashIterator = treeNodes.entrySet().iterator();
		MSTNode currentNode;
		while (hashIterator.hasNext()) {
			Entry<DoorLocation, MSTNode> entry = hashIterator.next();

			currentNode = entry.getValue();
			if (currentNode.roomNodeID == roomID) {
				list.add(currentNode);
			}
		}
		return list.iterator();
	}




}
