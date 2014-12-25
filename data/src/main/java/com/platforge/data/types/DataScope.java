package com.platforge.data.types;

import com.platforge.data.types.DataScope;

public enum DataScope {
	Global,
	Local,
	Param;
	
	public int toInt() {
		return ordinal();
	}
	
	public static DataScope fromInt(int i) {
		return values()[i];
	}
}