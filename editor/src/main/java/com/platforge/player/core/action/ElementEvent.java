package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.EventPointer;
import com.platforge.editor.maker.Screen;
import com.platforge.editor.maker.SelectorEvent;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ElementEvent extends Element {
	
	SelectorEvent selectorEvent;

	public ElementEvent(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorEvent.getEvent());
	}

	@Override
	protected void readParameters(Iterator params) {
		selectorEvent.setEvent((EventPointer)params.getObject());
	}

	@Override
	protected void genView() {
		LinearLayout layout = new LinearLayout(context);
		selectorEvent = new SelectorEvent(context, eventContext);
		LinearLayout.LayoutParams lps = new LayoutParams(
				Screen.dipToPx(200, context), LayoutParams.WRAP_CONTENT);
		layout.addView(selectorEvent, lps);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		Object o = selectorEvent.getSelectedItem();
		return o == null ? "[None]" : TextUtils.getColoredText(
				o.toString(), TextUtils.COLOR_VARIABLE);
	}

}
