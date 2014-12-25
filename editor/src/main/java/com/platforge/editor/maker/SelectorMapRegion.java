package com.platforge.editor.maker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.platforge.data.Map;
import com.platforge.data.PlatformGame;
import com.platforge.editor.input.Input;
import com.platforge.player.core.game.Debug;

public class SelectorMapRegion extends SelectorMapBase {

	private Rect originalSelection;

	@Override
	protected MapView getMapView(PlatformGame game, 
			Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		originalSelection = new Rect(
				extras.getInt("left"),
				extras.getInt("top"),
				extras.getInt("right"),
				extras.getInt("bottom")
		);
		return new RegionView(this, game, 
				savedInstanceState, originalSelection);
	}

	@Override
	protected boolean hasChanged() {
		RegionView view = (RegionView)this.view;
		Rect selection = view.getNormSelection();
		return selection.left != originalSelection.left ||
		selection.top != originalSelection.top ||
		selection.right != originalSelection.right ||
		selection.bottom != originalSelection.bottom;
	}

	@Override
	protected void finishOk(Intent intent) {
		RegionView view = (RegionView)this.view;
		Rect rect = view.getNormSelection();
		intent.putExtra("left", rect.left);
		intent.putExtra("top", rect.top);
		intent.putExtra("right", rect.right);
		intent.putExtra("bottom", rect.bottom);
		super.finishOk(intent);
	}

	protected static class RegionView extends SelectorMapView {

		protected final static int SCROLL_BORDER = 25;
		protected final static int SCROLL_TICK = 3;

		protected Rect selection, normSelection;
		protected RectF selectionF;
		protected Paint paint;

		public Rect getNormSelection() {
			normSelection.set(selection);
			if (normSelection.left > normSelection.right) {
				int temp = normSelection.left;
				normSelection.left = normSelection.right;
				normSelection.right = temp;
			}

			if (normSelection.top > normSelection.bottom) {
				int temp = normSelection.top;
				normSelection.top = normSelection.bottom;
				normSelection.bottom = temp;
			}

			Map map = game.getSelectedMap();
			normSelection.left = Math.max(0, normSelection.left);
			normSelection.top = Math.max(0, normSelection.top);
			normSelection.right = Math.min(game.getMapWidth(map) - 1, normSelection.right);
			normSelection.bottom = Math.min(game.getMapHeight(map) - 1, normSelection.bottom);


			return normSelection;
		}

		public RegionView(Context context, PlatformGame game, 
				Bundle savedInstanceState, Rect selection) {
			super(context, game, savedInstanceState);
			if (this.selection == null) {
				this.selection = new Rect();
				this.selection.set(selection);
			}
			normSelection = new Rect();
			selectionF = new RectF();
			paint = new Paint();
		}
		
		@Override
		protected void onLoadInstanceState(Bundle savedInstanceState) {
			super.onLoadInstanceState(savedInstanceState);
			this.selection = 
				(Rect)savedInstanceState.getParcelable("selection");
			Debug.write("load: %f,%f",
					savedInstanceState.getFloat("offX"),
					savedInstanceState.getFloat("offY"));
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putParcelable("selection", selection);
			Debug.write("save: %f,%f",
					outState.getFloat("offX"),
					outState.getFloat("offY"));
		}
		
		@Override
		protected boolean showLeftButton() {
			return true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			super.surfaceCreated(holder);
			Debug.write("Surface: %f,%f", offX, offY);
			if (offX == 0 && offY == 0) {
				offX = -selection.centerX() + width / 2;
				offY = -selection.centerY() + height / 2;
			}
		}

		@Override
		protected boolean doSelection() {
			float x = Input.getLastTouchX();
			float y = Input.getLastTouchY();

			if (!leftButton.isInButton(x, y) && !rightButton.isInButton(x, y)) {
				float mapX = x - offX;
				float mapY = y - offY;
				selection.set((int)mapX, (int)mapY, (int)mapX, (int)mapY);
			}

			return false;
		}

		@Override
		protected void updateSelection(float x, float y) {
			if (Input.isTouchDown() && !leftButton.down && !rightButton.down) {

				selection.right = (int)(x - offX);
				selection.bottom = (int)(y - offY);

				if (x <= SCROLL_BORDER) {
					offX += SCROLL_TICK;
					if (offX > 0) offX = 0;
				}
				if (y <= SCROLL_BORDER) {
					offY += SCROLL_TICK;
				}
				if (x >= width - SCROLL_BORDER) {
					offX -= SCROLL_TICK;
				}
				if (y >= height - SCROLL_BORDER) {
					offY -= SCROLL_TICK;
				}
			}
		}

		@Override
		public void drawGrid(Canvas c) {
			super.drawGrid(c);

			Rect normSelection = getNormSelection();
			selectionF.set(normSelection);
			selectionF.offset(offX, offY);

			paint.setColor(selectionFillColor);
			paint.setStyle(Style.FILL);
			c.drawRect(selectionF, paint);

			paint.setColor(selectionBorderColor);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(selectionBorderWidth);
			c.drawRect(selectionF, paint);

			paint.setColor(Color.BLACK);
			paint.setStyle(Style.FILL);
			paint.setTextSize(20);
			paint.setAntiAlias(true);

			//			String text = makeText(selection);
			//			c.drawText(text, 0, paint.getTextSize(), paint);

			if (!normSelection.isEmpty()) {
				String text = topLeftString(normSelection);
				c.drawText(text, selectionF.left - paint.measureText(text) / 2, 
						selectionF.top - 5, paint);

				text = bottomRightString(normSelection);
				c.drawText(text, selectionF.right - paint.measureText(text) / 2, 
						selectionF.bottom + paint.getTextSize() + 5, paint);
			}
		}

		protected String makeText(Rect rect) {
			StringBuilder sb = new StringBuilder();
			sb.append("\u21F1: (").append(rect.left).append(", ").append(rect.top)
			.append(") - \u21F2: (").append(rect.right).append(", ").append(rect.bottom)
			.append(")");
			return sb.toString();
		}

		protected String topLeftString(Rect rect) {
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(rect.left).append(", ").append(rect.top).append(")");
			return sb.toString();
		}

		protected String bottomRightString(Rect rect) {
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(rect.right).append(", ").append(rect.bottom).append(")");
			return sb.toString();
		}
	}
}
