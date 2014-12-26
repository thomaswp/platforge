package com.platforge.player.core.action;

import com.platforge.data.ObjectClass;
import com.platforge.data.action.ActionCreateObject;
import com.platforge.player.core.platform.PlatformLogic.ObjectAddable;

public class InterpreterCreateObject extends ActionInterpreter<ActionCreateObject>{

	@Override
	protected void interperate(ActionCreateObject action,
			PlatformGameState gameState) throws ParameterException {
		//Read the point where the Object should be created
		Point point = action.readPoint(gameState);
		//Read the type of Object to create
		ObjectClass objectClass = action.readObjectClass(gameState);
		//Tell the physics handler to add a new ObjectBody at
		//the next good opportunity.
		gameState.getPhysics().addObjectBody(
				new ObjectAddable(objectClass, point.x, point.y));
	}
}
