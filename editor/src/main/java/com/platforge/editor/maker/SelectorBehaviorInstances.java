package com.platforge.editor.maker;

import java.util.List;

import com.platforge.data.Behavior;
import com.platforge.data.BehaviorInstance;
import com.platforge.data.PlatformGame;
import com.platforge.data.Behavior.BehaviorType;
import com.platforge.data.Behavior.Parameter;
import com.platforge.data.Behavior.ParameterType;
import com.platforge.data.Event.Parameters;
import com.platforge.editor.maker.R;
import com.platforge.player.core.action.Element;
import com.platforge.player.core.action.ElementActorClass;
import com.platforge.player.core.action.ElementBoolean;
import com.platforge.player.core.action.ElementNumber;
import com.platforge.player.core.action.ElementObjectClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectorBehaviorInstances extends LinearLayout 
implements IPopulatable{

	private RadioGroup radioGroupBehaviors;
	private Button buttonNew, buttonEdit, buttonDelete;
	
	private List<BehaviorInstance> behaviors;
	private BehaviorType type;
	private PlatformGame game;
	
	public List<BehaviorInstance> getBehaviors() {
		return behaviors;
	}
	
	public void setBehaviors(List<BehaviorInstance> behaviors,
			 BehaviorType type) {
		this.behaviors = behaviors;
		this.type = type;
		createRadios();
	}
	
	public SelectorBehaviorInstances(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SelectorBehaviorInstances(Context context) {
		super(context);
		init();
	}
	
	private void init() {		
		LayoutInflater.from(getContext()).inflate(
				R.layout.selector_behavior_instances, this);
		
		if (isInEditMode()) {
			return;
		}
		
		radioGroupBehaviors = 
			(RadioGroup)findViewById(R.id.radioGroupBehaviors);
		buttonNew = (Button)findViewById(R.id.buttonNewBehavior);
		buttonEdit = (Button)findViewById(R.id.buttonEditBehavior);
		buttonDelete = (Button)findViewById(R.id.buttonDeleteBehavior);
		
		buttonNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<Behavior> behaviors = game.getBehaviors(type);
				if (behaviors.size() == 0) {
					new AlertDialog.Builder(getContext())
					.setTitle("No Behaviors")
					.setMessage("You must create a behavior before you can assign it.")
					.setPositiveButton("Ok", null)
					.show();
					return;
				}
				String[] choices = new String[behaviors.size()];
				for (int i = 0; i < behaviors.size(); i++) {
					choices[i] = behaviors.get(i).name;
				}
				new AlertDialog.Builder(getContext())
				.setTitle("Add Behavior")
				.setItems(choices, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BehaviorInstance instance = 
							new BehaviorInstance(which, type);
						SelectorBehaviorInstances.this
						.behaviors.add(instance);
						addButton(instance);
					}
				})
				.setNegativeButton("Cancel", null)
				.show();
			}
		});
		
		buttonEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final int index = getSelectedIndex();
				if (index < 0) return;
				BehaviorInstance instance = behaviors.get(index);
				Intent intent = new Intent(getContext(), 
						DatabaseEditBehaviorInstance.class);
				intent.putExtra("game", game);
				intent.putExtra("instance", instance);
				intent.putExtra("index", index);
				((Activity)getContext()).startActivityForResult(intent, 
						getId());
			}
		});
		
		buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final int index = getSelectedIndex();
				if (index < 0) return;
				behaviors.remove(index);
				radioGroupBehaviors.removeViewAt(index);
			}
		});
	}
	
	private int getSelectedIndex() {
		return radioGroupBehaviors.indexOfChild(
				radioGroupBehaviors.findViewById(
					radioGroupBehaviors.getCheckedRadioButtonId()));
	}
	
	private void createRadios() {
		if (game == null || behaviors == null) return;
		int index = getSelectedIndex();
		radioGroupBehaviors.removeAllViews();
		for (BehaviorInstance behavior : behaviors) {
			addButton(behavior);
		}
		if (index >= 0) {
			((RadioButton)radioGroupBehaviors.getChildAt(index))
			.setChecked(true);
		}
	}
	
	private void addButton(BehaviorInstance behavior) {
		RadioButton button = new RadioButton(getContext());
		Spanned name = getBehaviorNameSpanned(getContext(), game, behavior);
		button.setText(name);
		radioGroupBehaviors.addView(button);
	}
	
	public static Spanned getBehaviorNameSpanned(Context context, 
			PlatformGame game, BehaviorInstance behavior) {
		return Html.fromHtml(getBehaviorNameHtml(context, game, behavior));
	}
	
	public static String getBehaviorNameHtml(Context context, 
			PlatformGame game, BehaviorInstance behavior) {
		StringBuilder sb = new StringBuilder();
		Behavior base = behavior.getBehavior(game);
		TextUtils.addColoredText(sb, base.name, TextUtils.COLOR_ACTION);
		sb.append(" (");
		
		List<Parameters> params = behavior.parameters;
		
		if (params.size() != base.parameters.size()) {
			params.clear();
			for (int i = 0; i < base.parameters.size(); i++) {
				params.add(null);
			}
		}
		
		for (int i = 0; i < base.parameters.size(); i++) {
			if (i > 0) sb.append(", ");
			Parameter baseParam = base.parameters.get(i);
			TextUtils.addColoredText(sb, baseParam.name, 
					TextUtils.COLOR_MODE);
			sb.append(" = ");
			Parameters param = params.get(i);
			String name = "<None>";
			name = TextUtils.getColoredText(name, Color.LTGRAY);
			if (param != null) {
				Element element = null;
				try {
					if (baseParam.type == ParameterType.Switch) {
						element = new ElementBoolean(null, context);
						//dumb work around for "on"/"off" not being inited
						((ElementBoolean )element).genView();
					} else if (baseParam.type == ParameterType.Variable) {
						element = new ElementNumber(null, context);
					} else if (baseParam.type == ParameterType.ActorClass) {
						element = new ElementActorClass(null, context);
					} else if (baseParam.type == ParameterType.ObjectClass) {
						element = new ElementObjectClass(null, context);
					}
					if (element != null) {
						DatabaseActivity.populateViews(element.getView(), game);
						element.setParameters((Parameters) param);
						name = element.getDescription(game);
					}
				} catch (Exception e) {
					params.set(i, null);
				}
			}
			sb.append(name);		
		}
		
		sb.append(")");
		
		return sb.toString();
	}

	@Override
	public void populate(PlatformGame game) {
		this.game = game;
		createRadios();
	}

	@Override
	public boolean onActivityResult(int requestCode, Intent data) {
		if (requestCode == getId()) {
			int index = data.getIntExtra("index", 0);
			BehaviorInstance instance = (BehaviorInstance)
				data.getSerializableExtra("instance");
			behaviors.set(index, instance);
			((RadioButton)radioGroupBehaviors.getChildAt(index))
			.setText(getBehaviorNameSpanned(getContext(), game, instance));
		}
		return false;
	}

}
