package com.platforge.editor.maker;

import com.platforge.data.PlatformGame;

import android.content.Intent;

public interface IPopulatable {
	public void populate(PlatformGame game);
	public boolean onActivityResult(int requestCode, Intent data);
}
