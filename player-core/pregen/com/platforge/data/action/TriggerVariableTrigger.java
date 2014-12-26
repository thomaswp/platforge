package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.types.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;
import com.platforge.player.core.action.*;

@SuppressWarnings("unused")
public class TriggerVariableTrigger extends ScriptableInstance {
	public static final String NAME = "Variable Trigger";
	public static final int ID = 1;
	public static final String CATEGORY = null;
	
	/** Type: <b>&lt;variable&gt;</b> */
	public Variable variable;
	public int readVariable(GameState gameState) throws ParameterException {
		return gameState.readVariable(variable);
	}
	public boolean operatorEquals;
	public boolean operatorNotEquals;
	public boolean operatorGreater;
	public boolean operatorGreaterOrEqual;
	public boolean operatorLess;
	public boolean operatorLessOrEqual;
	public boolean operatorDivisible;
	/** Type: <b>&lt;number&gt;</b> */
	public Parameters value;
	public int readValue(GameState gameState) throws ParameterException {
		return gameState.readNumber(value);
	}
	
	@Override
	public void readParams(Iterator iterator) {
		variable = iterator.getVariable();
		int operator = iterator.getInt();
		operatorEquals = operator == 0;
		operatorNotEquals = operator == 1;
		operatorGreater = operator == 2;
		operatorGreaterOrEqual = operator == 3;
		operatorLess = operator == 4;
		operatorLessOrEqual = operator == 5;
		operatorDivisible = operator == 6;
		
		value = iterator.getParameters();
	}
	/**
	 * 001 <b><i>Variable Trigger</i></b> (null)<br />
	 * <ul>
	 * <li><b>&lt;variable&gt;</b> variable</li>
	 * <li><b>&lt;radio&gt;</b> operator</i>:</li><ul>
	 * <li>operatorEquals:</li>
	 * <li>operatorNotEquals:</li>
	 * <li>operatorGreater:</li>
	 * <li>operatorGreaterOrEqual:</li>
	 * <li>operatorLess:</li>
	 * <li>operatorLessOrEqual:</li>
	 * <li>operatorDivisible:</li>
	 * </ul>
	 * <li><b>&lt;number&gt;</b> value</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
