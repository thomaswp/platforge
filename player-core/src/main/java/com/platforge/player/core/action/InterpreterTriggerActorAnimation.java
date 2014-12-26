package com.platforge.player.core.action;

import com.platforge.data.action.ActionTriggerActorAnimation;

public class InterpreterTriggerActorAnimation extends ActionInterpreter<ActionTriggerActorAnimation> {

	@Override
	protected void interperate(ActionTriggerActorAnimation action,
			PlatformGameState gameState) throws ParameterException {
		action.readActorInstance(gameState).triggerAction();
	}

}
