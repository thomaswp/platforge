package edu.elon.honors.price.graphics;

import playn.core.Image;


public class AnimatedSprite extends Sprite {
	private int frame;
	private Image[] frames;
	private int animFrameLength, animStartFrame, animNumFrames, animTime = -1;
	
	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
		StopAnimation();
		setBitmap(frames[frame]);
	}

	public Image[] getFrames() {
		return frames;
	}
	
	public boolean isAnimated() {
		return animTime >= 0;
	}
	
	public AnimatedSprite(Viewport viewport, Image[] frames, float x, float y) {
		super(viewport, frames[0]);
		setX(x);
		setY(y);
		this.frames = frames;
	}
	
	public void Animate(int frameLength, int startFrame, int numFrames) {
		animTime = 0;
		animFrameLength = frameLength;
		animStartFrame = startFrame;
		animNumFrames = numFrames;
	}
	
	public void StopAnimation() {
		animTime = -1;
	}
	
	@Override
	public void update(long timeElapsed) {
		if (animTime >= 0) {
			animTime += timeElapsed;
			frame = animStartFrame + ((animTime / animFrameLength) % animNumFrames);
			setBitmap(frames[frame]);
		}
		super.update(timeElapsed);
	}
}
