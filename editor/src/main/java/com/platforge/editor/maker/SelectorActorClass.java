package com.platforge.editor.maker;

import java.util.ArrayList;
import java.util.List;

import com.platforge.data.ActorClass;
import com.platforge.data.MapClass;
import com.platforge.data.PlatformGame;
import com.platforge.data.Behavior.ParameterType;
import com.platforge.data.types.ActorClassPointer;
import com.platforge.editor.data.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SelectorActorClass extends SelectorMapClass<ActorClassPointer> {
	
	public SelectorActorClass(Context context) {
		super(context);
	}

	@Override
	protected ActorClassPointer getNewPointer() {
		return new ActorClassPointer();
	}

	@Override
	protected ParameterType getParameterType() {
		return ParameterType.ActorClass;
	}

	@Override
	protected MapClass[] getMapClasses() {
		return game.actors;
	}

	@Override
	protected Bitmap getBitmap(String imageName) {
		return Data.loadActorIcon(imageName);
	}

}
