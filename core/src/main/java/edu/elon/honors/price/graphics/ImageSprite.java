package edu.elon.honors.price.graphics;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;
import playn.core.ImageLayer;

/**
 * A class for a drawable sprites. Holds a Bitmap and provides
 * ways of manipulating it. 
 * 
 * @author Thomas Price
 *
 */
public class ImageSprite extends Sprite {

	protected ImageLayer layer;


	/**
	 * Gets the Bitmap held by this Sprite
	 * @return The Bitmap
	 */
	public Image getBitmap() {
		return layer.image();
	}

	/**
	 * Sets the Bitmap held by this Sprite.
	 * @param bitmap The Bitmap.
	 */
	public void setBitmap(Image bitmap) {
		layer.setImage(bitmap);
	}
	
	public boolean isMutable() {
		return getBitmap() instanceof CanvasImage;
	}
	
	public void setMutable(boolean mutable) {
		if (!isMutable() && mutable) {
			Image bitmap = getBitmap();
			CanvasImage newImage = graphics().createImage(bitmap.width(), bitmap.height());
			newImage.canvas().drawImage(bitmap, 0, 0);
			setBitmap(bitmap);
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
		return ((CanvasImage) getBitmap()).canvas();
	}


	/**
	 * Gets the width of this Sprite, including zoom
	 * @return The width
	 */
	public float getWidth() {
		return layer.width();
	}

	/**
	 * Gets the height of this Sprite, including zoom.
	 * @return The height
	 */
	public float getHeight() {
		return layer.height();
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
	public ImageSprite(Viewport viewport, float x, float y, int width, int height) {
		this(viewport, graphics().createImage(width, height));
		setX(x);
		setY(y);
	}

	/**
	 * Creates a Sprite with the given width and height.
	 * @param viewport The Viewport
	 * @param bitmap The Bitmap
	 */
	public ImageSprite(Viewport viewport, Image bitmap) {
		super(viewport, graphics().createImageLayer(bitmap));
		layer = (ImageLayer) super.layer;
	}
	
	public int convexHull(float[] xs, float ys[]) {

		//points = points in the path, checks = accuracy of the path
		int points = 8, checks = 60;

		Image bitmap = getBitmap();
		
		int width = (int) bitmap.width();
		int height = (int) bitmap.height();

		//middle x and y
		double mX = width / 2.0, mY = height / 2.0;
		
		int[] pixels = new int[width * height]; 
		bitmap.getRgb(0, 0, width, height, pixels, 0, width);

		//dTheta
		double dT = Math.PI * 2 / points;
		
		for (int i = 0; i < points; i ++) {
			//increase theta - calculate sin and cos
			double theta = dT * i;
			double sin = Math.sin(theta);
			double cos = Math.cos(theta);
			double length;

			//find out the max possible length of a line at this angle
			//going out from the origin
			if (cos == 0)
				length = mY;
			else if (sin == 0)
				length = mX;
			else
				length = Math.min(mX / Math.abs(cos), mY / Math.abs(sin));

			//deltaLength
			double dl = length / checks;

			for (int j = checks; j >= 0; j--) {
				//at each length
				double l = dl * j;
				//get x and y
				int x = (int)(mX + l * cos);
				int y = (int)(mY - l * sin);

				//if it's in the Bitmap
				if (x > 0 && y > 0 && x < width && y < height) {
					//check the color
					int color = pixels[x + y * width];
					if (Color.alpha(color) > 0) {
						//if it's not transparent, add it to the path
						xs[i] = x;
						ys[i] = y;
						
						//and go to the next theta
						break;
					}
				}
			}
		}	
		
		int pts = 8;
		for (int i = 0; i < 8; i += 2) {
			int before = (i + 7) % 8;
			int after = i + 1;
			float mpx = (xs[before] + xs[after]) / 2;
			float mpy = (ys[before] + ys[after]) / 2;
			double dx = xs[i] - mX, dy = ys[i] - mY; 
			double radThis = dx * dx + dy * dy;
			dx = mpx - mX; dy = mpy - mY;
			double radThat = dx * dx + dy * dy;
			if (radThis <= radThat + 1) {
				//xs[i] = (float)((xs[i] - mX) * (radThat + 1) / radThis + mX);
				//ys[i] = (float)((ys[i] - mY) * (radThat + 1) / radThis + mY);
				xs[i] = -1;
				ys[i] = -1;
				pts--;
			}
		}
		return pts;
	}

	@Override
	protected int baseWidth() {
		return (int) layer.image().width();
	}

	@Override
	protected int baseHeight() {
		return (int) layer.image().height();
	}
}
