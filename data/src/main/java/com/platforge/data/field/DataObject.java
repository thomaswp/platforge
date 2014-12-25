package com.platforge.data.field;

import java.util.HashMap;

import com.platforge.data.ActorClass;
import com.platforge.data.ActorInstance;
import com.platforge.data.Behavior;
import com.platforge.data.Behavior.Parameter;
import com.platforge.data.BehaviorInstance;
import com.platforge.data.Event;
import com.platforge.data.Event.Action;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Trigger;
import com.platforge.data.Hero;
import com.platforge.data.Map;
import com.platforge.data.MapLayer;
import com.platforge.data.ObjectClass;
import com.platforge.data.ObjectInstance;
import com.platforge.data.PlatformGame;
import com.platforge.data.Tileset;
import com.platforge.data.UILayout;
import com.platforge.data.UILayout.Button;
import com.platforge.data.UILayout.JoyStick;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.types.ActorClassPointer;
import com.platforge.data.types.EventPointer;
import com.platforge.data.types.ObjectClassPointer;
import com.platforge.data.types.Switch;
import com.platforge.data.types.Variable;
import com.platforge.physics.Vector;

public interface DataObject {

	public static abstract class Constructor {
		private static HashMap<Class<?>, Constructor> constructorMap = 
				new HashMap<Class<?>, Constructor>();
		private static HashMap<String, Class<?>> nameMap = 
				new HashMap<String, Class<?>>();
		static {
			// You MUST register your Persistable class here for it to be reconstructable
			constructorMap.put(ActorClass.class, ActorClass.constructor());
			constructorMap.put(ActorInstance.class, ActorInstance.constructor());
			constructorMap.put(Behavior.class, Behavior.constructor());
			constructorMap.put(Parameter.class, Parameter.constructor());
			constructorMap.put(BehaviorInstance.class, BehaviorInstance.constructor());
			constructorMap.put(Event.class, Event.constructor());
			constructorMap.put(Parameters.class, Parameters.constructor());
			constructorMap.put(Action.class, Action.constructor());
			constructorMap.put(Trigger.class, Trigger.constructor());
			constructorMap.put(Hero.class, Hero.constructor());
			constructorMap.put(Map.class, Map.constructor());
			constructorMap.put(MapLayer.class, MapLayer.constructor());
			constructorMap.put(ObjectClass.class, ObjectClass.constructor());
			constructorMap.put(ObjectInstance.class, ObjectInstance.constructor());
			constructorMap.put(PlatformGame.class, PlatformGame.constructor());
			constructorMap.put(Tileset.class, Tileset.constructor());
			constructorMap.put(UILayout.class, UILayout.constructor());
			constructorMap.put(Button.class, Button.constructor());
			constructorMap.put(JoyStick.class, JoyStick.constructor());
			constructorMap.put(ActorClassPointer.class, ActorClassPointer.constructor());
			constructorMap.put(EventPointer.class, EventPointer.constructor());
			constructorMap.put(ObjectClassPointer.class, ObjectClassPointer.constructor());
			constructorMap.put(Switch.class, Switch.constructor());
			constructorMap.put(Variable.class, Variable.constructor());
			constructorMap.put(Vector.class, Vector.constructor());
			
			for (Class<?> key : constructorMap.keySet()) {
				nameMap.put(key.getName(), key);
			}
		}
		
		public abstract DataObject construct();
		
		static DataObject construct(String type) throws ParseDataException {
			Class<?> c = nameMap.get(type);
			if (c == null) throw new ParseDataException("No constructor for type: " + type);
			Constructor constructor = constructorMap.get(c);
			return constructor.construct();
		}
	}
	
	void addFields(FieldData fields) throws ParseDataException, NumberFormatException;
}
