package com.platforge.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.platforge.data.Behavior.BehaviorType;
import com.platforge.data.Event.Parameters;
import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.field.StrictFieldData;

public class BehaviorInstance extends GameData {
	private static final long serialVersionUID = 1L;
	
	public int behaviorId;
	public BehaviorType type;
	public final List<Parameters> parameters = new LinkedList<Parameters>();

	@Override
	public void addFields(StrictFieldData fields) throws ParseDataException,
			NumberFormatException {
		behaviorId = fields.add(behaviorId, "behaviorId");
		int ordinal = fields.add(type == null ? -1 : type.ordinal(), "type"); 
		type = ordinal < 0 ? null : BehaviorType.values()[ordinal];
		fields.addList(parameters, "parameters");
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new BehaviorInstance(0, null);
			}
		};
	}
	
	public BehaviorInstance(int behaviorId, BehaviorType type) {
		this.behaviorId = behaviorId;
		this.type = type;
	}
	
	public Behavior getBehavior(PlatformGame game) {
		return game.getBehaviors(type).get(behaviorId);
	}
	
	@Override
	public String toString() {
		return Formatter.format("%d (%s): %s",
				behaviorId, type.toString(),
				Arrays.toString(parameters.toArray()));
	}
}
