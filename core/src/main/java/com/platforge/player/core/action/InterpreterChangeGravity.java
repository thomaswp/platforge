package com.platforge.player.core.action;

import com.platforge.data.action.ActionChangeGravity;
import com.platforge.physics.Vector;


public class InterpreterChangeGravity extends ActionInterpreter<ActionChangeGravity> {

	@Override
	protected void interperate(ActionChangeGravity action,
			PlatformGameState gameState) throws ParameterException {
		Vector vec = action.readVector(gameState);
		vec.multiply(action.readMagnitude(gameState));
		gameState.getPhysics().setGravity(vec);
	}

}
