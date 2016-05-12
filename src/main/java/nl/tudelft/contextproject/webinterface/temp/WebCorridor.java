package nl.tudelft.contextproject.webinterface.temp;

import org.json.JSONObject;

import nl.tudelft.contextproject.webinterface.temp.WebTile2.TileType;

import lombok.Data;

/**
 * Temporary class for corridors for the Web interface.
 */
@Data
public class WebCorridor {
	private final int xA, yA, xB, yB;
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		appendTiles(json);
		return json;
	}
	
	public void addTo(WebLevel2 lvl) {
		for (int y = yA; y < yB; y++) {
			if (lvl.getTiles()[xA][y] == null || lvl.getTiles()[xA][y].getType() == TileType.WALL) {
				lvl.getTiles()[xA][y] = new WebTile2(TileType.CORRIDOR);
			}
			if (lvl.getTiles()[xB][y] == null || lvl.getTiles()[xB][y].getType() == TileType.WALL) {
				lvl.getTiles()[xB][y] = new WebTile2(TileType.CORRIDOR);
			}
		}
		
		for (int x = xA; x < xB; x++) {
			if (lvl.getTiles()[x][yA] == null || lvl.getTiles()[x][yA].getType() == TileType.WALL) {
				lvl.getTiles()[x][yA] = new WebTile2(TileType.CORRIDOR);
			}
			if (lvl.getTiles()[x][yB] == null || lvl.getTiles()[x][yB].getType() == TileType.WALL) {
				lvl.getTiles()[x][yB] = new WebTile2(TileType.CORRIDOR);
			}
		}
	}
	
	public void addTo(WebLevel2 lvl, boolean explored) {
		for (int y = yA; y < yB; y++) {
			if (lvl.getTiles()[xA][y] == null || lvl.getTiles()[xA][y].getType() == TileType.WALL) {
				lvl.getTiles()[xA][y] = new WebTile2(TileType.CORRIDOR);
				lvl.getTiles()[xA][y].setExplored(explored);
			}
			if (lvl.getTiles()[xB][y] == null || lvl.getTiles()[xB][y].getType() == TileType.WALL) {
				lvl.getTiles()[xB][y] = new WebTile2(TileType.CORRIDOR);
				lvl.getTiles()[xB][y].setExplored(explored);
			}
		}
		
		for (int x = xA; x < xB; x++) {
			if (lvl.getTiles()[x][yA] == null || lvl.getTiles()[x][yA].getType() == TileType.WALL) {
				lvl.getTiles()[x][yA] = new WebTile2(TileType.CORRIDOR);
				lvl.getTiles()[x][yA].setExplored(explored);
			}
			if (lvl.getTiles()[x][yB] == null || lvl.getTiles()[x][yB].getType() == TileType.WALL) {
				lvl.getTiles()[x][yB] = new WebTile2(TileType.CORRIDOR);
				lvl.getTiles()[x][yB].setExplored(explored);
			}
		}
	}
	
	/**
	 * Appends the tiles of this corridor to "tiles" of the given JSONObject.
	 * 
	 * @param json
	 * 		the json to append to
	 */
	public void appendTiles(JSONObject json) {
		for (int y = yA; y <= yB; y++) {
			JSONObject objA = new JSONObject();
			objA.put("x", xA);
			objA.put("y", y);
			json.append("tiles", objA);
			
			JSONObject objB = new JSONObject();
			objB.put("x", xB);
			objB.put("y", y);
			json.append("tiles", objB);
		}
		
		for (int x = xA; x <= xB; x++) {
			JSONObject objA = new JSONObject();
			objA.put("x", x);
			objA.put("y", yA);
			json.append("tiles", objA);
			
			JSONObject objB = new JSONObject();
			objB.put("x", x);
			objB.put("y", yB);
			json.append("tiles", objB);
		}
	}
}
