package com.platforge.player.core.platform;

import com.platforge.data.Map;
import com.platforge.data.PlatformGame;
import com.platforge.data.Vector;
import com.platforge.player.core.game.Rect;
import com.platforge.player.core.graphics.BackgroundSprite;
import com.platforge.player.core.graphics.Graphics;

import playn.core.Image;

public class BackgroundHandler {

	private int startForegroundY;
	private BackgroundSprite foreground, background, midground;
	
	private PlatformGame game;
	private Map map;
	
	public BackgroundHandler(PlatformGame game) {
		this.game = game;
		this.map = game.getSelectedMap();
		
		Image bmp = Data.loadForeground(map.groundImageName);
		int bottom = map.height(game);
		startForegroundY = bottom - map.groundY;
		foreground = new BackgroundSprite(bmp, new Rect(0, startForegroundY, 
				Graphics.getWidth(), startForegroundY + (int) bmp.height()), -6);

		bmp = Data.loadBackground(map.skyImageName);
		//Debug.write("%dx%d", Graphics.getWidth(), Graphics.getHeight());
		int startBackgroundY = startForegroundY - Graphics.getHeight();
		background = new BackgroundSprite(bmp, new Rect(0, startBackgroundY, 
				Graphics.getWidth(), startForegroundY), -7);
		background.scroll(0, Graphics.getHeight() - (int) bmp.height());		

		if (map.midGrounds.size() > 0) {
			Image mid = Data.loadMidgrounds(map.midGrounds);
			int startMidgroundY = startForegroundY - 256;
			midground = new BackgroundSprite(mid, new Rect(0, 
					startMidgroundY, Graphics.getWidth(), startMidgroundY + 
					(int) mid.height()), -5);
		}
	}
	
	public void updateScroll(Vector offset) {
		float parallax = 0.7f;
		float scrollX = Math.round(-offset.getX() * parallax); //Not sure about rounding
		float offY = (offset.getY()) * parallax - (1 - parallax) * (map.height(game) - Graphics.getHeight());
		
		foreground.setY(startForegroundY + offY);
		foreground.setScroll(scrollX, 0);
		
		if (midground != null) {
			midground.setY(foreground.getY() - 256);
			midground.setScroll(scrollX, 0);
		}
		
		float bgMinY = foreground.getY() - background.getRect().height();
		float bgY = Math.min(0, bgMinY);
		float bgOffY = bgMinY - bgY;
		background.setY(bgY);
		background.setScroll(scrollX, -bgOffY);
	}
}
