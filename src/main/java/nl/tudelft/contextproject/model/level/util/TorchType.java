package nl.tudelft.contextproject.model.level.util;

/**
 * Enum that represents the different configurations of point light models.
 */
public enum TorchType {

	/**
	 * A torch that hangs to the wall from North direction.
	 */
	NORTH_WALL_LIGHT,
		
	/**
	 * A torch that hangs to the wall from the South rotation.
	 */
	SOUTH_WALL_LIGHT,
	
	/**
	 * A torch that hangs to the wall from the West rotation.
	 */
	WEST_WALL_LIGHT,
	
	/**
	 * A torch that hangs to the wall from the East rotation.
	 */
	EAST_WALL_LIGHT,
	
	/**
	 * A torch that hangs from the ceiling, where rotation does not matter.
	 */
	CIELING_LIGHT;
	
	/**
	 * Constructor to complete enum body.
	 */
	TorchType() {}
	
	
}
