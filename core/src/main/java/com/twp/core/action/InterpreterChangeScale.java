package com.twp.core.action;

import com.twp.core.action.ActionChangeScale;
import com.twp.core.platform.PlatformBody;

public class InterpreterChangeScale extends ActionInterpreter<ActionChangeScale> {

	@Override
	protected void interperate(ActionChangeScale action,
			PlatformGameState gameState) throws ParameterException {
		PlatformBody body;
		if (action.ofTheActor) {
			body = action.ofTheActorData.readActorInstance(gameState);
		} else if (action.ofTheObject) {
			body = action.ofTheObjectData.readObjectInstance(gameState);
		} else {
			throw new UnsupportedException();
		}
		//TODO: implement
	}

}
