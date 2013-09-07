package edu.elon.honors.price.game;

import java.util.HashMap;

import playn.core.Image;

public final class Cache {
	private static HashMap<String, Image> cache = new HashMap<String, Image>();

	public static boolean isBitmapRegistered(int id) {
		return isBitmapRegistered("" + id);
	}
	
	public static boolean isBitmapRegistered(String id) {
		return cache.containsKey(id);
	}

	public static Image getRegisteredBitmap(int id) {
		return getRegisteredBitmap("" + id);
	}
	
	public static Image getRegisteredBitmap(String id) {
		if (cache.containsKey(id))
			return cache.get(id);
		return null;
	}

	public static void clearCache() {
		cache.clear();
	}

	public static void RegisterBitmap(int id, Image bitmap) {
		RegisterBitmap("" + id, bitmap);
	}
	
	public static void RegisterBitmap(String id, Image bitmap) {
		cache.put(id, bitmap);
	}

}
