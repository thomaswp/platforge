package com.platforge.data.action;

import com.platforge.data.Event;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.player.core.action.ParameterException;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionTriggerEvent extends ScriptableInstance {
	public static final String NAME = "Trigger Event";
	public static final int ID = 23;
	public static final String CATEGORY = "Control";
	
	/** Type: <b>&lt;event&gt;</b> */
	public Object event;
	public Event readEvent(GameState gameState) throws ParameterException {
		return gameState.readEvent(event);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		event = iterator.getObject();
	}
	/**
	 * 023 <b><i>Trigger Event</i></b> (Control)<br />
	 * <ul>
	 * <li><b>&lt;event&gt;</b> event</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
