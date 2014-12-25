package com.platforge.editor.maker;

import com.platforge.data.PlatformGame;
import com.platforge.editor.maker.SelectorMapBase.SelectorMapView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SelectorMapPreview extends SelectorMapView implements IPopulatable {

	public SelectorMapPreview(Context context, PlatformGame game, 
			Bundle savedInstanceState) {
		super(context, game, savedInstanceState);
	}

	@Override
	protected boolean showRightButton() {
		return false;
	}

	@Override
	protected int getBackgroundTransparency() {
		return 255;
	}
	
	public void refreshTileset() {
		tiles = createTiles(game.tilesets[game.getSelectedMap().tilesetId], 
				getContext());
	}
	
	public void refreshMidgrounds() {
		createMidgrounds();
	}

	@Override
	public void populate(PlatformGame game) {
		synchronized (this.game) {
			this.game = game;
			refresh();
		}
	}

	@Override
	public boolean onActivityResult(int requestCode, Intent data) {
		return false;
	}
}
