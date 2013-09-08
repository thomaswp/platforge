package edu.elon.honors.price.graphics;

import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;
import edu.elon.honors.price.physics.Vector;

public class AnimatedSprite extends Sprite {

	private Image image;
	private Vector[][] frames;
	private int width, height;
	private int frameRow, frameCol;
	
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
	
	public AnimatedSprite(Viewport viewport, Image image, Vector[][] frames, 
			int x, int y, int width, int height) {
		super(viewport);
		setX(x);
		setY(y);
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
	}
	
	private void draw(Surface surface) {
		Vector offset = frames[frameRow][frameCol];
		surface.drawImage(image, offset.getX(), offset.getY());
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
