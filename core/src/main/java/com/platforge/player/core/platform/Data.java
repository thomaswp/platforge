package com.platforge.player.core.platform;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

import com.platforge.data.ActorClass;
import com.platforge.data.Directories;
import com.platforge.data.Map;
import com.platforge.data.ObjectClass;
import com.platforge.data.PlatformGame;
import com.platforge.data.Tileset;
import com.platforge.player.core.game.Cache;

/**
 * A class for saving and loading persistent data as well as
 * game resources.
 * 
 * @author Thomas Price
 *
 */
public class Data extends Directories {
	
	private static Image loadBitmap(String name) {
		try {
			if (Cache.isBitmapRegistered(name)) {
				//Debug.write("Cache: " + id);
				return Cache.getRegisteredBitmap(name);
			}
			else {
				Image image = assets().getImage(name);
				Cache.RegisterBitmap(name, image);
				return image;
			}
		} catch (Exception ex) {
			PlayN.log().debug("Loading image exception: " + ex.getMessage());
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
	
	private static class Counter {
		int value;
	}
	
	public static void preload(PlatformGame game, final Callback<Integer> callback) {
		final List<Image> images = new ArrayList<Image>();
		for (ActorClass actor : game.actors) {
			images.add(loadActor(actor.imageName));
		}
		for (ObjectClass obj : game.objects) {
			images.add(loadObject(obj.imageName));
		}
		for (Tileset tileset : game.tilesets) {
			images.add(loadTileset(tileset.bitmapName));
		}
		for (Map map : game.maps) {
			images.add(loadBackground(map.skyImageName));
			images.add(loadForeground(map.groundImageName));
			for (String midground : map.midGrounds) {
				images.add(loadMidground(midground));
			}
		}
		
		final Counter counter = new Counter();
		counter.value = images.size(); 
		
		for (Image image : images) {
			image.addCallback(new Callback<Image>() {
				@Override
				public void onSuccess(Image result) {
					count();
				}

				@Override
				public void onFailure(Throwable cause) {
					cause.printStackTrace();
					count();
				}
				
				private void count() {
					counter.value--;
					if (counter.value == 0) {
						callback.onSuccess(images.size());
					}
				}
			});
		}
	}
}
