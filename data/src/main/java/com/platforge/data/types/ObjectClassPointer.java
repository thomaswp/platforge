package com.platforge.data.types;

import com.platforge.data.Behavior;
import com.platforge.data.Formatter;
import com.platforge.data.PlatformGame;
import com.platforge.data.field.DataObject;
import com.platforge.data.types.DataScope;
import com.platforge.data.types.ObjectClassPointer;
import com.platforge.data.types.ScopedData;

public class ObjectClassPointer extends ScopedData<ObjectClassPointer> {
	private static final long serialVersionUID = 1L;

	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new ObjectClassPointer();
			}
		};
	}
	
	public ObjectClassPointer(int id, DataScope scope) {
		super(id, scope);
		
	}
	
	public ObjectClassPointer() {
		super(0, DataScope.Global);
	}
	
	public String getName(PlatformGame game, Behavior behavior) {
		if (scope == DataScope.Global) {
			if (game != null) {
				return game.objects[id].name;
			}
		} else if (scope == DataScope.Param){
			if (behavior != null) {
				return "{" + behavior.parameters.get(id).name + "}";
			}
		}
		return "<None>";
	}

	@Override
	public ObjectClassPointer copy() {
		return new ObjectClassPointer(id, scope);
	}
	
	@Override
	public String toString() {
		return Formatter.format("OC[%d,%d]", id, scope.toInt());
	}
}
