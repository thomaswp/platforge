package com.twp.core;

import tripleplay.util.Colors;
import edu.elon.honors.price.game.Logic;
import edu.elon.honors.price.graphics.Sprite;
import edu.elon.honors.price.graphics.Viewport;

public class PlatforgeLogic implements Logic {

	@Override
	public void setPaused(boolean paused) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		Sprite s = new Sprite(Viewport.DefaultViewport, 10, 10, 100, 100);
		s.getBitmapCanvas().setFillColor(Colors.WHITE);
		s.getBitmapCanvas().fillCircle(50, 50, 50);
	}

	@Override
	public void update(long timeElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

}
