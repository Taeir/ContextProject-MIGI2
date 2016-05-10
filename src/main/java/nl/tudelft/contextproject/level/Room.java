package nl.tudelft.contextproject.level;

import java.util.List;
import java.util.Set;

import com.jme3.light.Light;
import nl.tudelft.contextproject.Entity;

/**
 * Class to contain rooms.
 */
public class Room {
    private MazeTile[][] mazeTiles;
    private List<Light> lightList;
    private boolean explored;

    /**
     * Constructor to create a maze with specific mazeTiles.
     * @param maze The set of tiles to include in the maze.
     * @param entities The list of entities that is present in the maze.
     * @param lights A list with all the lights in the level.
     */
    public Room(MazeTile[][] maze, List<Light> lights) {
        this.mazeTiles = maze;
        this.lightList = lights;
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
     * Getter for the lights.
     * @return A list with all lights in the scene.
     */
    public List<Light> getLights() {
        return lightList;
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
