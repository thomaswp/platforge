package com.platforge.player.core.action;

import org.xml.sax.Attributes;

import com.platforge.data.PlatformGame;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ElementString extends Element {
	
	private int length;
	private EditText editText;
	
	public ElementString(Attributes atts, Context context) {
		super(atts, context);
	}

	@Override
	protected void readAttributes(Attributes atts) {
		super.readAttributes(atts);
		length = Integer.parseInt(atts.getValue("length"));
	}
	
	@Override
	protected void addParameters(Parameters params) {
		params.addParam(editText.getText().toString());
	}
	
	@Override
	protected void readParameters(Iterator params) {
		editText.setText(params.getString());
	}
	
	@Override
	public void genView() {
		LinearLayout layout = new LinearLayout(context);
		editText = new EditText(context);
		editText.setFilters(new InputFilter[] {
			new InputFilter.LengthFilter(length)	
		});
		layout.addView(editText);
		editText.setWidth(200);
		main = layout;
	}

	@Override
	public String getDescription(PlatformGame game) {
		return editText.getText().toString();
	}
}
