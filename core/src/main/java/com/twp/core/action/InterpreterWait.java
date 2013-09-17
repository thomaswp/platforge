package com.twp.core.action;

import com.twp.core.action.ActionWait;

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
