package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorPoint;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.widget.LinearLayout;

public class ElementExactPoint extends Element {
	
	private SelectorPoint selectorPoint;
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VALUE;
	}
	
	public ElementExactPoint(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorPoint.getPointX());
		params.addParam(selectorPoint.getPointY());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		selectorPoint.setPoint(params.getInt(), 
				params.getInt());
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		layout.setFocusableInTouchMode(true);
		selectorPoint = new SelectorPoint(context);
		layout.addView(selectorPoint);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		TextUtils.addColoredText(sb, selectorPoint.getPointX(), color);
		sb.append(", ");
		TextUtils.addColoredText(sb, selectorPoint.getPointY(), color);
		sb.append(")");
		return sb.toString();
	}
}

