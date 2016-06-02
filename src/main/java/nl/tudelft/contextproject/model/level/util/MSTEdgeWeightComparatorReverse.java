package nl.tudelft.contextproject.model.level.util;

import java.util.Comparator;

/**
 * MSTEdge weight Comparator. 
 * Largest weight first.
 */
public class MSTEdgeWeightComparatorReverse implements Comparator<MSTEdge> {

	@Override
	public int compare(MSTEdge e1, MSTEdge e2) {
		if (e1.weight < e2.weight) {
			return 1;
		} else if (e1.weight > e2.weight) {
			return -1;
		} else {
			return 0;
		}
	}

}
