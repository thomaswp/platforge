package edu.elon.honors.price.data;

import static playn.core.PlayN.assets;

import java.util.List;

import static playn.core.PlayN.*;
import playn.core.CanvasImage;
import playn.core.Image;
import edu.elon.honors.price.game.Cache;

/**
 * A class for saving and loading persistent data as well as
 * game resources.
 * 
 * @author Thomas Price
 *
 */
public final class Data extends Directories {

	private static Image loadBitmap(String name) {
		try {
			if (Cache.isBitmapRegistered(name)) {
				//Debug.write("Cache: " + id);
				return Cache.getRegisteredBitmap(name);
			}
			else {
				Image image = assets().getImageSync(name);
				Cache.RegisterBitmap(name, image);
				return image;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public static Image loadObject(String name) {
		return loadBitmap(OBJECTS_DIR + name);
	}

	/**
	 * Loads an actor from the ACTORS_DIR directory. Uses the current running
	 * Game as a context. Use only from an appropriate Logic class.
	 * @param name File name (without path but with extension), such as
	 * 'actor.png'.
	 * @return The bitmap.
	 */
	public static Image loadActor(String name) {
		return loadBitmap(ACTORS_DIR + name);
	}

	/**
	 * Loads a tileset from the TILESETS_DIR directory.
	 * @param name File name (without path but with extension), such as
	 * 'tileset.png'.
	 * @param context The context in which to load the resource.
	 * @return The bitmap.
	 */
	public static Image loadTileset(String name) {
		return loadBitmap(TILESETS_DIR + name);
	}

	public static Image loadBackground(String name) {
		return loadBitmap(BACKGROUNDS_DIR + name);
	}

	public static Image loadForeground(String name) {
		return loadBitmap(FOREGROUNDS_DIR + name);
	}

	public static Image loadMidground(String name) {
		return loadBitmap(MIDGROUNDS_DIR + name);
	}
	
	public static Image loadMidgrounds(List<String> midgrounds) {
		CanvasImage mid = null;
		if (midgrounds.size() > 0) {
			for (String path : midgrounds) {
				Image bmp = loadMidground(path);
				//Debug.write("%dx%d", bmp.getWidth(), bmp.getHeight());
				if (mid == null) {
					mid = graphics().createImage(bmp.width(), bmp.height());
					mid.canvas().drawImage(bmp, 0, 0);
				} else {
					if (bmp.width() > mid.width() || 
							bmp.height() > mid.height()) {
						CanvasImage temp = graphics().createImage(
								Math.max(mid.width(), bmp.width()),
								Math.max(mid.height(), bmp.height()));
						temp.canvas().drawImage(mid, 0, 0);
						mid = temp;
					}
					mid.canvas().drawImage(bmp, 0, 0);
				}
			}
		}
		return mid;
	}
}
