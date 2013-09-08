package com.twp.platform;

import org.jbox2d.common.Vec2;

public abstract class BodyAnimation {
	public final static float SCALE = PlatformBody.SCALE;
	
	protected int duration;
	protected long totalTimeElapsed;
	private Vec2 frameOffset = new Vec2();
	private float dX, dY;
	
	public boolean isFinished() {
		return totalTimeElapsed > duration;
	}
	
	public BodyAnimation(int durationMS) {
		this.duration = durationMS;
	}
	
	public Vec2 getResetVector() {
		frameOffset.set(-dX, -dY);
		return frameOffset;
	}
	
	public Vec2 getFrameOffset(long timeElapsed) {
		float startTime = totalTimeElapsed;
		totalTimeElapsed += timeElapsed;
		float endTime = totalTimeElapsed;
		if (endTime > duration) {
			frameOffset.set(-dX, -dY);
			dX = 0;
			dY = 0;
		} else {
			setFrameOffset(startTime, endTime, frameOffset);
			dX += frameOffset.x;
			dY += frameOffset.y;
		}
		return frameOffset;
	}
	
	protected abstract void setFrameOffset(float startTime, float endTime,
			Vec2 frameOffset);
}
