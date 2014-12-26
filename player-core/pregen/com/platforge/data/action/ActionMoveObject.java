package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.types.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;
import com.platforge.player.core.action.*;

@SuppressWarnings("unused")
public class ActionMoveObject extends ScriptableInstance {
	public static final String NAME = "Move Object";
	public static final int ID = 9;
	public static final String CATEGORY = "Object";
	
	/** Type: <b>&lt;objectInstance&gt;</b> */
	public Parameters objectInstance;
	public ObjectBody readObjectInstance(GameState gameState) throws ParameterException {
		return gameState.readObjectInstance(objectInstance);
	}
	/** Type: <b>&lt;point&gt;</b> */
	public Parameters point;
	public Point readPoint(GameState gameState) throws ParameterException {
		return gameState.readPoint(point);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		objectInstance = iterator.getParameters();
		point = iterator.getParameters();
	}
	/**
	 * 009 <b><i>Move Object</i></b> (Object)<br />
	 * <ul>
	 * <li><b>&lt;objectInstance&gt;</b> objectInstance</li>
	 * <li><b>&lt;point&gt;</b> point</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
