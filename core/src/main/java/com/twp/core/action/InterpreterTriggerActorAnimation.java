package com.twp.core.action;

import com.twp.core.action.ActionTriggerActorAnimation;

public class InterpreterTriggerActorAnimation extends ActionInterpreter<ActionTriggerActorAnimation> {

	@Override
	protected void interperate(ActionTriggerActorAnimation action,
			PlatformGameState gameState) throws ParameterException {
		action.readActorInstance(gameState).triggerAction();
	}

}
