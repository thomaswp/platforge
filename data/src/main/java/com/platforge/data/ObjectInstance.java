package com.platforge.data;

import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData;
import com.platforge.data.field.FieldData.ParseDataException;

import com.platforge.data.GameData;
import com.platforge.data.ObjectClass;
import com.platforge.data.ObjectInstance;
import com.platforge.data.PlatformGame;

public class ObjectInstance extends GameData implements Comparable<ObjectInstance> {
	private static final long serialVersionUID = 1L;
	
	public int id;
	public int classIndex;
	public int startX, startY;

	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		id = fields.add(id);
		classIndex = fields.add(classIndex);
		startX = fields.add(startX);
		startY = fields.add(startY);
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new ObjectInstance();
			}
		};
	}
	
	private ObjectInstance() { }
	
	public ObjectInstance(int id, int classIndex, int startX, int startY) {
		this.id = id;
		this.classIndex = classIndex;
		this.startX = startX;
		this.startY = startY;
	}

	public ObjectClass getObjectClass(PlatformGame game) {
		return game.objects[classIndex];
	}

	@Override
	public int compareTo(ObjectInstance another) {
		return id - another.id;
	}
}
