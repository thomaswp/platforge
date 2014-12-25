package com.platforge.data.types;

import com.platforge.data.Behavior;
import com.platforge.data.Formatter;
import com.platforge.data.GameData;
import com.platforge.data.ICopyable;
import com.platforge.data.PlatformGame;
import com.platforge.data.Behavior.BehaviorType;
import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData;
import com.platforge.data.field.PersistData;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.types.EventPointer;

public class EventPointer extends GameData implements ICopyable<EventPointer> {
	private static final long serialVersionUID = 1L;

	private BehaviorType behaviorType;
	private int behaviorIndex = -1;
	private int eventIndex = -1;
	
	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		int ordinal = fields.add(behaviorType == null ? -1 : behaviorType.ordinal()); 
		behaviorType = ordinal < 0 ? null : BehaviorType.values()[ordinal];
		behaviorIndex = fields.add(behaviorIndex);
		eventIndex = fields.add(eventIndex);
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new EventPointer();
			}
		};
	}
	
	public void setEvent(PlatformGame game, Behavior behavior, int index) {
		if (behavior != null) {
			behaviorType = behavior.type;
			behaviorIndex = game.getBehaviors(behaviorType).indexOf(behavior);
		} else {
			behaviorType = null;
			behaviorIndex = -1;
		}
		eventIndex = index;
	}
	
	public int getEventIndex() {
		return eventIndex;
	}
	
	public Behavior getBehavior(PlatformGame game) {
		if (behaviorType != null) {
			return game.getBehaviors(behaviorType).get(behaviorIndex);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return Formatter.format("Event[%d,%s:%d]", eventIndex, behaviorType, behaviorIndex);
	}

	
	@Override
	public EventPointer copy() {
		return PersistData.copy(this, EventPointer.class);
	}
}
