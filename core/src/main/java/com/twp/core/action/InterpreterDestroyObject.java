package com.twp.core.action;

import com.twp.core.action.ActionDestroyObject;

public class InterpreterDestroyObject extends ActionInterpreter<ActionDestroyObject> {

	@Override
	protected void interperate(ActionDestroyObject action,
			PlatformGameState gameState) throws ParameterException {
		action.readObjectInstance(gameState).destroy();
	}

}
