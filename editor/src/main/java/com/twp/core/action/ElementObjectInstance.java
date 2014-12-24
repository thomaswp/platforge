package com.twp.core.action;

import org.xml.sax.Attributes;

import android.content.Context;
import edu.elon.honors.price.maker.DatabaseEditEvent;
import com.twp.core.action.EventContext.Scope;
import com.twp.core.action.EventContext.TriggerType;

public class ElementObjectInstance extends ElementMulti {

	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}
	
	@Override
	protected String getGroupName() {
		return "Object";
	}

	public ElementObjectInstance(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected Option[] getOptions() {
		Option[] options = new Option[] {
			new OptionElement("the exact object", 
					new ElementExactObjectInstance(attributes, context)),
			new OptionEmpty(context, "the triggering object", 
					"the triggering object"),
			new OptionEmpty(context, "the last created object",
					"the last created object"),
			new OptionEmpty(context, "this object", "this object")
		};
		
		options[0].visible = 
			eventContext.getScope() == Scope.MapEvent;
		options[1].enabled = 
			eventContext.hasTrigger(TriggerType.ObjectTrigger) ||
			eventContext.hasTrigger(TriggerType.RegionTrigger);
		options[3].visible = 
			eventContext.getScope() == Scope.ObjectBehavior;
		
		return options;
	}

}
