package edu.elon.honors.price.maker;

import java.util.LinkedList;

import edu.elon.honors.price.data.Behavior.Parameter;
import edu.elon.honors.price.data.Behavior.ParameterType;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SelectorActivityVariable extends SelectorActivityIndex {

	EditText editTextDefaultValue;
	
	@Override
	protected String getType() {
		return "Variable";
	}

	@Override
	protected void setName(String name) {
		game.variableNames[id] = name;
	}

	@Override
	protected String makeRadioButtonText(int id) {
		return String.format("%03d: %s", id, game.variableNames[id]);
	}

	@Override
	protected void setupSelectors() {
		RelativeLayout host = (RelativeLayout)findViewById(R.id.relativeLayoutDefault);
		
		editTextDefaultValue = new EditText(this);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(5);
		editTextDefaultValue.setFilters(filterArray);
		editTextDefaultValue.setInputType(InputType.TYPE_CLASS_NUMBER);
		editTextDefaultValue.setMinimumWidth(100);
		host.addView(editTextDefaultValue);
		
		editTextDefaultValue.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				game.variableValues[id] = Integer.parseInt("0" + v.getText());
				return false;
			}
		});
	}
	
	@Override
	protected void setId(int id) {
		super.setId(id);
		editTextItemName.setText(game.variableNames[id]);
		textViewId.setText(String.format("%03d", id));
		editTextDefaultValue.setText("" + game.variableValues[id]);
	}

	@Override
	protected int getItemsSize() {
		return game.variableNames.length;
	}

	@Override
	protected void resizeItems(int newSize) {
		game.resizeVariables(newSize);
	}
	
	@Override
	protected String getLocalName(int id) {
		return eventContext.getBehavior().variableNames.get(id);
	}

	@Override
	protected String getLocalDefault(int id) {
		return eventContext.getBehavior()
		.variables.get(id).toString();
	}

	@Override
	protected int getLocalSize() {
		return eventContext.getBehavior().variableNames.size();
	}

	@Override
	protected String getParamName(int id) {
		LinkedList<Parameter> params = 
			eventContext.getBehavior().parameters;
		return params.get(id).name;
	}

	@Override
	protected int getParamSize() {
		return eventContext.getBehavior().parameters.size();
	}
	
	@Override 
	protected boolean getParamVisible(int id) {
		LinkedList<Parameter> params = 
			eventContext.getBehavior().parameters;
		return params.get(id).type == ParameterType.Variable;
	}
}
