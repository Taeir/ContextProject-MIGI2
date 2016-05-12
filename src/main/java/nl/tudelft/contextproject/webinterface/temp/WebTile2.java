package nl.tudelft.contextproject.webinterface.temp;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WebTile2 {
	private boolean explored;
	private TileType type;
	
	public WebTile2() {
		type = TileType.FLOOR;
	}
	
	public WebTile2(TileType type) {
		this.type = type;
	}
	
	public static enum TileType {
		FLOOR(1), WALL(2), CORRIDOR(3), INVISIBLE_WALL(4);
		
		private int nr;
		TileType(int nr) {
			this.nr = nr;
		}
		
		public int getNr() {
			return this.nr;
		}
	}
}
