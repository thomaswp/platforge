package com.platforge.data;

import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData;
import com.platforge.data.field.FieldData.ParseDataException;

import com.platforge.data.MapClass;
import com.platforge.data.ObjectClass;

public class ObjectClass extends MapClass {
	private static final long serialVersionUID = 1L;
	
	public final static int MAX_FRICTION = 5;
	
	public float density = 0.5f;
	public float friction = 1;
	public boolean moves = true;
	public boolean rotates = true;
	public float restitution = 0.4f;
	public boolean isPlatform;

	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		super.addFields(fields);
		density = fields.add(density);
		friction = fields.add(friction);
		moves = fields.add(moves);
		rotates = fields.add(rotates);
		restitution = fields.add(restitution);
		isPlatform = fields.add(isPlatform);
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new ObjectClass();
			}
		};
	}
	
	@Override
	protected String getDefaultImageName() {
		return "rock.png";
	}
}

