package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.Behavior;
import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.ObjectClassPointer;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorObjectClass;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ElementObjectClass extends Element {

	private SelectorObjectClass selectorObjectClass;
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}
	
	public ElementObjectClass(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorObjectClass.getSelectedClass());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		selectorObjectClass.setSelectedClass((ObjectClassPointer)params.getObject());
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		selectorObjectClass = new SelectorObjectClass(context);
		selectorObjectClass.setEventContext(eventContext);
		ViewGroup.LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.addView(selectorObjectClass, params);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		Behavior behavior = eventContext == null ? null : eventContext.getBehavior();
		String name = selectorObjectClass.getSelectedClass().getName(game, behavior);
		TextUtils.addColoredText(sb, name, color);
		return sb.toString();
	}

}

