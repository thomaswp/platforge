package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Rect;
import android.widget.LinearLayout;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorRegion;
import com.platforge.editor.maker.TextUtils;
import com.platforge.player.core.action.EventContext.Scope;

public class ElementRegion extends Element {
	
	private SelectorRegion selectorRegion;
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VALUE;
	}
	
	public ElementRegion(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		Parameters ps = new Parameters();
		Rect rect = selectorRegion.getRect();
		ps.addParam(rect.left);
		ps.addParam(rect.top);
		ps.addParam(rect.right);
		ps.addParam(rect.bottom);
		params.addParam(ps);
	}
	
	@Override
	protected void readParameters(Iterator params) {
		Rect rect = new Rect();
		Parameters ps = params.getParameters();
		rect.left = ps.getInt(0);
		rect.top = ps.getInt(1);
		rect.right = ps.getInt(2);
		rect.bottom = ps.getInt(3);
		selectorRegion.setRect(rect);
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		layout.setFocusableInTouchMode(true);
		selectorRegion = new SelectorRegion(context);
		selectorRegion.setHasMap(
				eventContext.getScope() == Scope.MapEvent);
		layout.addView(selectorRegion);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		Rect rect = selectorRegion.getRect();
		sb.append("(");
		TextUtils.addColoredText(sb, rect.left, color);
		sb.append(", ");
		TextUtils.addColoredText(sb, rect.top, color);
		sb.append(", ");
		TextUtils.addColoredText(sb, rect.right, color);
		sb.append(", ");
		TextUtils.addColoredText(sb, rect.bottom, color);
		sb.append(")");
		return sb.toString();
	}
}

