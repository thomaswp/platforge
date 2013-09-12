package edu.elon.honors.price.data.types;

import edu.elon.honors.price.data.GameData;
import edu.elon.honors.price.data.ICopyable;
import edu.elon.honors.price.data.field.FieldData;
import edu.elon.honors.price.data.field.FieldData.ParseDataException;

public abstract class ScopedData<T> extends GameData
implements ICopyable<T> {
	private static final long serialVersionUID = 1L;

	public int id;
	public DataScope scope;

	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		id = fields.add(id);
		int ordinal = fields.add(scope == null ? -1 : scope.ordinal()); 
		scope = ordinal < 0 ? null : DataScope.values()[ordinal];
	}

	public ScopedData(int id, DataScope scope) {
		this.id = id; this.scope = scope;
	}
}
