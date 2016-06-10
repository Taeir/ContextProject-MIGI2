package nl.tudelft.contextproject.model.level.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * MSTEdge weight Comparator. 
 * Largest weight first.
 */
public class MSTEdgeWeightComparatorReverse implements Comparator<MSTEdge>, Serializable {

	private static final long serialVersionUID = -8232031617208160265L;

	@Override
	public int compare(MSTEdge edge1, MSTEdge edge2) {
		return Double.compare(edge2.weight, edge1.weight);
	}

}
