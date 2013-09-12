package edu.elon.honors.price.data;

import edu.elon.honors.price.data.field.DataObject;

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
