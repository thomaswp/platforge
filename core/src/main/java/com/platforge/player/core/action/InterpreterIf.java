package com.platforge.player.core.action;

import com.platforge.data.ActorClass;
import com.platforge.physics.Vector;
import com.platforge.data.Event.Action;
import com.platforge.data.action.ActionIf;
import com.platforge.data.action.ActionIf.CheckIfTheActorData;
import com.platforge.data.action.ActionIf.CheckIfTheActorObjectData;
import com.platforge.data.action.ActionIf.CheckIfTheSwitchData;
import com.platforge.data.action.ActionIf.CheckIfTheVariableData;
import com.platforge.data.action.ActionIf.CheckIfTheActorData.CheckTypeData;
import com.platforge.data.action.ActionIf.CheckIfTheActorObjectData.CheckPositionData;
import com.platforge.player.core.game.Rect;
import com.platforge.player.core.game.RectF;
import com.platforge.player.core.platform.ActorBody;
import com.platforge.player.core.platform.PlatformBody;

public class InterpreterIf extends ActionInterpreter<ActionIf> {

	boolean result;
	RectF regionF = new RectF();
	
	@Override
	protected void interperate(ActionIf action, PlatformGameState gameState)
			throws ParameterException {
		
		result = false;
		if (action.checkIfTheSwitch) {
			result = checkIfTheSwitch(action.checkIfTheSwitchData, gameState);
		} else if (action.checkIfTheVariable) {
			result = checkIfTheVariable(action.checkIfTheVariableData, gameState);
		} else if (action.checkIfTheActorObject) {
			result = checkIfTheActor(action.checkIfTheActorObjectData, gameState);
		} else if (action.checkIfTheActor) {
			result = checkIfTheActor(action.checkIfTheActorData, gameState);
		} else {
			throw new UnsupportedException();
		}
	}
	
	private boolean checkIfTheSwitch(CheckIfTheSwitchData data,
			PlatformGameState gameState) throws ParameterException {
		boolean op1 = data.readASwitch(gameState);
		boolean op2 = false;
		if (data.withOn) {
			op2 = true;
		} else if (data.withOff) {
			op2 = false;
		} else if (data.withTheSwitch) {
			op2 = data.withTheSwitchData.readASwitch(gameState);
		} else {
			throw new UnsupportedException();
		}
		if (data.operatorDoesNotEqual) op2 = !op2;
		return op1 == op2;
	}
	
	private boolean checkIfTheVariable(CheckIfTheVariableData data,
			PlatformGameState gameState) throws ParameterException {
		int op1 = data.readVariable(gameState);
		int op2 = data.readNumber(gameState);
		if (data.operatorEquals) {
			return op1 == op2;
		} else if (data.operatorNotEquals) {
			return op1 != op2;
		} else if (data.operatorGreater) {
			return op1 > op2;
		} else if (data.operatorGreaterOrEqual) {
			return op1 >= op2;
		} else if (data.operatorLess) {
			return op1 < op2;
		} else if (data.operatorGreater) {
			return op1 <= op2;
		} else {
			throw new UnsupportedException();
		}
	}
	
	private boolean checkIfTheActor(CheckIfTheActorObjectData data,
			PlatformGameState gameState) throws ParameterException {
		
		PlatformBody body = null;
		try {
			if (data.bodyTheActor) {
				body = data.bodyTheActorData.readActorInstance(gameState);
			} else if (data.bodyTheObject) {
				body = data.bodyTheObjectData.readObjectInstance(gameState);
			}
		} catch (ParameterException e) { }
		
		if (data.checkRegion) {
			if (body == null) return false;
			
			//data.checkRegionData.region
			Rect region = data.checkRegionData.readRegion(gameState);
			regionF.set(region); 
			Vector offset = gameState.getLogic().getOffset();
			regionF.offset(offset.getX(), offset.getY());
			RectF bodyRect = body.getSprite().getRect();
			
			if (data.checkRegionData.checkIsInside) {
				return regionF.contains(bodyRect);
			} else if (data.checkRegionData.checkIsOutside) {
				return !regionF.intersects(bodyRect);
			} else if (data.checkRegionData.checkIsTouching) {
				return regionF.intersects(bodyRect);
			} else {
				throw new UnsupportedException();
			}
			
		} else if (data.checkProperty) {
			if (body == null) {
				return data.checkPropertyData.propertyIsDead;
			}
			return data.checkPropertyData.propertyIsAlive;
		} else if (data.checkPosition) {
			CheckPositionData cpData = data.checkPositionData;
			if (body == null) return false;
			PlatformBody body2 = null;
			if (cpData.ofTheActor) {
				body2 = cpData.ofTheActorData.readActorInstance(gameState);
			} else if (cpData.ofTheObject) {
				body2 = cpData.ofTheObjectData.readObjectInstance(gameState);
			}
			if (body2 == null) return false;

			final float THRESH = 20; //TODO: definitely fix this...
			
			//TODO: Figure out better system w/ bodies b/c transparency might screw it up
			RectF rectA = body.getSprite().getRect();
			RectF rectB = body2.getSprite().getRect();
			
			if (cpData.directionIsAbove) {
				return rectA.bottom() - rectB.top() <= THRESH;
			} else if (cpData.directionIsBelow) {
				return rectA.top() - rectB.bottom() >= -THRESH;
			} else if (cpData.directionIsLeftOf) {
				return rectA.right() - rectB.left() <= THRESH;
			} else if (cpData.directionIsRightOf) {
				return rectA.left() - rectB.right() >= -THRESH;
			} else {
				throw new UnsupportedException();
			}
		} else {
			throw new UnsupportedException();
		}
	}
	
	private boolean checkIfTheActor(CheckIfTheActorData data, 
			PlatformGameState gameState) throws ParameterException {
		ActorBody actor = data.readActorInstance(gameState);
		if (data.checkType) {
			CheckTypeData ctData = data.checkTypeData;
			ActorClass actorClass = ctData.readActorClass(gameState);
			boolean match = actor.getActor() == actorClass;
			if (ctData.compareIs) {
				return match;
			} else if (ctData.compareIsNot) {
				return !match;
			} else {
				throw new UnsupportedException();
			}
		} else {
			throw new UnsupportedException();
		}
	}

	@Override
	protected void updateControl(ActionControl control, Action action) {
		super.updateControl(control, action);
		control.setExecutionData(action, result);
		int indent = action.indent;
		if (!result) {
			while (control.hasNextActionInEvent() && 
					control.getNextAction().indent > indent) {
				control.actionIndex++;
			}
		}
	}
}
