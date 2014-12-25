package com.platforge.editor.maker;

import com.platforge.data.MapClass;
import com.platforge.data.Behavior.ParameterType;
import com.platforge.data.types.ObjectClassPointer;
import com.platforge.editor.data.Data;

import android.content.Context;
import android.graphics.Bitmap;

public class SelectorObjectClass extends SelectorMapClass<ObjectClassPointer> {

	public SelectorObjectClass(Context context) {
		super(context);
	}

	@Override
	protected ObjectClassPointer getNewPointer() {
		return new ObjectClassPointer();
	}

	@Override
	protected ParameterType getParameterType() {
		return ParameterType.ObjectClass;
	}

	@Override
	protected MapClass[] getMapClasses() {
		return game.objects;
	}

	@Override
	protected Bitmap getBitmap(String imageName) {
		return Data.loadObject(imageName);
	}

}
