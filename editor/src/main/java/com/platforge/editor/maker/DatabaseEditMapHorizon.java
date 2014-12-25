package com.platforge.editor.maker;

import com.platforge.data.PlatformGame;
import com.platforge.editor.data.tutorial.Tutorial.EditorButton;
import com.platforge.editor.input.Input;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DatabaseEditMapHorizon extends SelectorMapBase {

	int originalHorizon;
	
	@Override
	public MapView getMapView(PlatformGame game,
			Bundle savedInstanceState) {
		originalHorizon = game.getSelectedMap().groundY;
		return new HorizonView(this, game, savedInstanceState);
	}

	@Override
	protected boolean hasChanged() {
		return originalHorizon != game.getSelectedMap().groundY;
	}

	@Override
	protected void finishOk(Intent intent) {
		intent.putExtra("game", game);
		super.finishOk(intent);
	}

	private static class HorizonView extends SelectorMapView {

		private int mapStartHorizon;
		private float touchStartY;

		public HorizonView(Context context, PlatformGame game,
				Bundle savedInstanceState) {
			super(context, game, savedInstanceState);
		}
		
		@Override
		protected void createButtons() {
			super.createButtons();
			rightButton.editorButton = EditorButton.EditMapHorizonOk;
			rightButton.editorButtonDelayed = true;
		}

		@Override
		protected String getLeftButtonText() {
			switch (mode) {
			case MODE_MOVE: return "Move";
			case MODE_SELECT: return "Edit";
			}
			return "";
		}

		@Override
		protected boolean showLeftButton() {
			return true;
		}

		@Override
		protected int getBackgroundTransparency() {
			return 255;
		}

		@Override
		protected boolean doSelection() {
			float x = Input.getLastTouchX();
			float y = Input.getLastTouchY();

			if (!leftButton.isInButton(x, y) && !rightButton.isInButton(x, y)) {
				mapStartHorizon = game.getSelectedMap().groundY;
				touchStartY = y - offY;
			}
			return super.doSelection();
		}

		@Override
		protected void updateSelection(float x, float y) {
			if (Input.isTouchDown() && !leftButton.down && !rightButton.down) {
				synchronized (game) {
					game.getSelectedMap().groundY =	
							mapStartHorizon + 
							(int)(touchStartY - (Input.getLastTouchY() - offY));
				} 
			}
			super.updateSelection(x, y);
		}
	}
}
