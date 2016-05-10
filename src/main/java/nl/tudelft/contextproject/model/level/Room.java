package nl.tudelft.contextproject.model.level;

import java.util.List;

import com.jme3.light.Light;

/**
 * Class to contain rooms.
 */
public class Room {
    private MazeTile[][] mazeTiles;
    private boolean explored;

    /**
     * Constructor to create a maze with specific mazeTiles.
     * @param maze The set of tiles to include in the maze.
     * @param lights A list with all the lights in the level.
     * @param level The level to add the lights to.
     */
    public Room(MazeTile[][] maze, List<Light> lights, Level level) {
        this.mazeTiles = maze;
        for (Light l : lights) {
        	level.addLight(l);
        }
        this.explored = false;
    }
    
    /**
     * Simple constructor for a room.
     * @param maze The collection of MazeTiles in this room.
     */
    public Room(MazeTile[][] maze) {
        this.mazeTiles = maze;
        this.explored = false;
    }

    /**
     * Getter for the height of the maze.
     * @return the height of the maze.
     */
    public int getHeight() {
        return mazeTiles[0].length;
    }

    /**
     * Getter for the width of the maze.
     * @return the width of the maze.
     */
    public int getWidth() {
        return mazeTiles.length;
    }

    /**
     * Checks if there is a tile in the maze at the specified position.
     * @param x the x-location in the maze
     * @param y the y-location in the maze
     * @return true when there is a tile at that position, false otherwise.
     */
    public boolean isTileAtPosition(int x, int y) {
        return mazeTiles[x][y] != null;
    }

    /**
     * Getter for a tile at a certain position.
     * @param x the x-location in the maze
     * @param y the y-location in the maze
     * @return the tile at the specified location or null when no tile is present.
     */
    public MazeTile getTile(int x, int y) {
        return mazeTiles[x][y];
    }

    /**
     * Check if this tile is explored.
     * @return True when explored, else otherwise.
     */
    public boolean isExplored() {
        return explored;
    }

    /**
     * Set the explored value for this tile.
     * @param newValue The new explored value.
     * @return The old value associated with this field.
     */
    public boolean setExplored(boolean newValue) {
        boolean res = explored;
        explored = newValue;
        return res;
    }
}
