package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;

import android.content.Context;

public class ElementGroup extends Element {

	public ElementGroup(Attributes atts, Context context) {
		super(atts, context);
	}
	
	@Override
	protected void addParameters(Parameters params) {
		Parameters childPs = new Parameters();
		super.addParameters(childPs);
		params.addParam(childPs);
	}
	
	@Override
	protected void readParameters(Iterator params) {
		Parameters childPs = params.getParameters();
		Iterator iterator = childPs.iterator(true);
		super.readParameters(iterator);
		iterator.dispose();
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
