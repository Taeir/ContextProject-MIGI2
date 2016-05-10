//package nl.tudelft.contextproject.level;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import com.jme3.math.Vector3f;
//import nl.tudelft.contextproject.Entity;
//import nl.tudelft.contextproject.VRPlayer;
//
///**
// * Class that can generate a map of a level.
// */
//public final class MapBuilder {
//	private static Level level;
//
//	/**
//	 * Private constructor.
//	 */
//	private MapBuilder(){}
//
//	/**
//	 * Set the level of this MapBuilder.
//	 * @param l the level to connect to the MapBuilder.
//	 */
//	public static void setLevel(Level l) {
//		level = l;
//	}
//
//	/**
//	 * Export the map to a *.png image file.
//	 * @param path The path to store the image.
//	 * @param filter A filter to filter the entity list.
//	 * @param resolution The resolution of a single tile in the map.
//	 */
//	public static void export(String path, DrawableFilter filter, int resolution) {
//		try {
//			BufferedImage bi = new BufferedImage(resolution * level.getWidth(), resolution * level.getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//			Graphics2D g = bi.createGraphics();
//			g.setPaint(Color.blue);
//			for (int x = 0; x < level.getWidth(); x++) {
//				for (int y = 0; y < level.getHeight(); y++) {
//					if (level.isTileAtPosition(x, y)) {
//						MazeTile tile = level.getTile(x, y);
//						if (filter.hideUnexplored() && !tile.isExplored()) continue;
//						tile.mapDraw(g, resolution);
//					}
//				}
//			}
//			g.setPaint(Color.white);
//			for (Entity e : level.getEntities()) {
//				if (filter.entity(e)) {
//					if (filter.hideUnexplored()) {
//						Vector3f trans = e.getGeometry().getLocalTranslation();
//						int x = (int) trans.x;
//						int y = (int) trans.y;
//						if (!level.getTile(x, y).isExplored()) continue;
//					}
//					e.mapDraw(g, resolution);
//				}
//			}
//			VRPlayer player = level.getPlayer();
//			if (filter.entity(player)) {
//				player.mapDraw(g, resolution);
//			}
//			ImageIO.write(bi, "PNG", new File(path));
//
//
//		} catch (IOException ie) {
//			ie.printStackTrace();
//		}
//
//	}
//}
