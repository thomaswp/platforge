package com.platforge.player.core.action;

import com.platforge.data.Vector;
import com.platforge.data.action.ActionChangeGravity;


public class InterpreterChangeGravity extends ActionInterpreter<ActionChangeGravity> {

	@Override
	protected void interperate(ActionChangeGravity action,
			PlatformGameState gameState) throws ParameterException {
		Vector vec = action.readVector(gameState);
		vec.multiply(action.readMagnitude(gameState));
		gameState.getPhysics().setGravity(vec);
	}

}
