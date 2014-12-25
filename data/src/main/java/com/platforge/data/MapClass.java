package com.platforge.data;

import java.util.LinkedList;
import java.util.List;

import com.platforge.data.field.FieldData;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.game.Color;

import com.platforge.data.BehaviorInstance;
import com.platforge.data.GameData;

public abstract class MapClass extends GameData {
	private static final long serialVersionUID = 1L;

	public enum CollidesWith {
		Hero, Actors, Objects, Terrain 
	}
	
	protected abstract String getDefaultImageName();

	public float zoom = 1;
	public int color = Color.WHITE;
	public String imageName = getDefaultImageName();
	public String name = "";
	public List<BehaviorInstance> behaviors = 
			new LinkedList<BehaviorInstance>();
	public final boolean[] collidesWith = new boolean[] {
			true, true, true, true	
	};
	
	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		zoom = fields.add(zoom);
		color = fields.add(color);
		imageName = fields.add(imageName);
		name = fields.add(name);
		behaviors = fields.addList(behaviors);
		fields.addArray(collidesWith);
	}

	public boolean collidesWith(CollidesWith with) {
		return collidesWith[with.ordinal()];
	}

	public void setCollidesWith(CollidesWith with, boolean collides) {
		collidesWith[with.ordinal()] = collides;
	}

}
