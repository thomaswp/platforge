package com.twp.core.action;

import com.twp.core.action.ActionSetVelocity;
import com.twp.core.platform.PlatformBody;

import edu.elon.honors.price.physics.Vector;

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
