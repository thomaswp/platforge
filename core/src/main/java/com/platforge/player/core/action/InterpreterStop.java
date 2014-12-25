package com.platforge.player.core.action;

import com.platforge.data.Event.Action;
import com.platforge.data.action.ActionStop;

public class InterpreterStop extends ActionInterpreter<ActionStop> {

	@Override
	protected void interperate(ActionStop action, PlatformGameState gameState)
			throws ParameterException {
		
	}

	@Override
	protected void updateControl(ActionControl control, Action action) {
		control.actionIndex = control.getActions().size();
	}

}
