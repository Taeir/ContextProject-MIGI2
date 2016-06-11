package nl.tudelft.contextproject.model.level;

import nl.tudelft.contextproject.controller.Controller;

import jme3tools.optimize.GeometryBatchFactory;

public class Optimize {
	public static void optimize(Controller controller) {
		System.out.println(GeometryBatchFactory.optimize(controller.floorsNode, false, true).getQuantity());
		System.out.println(GeometryBatchFactory.optimize(controller.wallsNode, false, true).getQuantity());
	}
}
