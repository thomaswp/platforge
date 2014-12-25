package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.Behavior;
import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.ActorClassPointer;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorActorClass;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ElementActorClass extends Element {

	private SelectorActorClass selectorActorClass;
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}
	
	public ElementActorClass(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorActorClass.getSelectedClass());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		selectorActorClass.setSelectedClass((ActorClassPointer)params.getObject());
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		selectorActorClass = new SelectorActorClass(context);
		selectorActorClass.setEventContext(eventContext);
		ViewGroup.LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.addView(selectorActorClass, params);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		Behavior behavior = eventContext == null ? null : eventContext.getBehavior();
		String name = selectorActorClass.getSelectedClass().getName(game, behavior);
		TextUtils.addColoredText(sb, name, color);
		return sb.toString();
	}

}
