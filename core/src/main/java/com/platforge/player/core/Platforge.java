package com.platforge.player.core;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.touch;
import playn.core.Game;
import playn.core.PlayN;
import playn.core.util.Callback;

import com.platforge.data.PlatformGame;
import com.platforge.data.field.PersistData;
import com.platforge.player.core.game.Debug;
import com.platforge.player.core.game.Logic;
import com.platforge.player.core.graphics.Graphics;
import com.platforge.player.core.input.Input;
import com.platforge.player.core.platform.Data;
import com.platforge.player.core.platform.PlatformLogic;

public class Platforge extends Game.Default {

	private final static int MAX_FRAME_DELTA = 1000 * 3 / 2 / 60;
	
	private Logic logic;
	private PlatformGame game;
	
	public Platforge(PlatformGame game) {
		super(16);
		this.game = game;
	}
	
	public Platforge(String data) {
		this(PersistData.readData(PlatformGame.class, data));
	}
		
	public Platforge() {
		this((PlatformGame)null);
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
		
		if (game == null) {
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
		} else {
			startGame(game);
		}
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
