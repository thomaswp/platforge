package com.platforge.player.core.platform;

import com.platforge.data.Behavior;
import com.platforge.data.BehaviorInstance;
import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;

/**
 * Owned by a actor/object/map to keep track of one behavior's 
 * variables, switches, etc. during execution
 */
public class BehaviorRuntime {
	public int[] variables;
	public boolean[] switches;
	public Parameters[] parameters;
	public BehaviorInstance instance;
	
	public Parameters getParameter(int index) {
		return parameters[index];
	}
	
	public BehaviorRuntime(BehaviorInstance instance, PlatformGame game) {
		this.instance = instance;
		Behavior behavior = instance.getBehavior(game);
		switches = new boolean[behavior.switches.size()];
		for (int i = 0; i < switches.length; i++) {
			switches[i] = behavior.switches.get(i);
		}
		variables = new int[behavior.variables.size()];
		for (int i = 0; i < variables.length; i++) {
			variables[i] = behavior.variables.get(i);
		}
		parameters = new Parameters[behavior.parameters.size()];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = instance.parameters.get(i);
		}
	}
}
