package com.twp.android;

import playn.android.GameActivity;
import playn.core.PlayN;
import android.os.Vibrator;

import com.platforge.player.core.Platforge;
import com.platforge.player.core.input.Input;

import edu.elon.honors.price.game.Formatter;
import edu.elon.honors.price.game.Formatter.Impl;

public class PlatforgeActivity extends GameActivity {

	@Override
	public void main() {
		Input.setInstance(new AndroidInput());
		AndroidInput.setVibrator((Vibrator) getSystemService(VIBRATOR_SERVICE));
		Formatter.impl = new Impl() {
			@Override
			public String format(String format, Object... args) {
				return String.format(format, args);
			}
		};
		PlayN.run(new Platforge());
	}
}
