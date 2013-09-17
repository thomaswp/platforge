package edu.elon.honors.price.data.types;

import edu.elon.honors.price.data.Behavior;
import edu.elon.honors.price.data.Behavior.BehaviorType;
import edu.elon.honors.price.data.GameData;
import edu.elon.honors.price.data.ICopyable;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.field.DataObject;
import edu.elon.honors.price.data.field.FieldData;
import edu.elon.honors.price.data.field.FieldData.ParseDataException;
import edu.elon.honors.price.data.field.PersistData;
import edu.elon.honors.price.game.Formatter;

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
