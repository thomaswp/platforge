package edu.elon.honors.price.graphics;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;
import playn.core.ImageLayer;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import tripleplay.util.Colors;

/**
 * A class for a drawable sprites. Holds a Bitmap and provides
 * ways of manipulating it. 
 * 
 * @author Thomas Price
 *
 */
public class Sprite implements Comparable<Sprite> {

	private Image bitmap;
	private Viewport viewport;
	
	private Object tag;

	protected ImageLayer imageLayer;
	
	private int timeout = -1;

	private boolean bitmapModified;
	
	private float opacity = 1;
	
//	private float lastViewportX, lastViewportY;
	
	private Rectangle rect = new Rectangle(); //, mapRect = new Rectangle();

	//Matrix to transform this sprite to its location, zoom and rotation
//	private Matrix drawMatrix = new Matrix();
//	private boolean resetMatrix = true;
	//Region of this sprite, not including transparent area
//	private Region collidableRegion = new Region();
	//A path created for the bitmap of the opaque regions
//	private Path bitmapPath = new Path();
	
	private int baseColor = Colors.WHITE;
	private int flashColor, flashDuration, flashFrame;


	/**
	 * Gets the Bitmap held by this Sprite
	 * @return The Bitmap
	 */
	public Image getBitmap() {
		return bitmap;
	}

	/**
	 * Sets the Bitmap held by this Sprite.
	 * @param bitmap The Bitmap.
	 */
	public void setBitmap(Image bitmap) {
		this.bitmap = bitmap;
		//bitmapPath.reset();
	}
	
	public boolean isMutable() {
		return bitmap instanceof CanvasImage;
	}
	
	public void setMutable(boolean mutable) {
		if (!isMutable() && mutable) {
			CanvasImage newImage = graphics().createImage(bitmap.width(), bitmap.height());
			newImage.canvas().drawImage(bitmap, 0, 0);
			bitmap = newImage;
		}
	}

	/**
	 * Gets the Canvas used to draw on this Sprite's Bitmap.
	 * Returns null if the Bitmap is not mutable. Do not store
	 * the bitmap canvas. This may cause unexpected results.
	 * Reacquire it each time you access it.
	 * @return The Canvas
	 */
	public Canvas getBitmapCanvas() {
		if (!isMutable()) return null;
		
		//bitmapPath.reset();
		bitmapModified = true;
		return ((CanvasImage) bitmap).canvas();
	}

	public int getCompositeColor() {
		return imageLayer.tint();
	}
	
	public int getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(int color) {
		this.baseColor = color;
		updateCompositeColor();
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = Math.min(Math.max(opacity, 0), 1);
	}

	/**
	 * Gets the X coordinate of this Sprite.
	 * @return The X coordinate
	 */
	public float getX() {
		return imageLayer.tx();
	}

	/**
	 * Sets the X coordinate of this Sprite
	 * @param x The X Coordinate
	 */
	public void setX(float x) {
		imageLayer.setTx(x);
	}

	/**
	 * Gets the Y coordinate of this Sprite.
	 * @return The Y coordinate
	 */
	public float getY() {
		return imageLayer.ty();
	}

	/**
	 * Sets the Y coordinate of this Sprite
	 * @param y The Y Coordinate
	 */
	public void setY(float y) {
		imageLayer.setTy(y);
	}

	/**
	 * Gets the X coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The X coordinate
	 */
	public float getOriginX() {
		return imageLayer.originX();
	}

	/**
	 * Sets the X coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return
	 */
	public void setOriginX(float originX) {
		imageLayer.setOrigin(originX, getOriginY());
	}

	/**
	 * Gets the Y coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The Y coordinate
	 */
	public float getOriginY() {
		return imageLayer.originY();
	}

	/**
	 * Sets the Y coordinate of this Sprite's origin.
	 * The origin is point where the Sprite will be draw.
	 * @return The Y coordinate
	 */
	public void setOriginY(float originY) {
		imageLayer.setOrigin(getOriginX(), originY);
	}

	/**
	 * Gets the Z coordinate of the Sprite. This determines
	 * the drawing order of this Sprite. High Z coordinates
	 * put the Sprite further on top.
	 * @return The Z coordinate
	 */
	public int getZ() {
		return (int)(imageLayer.depth() + 0.5f);
	}
	/**
	 * Sets the Z coordinate of the Sprite. This determines
	 * the drawing order of this Sprite. High Z coordinates
	 * put the Sprite further on top.
	 * @param z The Z coordinate
	 */
	public void setZ(int z) {
		imageLayer.setDepth(z);
	}

	/**
	 * Gets the width of this Sprite, including zoom
	 * @return The width
	 */
	public float getWidth() {
		return imageLayer.width();
	}

	/**
	 * Gets the height of this Sprite, including zoom.
	 * @return The height
	 */
	public float getHeight() {
		return imageLayer.height();
	}

	
	Point temp = new Point();
	/**
	 * Gets the Rect of this sprite, or the Bitmap's Rect
	 * transformed by this Sprite's transform Matrix.
	 * @return
	 */
	public Rectangle getRect() {
		temp.set(0, 0);
		imageLayer.transform().inverseTransform(temp, temp);
		float x = temp.x, y = temp.y;
		temp.set(bitmap.width(), bitmap.height());
		imageLayer.transform().inverseTransform(temp, temp);
		rect.setBounds(x, y, temp.x - x, temp.y - y);
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
		return imageLayer.visible();
	}


	/**
	 * Sets whether or not this Sprite will be drawn
	 * @return The visibility
	 */
	public void setVisible(boolean visible) {
		imageLayer.setVisible(visible);
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
		return imageLayer.scaleX();
	}

	/**
	 * Sets the x zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @param zoom The zoom
	 */
	public void setZoomX(float zoom) {
		imageLayer.setScaleX(zoom);
	}
	
	/**
	 * Gets the y zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @return The zoom
	 */
	public float getZoomY() {
		return imageLayer.scaleY();
	}

	/**
	 * Sets the y zoom of this sprite.
	 * Zoom of less than 1.0 shrinks the image and
	 * more than 1.0 enlarges it.
	 * @param zoom The zoom
	 */
	public void setZoomY(float zoom) {
		imageLayer.setScaleY(zoom);
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
		return imageLayer.rotation();
	}
	
	/**
	 * Sets the rotation of this Sprite.
	 * Rotation ranges from 0 to 360 degrees with 0 being North.
	 * @param rotation The rotation
	 */
	public void setRotation(float rotation) {
		while (rotation < 0)
			rotation += 360;
		imageLayer.setRotation(rotation);
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
	
	public boolean isBitmapModified() {
		return bitmapModified;
	}

	public void setBitmapModified(boolean bitmapModified) {
		this.bitmapModified = bitmapModified;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isDisposed() {
		return imageLayer.destroyed();
	}

	/**
	 * Creates a Sprite with the given Viewport and coordinates and creates
	 * a new Bitmap with the given width and height.
	 * 
	 * @param viewport The Viewport
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param width The width this Sprite's Bitmap
	 * @param height The height this Sprite's Bitmap
	 */
	public Sprite(Viewport viewport, float x, float y, int width, int height) {
		this(viewport, graphics().createImage(width, height));
		setX(x);
		setY(y);
	}

	/**
	 * Creates a Sprite with the given width and height.
	 * @param viewport The Viewport
	 * @param bitmap The Bitmap
	 */
	public Sprite(Viewport viewport, Image bitmap) {
		this.viewport = viewport;
		this.imageLayer = graphics().createImageLayer(bitmap);
		setBitmap(bitmap);
		viewport.addSprite(this);
	}

	/**
	 * Marks the sprite as disposed
	 */
	public void dispose() {
		imageLayer.destroy();
	}

//	/**
//	 * Gets the region of intersection between this sprite
//	 * and another Sprite. If empty the two Sprites are disjoint.
//	 * @param sprite The sprite to test
//	 * @return The region of intersection.
//	 */
//	public Region intersection(Sprite sprite) {
//		if (sprite.getRect().intersect(getRect())) {
//			collideRegion.set(getCollidableRegion());
//			collideRegion.op(sprite.getCollidableRegion(), Op.INTERSECT);
//			return collideRegion;
//		} else {
//			return new Region();
//		}
//	}
//
//	private Region rectRegion = new Region();
//	private Region collideRegion = new Region();
//	private Rect convertRect = new Rect();
//	public Region intersection(RectF rect) {
//		if (rect.intersect(getRect())) {
//			collideRegion.set(getCollidableRegion());
//			convertRect.set((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
//			rectRegion.set(convertRect);
//			collideRegion.op(rectRegion, Op.INTERSECT);
//			return collideRegion;
//		} else {
//			return new Region();
//		}
//	}

	
	/**
	 * Centers the origin of this Sprite.
	 */
	public void centerOrigin() {
		imageLayer.setOrigin(bitmap.width() / 2, bitmap.height() / 2);
	}

	@Override
	public int compareTo(Sprite another) {
		return ((Integer)getZ()).compareTo(another.getZ());
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
			imageLayer.setTint(baseColor);
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
	
		
		imageLayer.setTint(Color.argb(
				(int)(Color.alpha(c1) * (1 - perc) + Color.alpha(c2) * perc), 
				(int)(Color.red(c1) * (1 - perc) + Color.red(c2) * perc), 
				(int)(Color.green(c1) * (1 - perc) + Color.green(c2) * perc), 
				(int)(Color.blue(c1) * (1 - perc) + Color.blue(c2) * perc)));
	}
	
	public void flash(int color, int duration) {
		imageLayer.setTint(baseColor);
		this.flashColor = color;
		this.flashDuration = duration;
		this.flashFrame = 0;
	}
	
	@Override
	public String toString() {
		return "Sprite: {" + getX() + "," + getY() + "," + getZ() + "}";
	}

	private void createDrawMatrix() {
//		drawMatrix.reset();
//		//center the sprite at the origin
//		if (originX != 0 || originY != 0)
//			drawMatrix.postTranslate(-originX, -originY);
//		//rotate and zoom
//		if (rotation != 0)
//			drawMatrix.postRotate(rotation);
//		if (zoomX != 1 || zoomY != 1)
//			drawMatrix.postScale(zoomX, zoomY);
//		//then move it to it's position (relative to the Viewport)
//		if (viewport.getX() != 0 || x != 0 || viewport.getY() != 0 || y != 0) {
//			drawMatrix.postTranslate(viewport.getX() + x, 
//					viewport.getY() + y);
//		}
//		resetMatrix = false;
	}

	private void createBitmapPath() {

//		bitmapPath.reset();
//
//		//points = points in the path, checks = accuracy of the path
//		int points = 120, checks = 60;
//
//		int width = bitmap.getWidth();
//		int height = bitmap.getHeight();
//
//		//middle x and y
//		double mX = width / 2.0, mY = height / 2.0;
//
//		//dTheta
//		double dT = Math.PI * 2 / points;
//
//		//have we started the path
//		boolean start = false;
//
//		for (int i = 0; i < points - 1; i ++) {
//			//increase theta - calculate sin and cos
//			double theta = dT * i;
//			double sin = Math.sin(theta);
//			double cos = Math.cos(theta);
//			double length;
//
//			//find out the max possible length of a line at this angle
//			//going out from the origin
//			if (cos == 0)
//				length = mY;
//			else if (sin == 0)
//				length = mX;
//			else
//				length = Math.min(mX / Math.abs(cos), mY / Math.abs(sin));
//
//			//deltaLength
//			double dl = length / checks;
//
//			for (int j = checks; j >= 0; j--) {
//				//at each length
//				double l = dl * j;
//				//get x and y
//				int x = (int)(mX + l * cos);
//				int y = (int)(mY - l * sin);
//
//				//if it's in the Bitmap
//				if (x > 0 && y > 0 && x < width && y < height) {
//					//check the color
//					int color = bitmap.getPixel(x, y);
//					if (Color.alpha(color) > 0) {
//						//if it's not transparent, add it to the path
//						if (!start) {
//							bitmapPath.moveTo(x, y);
//							start = true;
//						}
//						else
//							bitmapPath.lineTo(x, y);
//						//and go to the next theta
//						break;
//					}
//				}
//			}
//		}
//
//		bitmapPath.close();		
	}
	
//	private int convexHull(float[] xs, float ys[]) {
//
//		bitmapPath.reset();
//
//		//points = points in the path, checks = accuracy of the path
//		int points = 8, checks = 60;
//
//		int width = bitmap.getWidth();
//		int height = bitmap.getHeight();
//
//		//middle x and y
//		double mX = width / 2.0, mY = height / 2.0;
//
//		//dTheta
//		double dT = Math.PI * 2 / points;
//		
//		for (int i = 0; i < points; i ++) {
//			//increase theta - calculate sin and cos
//			double theta = dT * i;
//			double sin = Math.sin(theta);
//			double cos = Math.cos(theta);
//			double length;
//
//			//find out the max possible length of a line at this angle
//			//going out from the origin
//			if (cos == 0)
//				length = mY;
//			else if (sin == 0)
//				length = mX;
//			else
//				length = Math.min(mX / Math.abs(cos), mY / Math.abs(sin));
//
//			//deltaLength
//			double dl = length / checks;
//
//			for (int j = checks; j >= 0; j--) {
//				//at each length
//				double l = dl * j;
//				//get x and y
//				int x = (int)(mX + l * cos);
//				int y = (int)(mY - l * sin);
//
//				//if it's in the Bitmap
//				if (x > 0 && y > 0 && x < width && y < height) {
//					//check the color
//					int color = bitmap.getPixel(x, y);
//					if (Color.alpha(color) > 0) {
//						//if it's not transparent, add it to the path
//						xs[i] = x;
//						ys[i] = y;
//						
//						//and go to the next theta
//						break;
//					}
//				}
//			}
//		}	
//		
//		int pts = 8;
//		for (int i = 0; i < 8; i += 2) {
//			int before = (i + 7) % 8;
//			int after = i + 1;
//			float mpx = (xs[before] + xs[after]) / 2;
//			float mpy = (ys[before] + ys[after]) / 2;
//			double dx = xs[i] - mX, dy = ys[i] - mY; 
//			double radThis = dx * dx + dy * dy;
//			dx = mpx - mX; dy = mpy - mY;
//			double radThat = dx * dx + dy * dy;
//			if (radThis <= radThat + 1) {
//				//xs[i] = (float)((xs[i] - mX) * (radThat + 1) / radThis + mX);
//				//ys[i] = (float)((ys[i] - mY) * (radThat + 1) / radThis + mY);
//				xs[i] = -1;
//				ys[i] = -1;
//				pts--;
//			}
//		}
//		return pts;
//	}

//	private Path tempPath = new Path();
//	private Path getPath() {
//		//get the path and transform it by the drawMatrix
//		if (bitmapPath.isEmpty()) {
//			createBitmapPath();
//		}
//		tempPath.set(bitmapPath);
//		tempPath.transform(getDrawMatrix());
//		return tempPath;
//	}
//
//	private void createCollidableRegion() {
//		//create a region from the path
//		collidableRegion.setEmpty();
//		collideRegion.set(viewport.getRect());
//		collidableRegion.setPath(getPath(), collideRegion);
//	}
}
