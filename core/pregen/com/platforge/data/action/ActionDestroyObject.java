package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.physics.*;
import com.platforge.player.core.action.ParameterException;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionDestroyObject extends ScriptableInstance {
	public static final String NAME = "Destroy Object";
	public static final int ID = 15;
	public static final String CATEGORY = "Object";
	
	/** Type: <b>&lt;objectInstance&gt;</b> */
	public Parameters objectInstance;
	public ObjectBody readObjectInstance(GameState gameState) throws ParameterException {
		return gameState.readObjectInstance(objectInstance);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		objectInstance = iterator.getParameters();
	}
	/**
	 * 015 <b><i>Destroy Object</i></b> (Object)<br />
	 * <ul>
	 * <li><b>&lt;objectInstance&gt;</b> objectInstance</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
