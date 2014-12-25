package com.platforge.player.core.action;

import java.util.List;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Behavior.BehaviorType;
import com.platforge.data.Behavior.ParameterType;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.Screen;
import com.platforge.editor.maker.SelectorSetBehaviorParameter;
import com.platforge.editor.maker.TextUtils;
import com.platforge.editor.maker.R;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ElementBehavior extends ElementGroup {

	private BehaviorType type;
	private SelectorSetBehaviorParameter selectorSetBehaviorParameter;
	
	public ElementBehavior(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected void readAttributes(Attributes atts) {
		String t = atts.getValue("type");
		if (t != null) {
			if (t.equalsIgnoreCase("actor")) {
				type = BehaviorType.Actor;
			} else if (t.equalsIgnoreCase("object")) {
				type = BehaviorType.Object;
			}
		}
		if (type == null) {
			throw new RuntimeException("Unrecognized type!");
		}
	}
	
	@Override
	protected void addParameters(Parameters params) {
		Parameters ps = new Parameters();
		ps.addParam(selectorSetBehaviorParameter.getBehaviorIndex());
		ps.addParam(selectorSetBehaviorParameter.getParameters());
		params.addParam(ps);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void readParameters(Iterator params) {
		Parameters ps = params.getParameters();
		selectorSetBehaviorParameter.setBehaviorIndex(ps.getInt(0));
		selectorSetBehaviorParameter.setParameters((List<Parameters>)ps.getObject(1));
	}
	
	@Override 
	protected void genView() {
		selectorSetBehaviorParameter = new SelectorSetBehaviorParameter(
				(Activity)context, type, eventContext);
		//LinearLayout layout = new LinearLayout(context);
//		LayoutParams lps = new LayoutParams(Screen.dipToPx(200, context), 
//				LayoutParams.WRAP_CONTENT);
		//layout.addView(selectorSetBehaviorParameter);
		main = selectorSetBehaviorParameter;
	}
	

	@Override
	public String getDescription(PlatformGame game) {
		return selectorSetBehaviorParameter.getDescription();
	}
}
