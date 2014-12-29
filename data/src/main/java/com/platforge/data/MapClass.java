package com.platforge.data;

import java.util.LinkedList;
import java.util.List;

import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.field.StrictFieldData;

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
	public void addFields(StrictFieldData fields) throws ParseDataException,
			NumberFormatException {
		zoom = fields.add(zoom, "zoom");
		color = fields.add(color, "color");
		imageName = fields.add(imageName, "imageName");
		name = fields.add(name, "name");
		behaviors = fields.addList(behaviors, "behaviors");
		fields.addArray(collidesWith, "collidesWith");
	}

	public boolean collidesWith(CollidesWith with) {
		return collidesWith[with.ordinal()];
	}

	public void setCollidesWith(CollidesWith with, boolean collides) {
		collidesWith[with.ordinal()] = collides;
	}

}
