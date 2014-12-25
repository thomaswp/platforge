package com.platforge.data.types;

import com.platforge.data.Behavior;
import com.platforge.data.Formatter;
import com.platforge.data.PlatformGame;
import com.platforge.data.field.DataObject;
import com.platforge.data.types.ActorClassPointer;
import com.platforge.data.types.DataScope;
import com.platforge.data.types.ScopedData;

public class ActorClassPointer extends ScopedData<ActorClassPointer> {
	private static final long serialVersionUID = 1L;

	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new ActorClassPointer();
			}
		};
	}
	
	public ActorClassPointer(int id, DataScope scope) {
		super(id, scope);
	}
	
	public ActorClassPointer() {
		super(0, DataScope.Global);
	}
	
	public String getName(PlatformGame game, Behavior behavior) {
		if (scope == DataScope.Global) {
			if (game != null) {
				return game.actors[id].name;
			}
		} else if (scope == DataScope.Param){
			if (behavior != null) {
				return "{" + behavior.parameters.get(id).name + "}";
			}
		}
		return "<None>";
	}

	@Override
	public ActorClassPointer copy() {
		return new ActorClassPointer(id, scope);
	}
	
	@Override
	public String toString() {
		return Formatter.format("AC[%d,%d]", id, scope.toInt());
	}
}
