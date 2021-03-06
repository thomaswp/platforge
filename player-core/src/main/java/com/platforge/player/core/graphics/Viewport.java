package com.platforge.player.core.graphics;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;

import com.platforge.player.core.game.Rect;
import com.platforge.player.core.game.RectF;

import playn.core.GroupLayer;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;

public class Viewport implements Comparable<Viewport> {

	//A width or height given when the Viewport should be stretched
	//to fill the drawable area
	public static final int STRETCH = -1;

	private int width, height;

	private Rect rect = new Rect();
	private RectF rectF = new RectF();
	
	protected GroupLayer layer;
	
	private List<Sprite> sprites = new ArrayList<Sprite>();

	public static Viewport DefaultViewport = new Viewport(0, 0, STRETCH, STRETCH);
	public static Viewport DebugViewport = new Viewport(0, 0, STRETCH, STRETCH);

	/**
	 * Gets the X coordinate of the Viewport
	 * @return The X coordinate
	 */
	public float getX() {
		return layer.tx();
	}

	/**
	 * Sets the X coordinate of the Viewport
	 * @param x The X coordinate
	 */
	public void setX(float x) {
		layer.setTx(x);
	}

	/**
	 * Gets the Y coordinate of the Viewport
	 * @return The Y Coordinate
	 */
	public float getY() {
		return layer.ty();
	}

	/**
	 * Sets the Y coordinate of the Viewport
	 * @param y The Y Coordinate
	 */
	public void setY(float y) {
		layer.setTy(y);
	}

	/**
	 * Gets the Z coordinate of the Viewport. This determines
	 * the drawing order of this Viewport. High Z coordinates
	 * put the Viewport further on top.
	 * @return The Z Coordinate
	 */
	public int getZ() {
		return (int)(layer.depth() + 0.5f);
	}
	
	/**
	 * Sets the Z coordinate of the Viewport. This determines
	 * the drawing order of this Viewport. High Z coordinates
	 * put the Viewport further on top.
	 * @param z The Z Coordinate
	 */
	public void setZ(int z) {
		layer.setDepth(z);
	}

	public int getColor() {
		return layer.tint();
	}

	public void setColor(int color) {
		layer.setTint(color);
	}

	public float getOpacity() {
		return layer.alpha();
	}

	public void setOpacity(float opacity) {
		layer.setAlpha(opacity);
	}

	public float getZoomX() {
		return layer.scaleX();
	}

	public void setZoomX(float zoomX) {
		layer.setScaleX(zoomX);
	}

	public float getZoomY() {
		return layer.scaleY();
	}

	public void setZoomY(float zoomY) {
		layer.setScaleY(zoomY);
	}

	public float getRotation() {
		return layer.rotation();
	}

	public void setRotation(float rotation) {
		layer.setRotation(rotation);
	}

	public float getOriginX() {
		return layer.originX();
	}

	public void setOriginX(float originX) {
		layer.setOrigin(originX, getOriginY());
	}

	public float getOriginY() {
		return layer.originY();
	}

	public void setOriginY(float originY) {
		layer.setOrigin(getOriginX(), originY);
	}


	/**
	 * Gets the width of the Viewport.
	 * Viewport.STRETCH means it will fill the drawable area.
	 * @return The width
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * Sets the width of the Viewport.
	 * Viewport.STRETCH means it will fill the drawable area.
	 * @param width The width
	 */
	public void setWidth(int width) {
		this.width = width;
		refreshClip();
	}

	/**
	 * Gets the height of the Viewport.
	 * Viewport.STRETCH means it will fill the drawable area.
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the Viewport.
	 * Viewport.STRETCH means it will fill the drawable area.
	 * @param height The width
	 */
	public void setHeight(int height) {
		this.height = height;
		refreshClip();
	}

	/**
	 * Returns true if the Viewport has a defined
	 * height and width.
	 * @return 
	 */
	public boolean hasRect() {
		return height != STRETCH && width != STRETCH;
	}

	/**
	 * Gets the rect that the Viewport fills.
	 * @return
	 */
	public Rect getRect() {
		if (!hasRect()) {
			rect.set(0, 0, Graphics.getWidth(), Graphics.getHeight());
		} else {
			rect.set((int)getX(), (int)getY(), width + (int)getX(), height + (int)getY());
		}
		return rect;
	}

	public RectF getRectF() {
		if (!hasRect()) {
			rectF.set(0, 0, Graphics.getWidth(), Graphics.getHeight());
		} else {
			rectF.set(getX(), getY(), width + getX(), height + getY());
		}
		return rectF;
	}

	/**
	 * Gets the list of Sprites contained in this Viewport 
	 * @return The Sprites
	 */
	public List<Sprite> getSprites() {
		return sprites;
	}

	/**
	 * Gets whether or not this Viewport's Sprites will be drawn
	 * @return The visibility
	 */
	public boolean isVisible() {
		return layer.visible();
	}

	/**
	 * Sets whether or not this Viewport's Sprites will be drawn
	 * @param visible The visibility
	 */
	public void setVisible(boolean visible) {
		layer.setVisible(visible);
	}

	public Viewport() {
		this(0, 0, STRETCH, STRETCH);
	}

	/**
	 * Initializes the viewport with the given dimensions.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param width The width
	 * @param height The height
	 */
	public Viewport(float x, float y, int width, int height) {
		layer = graphics().createGroupLayer();
		setX(x); setY(y);
		this.width = width;
		this.height = height;
		refreshClip();

		Graphics.addViewport(this);
	}
	
	private void refreshClip() {
		layer.clear();
		
		Renderer renderer = new Renderer() {
			@Override
			public void render(Surface surface) {
				for (int i = 0; i < sprites.size(); i++) {
					surface.drawLayer(sprites.get(i).layer);
				}
			}
		};
		
		ImmediateLayer immediateLayer;
		if (width == STRETCH || height == STRETCH) {
			immediateLayer = graphics().createImmediateLayer(renderer);
		} else {
			immediateLayer = graphics().createImmediateLayer(width, height, renderer);
		}
		layer.add(immediateLayer);
	}

	/**
	 * Adds a Sprite to this Viewport
	 * @param sprite the Sprite
	 */
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	/**
	 * Removes a Sprite from this Viewport
	 * @param sprite The Sprite
	 */
	public void removeSprite(Sprite sprite) {
		sprites.remove(sprite);
	}

	public boolean isSpriteInBounds(ImageSprite sprite) {
		RectF spriteRect = sprite.getRect();
		RectF viewportRect = getRectF();
		return !(spriteRect.minX() > viewportRect.maxX() ||
				spriteRect.maxX() < viewportRect.minX() ||
				spriteRect.minY() > viewportRect.maxY() ||
				spriteRect.maxY() < viewportRect.minY());
	}

	@Override
	public int compareTo(Viewport another) {
		if (this == DebugViewport) {
			return -1;
		} else if (another == DebugViewport) {
			return 1;
		}
		return ((Integer)getZ()).compareTo(another.getZ());
	}

	/**
	 * Updates this Viewport and each Sprite in it
	 */
	public void update(long timeElapsed) {
		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.get(i);
			if (sprite.isDisposed()) {
				removeSprite(sprite);
				i--;
			} else if (sprite != null) {
				sprite.update(timeElapsed);
			}
		}
	}


}
