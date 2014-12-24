package com.twp.core.action;

import org.xml.sax.Attributes;

import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.Event.Parameters;
import edu.elon.honors.price.data.Event.Parameters.Iterator;
import edu.elon.honors.price.data.types.Switch;
import edu.elon.honors.price.maker.DatabaseEditEvent;
import edu.elon.honors.price.maker.SelectorSwitch;
import edu.elon.honors.price.maker.TextUtils;

import android.content.Context;
import android.widget.LinearLayout;

public class ElementSwitch extends Element {

	private SelectorSwitch selectorSwitch;
	private String scope;
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}
	
	public ElementSwitch(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void readAttributes(Attributes atts) {
		super.readAttributes(atts);
		scope = atts.getValue("scope");
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorSwitch.getSwitch());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		Switch s = params.getSwitch();
		selectorSwitch.setSwitch(s);
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		selectorSwitch = new SelectorSwitch(context);
		selectorSwitch.setEventContext(eventContext);
		selectorSwitch.setAllowedScopes(scope);
		layout.addView(selectorSwitch);
		selectorSwitch.setWidth(200);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		String name = selectorSwitch.getText().toString();
		TextUtils.addColoredText(sb, name, color);
		return sb.toString();
	}
}
