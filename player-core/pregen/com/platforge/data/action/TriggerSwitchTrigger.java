package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.types.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;
import com.platforge.player.core.action.*;

@SuppressWarnings("unused")
public class TriggerSwitchTrigger extends ScriptableInstance {
	public static final String NAME = "Switch Trigger";
	public static final int ID = 0;
	public static final String CATEGORY = null;
	
	/** Type: <b>&lt;switch&gt;</b> */
	public Switch aSwitch;
	public boolean readASwitch(GameState gameState) throws ParameterException {
		return gameState.readSwitch(aSwitch);
	}
	/** Type: <b>&lt;boolean&gt;</b> */
	public Parameters value;
	public boolean readValue(GameState gameState) throws ParameterException {
		return gameState.readBoolean(value);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		aSwitch = iterator.getSwitch();
		value = iterator.getParameters();
	}
	/**
	 * 000 <b><i>Switch Trigger</i></b> (null)<br />
	 * <ul>
	 * <li><b>&lt;switch&gt;</b> aSwitch</li>
	 * <li><b>&lt;boolean&gt;</b> value</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
