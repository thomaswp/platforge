package com.twp.core;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.regularExpression;
import static playn.core.PlayN.touch;
import playn.core.Game;
import playn.core.PlayN;

import com.twp.platform.PlatformLogic;

import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.game.Debug;
import edu.elon.honors.price.game.Formatter;
import edu.elon.honors.price.game.Formatter.Impl;
import edu.elon.honors.price.game.Logic;
import edu.elon.honors.price.graphics.Graphics;
import edu.elon.honors.price.input.Input;

public class Platforge extends Game.Default {

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
		Formatter.impl = new Impl() {
			@Override
			public String format(String format, Object... args) {
				final StringBuffer msg = new StringBuffer();
				int argIndex = 0;
				for (int i = 0; i < format.length(); i++) {
					if (i < format.length() - 1) {
						String sub = format.substring(i, i + 2);
						if (regularExpression().matches("%[a-z]", sub)) {
							msg.append(args[argIndex++]);
							i++;
							continue;
						}
					}
					msg.append(format.charAt(i));
				}
				return msg.toString();
			}
		};
		
//		if (touch().hasTouch()) {
//			touch().setListener(Input.getInstance());
//		} else {
			pointer().setListener(Input.getInstance());
//		}
		keyboard().setListener(Input.getInstance());
		
		Graphics.resize(PlayN.graphics().width(), PlayN.graphics().height());
		
		logic = new PlatformLogic(game);
		logic.initialize();
	}

	@Override
	public void update(int delta) {
		Input.update(delta);
		logic.update(delta);
		Graphics.update(delta);
		time += delta;
		if (time > 1000) {
			Debug.write("%d FPS", frames);
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
