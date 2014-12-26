package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.player.core.action.ParameterException;
import com.platforge.player.core.action.Point;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionMoveActor extends ScriptableInstance {
	public static final String NAME = "Move Actor";
	public static final int ID = 4;
	public static final String CATEGORY = "Actor";
	
	/** Type: <b>&lt;actorInstance&gt;</b> */
	public Parameters actorInstance;
	public ActorBody readActorInstance(GameState gameState) throws ParameterException {
		return gameState.readActorInstance(actorInstance);
	}
	/** Type: <b>&lt;point&gt;</b> */
	public Parameters point;
	public Point readPoint(GameState gameState) throws ParameterException {
		return gameState.readPoint(point);
	}
	public boolean facingLeft;
	public boolean facingRight;
	public boolean facingTheSameDirection;
	
	@Override
	public void readParams(Iterator iterator) {
		actorInstance = iterator.getParameters();
		point = iterator.getParameters();
		int facing = iterator.getInt();
		facingLeft = facing == 0;
		facingRight = facing == 1;
		facingTheSameDirection = facing == 2;
		
	}
	/**
	 * 004 <b><i>Move Actor</i></b> (Actor)<br />
	 * <ul>
	 * <li><b>&lt;actorInstance&gt;</b> actorInstance</li>
	 * <li><b>&lt;point&gt;</b> point</li>
	 * <li><b>&lt;radio&gt;</b> facing</i>:</li><ul>
	 * <li>facingLeft:</li>
	 * <li>facingRight:</li>
	 * <li>facingTheSameDirection:</li>
	 * </ul>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
