package com.twp.core.action;

import com.twp.core.action.ActionDrawToScreen;
import com.twp.core.action.ActionDrawToScreen.ActionDrawAData;
import com.twp.core.platform.PlatformLogic;

public class InterpreterDrawToScreen extends ActionInterpreter<ActionDrawToScreen> {
	
	@Override
	protected void interperate(ActionDrawToScreen action,
			PlatformGameState gameState) throws ParameterException {
		PlatformLogic logic = gameState.getLogic();
		if (action.actionClearTheScreen) {
			logic.clearDrawScreen();
		} else if (action.actionDrawA) {
			ActionDrawAData data = action.actionDrawAData;
			boolean world = data.useWorldCoordinates;
			
			if (data.shapeBox) {
				Point point = data.shapeBoxData.readCorner1(gameState);
				int x1 = point.x, y1 = point.y;
				point = data.shapeBoxData.readCorner2(gameState);
				int x2 = point.x, y2 = point.y;
				logic.drawBox(x1, y1, x2, y2, world, data.color, data.styleFilledIn);
			} else if (data.shapeLine) {
				Point point = data.shapeLineData.readFrom(gameState);
				int x1 = point.x, y1 = point.y;
				point = data.shapeLineData.readTo(gameState);
				int x2 = point.x, y2 = point.y;
				logic.drawLine(x1, y1, x2, y2, world, data.color);
			} else if (data.shapeLine) {
				Point center = data.shapeCircleData.readCenter(gameState);
				int radius = data.shapeCircleData.readRadius(gameState);
				logic.drawCircle(center.x, center.y, radius, world, data.color, data.styleFilledIn);
			} else {
				throw new UnsupportedException();
			}
			
		} else {
			throw new UnsupportedException();
		}
	}

}
