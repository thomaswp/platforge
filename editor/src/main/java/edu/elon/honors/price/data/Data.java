package edu.elon.honors.price.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;

import com.twp.core.game.Debug;

/**
 * A class for saving and loading persistent data as well as
 * game resources.
 * 
 * @author Thomas Price
 *
 */
public final class Data extends Directories {

	public final static Uri CONTENT_URI = Uri.parse("content://edu.elon.honors.price.maker/");
	private static Context defaultParent;

	public static Context getDefaultParent() {
		return defaultParent;
	}

	public static void setDefaultParent(Context parent) {
		defaultParent = parent;
	}

	private static Bitmap loadBitmap(String name, Context parent) {
		try {
			if (Cache.isBitmapRegistered(name)) {
				//Debug.write("Cache: " + id);
				return Cache.getRegisteredBitmap(name);
			}
			else {
//				Debug.write("Load New: " + id);
				try {
					ContentResolver cr = parent.getContentResolver();
					AssetFileDescriptor afd = cr.openAssetFileDescriptor(Uri.withAppendedPath(Data.CONTENT_URI, name), "r");
					InputStream is = afd.createInputStream();
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Config.ARGB_8888;
					Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
					Cache.RegisterBitmap(name, bmp);
					return bmp;
				} catch (Exception ex) {
					Debug.write("Could not find '" + name + "' in local resrounces. Attempting external storage.");
					ex.printStackTrace();
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						File file = new File(Environment.getExternalStorageDirectory(), Data.SD_FOLDER + name);
						return BitmapFactory.decodeFile(file.getAbsolutePath());
					} else {
						Debug.write("Failed: No SD Card detected");
						return null;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a list of the file names of all resources in the given directory. 
	 * @param dir The directory to be searched. Use one of the directory contants
	 * found in this class. This will only return Assets if called from the GameMaker
	 * namespace.
	 * @param parent The context in which the request should be made. This should
	 * be an Activity in the GameMaker app.
	 * @return
	 */
	public static ArrayList<String> getResources(String dir, Context parent) {
		ArrayList<String> files = new ArrayList<String>();

		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);

		try {
			String[] assets = parent.getAssets().list(dir);
			for (int i = 0; i < assets.length; i++) {
				//To avoid subdirectories
				if (assets[i].contains(".")) {
					files.add(assets[i]);
				}
			}
		} catch (Exception ex) {
			Debug.write("No local resources for '" + dir + "'");
		}

		try {
			File file = new File(Environment.getExternalStorageDirectory(), Data.SD_FOLDER + dir);
			String[] externals = file.list();
			for (int i = 0; i < externals.length; i++) {
				if (externals[i].contains(".")) {
					files.add(externals[i]);
				}
			}
		} catch (Exception ex) {
			Debug.write("No external resources for '" + dir + "'");
		}

		return files;
	}

	public static ArrayList<String> getActorResources(Context parent) {

		ArrayList<String> imageNames = new ArrayList<String>();
		for (String type : ACTOR_TYPES) {
			ArrayList<String> actors = Data.getResources(ACTORS_DIR + type, parent);
			for (String actor : actors) {
				imageNames.add(type + actor);
			}
		}

		return imageNames;
	}

	public static InputStream loadAction(int id, Context context) throws IOException {
		ArrayList<String> resources = getResources(ACTIONS_DIR, context);
		String idString = String.format("%03d", id);
		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).startsWith(idString)) {
				return context.getAssets().open(ACTIONS_DIR + "/" + resources.get(i));
			}
		}
		Debug.write("No actions found for id " + idString);
		return null;
	}
	
	public static InputStream loadTrigger(int id, Context context) throws IOException {
		ArrayList<String> resources = getResources(TRIGGERS_DIR, context);
		String idString = String.format("%03d", id);
		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).startsWith(idString)) {
				return context.getAssets().open(TRIGGERS_DIR + "/" + resources.get(i));
			}
		}
		Debug.write("No triggers found for id " + idString);
		return null;
	}

	public static Bitmap loadObject(String name) {
		return loadBitmap(OBJECTS_DIR + name, getDefaultParent());
	}

	/**
	 * Loads an actor from the ACTORS_DIR directory. Uses the current running
	 * Game as a context. Use only from an appropriate Logic class.
	 * @param name File name (without path but with extension), such as
	 * 'actor.png'.
	 * @return The bitmap.
	 */
	public static Bitmap loadActor(String name) {
		return loadActor(name, getDefaultParent());
	}

	/**
	 * Loads an actor from the ACTORS_DIR directory.
	 * @param name File name (without path but with extension), such as
	 * 'actor.png'.
	 * @param context The context in which to load the resource.
	 * @return The bitmap.
	 */
	public static Bitmap loadActor(String name, Context context) {
		return loadBitmap(ACTORS_DIR + name, context);
	}

	public static Bitmap loadActorIcon(String name) {
		return loadActorIcon(name, getDefaultParent());				
	}

	public static Bitmap loadActorIcon(String name, Context context) {
		Bitmap bmp = loadActor(name, context);
		String key = ACTORS_DIR + name + "``icon";
		ActorAnimator anim = ActorAnimator.create(name);
		if (Cache.isBitmapRegistered(key)) {
			return Cache.getRegisteredBitmap(key);
		} else {
			Bitmap icon = Bitmap.createBitmap(bmp, 0, 0, 
					bmp.getWidth() / anim.getTotalCols(), 
					bmp.getHeight() / anim.getTotalRows());
			Cache.RegisterBitmap(key, icon);
			return icon;
		}
	}

	/**
	 * Loads a tileset from the TILESETS_DIR directory. Uses the current running
	 * Game as a context. Use only from an appropriate Logic class.
	 * @param name File name (without path but with extension), such as
	 * 'tileset.png'.
	 * @return The bitmap.
	 */
	public static Bitmap loadTileset(String name) {
		return loadTileset(name, getDefaultParent());
	}

	/**
	 * Loads a tileset from the TILESETS_DIR directory.
	 * @param name File name (without path but with extension), such as
	 * 'tileset.png'.
	 * @param context The context in which to load the resource.
	 * @return The bitmap.
	 */
	public static Bitmap loadTileset(String name, Context parent) {
		return loadBitmap(TILESETS_DIR + name, parent);
	}

	public static Bitmap loadBackground(String name) {
		return loadBitmap(BACKGROUNDS_DIR + name, getDefaultParent());
	}

	public static Bitmap loadForeground(String name) {
		return loadBitmap(FOREGROUNDS_DIR + name, getDefaultParent());
	}

	public static Bitmap loadMidground(String name) {
		return loadBitmap(MIDGROUNDS_DIR + name, getDefaultParent());
	}

	public static Bitmap loadMidgrounds(List<String> midgrounds) {
		Bitmap mid = null;
		if (midgrounds.size() > 0) {
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			for (String path : midgrounds) {
				Bitmap bmp = loadMidground(path);
				//Debug.write("%dx%d", bmp.getWidth(), bmp.getHeight());
				if (mid == null) {
					mid = bmp.copy(bmp.getConfig(), true);
				} else {
					if (bmp.getWidth() > mid.getWidth() || 
							bmp.getHeight() > mid.getHeight()) {
						Bitmap temp = Bitmap.createBitmap(
								Math.max(mid.getWidth(), bmp.getWidth()),
								Math.max(mid.getHeight(), bmp.getHeight()),
								mid.getConfig());
						Canvas c = new Canvas(temp);
						c.drawBitmap(mid, 0, 0, paint);
						mid = temp;
					}
					Canvas c = new Canvas(mid);
					c.drawBitmap(bmp, 0, 0, paint);
				}
			}
		}
		return mid;
	}

	public static Bitmap loadEditorBmp(String name, Context context) {
		try {
			return BitmapFactory.decodeStream(
					context.getAssets().open(EDITOR_DIR + name));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap loadEditorBmp(String name) {
		return loadEditorBmp(name, getDefaultParent());
	}
	
	public static <T extends Serializable> T loadData(String name, Context context) {
		try {
			InputStream is = context.openFileInput(name);
			return loadData(is);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Used to load game data stored in the GameMaker namespace. Can
	 * be used outside of this namespace.
	 * @param name The file name of the game to be loaded. No directory
	 * should be provided
	 * @param context The context in which to load the game. This does not
	 * have to be in the GameMaker namespace.
	 * @return The PlatformGame object.
	 */
	public static <T extends Serializable> T loadDataGlobal(String name, Context context) {
		try {
			InputStream is = context.getContentResolver().openInputStream(
					Uri.withAppendedPath(Data.CONTENT_URI, name));
			return loadData(is);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Serializable> T loadData(InputStream is) 
			throws OptionalDataException, ClassNotFoundException, IOException {
			ObjectInputStream in = new ObjectInputStream(is);
			Object data = in.readObject();
			in.close();
			return (T)data;
	}

	public static boolean saveData(String name, Context context, Serializable data) {
		try {
			FileOutputStream fos = context.openFileOutput(name, 
					Context.MODE_PRIVATE);
			return saveData(fos, data);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Used to save game data stored in the GameMaker namespace. Can
	 * be used outside of this namespace.
	 * @param name The file name of the game to be saved. No directory
	 * should be provided
	 * @param context The context in which to save the game. This does not
	 * have to be in the GameMaker namespace.
	 * @param data The PlatformGame object.
	 * @return true if the save was successful.
	 * @throws IOException 
	 */
	@SuppressLint("WorldReadableFiles")
	public static boolean saveDataGlobal(String name, Context context, Serializable data) {
		try {
			FileOutputStream fos = context.openFileOutput(name, 
					Context.MODE_WORLD_READABLE);
			return saveData(fos, data);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private static boolean saveData(OutputStream os, Serializable data) 
			throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(os);
		out.writeObject(data);
		out.close();
		return true;
	}
}
