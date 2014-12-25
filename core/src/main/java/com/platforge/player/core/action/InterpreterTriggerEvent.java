package com.platforge.player.core.action;

import com.platforge.data.Event;
import com.platforge.data.Event.Action;
import com.platforge.data.action.ActionTriggerEvent;

public class InterpreterTriggerEvent extends ActionInterpreter<ActionTriggerEvent> {

	private Event nextEvent;
	
	@Override
	protected void interperate(ActionTriggerEvent action,
			PlatformGameState gameState) throws ParameterException {
		nextEvent = action.readEvent(gameState);
	}

	@Override
	protected void updateControl(ActionControl control, Action action) {
		super.updateControl(control, action);
		try {
			control.pushEvent(nextEvent);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}
}
