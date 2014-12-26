package com.platforge.player.core.action;

import com.platforge.data.action.ActionMoveObject;
import com.platforge.player.core.platform.ObjectBody;

public class InterpreterMoveObject extends ActionInterpreter<ActionMoveObject> {

	@Override
	protected void interperate(ActionMoveObject action,
			PlatformGameState gameState) throws ParameterException {
		Point point = action.readPoint(gameState);
		ObjectBody body = action.readObjectInstance(gameState);
		body.setScaledPosition(point.x, point.y);
	}
	
}
