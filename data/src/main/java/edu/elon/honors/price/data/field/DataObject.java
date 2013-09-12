package edu.elon.honors.price.data.field;

import java.util.HashMap;

import edu.elon.honors.price.data.ActorClass;
import edu.elon.honors.price.data.ActorInstance;
import edu.elon.honors.price.data.Behavior;
import edu.elon.honors.price.data.BehaviorInstance;
import edu.elon.honors.price.data.Event;
import edu.elon.honors.price.data.Hero;
import edu.elon.honors.price.data.MapLayer;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.Tileset;
import edu.elon.honors.price.data.UILayout;
import edu.elon.honors.price.data.Event.Action;
import edu.elon.honors.price.data.Event.Parameters;
import edu.elon.honors.price.data.Event.Trigger;
import edu.elon.honors.price.data.UILayout.Button;
import edu.elon.honors.price.data.UILayout.JoyStick;
import edu.elon.honors.price.data.Map;
import edu.elon.honors.price.data.ObjectClass;
import edu.elon.honors.price.data.ObjectInstance;
import edu.elon.honors.price.data.field.FieldData.ParseDataException;
import edu.elon.honors.price.data.types.ActorClassPointer;
import edu.elon.honors.price.data.types.EventPointer;
import edu.elon.honors.price.data.types.ObjectClassPointer;
import edu.elon.honors.price.data.types.Switch;
import edu.elon.honors.price.data.types.Variable;
import edu.elon.honors.price.physics.Vector;

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
