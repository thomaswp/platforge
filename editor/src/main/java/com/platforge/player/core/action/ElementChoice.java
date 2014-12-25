package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.editor.maker.DatabaseEditEvent;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;

public class ElementChoice extends Element {

	private String text;
	private boolean mute;
	
	public String getText() {
		return text;
	}
	
	@Override
	protected String getDefaultColor() {
		return DatabaseEditEvent.COLOR_MODE;
	}
	
	public ElementChoice(Attributes atts, Context context) {
		super(atts, context);
	}
	
	public ElementChoice(Context context, String text, boolean mute) {
		super(context);
		this.text = text;
		this.mute = mute;
		genView();
	}
	
	@Override
	protected void readAttributes(Attributes atts) {
		super.readAttributes(atts);
		text = atts.getValue("text");
		String mute = atts.getValue("mute");
		if (mute != null) {
			this.mute = Boolean.parseBoolean(mute);
		}
	}

	@Override
	public String getDescription(PlatformGame game) {		
		StringBuilder sb = new StringBuilder();
		if (!mute) {
			TextUtils.addColoredText(sb, text, color);
		}
		for (int i = 0; i < children.size(); i++) {
			sb.append(" ");
			sb.append(children.get(i).getDescription(game));
		}
		
		return sb.toString();
	}
}
