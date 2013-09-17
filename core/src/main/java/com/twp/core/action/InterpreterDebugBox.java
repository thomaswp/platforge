package com.twp.core.action;

import com.twp.core.action.ActionDebugBox;

import edu.elon.honors.price.game.Formatter;


public class InterpreterDebugBox extends ActionInterpreter<ActionDebugBox> {

	@Override
	public void interperate(ActionDebugBox action, PlatformGameState gameState) throws ParameterException {
		String message = "";
		if (action.showTheMessage) {
			message = action.showTheMessageData.string;
		} else if (action.showTheSwitch) {
			String name = gameState.getSwitchName(action.showTheSwitchData.aSwitch);
			message = Formatter.format("%s = %s", name,
					action.showTheSwitchData.readASwitch(gameState) ? "On" : "Off");
		} else if (action.showTheVariable) {
			String name = gameState.getVariableName(action.showTheVariableData.variable);
			message = Formatter.format("%s = %d", name,
					action.showTheVariableData.readVariable(gameState));
		}
		//Game.getCurrentGame().showToast(message);
		//TODO: Implement
	}
}
