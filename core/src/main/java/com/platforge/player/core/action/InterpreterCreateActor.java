package com.platforge.player.core.action;

import com.platforge.data.ActorClass;
import com.platforge.data.Hero;
import com.platforge.data.action.ActionCreateActor;
import com.platforge.player.core.platform.PlatformLogic.ActorAddable;


public class InterpreterCreateActor extends ActionInterpreter<ActionCreateActor> {

	@Override
	protected void interperate(ActionCreateActor action,
			PlatformGameState gameState) throws ParameterException {
		Point loc = action.readPoint(gameState);
		int dir = action.facingLeft ? -1 : 1;
		ActorClass actorClass = action.readActorClass(gameState);
		if (actorClass instanceof Hero) {
			throw new ParameterException("Cannot create a hero!");
		} else {
			gameState.getPhysics().addActorBody(new ActorAddable(
					actorClass, loc.x, loc.y, dir));
		}
	}
	

}
