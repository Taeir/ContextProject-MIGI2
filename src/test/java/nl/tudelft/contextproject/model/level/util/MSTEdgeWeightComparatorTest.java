package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Test class for MSTEdgeWeightComparator.
 */
public class MSTEdgeWeightComparatorTest {
	
	/**
	 * Test if edge e1 is first if e1 has smallest weight.
	 */
	@Test
	public void testCompareE1First() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge e1 = new MSTEdge(testNode, testNode, 0.0, 1);
		MSTEdge e2 = new MSTEdge(testNode, testNode, 1.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(e1);
		testList.add(e2);
		testList.sort(new MSTEdgeWeightComparator());
		assertEquals(testList.get(0).corridorID, 1);
	}
	
	/**
	 * Test if e2 is second if e1 has smallest weight.
	 */
	@Test
	public void testCompareE1FirstCheckSecondPlace() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge e1 = new MSTEdge(testNode, testNode, 0, 1);
		MSTEdge e2 = new MSTEdge(testNode, testNode, 1, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(e1);
		testList.add(e2);
		testList.sort(new MSTEdgeWeightComparator());
		assertEquals(2, testList.get(1).corridorID);
	}
	
	/**
	 * Check if e2 is first if e2 has smallest weight.
	 */
	@Test
	public void testCompareE2First() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge e1 = new MSTEdge(testNode, testNode, 1.0, 1);
		MSTEdge e2 = new MSTEdge(testNode, testNode, 0.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(e1);
		testList.add(e2);
		testList.sort(new MSTEdgeWeightComparator());
		assertEquals(testList.get(0).corridorID, 2);
	}
	
	/**
	 * Check if e1 is first if weights are equal.
	 */
	@Test
	public void testCompareEqual() {
		MSTNode testNode = new MSTNode(null, null, 0);
		MSTEdge e1 = new MSTEdge(testNode, testNode, 0.0, 1);
		MSTEdge e2 = new MSTEdge(testNode, testNode, 0.0, 2);
		ArrayList<MSTEdge> testList = new ArrayList<MSTEdge>(2);
		testList.add(e1);
		testList.add(e2);
		testList.sort(new MSTEdgeWeightComparator());
		assertEquals(testList.get(0).corridorID, 1);
	}
}
