package com.twp.core.action;

import com.twp.core.action.ActionChangeGravity;

import edu.elon.honors.price.physics.Vector;


public class InterpreterChangeGravity extends ActionInterpreter<ActionChangeGravity> {

	@Override
	protected void interperate(ActionChangeGravity action,
			PlatformGameState gameState) throws ParameterException {
		Vector vec = action.readVector(gameState);
		vec.multiply(action.readMagnitude(gameState));
		gameState.getPhysics().setGravity(vec);
	}

}