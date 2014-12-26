package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.player.core.action.ParameterException;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionWait extends ScriptableInstance {
	public static final String NAME = "Wait";
	public static final int ID = 20;
	public static final String CATEGORY = "Control";
	
	/** Type: <b>&lt;number&gt;</b> */
	public Parameters number;
	public int readNumber(GameState gameState) throws ParameterException {
		return gameState.readNumber(number);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		number = iterator.getParameters();
	}
	/**
	 * 020 <b><i>Wait</i></b> (Control)<br />
	 * <ul>
	 * <li><b>&lt;number&gt;</b> number</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
