package com.platforge.editor.maker;

import com.platforge.data.ObjectClass;
import com.platforge.data.Behavior.BehaviorType;
import com.platforge.editor.data.tutorial.Tutorial.EditorButton;
import com.platforge.editor.maker.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@AutoAssign
public class DatabaseEditObjectClass extends DatabaseActivity {
	
	private int id;
	
	private EditText editTextObjectName;
	private SelectorObjectImage selectorObjectImage;
	private SelectorBehaviorInstances selectorBehaviorInstances;
	private Button buttonScale;
	private SeekBar seekBarDensity, seekBarRestitution, seekBarFriction;
	private SelectorCollidesWith selectorCollidesWith;
	private CheckBox checkBoxMoves, checkBoxRotates, checkBoxIsPlatform;
	
	public ObjectClass getObject() {
		return game.objects[id];
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		id = getIntent().getExtras().getInt("id");
		ObjectClass objectClass = getObject();
		
		setContentView(R.layout.database_edit_object);
		setDefaultButtonActions();
		autoAssign();
		
		editTextObjectName.setText(objectClass.name);
		selectorObjectImage.setSelectedImageName(objectClass.imageName);
		selectorBehaviorInstances.setBehaviors(objectClass.behaviors, 
				BehaviorType.Object);
		selectorBehaviorInstances.populate(game);
		
		buttonScale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectorActivityScale.startForResult(
						DatabaseEditObjectClass.this, false, id);
			}
		});
		
		setButtonScaleText();
		
		seekBarDensity.setProgress((int)(objectClass.density * seekBarDensity.getMax()));
		seekBarDensity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				getObject().density = (float)progress / seekBar.getMax();
			}
		});
		
		seekBarFriction.setProgress((int)(objectClass.friction * seekBarFriction.getMax() / ObjectClass.MAX_FRICTION));		
		
		selectorCollidesWith.setMapClass(getObject());
		
		checkBoxMoves.setChecked(objectClass.moves);
		checkBoxRotates.setEnabled(objectClass.moves);
		checkBoxRotates.setChecked(objectClass.rotates);
		
		checkBoxMoves.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getObject().moves = isChecked;
				checkBoxRotates.setEnabled(isChecked);
				TutorialUtils.fireCondition(EditorButton.EditObjectIsMovable, 
						DatabaseEditObjectClass.this);
			}
		});
		
		checkBoxRotates.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getObject().rotates = isChecked;
			}
		});
		
		seekBarRestitution.setProgress((int)(objectClass.restitution * seekBarRestitution.getMax()));
		seekBarRestitution.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				getObject().restitution = (float)progress / seekBar.getMax();
			}
		});
		
		checkBoxIsPlatform.setChecked(objectClass.isPlatform);
		checkBoxIsPlatform.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getObject().isPlatform = isChecked;
				TutorialUtils.fireCondition(EditorButton.EditObjectIsPlatform, 
						DatabaseEditObjectClass.this);
			}
		});
		
		TutorialUtils.fireCondition(EditorButton.DatabaseObjectsEdit, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		TutorialUtils.addHighlightable(checkBoxIsPlatform, 
				EditorButton.EditObjectIsPlatform, this);
		TutorialUtils.addHighlightable(checkBoxMoves, 
				EditorButton.EditObjectIsMovable, this);
		TutorialUtils.addHighlightable(findViewById(R.id.buttonOk), 
				EditorButton.EditObjectOk, this);
	}
	
	protected void setButtonScaleText() {
		buttonScale.setText(String.format("%.02f", getObject().zoom));
	}
	
	@Override
	protected void onFinishing() {
		ObjectClass objectClass = getObject();
		objectClass.name = editTextObjectName.getText().toString();
		objectClass.imageName = selectorObjectImage.getSelectedImageName();
		objectClass.behaviors = selectorBehaviorInstances.getBehaviors();
		objectClass.friction = (float)seekBarFriction.getProgress() * 
				ObjectClass.MAX_FRICTION / seekBarFriction.getMax();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			selectorBehaviorInstances.populate(game);
			selectorBehaviorInstances.onActivityResult(requestCode, data);
			setButtonScaleText();
			selectorCollidesWith.setMapClass(getObject());
		}
	}
}