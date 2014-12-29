package com.platforge.data.types;

import com.platforge.data.GameData;
import com.platforge.data.ICopyable;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.field.StrictFieldData;

public abstract class ScopedData<T> extends GameData
implements ICopyable<T> {
	private static final long serialVersionUID = 1L;

	public int id;
	public DataScope scope;

	@Override
	public void addFields(StrictFieldData fields) throws ParseDataException,
			NumberFormatException {
		id = fields.add(id, "id");
		int ordinal = fields.add(scope == null ? -1 : scope.ordinal(), "scope"); 
		scope = ordinal < 0 ? null : DataScope.values()[ordinal];
	}

	public ScopedData(int id, DataScope scope) {
		this.id = id; this.scope = scope;
	}
}
