package com.platforge.editor.maker;

import com.platforge.data.ActorClass;
import com.platforge.data.ActorInstance;
import com.platforge.data.PlatformGame;
import com.platforge.editor.data.Data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class SelectorActorInstance extends Button implements IPopulatable {

	private PlatformGame game;
	private int id = 1;
	
	public int getSelectedInstanceId() {
		return id;
	}
	
	public void setSelectedInstance(int id) {
		this.id = id;
		
		ActorInstance instance = game.getSelectedMap().getActorInstanceById(id);

		BitmapDrawable drawable;
		String text;
		if (instance == null) {
			drawable = null;
			text = "None";
		} else {
			ActorClass actor = instance.getActorClass(game);
			Bitmap bmp = Data.loadActorIcon(actor.imageName);
			
			drawable = new BitmapDrawable(bmp);
			//text = String.format("%03d: ", id) + actor.name;
			text = actor.name;
		}
		
		setText(text);
		setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
	}
	
	public SelectorActorInstance(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectorActorInstance(Context context) {
		super(context);
	}

	@Override
	public void populate(final PlatformGame game) {
		this.game = game; 
		setSelectedInstance(id);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), SelectorMapActorInstance.class);
				intent.putExtra("game", game);
				intent.putExtra("id", id);
				((Activity)getContext()).startActivityForResult(intent, getId());
			}
		});
	}

	@Override
	public boolean onActivityResult(int requestCode, Intent data) {
		if (requestCode == getId()) {
			setSelectedInstance(data.getExtras().getInt("id"));
			return true;
		}
		return false;
	}
}
