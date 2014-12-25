package com.platforge.player.core.action;

import com.platforge.data.action.ActionAnimate;
import com.platforge.data.action.ActionAnimate.WithBounceData;
import com.platforge.physics.Vector;
import com.platforge.player.core.platform.BodyAnimation;
import com.platforge.player.core.platform.BodyAnimationBounce;
import com.platforge.player.core.platform.PlatformBody;

public class InterpreterAnimate extends ActionInterpreter<ActionAnimate> {

	@Override
	protected void interperate(ActionAnimate action, PlatformGameState gameState)
			throws ParameterException {

		PlatformBody body;
		if (action.animateActor) {
			body = action.animateActorData.readActorInstance(gameState);
		} else if (action.animateObject) {
			body = action.animateObjectData.readObjectInstance(gameState);
		} else {
			throw new UnsupportedException();
		}
		
		final BodyAnimation animation;
		if (action.withBounce) {
			WithBounceData data = action.withBounceData;
			int duration = data.readDuration(gameState) * 100;
			int distance = data.readDistance(gameState);
			Vector vector = data.readDirection(gameState);
			animation = new BodyAnimationBounce(
					duration, vector, distance);
			body.animate(animation, false);
		} else if (action.withSwirl) {
			animation = null;
		} else {
			throw new UnsupportedException();
		}
		
		if (action.thenWaitForTheAnimationToEnd) {
			waitChecker = new WaitChecker() {
				@Override
				public boolean isWaiting(PlatformGameState gameState) {
					return animation == null || !animation.isFinished();
				}
			};
		}
	}

}
