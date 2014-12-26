package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionStop extends ScriptableInstance {
	public static final String NAME = "Stop";
	public static final int ID = 21;
	public static final String CATEGORY = "Control";
	
	
	@Override
	public void readParams(Iterator iterator) {
	}
	/**
	 * 021 <b><i>Stop</i></b> (Control)<br />
	 * <ul>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
