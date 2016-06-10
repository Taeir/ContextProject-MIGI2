package nl.tudelft.contextproject.model.level.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * MSTEdge weight Comparator. 
 * Smallest weight first.
 */
public class MSTEdgeWeightComparator implements Comparator<MSTEdge>, Serializable {

	private static final long serialVersionUID = -2661444468166732734L;

	@Override
	public int compare(MSTEdge edge1, MSTEdge edge2) {
		return Double.compare(edge1.weight, edge2.weight);
	}

}
