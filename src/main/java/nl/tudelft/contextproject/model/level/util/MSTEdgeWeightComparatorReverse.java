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
	public int compare(MSTEdge e1, MSTEdge e2) {
		return Double.compare(e2.weight, e1.weight);
	}

}
