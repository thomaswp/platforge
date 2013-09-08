package edu.elon.honors.price.graphics;

import playn.core.Color;
import playn.core.Layer;
import pythagoras.f.Point;
import tripleplay.util.Colors;

import com.twp.platform.VectorUtils;

import edu.elon.honors.price.game.RectF;

public abstract class Sprite implements Comparable<Sprite> {
	
	protected Viewport viewport;
	protected Object tag;
	protected int timeout = -1;
	protected RectF rect = new RectF();
	protected int baseColor = Colors.WHITE;
	protected int flashColor, flashDuration, flashFrame;
	protected Layer layer;
	
	protected abstract int baseWidth();
	protected abstract int baseHeight();
	
	public int getCompositeColor() {
		return layer.tint();
	}
	
	public int getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(int color) {
		this.baseColor = color;
		updateCompositeColor();
	}

	public float getOpacity() {
		return layer.alpha();
	}

	public void setOpacity(float opacity) {
		layer.setAlpha(Math.min(Math.max(opacity, 0), 1));
	}

	/**
	 * Gets the X coordinate of this Sprite.
	 * @return The X coordinate
	 */
	public float getX() {
		return layer.tx();
	}

	/**
	 * Sets the X coordinate of this Sprite
	 * @param x The X Coordinate
	 */
	public void setX(float x) {
		layer.setTx(x);
	}

	/**
	 * Gets the Y coordinate of this Sprite.
	 * @return The Y coordinate
	 */
	public float getY() {
		return layer.ty();
	}

	/**
	 * Sets the Y coordinate of this Sprite
	 * @param y The Y Coordinate
	 */
	public void setY(float y) {
		layer.setTy(y);
	}
	
	/**
	 * Gets the width of this Sprite, including zoom
	 * @return The width
	 */
	public float getWidth() {
		return baseWidth() * layer.scaleX();
	}

	/**
	 * Gets the height of this Sprite, including zoom.
	 * @return The height
	 */
	public float getHeight() {
		return baseHeight() * layer.scaleY();
	}

	/**
	 * Gets the X coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The X coordinate
	 */
	public float getOriginX() {
		return layer.originX();
	}

	/**
	 * Sets the X coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return
	 */
	public void setOriginX(float originX) {
		layer.setOrigin(originX, getOriginY());
	}

	/**
	 * Gets the Y coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The Y coordinate
	 */
	public float getOriginY() {
		return layer.originY();
	}

	/**
	 * Sets the Y coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The Y coordinate
	 */
	public void setOriginY(float originY) {
		layer.setOrigin(getOriginX(), originY);
	}

	/**
	 * Gets the Z coordinate of the Sprite. This determines
	 * the drawing order of this Sprite. High Z coordinates
	 * put the Sprite further on top.
	 * @return The Z coordinate
	 */
	public int getZ() {
		return (int)(layer.depth() + 0.5f);
	}
	/**
	 * Sets the Z coordinate of the Sprite. This determines
	 * the drawing order of this Sprite. High Z coordinates
	 * put the Sprite further on top.
	 * @param z The Z coordinate
	 */
	public void setZ(int z) {
		layer.setDepth(z);
	}

	
	Point temp = new Point();
	/**
	 * Gets the Rect of this sprite, or the Bitmap's Rect
	 * transformed by this Sprite's transform Matrix.
	 * @return
	 */
	public RectF getRect() {
		temp.set(0, 0);
		Layer.Util.layerToScreen(layer, temp, temp);
		float x = temp.x, y = temp.y;
		temp.set(baseWidth(), baseHeight());
		Layer.Util.layerToScreen(layer, temp, temp);
		rect.set(x, y, temp.x, temp.y);
		return rect;
	}

	/**
	 * Gets the Viewport on which the Sprite is drawn.
	 * @return The Viweport
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * Gets whether or not this Sprite will be drawn
	 * @return The visibility
	 */
	public boolean isVisible() {
		return layer.visible();
	}


	/**
	 * Sets whether or not this Sprite will be drawn
	 * @return The visibility
	 */
	public void setVisible(boolean visible) {
		layer.setVisible(visible);
	}

//	/**
//	 * Gets whether or not this Sprite's Bitmap can be edited
//	 * using it's canvas.
//	 * @return
//	 */
//	public boolean isMutable() {
//		return bitmap.isMutable();
//	}
//
//	/**
//	 * Sets the mutability of this Sprite's bitmap.
//	 * @param mutable
//	 */
//	public void setMutable(boolean mutable) {
//		if (mutable != bitmap.isMutable()) {
//			bitmap = bitmap.copy(defaultConfig, mutable);
//			if (mutable) {
//				bitmapCanvas = new Canvas();
//				bitmapCanvas.setBitmap(bitmap);
//			} else {
//				bitmapCanvas = null;
//			}
//		}
//	}

	/**
	 * Gets the x zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @return The zoom
	 */
	public float getZoomX() {
		return layer.scaleX();
	}

	/**
	 * Sets the x zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @param zoom The zoom
	 */
	public void setZoomX(float zoom) {
		layer.setScaleX(zoom);
	}
	
	/**
	 * Gets the y zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @return The zoom
	 */
	public float getZoomY() {
		return layer.scaleY();
	}

	/**
	 * Sets the y zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @param zoom The zoom
	 */
	public void setZoomY(float zoom) {
		layer.setScaleY(zoom);
	}
	
	/**
	 * Sets the x and y zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @param zoom The zoom
	 */
	public void setZoom(float zoom) {
		setZoomX(zoom);
		setZoomY(zoom);
	}
	
	/**
	 * Gets the rotation of this Sprite.
	 * Rotation ranges from 0 to 360 degrees with 0 being North.
	 * @return The rotation
	 */
	public float getRotation() {
		return layer.rotation() * VectorUtils.RADIANS_TO_DEGREES;
	}
	
	/**
	 * Sets the rotation of this Sprite.
	 * Rotation ranges from 0 to 360 degrees with 0 being North.
	 * @param rotation The rotation
	 */
	public void setRotation(float rotation) {
		while (rotation < 0)
			rotation += 360;
		layer.setRotation(rotation / VectorUtils.RADIANS_TO_DEGREES);
	}

//	public Matrix getDrawMatrix() {
//		if (lastViewportX != viewport.getX() ||
//				lastViewportY != viewport.getY()) {
//			lastViewportX = viewport.getX();
//			lastViewportY = viewport.getY();
//			resetMatrix = true;
//		}
//		if (resetMatrix) {
//			createDrawMatrix();
//		}
//		return drawMatrix;
//	}


//	/**
//	 * Gets the collidable region for this sprite.
//	 * This region includes all opaque regions of the bitmap
//	 * transformed to the current location of the sprite.
//	 * @return
//	 */
//	public Region getCollidableRegion() {
//		createCollidableRegion();
//		return collidableRegion;
//	}
	
	public Object getTag() {
		return tag;
	}
	
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isDisposed() {
		return layer.destroyed();
	}

	protected Sprite(Viewport viewport, Layer layer) {
		this.viewport = viewport;
		this.layer = layer;
		addToViewport();
	}
	
	protected Sprite(Viewport viewport) {
		this.viewport = viewport;
	}
	
	protected void addToViewport() {
		viewport.addSprite(this);
	}
	
	/**
	 * Centers the origin of this Sprite.
	 */
	public void centerOrigin() {
		layer.setOrigin(baseWidth() / 2, baseHeight() / 2);
	}

	@Override
	public int compareTo(Sprite another) {
		return ((Integer)getZ()).compareTo(another.getZ());
	}

	/**
	 * Marks the sprite as disposed
	 */
	public void dispose() {
		layer.destroy();
	}
	
	/**
	 * Updates the Sprite
	 */
	public void update(long timeElapsed) {
		if (timeout > 0) {
			if (timeElapsed >= timeout) {
				timeout = -1;
				dispose();
			} else {
				timeout -= timeElapsed;
			}
		}
		
		if (flashFrame < flashDuration) {
			updateCompositeColor();
			
			flashFrame += timeElapsed;
			
			if (flashFrame >= flashDuration) {
				updateCompositeColor();
			}
		}
	}
	
	private void updateCompositeColor() {
		
		if (flashFrame >= flashDuration) {
			layer.setTint(baseColor);
			return;
		}
		
		float perc = flashFrame * 1.0f / flashDuration;
		
		int c1 = baseColor;
		int c2 = flashColor;
		if (perc < 0.5f) {
			perc *= 2;
		} else {
			perc = (1 - perc) * 2;
		}
	
		
		layer.setTint(Color.argb(
				(int)(Color.alpha(c1) * (1 - perc) + Color.alpha(c2) * perc), 
				(int)(Color.red(c1) * (1 - perc) + Color.red(c2) * perc), 
				(int)(Color.green(c1) * (1 - perc) + Color.green(c2) * perc), 
				(int)(Color.blue(c1) * (1 - perc) + Color.blue(c2) * perc)));
	}
	
	public void flash(int color, int duration) {
		layer.setTint(baseColor);
		this.flashColor = color;
		this.flashDuration = duration;
		this.flashFrame = 0;
	}
	
	@Override
	public String toString() {
		return "Sprite: {" + getX() + "," + getY() + "," + getZ() + "}";
	}
}
