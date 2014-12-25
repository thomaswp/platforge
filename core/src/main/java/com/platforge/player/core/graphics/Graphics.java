package com.platforge.player.core.graphics;

import java.util.ArrayList;

import com.platforge.player.core.game.Debug;
import com.platforge.player.core.game.Rect;
import com.platforge.player.core.game.RectF;

import playn.core.PlayN;
import static playn.core.PlayN.*;

public class Graphics {

	final static int FRAME_BLOCK = 60;

	private static ArrayList<Viewport> viewports = new ArrayList<Viewport>();
	private static int width, height;

	private static int frameCountDraw = 0;
	private static long lastSystemTimeDraw = System.currentTimeMillis();
	private static int fpsDraw;
	private static int frameCountGame = 0;
	private static long lastSystemTimeGame = System.currentTimeMillis();
	private static int fpsGame;
	private static boolean showFPS;
	private static boolean fpsBitmapRefresh;
	private static int backgroundColor;
	private static float scale = 1f;
	
	public static float getGlobalScale() {
		return scale;
	}
	
	public static int getBackgroundColor() {
		return backgroundColor;
	}

	public static void setBackgroundColor(int backgroundColor) {
		Graphics.backgroundColor = backgroundColor;
	}

	public static boolean getFPSBitmapRefresh() {
		return fpsBitmapRefresh;
	}
	
	public static void setFPSBitmapRefresh(boolean refresh) {
		Graphics.fpsBitmapRefresh = refresh;
	}

	/**
	 * Returns the width of the current drawable area.
	 * @return The width
	 */
	public static int getWidth() {
		return (int)(width / scale);
	}
	
	public static int getScreenWidth() {
		return width;
	}
	
	public static void setWidth(int width) {
		scale = (float)getScreenWidth() / width;
		PlayN.graphics().rootLayer().setScale(scale);
	}
	
	public static void setHeight(int height) {
		scale = (float)getScreenHeight() / height;
		PlayN.graphics().rootLayer().setScale(scale);
	}

	/**
	 * Returns the height of the current drawable area.
	 * @return
	 */
	public static int getHeight() {
		return (int)(height / scale);
	}
	
	public static int getScreenHeight() {
		return height;
	}
	
	public static Rect getRect() {
		return new Rect(0, 0, getWidth(), getHeight());
	}
	
	public static RectF getRectF() {
		return new RectF(0, 0, getWidth(), getHeight());
	}
	
	public static boolean isShowingFPS() {
		return showFPS;
	}

	public static void setShowingFPS(boolean showFPS) {
		Graphics.showFPS = showFPS;
	}

	public static int getFpsDraw() {
		return fpsDraw;
	}

	public static int getFpsGame() {
		return fpsGame;
	}
	
	public static ArrayList<Viewport> getViewports() {
		return viewports;
	}

	/**
	 * Adds a viewport to the list of drawn viewports.
	 * @param viewport The viewport.
	 */
	public static void addViewport(Viewport viewport) {
		graphics().rootLayer().add(viewport.layer);
		viewports.add(viewport);
	}

	public static void resize(int width, int height) {
		Graphics.width = width;
		Graphics.height = height;
	}
	
	/**
	 * Updates the Graphics class, which updates all of its Viewports.
	 */
	public static void update(long timeElapsed) {
		for (int i = 0; i < viewports.size(); i++) {
			Viewport viewport = viewports.get(i);
			if (viewport != null) {
				viewport.update(timeElapsed);
			}
		}
		updateFPSGame();
	}
	
	public static int powerOfTwoSize(int size) {
		int s = 1;
		while (s < size) s *= 2;
		return s;
	}
	
	public static void reset() {
		Debug.write("Graphics Reset");
		for (int i = 0; i < viewports.size(); i++) {
			Viewport viewport = viewports.get(i);
			viewport.getSprites().clear();
			viewport.layer.destroy();
		}
		scale = 1f;
		viewports.clear();
		graphics().rootLayer().clear();
		viewports.add(Viewport.DefaultViewport);
	}

	public static void updateFPSDraw() {
		frameCountDraw = (frameCountDraw + 1) % FRAME_BLOCK;
		if (frameCountDraw == 0) {
			long currentTime = System.currentTimeMillis();
			fpsDraw = (int)(FRAME_BLOCK * 1000 / (currentTime - lastSystemTimeDraw));
			lastSystemTimeDraw = currentTime;
			fpsBitmapRefresh = true;
		}
	}

	private static void updateFPSGame() {
		frameCountGame = (frameCountGame + 1) % FRAME_BLOCK;
		if (frameCountGame == 0) {
			long currentTime = System.currentTimeMillis();
			fpsGame = (int)(FRAME_BLOCK * 1000 / (currentTime - lastSystemTimeGame));
			lastSystemTimeGame = currentTime;
			fpsBitmapRefresh = true;
		}
	}
	
}
