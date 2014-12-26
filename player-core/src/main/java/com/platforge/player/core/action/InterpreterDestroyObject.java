package com.platforge.player.core.action;

import com.platforge.data.action.ActionDestroyObject;

public class InterpreterDestroyObject extends ActionInterpreter<ActionDestroyObject> {

	@Override
	protected void interperate(ActionDestroyObject action,
			PlatformGameState gameState) throws ParameterException {
		action.readObjectInstance(gameState).destroy();
	}

}
