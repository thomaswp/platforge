package edu.elon.honors.price.graphics;

import edu.elon.honors.price.game.Rect;
import playn.core.Image;

public class BackgroundSprite {
	private Rect fullRect;
	private Image tile;
	private ImageSprite[][] sprites;
	private Viewport viewport;
	
	
	public int getZ() {
		return viewport.getZ();
	}
	
	public void setZ(int z) {
		viewport.setZ(z);
	}
	
	public float getOpacity() {
		return viewport.getOpacity();
	}
	
	public void setOpacity(float opacity) {
		viewport.setOpacity(opacity);
	}
	
	public boolean isVisible() {
		return viewport.isVisible();
	}
	
	public void setVisible(boolean visible) {
		viewport.setVisible(visible);
	}

	public float getX() {
		return viewport.getY();
	}
	
	public void setX(float x) {
		viewport.setY(x);
	}
	
	public float getY() {
		return viewport.getY();
	}
	
	public void setY(float y) {
		viewport.setY(y);
	}
	
	public Rect getRect() {
		return viewport.getRect();
	}
	
	public BackgroundSprite(Image bitmap, Rect rect, int z) {
		viewport = createViewport(rect, z);
		fullRect = viewport.getRect();
		tile = bitmap;
		createSprites();
	}
	
	public void setScroll(float x, float y) {
		ImageSprite corner = sprites[0][0];
		scroll(x + corner.getX(), y + corner.getY());
	}
	
	public void scroll(float x, float y) {
		shiftAll(-x, -y);
		
		ImageSprite corner = sprites[0][0];
		while (corner.getX() > 0) {
			shiftAll(-tile.width(), 0);
		}
		while (corner.getX() < -tile.width()) {
			shiftAll(tile.width(), 0);
		}
		while (corner.getY() > 0) {
			shiftAll(0, -tile.height());
		}
		while (corner.getY() < -tile.height()) {
			shiftAll(0, tile.height());
		}
	}

	public void dispose() {
		for (int i = 0; i < sprites.length; i++) {
			for (int j = 0; j < sprites[i].length; j++) {
				if (sprites[i][j] != null)
					sprites[i][j].dispose();
			}
		}
		Graphics.getViewports().remove(viewport);
	}

	private static Viewport createViewport(Rect rect, int z) {
		Viewport vp = new Viewport(rect.minX(), rect.minY(), rect.width(), rect.height());
		vp.setZ(z);
		return vp;
	}
	
	private void shiftAll(float x, float y) {
		for (int i = 0; i < sprites.length; i++) {
			for (int j = 0; j < sprites[i].length; j++) {
				if (sprites[i][j] != null) {
					ImageSprite s = sprites[i][j];
					s.setX(Math.round(s.getX() + x));
					s.setY(Math.round(s.getY() + y));
				}
			}
		}
	}

	private void createSprites() {
		int rows = (fullRect.height() - 1) / (int)tile.height() + 2; //round up + 1
		int cols = (fullRect.width() - 1) / (int)tile.width() + 2;
		
		sprites = new ImageSprite[rows][cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sprites[i][j] = new ImageSprite(viewport, tile);
				sprites[i][j].setX(j * (int)tile.width());
				sprites[i][j].setY(i * (int)tile.height());
			}
		}
	}
}
