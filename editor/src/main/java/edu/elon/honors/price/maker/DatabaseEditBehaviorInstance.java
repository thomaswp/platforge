package edu.elon.honors.price.maker;

import java.util.LinkedList;
import java.util.List;

import edu.elon.honors.price.data.Behavior;
import edu.elon.honors.price.data.BehaviorInstance;
import edu.elon.honors.price.data.Event.Parameters;
import edu.elon.honors.price.data.Behavior.Parameter;
import edu.elon.honors.price.data.Behavior.ParameterType;
import edu.elon.honors.price.data.PlatformGame;
import com.twp.core.game.Debug;
import com.twp.core.action.Element;
import com.twp.core.action.ElementActorClass;
import com.twp.core.action.ElementBoolean;
import com.twp.core.action.ElementNumber;
import com.twp.core.action.ElementObjectClass;
import com.twp.core.action.EventContext;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DatabaseEditBehaviorInstance extends DatabaseActivity implements IEventContextual {

	private BehaviorInstance instance;
	private List<Element> elements;
	private View root;
	private EventContext eventContext;

	public static void startForResult(Activity activity, int requestCode,
			PlatformGame game, BehaviorInstance instance, EventContext eventContext) {
		Intent intent = new Intent(activity, DatabaseEditBehaviorInstance.class);
		intent.putExtra("game", game);
		intent.putExtra("instance", instance);
		intent.putExtra("eventContext", eventContext);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static BehaviorInstance getBehaviorInstance(Intent data) {
		return (BehaviorInstance) data.getExtras().getSerializable("instance");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.database_edit_behavior_instance);
		setDefaultButtonActions();

		instance = (BehaviorInstance)getExtra("instance");
		eventContext = (EventContext)getExtra("eventContext");
		Behavior behavior = instance.getBehavior(game);
		
		String name = TextUtils.getColoredText(behavior.name, 
				TextUtils.COLOR_ACTION);
		String title = "Edit " + name + "'s Parameters";
		if (behavior.parameters.size() == 0) {
			title += " (No parameters to edit!)";
		}
		((TextView)findViewById(R.id.textViewTitle)).setText(
				Html.fromHtml(title));

		int id = 100;

		while (instance.parameters.size() < behavior.parameters.size()) {
			instance.parameters.add(null);
		}
		
		elements = new LinkedList<Element>();
		TableLayout layout = new TableLayout(this);
		for (int i = 0; i < instance.parameters.size(); i++) {
			Parameter param = behavior.parameters.get(i);
			TableRow row = new TableRow(this);
			//row.setGravity(Gravity.CENTER);
			TextView tv = new TextView(this);
			tv.setText(param.name + ":   ");
			tv.setTextAppearance(this, 
					android.R.attr.textAppearanceLarge);
			TableRow.LayoutParams lps = 
				new TableRow.LayoutParams();
			lps.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
			row.addView(tv, lps);

			Object instanceParam = instance.parameters.get(i);
			Element element = null;
			if (param.type == ParameterType.Switch) {
				element = new ElementBoolean(null, this);
				//dumb work around for "on"/"off" not being inited
				((ElementBoolean )element).genView();
			} else if (param.type == ParameterType.Variable) {
				element = new ElementNumber(null, this);
			} else if (param.type == ParameterType.ActorClass) {
				element = new ElementActorClass(null, this);
			} else if (param.type == ParameterType.ObjectClass) {
				element = new ElementObjectClass(null, this);
			}
			
			if (element != null) {
				row.addView(element.getView());
				elements.add(element);
				
				if (instanceParam != null) {
					element.setParameters((Parameters)instanceParam);
				}
			}

			layout.addView(row);
		}

		ScrollView sv = new ScrollView(this);
		sv.addView(layout);
		sv.setId(id++);

		((LinearLayout)findViewById(R.id.linearLayoutContent))
		.addView(sv);
		
		root = layout;
		setPopulatableViewIds(root, id);
		populateViews(root);
	}

	@Override
	protected void onFinishing() {
		instance.parameters.clear();
		for (Element element : elements) {
			Parameters params = element.getParameters();
			instance.parameters.add(params);
			//Debug.write(params);
		}
	}

	@Override
	protected boolean hasChanged() {
		BehaviorInstance old = (BehaviorInstance)getExtra("instance");
		//Debug.write("%s vs %s", instance.toString(), old.toString());
		return !instance.equals(old) || super.hasChanged(); 
	}
	
	@Override
	protected void putExtras(Intent data) {
		data.putExtra("instance", instance);
		data.putExtra("index", 
				getIntent().getIntExtra("index", 0));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			populateViews(root);
			View v = findViewById(requestCode);
			if (v != null && v instanceof IPopulatable) {
				((IPopulatable)v).onActivityResult(requestCode, data);
			}
		}
	}

	@Override
	public EventContext getEventContext() {
		return eventContext;
	}
}
