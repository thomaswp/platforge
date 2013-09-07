package com.twp.core;

import static playn.core.PlayN.regularExpression;
import playn.core.Game;
import edu.elon.honors.price.game.Formatter;
import edu.elon.honors.price.game.Formatter.Impl;
import edu.elon.honors.price.game.Logic;
import edu.elon.honors.price.graphics.Graphics;
import edu.elon.honors.price.input.Input;

public class Platforge extends Game.Default {

	private Logic logic = new PlatforgeLogic();
	
	public Platforge() {
		super(16); // call update every 33ms (30 times per second)
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
		
		logic.initialize();

	}

	@Override
	public void update(int delta) {
		Input.update(delta);
		logic.update(delta);
		Graphics.update(delta);
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything here!
	}
}
