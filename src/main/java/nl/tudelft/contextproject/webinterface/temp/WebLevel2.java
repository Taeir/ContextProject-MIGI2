package nl.tudelft.contextproject.webinterface.temp;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.tudelft.contextproject.webinterface.temp.WebTile2.TileType;

import lombok.Getter;

public class WebLevel2 {
	@Getter
	private final WebTile2[][] tiles;
	
	public WebLevel2(int width, int height) {
		tiles = new WebTile2[width][height];
	}
	
	public void createRoom(int x, int y, int width, int height) {
		for (int ix = x; ix < x + width; ix++) {
			for (int iy = y; iy < y + height; iy++) {
				if (ix == x || ix == x + width - 1 || iy == y || iy == y + height - 1) {
					tiles[ix][iy] = new WebTile2(TileType.WALL);
				} else {
					tiles[ix][iy] = new WebTile2(TileType.FLOOR);
				}
			}
		}
	}
	
	public void createRoom(int x, int y, int width, int height, boolean explored) {
		for (int ix = x; ix < x + width; ix++) {
			for (int iy = y; iy < y + height; iy++) {
				if (ix == x || ix == x + width - 1 || iy == y || iy == y + height - 1) {
					tiles[ix][iy] = new WebTile2(TileType.WALL);
					tiles[ix][iy].setExplored(explored);
				} else {
					tiles[ix][iy] = new WebTile2(TileType.FLOOR);
					tiles[ix][iy].setExplored(explored);
				}
			}
		}
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("width", tiles.length);
		json.put("height", tiles[0].length);
		
		JSONObject jsonTiles = new JSONObject();
		//{0: [], 1: []}
		for (int x = 0; x < tiles.length; x++) {
			JSONArray arr = new JSONArray();
			WebTile2[] row = tiles[x];
			for (WebTile2 tile : row) {
				if (tile == null) {
					arr.put(0);
				} else {
					arr.put(tile.getType().getNr());
				}
			}
			
			jsonTiles.put("" + x, arr);
		}
		
		json.put("tiles", jsonTiles);
		
		return json;
	}
	
	public JSONObject toExploredJSON() {
		JSONObject json = new JSONObject();
		//{0: [0, 100], 1: []}
		for (int x = 0; x < tiles.length; x++) {
			JSONArray arr = new JSONArray();
			WebTile2[] row = tiles[x];
			for (int y = 0; y < row.length; y++) {
				if (row[y] == null || !row[y].isExplored()) continue;
				
				arr.put(y);
			}
			
			json.put("" + x, arr);
		}
		
		return json;
	}
	
	public static WebLevel2 testLevel() {
		WebLevel2 tbr = new WebLevel2(200, 200);
		
		tbr.createRoom(0, 0, 5, 5, true);
		tbr.createRoom(0, 10, 5, 5, true);
		tbr.createRoom(8, 0, 5, 5);
		
		WebCorridor wcAB = new WebCorridor(3, 5, 3, 10);
		WebCorridor wcAC = new WebCorridor(5, 1, 8, 1);
		wcAB.addTo(tbr, true);
		wcAC.addTo(tbr);
		
		return tbr;
	}
}
