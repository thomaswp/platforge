package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.editor.maker.TextUtils;

import android.content.Context;

public class ElementDescription extends Element {

	private String text;
	
	public ElementDescription(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void readAttributes(Attributes atts) {
		super.readAttributes(atts);
		text = atts.getValue("text");
	}

	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		TextUtils.addColoredText(sb, text, color);
		return sb.toString();
	}
}
