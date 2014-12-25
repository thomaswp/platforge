package com.platforge.player.core.graphics;

import static playn.core.PlayN.graphics;

import com.platforge.data.Vector;

import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;

public class AnimatedSprite extends Sprite {

	private Image image;
	private Vector[][] frames;
	private int width, height;
	private int frameRow, frameCol;
	private boolean flipped;
	
	public int getFrameRow() {
		return frameRow;
	}
	
	public int getFrameCol() {
		return frameCol;
	}

	public void setFrame(int frameRow, int frameCol) {
		this.frameRow = frameRow;
		this.frameCol = frameCol;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}
	
	public AnimatedSprite(Viewport viewport, Image image, Vector[][] frames, 
			float x, float y, int width, int height) {
		super(viewport);
		this.image = image;
		this.frames = frames;
		this.width = width;
		this.height = height;
		
		ImmediateLayer layer = graphics().createImmediateLayer(width, height, new Renderer() {
			@Override
			public void render(Surface surface) {
				draw(surface);
			}
		});
		super.layer = layer;
		addToViewport();

		setX(x);
		setY(y);
	}
	
	private void draw(Surface surface) {
		Vector offset = frames[frameRow][frameCol];
		if (flipped) {
			surface.drawImage(image, 0, 0, width, height, offset.getX() + width, offset.getY(), -width, height); 
		} else {
			surface.drawImage(image, -offset.getX(), -offset.getY() - 1);
		}
	}

	@Override
	protected int baseWidth() {
		return width;
	}

	@Override
	protected int baseHeight() {
		return height;
	}
}
