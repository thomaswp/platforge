package com.platforge.data.action;

import com.platforge.data.ObjectClass;
import com.platforge.data.types.ObjectClassPointer;

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
public class ActionCreateObject extends ScriptableInstance {
	public static final String NAME = "Create Object";
	public static final int ID = 8;
	public static final String CATEGORY = "Object";
	
	/** Type: <b>&lt;objectClass&gt;</b> */
	public ObjectClassPointer objectClass;
	public ObjectClass readObjectClass(GameState gameState) throws ParameterException {
		return gameState.readObjectClass(objectClass);
	}
	/** Type: <b>&lt;point&gt;</b> */
	public Parameters point;
	public Point readPoint(GameState gameState) throws ParameterException {
		return gameState.readPoint(point);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		objectClass = iterator.getObjectClassPointer();
		point = iterator.getParameters();
	}
	/**
	 * 008 <b><i>Create Object</i></b> (Object)<br />
	 * <ul>
	 * <li><b>&lt;objectClass&gt;</b> objectClass</li>
	 * <li><b>&lt;point&gt;</b> point</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
