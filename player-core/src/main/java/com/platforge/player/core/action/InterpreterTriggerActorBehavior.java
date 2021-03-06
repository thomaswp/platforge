package com.platforge.player.core.action;

import com.platforge.data.action.ActionTriggerActorBehavior;
import com.platforge.player.core.game.Debug;
import com.platforge.player.core.platform.ActorBody;
import com.platforge.player.core.platform.IBehaving;

public class InterpreterTriggerActorBehavior 
extends ActionInterpreter<ActionTriggerActorBehavior> {

	@Override
	protected void interperate(ActionTriggerActorBehavior action,
			PlatformGameState gameState) throws ParameterException {
		//TODO: Make a better way for "cause" to be passed
		IBehaving behaving = null;
		ActorBody cause = null;
		if (behaving instanceof ActorBody) {
			cause = (ActorBody)behaving;
			//Debug.write(cause.getActor().name);
		}
		
		action.readActorInstance(gameState).doBehavior(
				action.actorBehavior, cause);
	}
}
