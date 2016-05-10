package nl.tudelft.contextproject.model.level;

/**
 * Interface for a level factory
 * This interface defines two methods:
 * One for creating a random level and one for creating a seeded one.
 */
public interface LevelFactory {
	/**
	 * Generate a level with a given seed.
	 * @param seed A seed value for the level generation.
	 * @return The generated level.
	 */
	public Level generateSeeded(long seed);
	
	/**
	 * Generate a random level that is generated using the current time as a seed.
	 * @return The generated level.
	 */
	public default Level generateRandom() {
		return generateSeeded(System.currentTimeMillis());
	}
}
