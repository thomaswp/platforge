package com.platforge.player.core.action;

import com.platforge.data.action.ActionWait;

public class InterpreterWait extends ActionInterpreter<ActionWait>{

	@Override
	protected void interperate(ActionWait action, PlatformGameState gameState)
			throws ParameterException {
		final long waitUntil = gameState.getGameTime() + 
				action.readNumber(gameState) * 100;
		waitChecker = new WaitChecker() {
			@Override
			public boolean isWaiting(PlatformGameState gameState) {
				return gameState.getGameTime() < waitUntil;
			}
			
		};
	}
}
