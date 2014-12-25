package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.Screen;
import com.platforge.editor.maker.SelectorUIControl;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ElementJoystick extends Element {

	private SelectorUIControl selectorJoystick;
	
	@Override
	public String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}
	
	public ElementJoystick(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorJoystick.getSelectedControlId());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		final int id = params.getInt();
		selectorJoystick.post(new Runnable() {
			@Override
			public void run() {
				selectorJoystick.setSelectedControlId(id);
			}
		});
	}

	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		LayoutParams lps = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		selectorJoystick = new SelectorUIControl(context, 
				SelectorUIControl.CONTROL_JOY);
		selectorJoystick.setMinimumWidth(Screen.dipToPx(200, context));
		layout.addView(selectorJoystick, lps);
		main = layout;
	}
	
	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		String name = game.uiLayout.joysticks.get(
				selectorJoystick.getSelectedControlId()).name; 
		TextUtils.addColoredText(sb, name, color);
		return sb.toString();
	}

}
