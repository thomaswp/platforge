package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import android.content.Context;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorVector;
import com.platforge.editor.maker.TextUtils;
import com.platforge.player.core.action.EventContext.Scope;
import com.platforge.player.core.action.EventContext.TriggerType;

public class ElementVector extends ElementMulti {
	
	@Override
	protected String getGroupName() {
		return "Vector";
	}
	
	public ElementVector(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected int getVersion() {
		return 1;
	}
	
	@Override
	protected void upgrade(Parameters params) {
		if (params.version < 1) {
			if (params.getInt(0) > 0) {
				params.set(0, params.getInt(0) + 1);
			}
		}
	}

	@Override
	protected Option[] getOptions() {
		final SelectorVector selectorVector = new SelectorVector(context);
		Option optionExact = new Option(selectorVector, "the exact Vector") {
			@Override
			public void writeDescription(StringBuilder sb, PlatformGame game) {
				sb.append("[");
				TextUtils.addColoredText(sb, selectorVector.getVectorX(), 
						DatabaseEditEvent.COLOR_VALUE);
				sb.append(", ");
				TextUtils.addColoredText(sb, selectorVector.getVectorY(), 
						DatabaseEditEvent.COLOR_VALUE);
				sb.append("]");
			}
			
			@Override
			public void readParams(Iterator params) {
				final float x = params.getFloat();
				final float y = params.getFloat();
				selectorVector.post(new Runnable() {
					@Override
					public void run() {
						selectorVector.setVector(x, y);
					}
				});
			}
			
			@Override
			public void addParams(Parameters params) {
				params.addParam(selectorVector.getVectorX());
				params.addParam(selectorVector.getVectorY());
			}
		};
		
		Option[] options = new Option[] {
				optionExact,
				new OptionEmpty(context, "a random vector",
						"a random vector"),
				new OptionEmpty(context, "the triggering vector", 
						"the triggering vector"),
				new OptionElement("a joystick's vector",
						new ElementJoystick(attributes, context))
		};
		
		options[2].enabled = eventContext.hasTrigger(TriggerType.UITrigger);
		options[3].visible = eventContext.getScope() == Scope.MapEvent;
		
		return options;
	}
}

