package com.platforge.data.types;

import com.platforge.data.Behavior;
import com.platforge.data.PlatformGame;
import com.platforge.data.field.DataObject;
import com.platforge.game.Formatter;

import com.platforge.data.types.DataScope;
import com.platforge.data.types.ScopedData;
import com.platforge.data.types.Switch;

public class Switch extends ScopedData<Switch> {
	private static final long serialVersionUID = 1L;

	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new Switch();
			}
		};
	}
	
	public Switch(int id, DataScope scope) {
		super(id, scope);
	}
	
	public Switch() {
		this(0, DataScope.Global);
	}
	
	public String getName(PlatformGame game, Behavior behavior) {
		if (scope == DataScope.Global) {
			if (game != null) {
				return Formatter.format("%03d: %s", id, 
						game.switchNames[id]);
			}
		} else if (scope == DataScope.Local) {
			if (behavior != null) {
				return "[" + behavior.switchNames.get(id) + "]";				
			}
		} else if (scope == DataScope.Param){
			if (behavior != null) {
				return "{" + behavior.parameters.get(id).name + "}";
			}
		}
		return "<None>";
	}
	
	@Override 
	public String toString() {
		return Formatter.format("[S[%d,%d]", id, scope.toInt());
	}

	@Override
	public Switch copy() {
		return new Switch(id, scope);
	}
}
