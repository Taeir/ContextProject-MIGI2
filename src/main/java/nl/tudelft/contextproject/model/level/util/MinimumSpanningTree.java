package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nl.tudelft.contextproject.model.level.MSTBasedLevelFactory;

import java.util.PriorityQueue;

/**
 * Minimum spanning tree (MST) algorithm class. 
 * Takes a graph and turns it into a minimum spanning tree,
 * this minimum tree is not an actual minimum tree as it contains several more edges
 * to ensure that every exit and entrance of a room is connected.
 *
 */
public class MinimumSpanningTree {

	public List<RoomNode> roomNodes;
	public HashMap<Integer, CorridorEdge> corridorEdges;
	public HashSet<MSTNode> graphNodes;
	public MSTNode startRoomNode;

	/**
	 * Constructor.
	 * 
	 * @param roomNodes
	 * 		RoomNodes of graph
	 * @param corridorEdges
	 * 		CorridorEdges of graph in a HashMap with corridor IDs as key
	 */
	public MinimumSpanningTree(List<RoomNode> roomNodes, HashMap<Integer, CorridorEdge> corridorEdges) {
		this.roomNodes = roomNodes;
		this.corridorEdges = corridorEdges;
		this.graphNodes = new HashSet<MSTNode>();
	}

	/**
	 * Run the Reverse Delete algorithm.
	 * A greedy algorithm to find a minimum spanning tree.
	 * 
	 * First generates a new graph that deals with multiple exits and entrances per room.
	 * Then run the algorithm on that graph.
	 */
	public void runReverseDeleteAlgorithm() {
		createTransformedGraph();
		createMSTReverseDelete();
	}

	/**
	 * Generate a new graph that deals better with rooms.
	 * 
	 */
	protected void createTransformedGraph() {
		HashMap<DoorLocation, MSTNode> treeNodes = new HashMap<DoorLocation, MSTNode>();
		MSTNode startNode, endNode;
		MSTEdge currentEdge;
		for (CorridorEdge corridorEdge : corridorEdges.values()) {
			//Create RoomNode exit node equivalent, in other words, the start node of the corridor
			startNode = new MSTNode(MSTNodeType.EXIT_NODE, 
					corridorEdge.start, 
					corridorEdge.start.node.id);

			//Create RoomNode entrance node equivalent, in other words, the end node the corridor
			endNode = new MSTNode(MSTNodeType.ENTRANCE_NODE,
					corridorEdge.end,
					corridorEdge.end.node.id);

			//Check if there nodes already existed.
			if (treeNodes.containsKey(startNode.originalDoor)) {
				startNode = treeNodes.get(startNode.originalDoor);
			} else {
				treeNodes.put(startNode.originalDoor, startNode);
			}

			if (treeNodes.containsKey(endNode.originalDoor)) {
				endNode = treeNodes.get(endNode.originalDoor);
			} else {
				treeNodes.put(endNode.originalDoor, endNode);
			}

			//Create edge MSTEdge
			currentEdge = new MSTEdge(startNode, endNode, corridorEdge.weight, corridorEdge.id);
			startNode.addOutGoingEdge(currentEdge);
		}
		reconnectRoomNodes(treeNodes);
	}

	/**
	 * Reconnect individual MST exit and entrance nodes with a connector node.
	 * This is done by creating an extra node per RoomNode id. The entrance nodes
	 * are then connected to the connector node and the connector node is connected to the
	 * exit nodes. This step makes sure the graphNodes is a connected graph.
	 * 
	 * @param treeNodes
	 * 		HashMap with DoorLocations as key and values as MSTNodes
	 */
	protected void reconnectRoomNodes(HashMap<DoorLocation, MSTNode> treeNodes) {
		int roomID;
		MSTNode currentNode, connectorNode;
		MSTEdge newEdge;
		startRoomNode = null;
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
					currentNode.addOutGoingEdge(newEdge);
				} else {
					newEdge = new MSTEdge(connectorNode, currentNode, 0, -1);
					connectorNode.addOutGoingEdge(newEdge);
				}
				//add the current node with edges to the graph
				graphNodes.add(currentNode);
			}
			//add the connector node with edges to the graph
			graphNodes.add(connectorNode);
			if (connectorNode.roomNodeID == MSTBasedLevelFactory.START_ROOM_ID) {
				startRoomNode = connectorNode;
			}
		}
	}

	/**
	 * Do Kruskal's algorithm in reverse, the Reverse Delete Algorithm.
	 * Using this algorithm we can enforce the additional constraint that
	 * each room exit location has to be connected to at least 1 or more
	 * room entrance locations of another room.
	 * <p>
	 * This algorithm adds reverse edges, for every edge. This lowers to time to check whether
	 * the graph is still connected, as it allows for a breadth first search from node 1 of the removed
	 * edge to node 2 of the removed edge.
	 */
	protected void createMSTReverseDelete() {
		PriorityQueue<MSTEdge> edgesPriorityQueue = new PriorityQueue<>(new MSTEdgeWeightComparatorReverse());

		//Add all edges to priority queue
		for (MSTNode node : graphNodes) {
			edgesPriorityQueue.addAll(node.outgoingEdges);
			
			//For each node, also adds it incoming edge to speed up the constraint checking
			for (MSTEdge outGoingEdge: node.outgoingEdges) {
				MSTNode endNode = outGoingEdge.endNode;
				endNode.addInComingEdge(outGoingEdge);
			}
		}

		//Run the Reverse Delete algorithm
		MSTEdge currentEdge;
		MSTNode startNode, endNode;
		while (!edgesPriorityQueue.isEmpty()) {
			currentEdge = edgesPriorityQueue.poll();

			//Skip edges of weight zero, as they must exist in the tree.
			if (currentEdge.weight == 0) {
				continue;
			}
			
			startNode = currentEdge.startNode;
			endNode = currentEdge.endNode;
			if (startNode.outgoingEdges.size() > 1 
				&& endNode.incomingEdges.size() > 1
				&& checkConnectionAfterRemoval(currentEdge)) {
				startNode.outgoingEdges.remove(currentEdge);
				endNode.incomingEdges.remove(currentEdge);
			}
		}
	}
	
	/**
	 * Check if two nodes are still connected after edge is excluded.
	 * 
	 * @param excludedEdge
	 * 		edge that should be excluded
	 * @return
	 * 		true if nodes are still connected
	 */
	public boolean checkConnectionAfterRemoval(MSTEdge excludedEdge) {
		HashSet<MSTNode> visitedNodes = new HashSet<MSTNode>();
		ArrayDeque<MSTNode> queue = new ArrayDeque<MSTNode>();
			
		MSTNode node1 = excludedEdge.startNode;
		queue.add(node1);
		MSTNode node2 = excludedEdge.endNode;
		MSTNode currentNode, currentEndNode, currentStartNode;
		while (!queue.isEmpty()) {
			currentNode = queue.poll();
			visitedNodes.add(currentNode);
			//Check outgoing edges
			for (MSTEdge currentEdge : currentNode.outgoingEdges) {
				if (!currentEdge.equals(excludedEdge)) {
					currentEndNode = currentEdge.endNode;
					if (currentNode.equals(node2)) {
						return true;
					}
					if (!visitedNodes.contains(currentEndNode)) {
						queue.add(currentEndNode);
					}
				}
			}
			
			//Check incoming edges
			for (MSTEdge currentEdge : currentNode.incomingEdges) {
				if (!currentEdge.equals(excludedEdge)) {
					currentStartNode = currentEdge.startNode;
					if (currentStartNode.equals(node2)) {
						return true;
					}
					if (!visitedNodes.contains(currentStartNode)) {
						queue.add(currentStartNode);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Translate the MST tree back to a list of corridor IDs when using the Reverse algorithm.
	 * 
	 * @return
	 * 		list of corridor IDs
	 */
	public ArrayList<Integer> getCorridorIDsReverseAlgorithm() {
		ArrayList<Integer> corridorIDs = new ArrayList<Integer>();
		int corridorID;
		for (MSTNode node : graphNodes) {
			for (MSTEdge edge : node.outgoingEdges) {
				corridorID = edge.corridorID;
				if (corridorID != -1) {
					corridorIDs.add(corridorID);
				}
			}
		}
		return corridorIDs;
	}

	/**
	 * Scan treeNodes HashMap to find nodes that have the same RoomNode ID.
	 * Used in reconnecting the split rooms with the connector Node.
	 * 
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
