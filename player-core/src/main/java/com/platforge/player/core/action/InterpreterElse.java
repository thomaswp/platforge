package com.platforge.player.core.action;

import com.platforge.data.Event.Action;
import com.platforge.data.action.ActionElse;

public class InterpreterElse extends ActionInterpreter<ActionElse> {

	@Override
	protected void interperate(ActionElse action, PlatformGameState gameState)
			throws ParameterException {
	}

	@Override
	protected void updateControl(ActionControl control, Action action) {
		super.updateControl(control, action);
		
		int indent = action.indent;
		boolean result = (Boolean)control.getExecutionData(action.dependsOn);
		if (result) {
			while (control.hasNextActionInEvent() && 
					control.getNextAction().indent > indent) {
				control.actionIndex++;
			}
		}
	}
}
