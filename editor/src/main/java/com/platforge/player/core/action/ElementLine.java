package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;

import android.content.Context;
import android.widget.LinearLayout;

public class ElementLine extends Element {
	
	public ElementLine(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected void genView() {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		main = layout;
		host = layout;
	}
	
	@Override
	public String getDescription(PlatformGame game) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < children.size(); i++) {
			if (i != 0) {
				sb.append(" ");
			}
			sb.append(children.get(i).getDescription(game));
		}
		
		return sb.toString();
	}
}
