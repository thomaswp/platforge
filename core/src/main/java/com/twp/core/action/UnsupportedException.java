package com.twp.core.action;

import com.twp.core.game.Debug;

public class UnsupportedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnsupportedException() {
		super("Unsupported Operation!");
		Debug.write("Unsupported operation!! Not Good!");
	}

}
