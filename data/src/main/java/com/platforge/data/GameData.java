package com.platforge.data;

import java.io.Serializable;

import com.platforge.data.Behavior.Parameter;
import com.platforge.data.Event.Action;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Trigger;
import com.platforge.data.UILayout.Button;
import com.platforge.data.UILayout.JoyStick;
import com.platforge.data.field.EqualsData;
import com.platforge.data.field.HashData;
import com.platforge.data.field.StrictDataObject;
import com.platforge.data.types.ActorClassPointer;
import com.platforge.data.types.EventPointer;
import com.platforge.data.types.ObjectClassPointer;
import com.platforge.data.types.Switch;
import com.platforge.data.types.Variable;

public abstract class GameData extends StrictDataObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	static {
		Constructor.register(ActorClass.class, ActorClass.constructor());
		Constructor.register(ActorInstance.class, ActorInstance.constructor());
		Constructor.register(Behavior.class, Behavior.constructor());
		Constructor.register(Parameter.class, Parameter.constructor());
		Constructor.register(BehaviorInstance.class, BehaviorInstance.constructor());
		Constructor.register(Event.class, Event.constructor());
		Constructor.register(Parameters.class, Parameters.constructor());
		Constructor.register(Action.class, Action.constructor());
		Constructor.register(Trigger.class, Trigger.constructor());
		Constructor.register(Hero.class, Hero.constructor());
		Constructor.register(Map.class, Map.constructor());
		Constructor.register(MapLayer.class, MapLayer.constructor());
		Constructor.register(ObjectClass.class, ObjectClass.constructor());
		Constructor.register(ObjectInstance.class, ObjectInstance.constructor());
		Constructor.register(PlatformGame.class, PlatformGame.constructor());
		Constructor.register(Tileset.class, Tileset.constructor());
		Constructor.register(UILayout.class, UILayout.constructor());
		Constructor.register(Button.class, Button.constructor());
		Constructor.register(JoyStick.class, JoyStick.constructor());
		Constructor.register(ActorClassPointer.class, ActorClassPointer.constructor());
		Constructor.register(EventPointer.class, EventPointer.constructor());
		Constructor.register(ObjectClassPointer.class, ObjectClassPointer.constructor());
		Constructor.register(Switch.class, Switch.constructor());
		Constructor.register(Variable.class, Variable.constructor());
		Constructor.register(Vector.class, Vector.constructor());
	}

	public transient EqualsData equalsData = new EqualsData(this);
	public transient HashData hashData = new HashData(this);
	
	@Override
	public boolean equals(Object data) {
		if (this == data) return true;
		if (data == null) return false;
		if (getClass() != data.getClass()) return false;
		if (equalsData == null) equalsData = new EqualsData(this);
		return equalsData.equals(((GameData) data).equalsData);
	}
	
	@Override
	public int hashCode() {
		if (hashData == null) hashData = new HashData(this);
		return hashData.hashCode();
	}
	
	public static boolean areEqual(GameData o1, GameData o2) {	
		if (o1 == null || o2 == null)
			return (o1 == o2);

		if (o1.getClass() != o2.getClass())
			return false;
		
		return o1.equals(o2);
	}
}
