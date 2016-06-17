package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Test class for the reverse MSTEdgeWeightReverseComparator.
 */
public class MSTEdgeWeightComparatorReverseTest {

	/**
	 * Test if edge e1 is first if e1 has smallest weight.
	 */
	@Test
	public void testCompareE1First() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge edge1 = new MSTEdge(testNode, testNode, 1.0, 1);
		MSTEdge edge2 = new MSTEdge(testNode, testNode, 0.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(edge1);
		testList.add(edge2);
		testList.sort(new MSTEdgeWeightComparatorReverse());
		assertEquals(testList.get(0).corridorID, 1);
	}
	
	/**
	 * Test if e2 is second if e1 has largest weight.
	 */
	@Test
	public void testCompareE1FirstCheckSecondPlace() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge edge1 = new MSTEdge(testNode, testNode, 1.0, 1);
		MSTEdge edge2 = new MSTEdge(testNode, testNode, 0.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(edge1);
		testList.add(edge2);
		testList.sort(new MSTEdgeWeightComparatorReverse());
		assertEquals(2, testList.get(1).corridorID);
	}
	
	/**
	 * Check if e2 is first if e2 has largest weight.
	 */
	@Test
	public void testCompareE2First() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge edge1 = new MSTEdge(testNode, testNode, 1.0, 1);
		MSTEdge edge2 = new MSTEdge(testNode, testNode, 2.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(edge1);
		testList.add(edge2);
		testList.sort(new MSTEdgeWeightComparatorReverse());
		assertEquals(testList.get(0).corridorID, 2);
	}
	
	/**
	 * Check if e1 is first if weights are equal.
	 */
	@Test
	public void testCompareEqual() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge edge1 = new MSTEdge(testNode, testNode, 0.0, 1);
		MSTEdge edge2 = new MSTEdge(testNode, testNode, 0.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(edge1);
		testList.add(edge2);
		testList.sort(new MSTEdgeWeightComparatorReverse());
		assertEquals(testList.get(0).corridorID, 1);
	}

}
