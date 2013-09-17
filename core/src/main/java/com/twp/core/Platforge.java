package com.twp.core;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.touch;
import playn.core.Game;
import playn.core.PlayN;

import com.twp.platform.PlatformLogic;

import edu.elon.honors.price.data.Map;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.Event.Parameters;
import edu.elon.honors.price.data.field.DataObject;
import edu.elon.honors.price.data.field.EqualsData;
import edu.elon.honors.price.data.field.HashData;
import edu.elon.honors.price.data.field.PersistData;
import edu.elon.honors.price.game.Debug;
import edu.elon.honors.price.game.Logic;
import edu.elon.honors.price.graphics.Graphics;
import edu.elon.honors.price.input.Input;

public class Platforge extends Game.Default {

	private final static int MAX_FRAME_DELTA = 1000 * 3 / 2 / 60;
	
	private Logic logic;
	private PlatformGame game;
	
	public Platforge() {
		this(new PlatformGame());
	}
	
	public Platforge(PlatformGame game) {
		super(16);
		this.game = game;
	}

	@Override
	public void init() {
		
		if (touch().hasTouch()) {
			touch().setListener(Input.getInstance());
		} else {
			pointer().setListener(Input.getInstance());
		}
		keyboard().setListener(Input.getInstance());
		
		Graphics.resize(PlayN.graphics().width(), PlayN.graphics().height());
		
		String data = null;
		try {
			data = PlayN.assets().getTextSync("game.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		game = PersistData.readData(PlatformGame.class, data);	
		game.objects[1].zoom = 0.3f;
		
		
		logic = new PlatformLogic(game);
		logic.initialize();
	}

	@Override
	public void update(int delta) {
		delta = Math.min(delta, MAX_FRAME_DELTA);
		Input.update(delta);
		logic.update(delta);
		Graphics.update(delta);
		time += delta;
		if (time > 1000) {
//			Debug.write("%d FPS", frames);
			time -= 1000;
			frames = 0;
		}
	}

	int frames; long time;
	@Override
	public void paint(float alpha) {
		frames++;
	}
}
