package com.platforge.player.core.action;

import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;

public abstract class ScriptableFragment {

	protected Parameters params;
	
	protected abstract void readParams(Parameters.Iterator iterator);
	
	public Parameters getParameters() {
		return params;
	}
	
	public void setParameters(Parameters params) {
		this.params = params;
		Iterator iterator = params.iterator(true);
		readParams(iterator);
		iterator.dispose();
	}
	
	public ScriptableFragment() {
	}
}
