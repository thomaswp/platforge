package com.platforge.player.core.action;

import com.platforge.data.Vector;
import com.platforge.data.action.ActionPointOperation;
import com.platforge.data.types.Variable;
import com.platforge.player.core.platform.ActorBody;
import com.platforge.player.core.platform.ObjectBody;

public class InterpreterPointOperation extends 
ActionInterpreter<ActionPointOperation> {

	Point operand = new Point();
	
	@Override
	protected void interperate(ActionPointOperation action,
			PlatformGameState gameState) throws ParameterException {
		Variable vx = action.point.getVariable(0);
		Variable vy = action.point.getVariable(1);
		
		if (action.withPoint) {
			operand.set(action.withPointData.readPoint(gameState));
		} else if (action.withVector) {
			Vector vector = action.withVectorData.readVector(gameState);
			vector.multiply(action.withVectorData.readMagnitude(gameState));
			operand.setF(vector.getX(), vector.getY());
		} else if (action.withActorLocation) {
			ActorBody body = action.withActorLocationData.readActorInstance(gameState);
			Vector pos = body.getScaledPosition();
			operand.setF(pos.getX(), pos.getY());
		} else if (action.withObjectLocation) {
			ObjectBody body = action.withObjectLocationData.readObjectInstance(gameState);
			Vector pos = body.getScaledPosition();
			operand.set(Math.round(pos.getX()), Math.round(pos.getY()));
		}

		int x = gameState.readVariable(vx);
		int y = gameState.readVariable(vy);
		
		
		if (action.operatorSetItTo) {
			x = operand.x;
			y = operand.y;
		} else if (action.operatorAdd) {
			x += operand.x;
			y += operand.y;
		} else if (action.operatorSubtract) {
			x -= operand.x;
			y -= operand.y;
		}

		gameState.setVariable(vx, x);
		gameState.setVariable(vy, y);
	}
	

}
