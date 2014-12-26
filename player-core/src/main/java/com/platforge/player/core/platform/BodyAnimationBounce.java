package com.platforge.player.core.platform;

import org.jbox2d.common.Vec2;

import com.platforge.data.Vector;

public class BodyAnimationBounce extends BodyAnimation {

	private float extendX, extendY;
	
	public BodyAnimationBounce(int durationMS, Vector vector, int pixels) {
		super(durationMS);
		extendX = vector.getX() * pixels / SCALE;
		extendY = vector.getY() * pixels / SCALE;
	}

	@Override
	protected void setFrameOffset(float startTime, float endTime,
			Vec2 frameOffset) {
		float startExtension = (float)Math.sin(startTime / duration * Math.PI);
		float endExtension = (float)Math.sin(endTime / duration * Math.PI);
		float extended = endExtension - startExtension;
		
		frameOffset.set(extended * extendX, extended * extendY);
	}

}
