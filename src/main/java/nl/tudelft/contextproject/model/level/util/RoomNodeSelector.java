package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class used for RoomNode selecting.
 * This class holds two classifiers: The number of entrances classifier
 * and the number of exits classifier. 
 * 
 * This class builds the classifiers automatically and provides RoomNodes with the correct
 * number of either entrances or exits.
 * 
 * This class is heavily used by GraphLevel to supply it with the correct rooms.
 */
public class RoomNodeSelector {
	
	//Allow selector to provide duplicate rooms in maze generation
	public static final Boolean ALLOW_DUPLICATE_ROOMS = false;
	
	//Number of entrances classifier
	public ArrayList<ArrayList<RoomNode>> entranceClassifier;
	//Number of exits classifier
	public ArrayList<ArrayList<RoomNode>> exitClassifier;
	
	public RoomNodeSelector(List<RoomNode> nodes) {
		entranceClassifier = new ArrayList<ArrayList<RoomNode>>();
		exitClassifier = new ArrayList<ArrayList<RoomNode>>();
		for (RoomNode node : nodes) {
			addEntrance(node);
			addExit(node);
		}
	}

	public void addExit(RoomNode node) {
		int numberOfExits = node.getNumberOfOutgoingConnections();
		
	}

	public void addEntrance(RoomNode node) {
		// TODO Auto-generated method stub
		
	}
}
