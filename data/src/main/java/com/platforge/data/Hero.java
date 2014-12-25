package com.platforge.data;

import com.platforge.data.field.DataObject;

import com.platforge.data.ActorClass;
import com.platforge.data.Hero;

public class Hero extends ActorClass {
	private static final long serialVersionUID = 1L;

	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new Hero();
			}
		};
	}
}
