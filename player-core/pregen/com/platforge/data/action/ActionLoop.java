package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.types.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;
import com.platforge.player.core.action.*;

@SuppressWarnings("unused")
public class ActionLoop extends ScriptableInstance {
	public static final String NAME = "Loop...";
	public static final int ID = 17;
	public static final String CATEGORY = "Control";
	
	/** Type: <b>&lt;number&gt;</b> */
	public Parameters numTimes;
	public int readNumTimes(GameState gameState) throws ParameterException {
		return gameState.readNumber(numTimes);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		numTimes = iterator.getParameters();
	}
	/**
	 * 017 <b><i>Loop...</i></b> (Control)<br />
	 * <ul>
	 * <li><b>&lt;number&gt;</b> numTimes</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
