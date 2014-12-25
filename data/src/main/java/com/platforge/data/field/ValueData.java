package com.platforge.data.field;

import com.platforge.data.field.ArrayHash;
import com.platforge.data.field.ArrayIO;
import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData;

abstract class ValueData implements FieldData {
	protected final DataObject dataObject;
	
	public ValueData(DataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	protected abstract void reset();
	
	protected final static int prime = 31;
	
	protected static int hashArray(Object a, ArrayHash io) {
		int length = io.length(a);
		int result = 1;
		for (int i = 0; i < length; i++) {
			result = prime * result + io.hash(a, i);
		}
		return result;
	}
	
	protected static boolean areArraysEqual(Object a1, Object a2, ArrayHash io) {
		int length1 = io.length(a1);
		int length2 = io.length(a2);
		if (length1 != length2) return false;
		for (int i = 0; i < length1; i++) {
			if (!io.areEqual(a1, a2, i)) return false;
		}
		return true;
	}
	
	public final static ArrayHash int2dIO = new ArrayHash() {
		
		private int[][] cast(Object x) {
			return (int[][]) x;
		}

		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return areArraysEqual(cast(x1)[index], cast(x2)[index], ArrayIO.intIO);
		}
		
		@Override
		public int hash(Object x, int index) {
			return hashArray(cast(x)[index], ArrayIO.intIO);
		}
	};

	@Override
	public boolean writeMode() {
		return true;
	}

	@Override
	public boolean readMode() {
		return false;
	}
}
