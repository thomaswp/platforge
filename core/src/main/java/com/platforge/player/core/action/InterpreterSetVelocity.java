package com.platforge.player.core.action;

import com.platforge.data.action.ActionSetVelocity;
import com.platforge.physics.Vector;
import com.platforge.player.core.platform.PlatformBody;

public class InterpreterSetVelocity extends ActionInterpreter<ActionSetVelocity> {

	@Override
	protected void interperate(ActionSetVelocity action,
			PlatformGameState gameState) throws ParameterException {
		PlatformBody body = null;
		if (action.setActor) {
			body = action.setActorData.readActorInstance(gameState);
		} else if (action.setObject) {
			body = action.setObjectData.readObjectInstance(gameState);
		}
		
		Vector vector = action.readDirectionVector(gameState);
		vector.multiply(action.readDirectionMagnitude(gameState));
		body.setVelocity(vector.getX(), vector.getY());
	}

}
