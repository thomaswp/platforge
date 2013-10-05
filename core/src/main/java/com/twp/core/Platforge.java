package com.twp.core;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.touch;
import playn.core.Game;
import playn.core.PlayN;
import playn.core.util.Callback;

import com.twp.core.game.Debug;
import com.twp.core.game.Logic;
import com.twp.core.graphics.Graphics;
import com.twp.core.input.Input;
import com.twp.core.platform.Data;
import com.twp.core.platform.PlatformLogic;

import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.field.PersistData;

public class Platforge extends Game.Default {

	private final static int MAX_FRAME_DELTA = 1000 * 3 / 2 / 60;
	
	private Logic logic;
	
	public Platforge() {
		super(16);
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
		
		PlayN.assets().getText("game.txt", new Callback<String>() {
			@Override
			public void onSuccess(String result) {
				startGame(result);
			}

			@Override
			public void onFailure(Throwable cause) {
				startGame(new PlatformGame());
			}
		});		
	}
	
	private void startGame(String data) {
		PlatformGame game = PersistData.readData(PlatformGame.class, data);
		if (game == null) {
			game = new PlatformGame();
		} else {
			game.objects[1].zoom = 0.3f;
		}
		startGame(game);
	}
	
	private void startGame(final PlatformGame game) {
		Data.preload(game, new Callback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				logic = new PlatformLogic(game);
				logic.initialize();
			}

			@Override
			public void onFailure(Throwable cause) {
				Debug.write("This shouldn't happen. How did this happen!?");
			}
		});
	}

	@Override
	public void update(int delta) {
		if (logic == null) return;
		
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
