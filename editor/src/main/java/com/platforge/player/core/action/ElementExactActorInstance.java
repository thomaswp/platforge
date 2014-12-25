package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.ActorInstance;
import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.SelectorActorInstance;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ElementExactActorInstance extends Element {

	private SelectorActorInstance selectorActorInstance;

	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_VARIABLE;
	}

	public ElementExactActorInstance(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected void addParameters(Parameters params) {
		params.addParam(selectorActorInstance.getSelectedInstanceId());
	}

	@Override
	protected void readParameters(Iterator params) {
		final int id = params.getInt();
		selectorActorInstance.post(new Runnable() {
			@Override
			public void run() {
				selectorActorInstance.setSelectedInstance(id);
			}
		});
	}

	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		selectorActorInstance = new SelectorActorInstance(context);
		LayoutParams lps = new LayoutParams(200, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.addView(selectorActorInstance, lps);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		int id = selectorActorInstance.getSelectedInstanceId();
		ActorInstance instance = game.getSelectedMap().getActorInstanceById(id);
		if (instance != null) {
			StringBuilder sb = new StringBuilder();
			String actorString;
			if (instance.classIndex > 0)
				actorString = String.format("%s %03d", 
						game.actors[instance.classIndex].name, instance.id);
			else
				actorString = game.getHero().name;
			TextUtils.addColoredText(sb, actorString, color);
			return sb.toString();
		} else {
			return "[None]";
		}
	}

}
