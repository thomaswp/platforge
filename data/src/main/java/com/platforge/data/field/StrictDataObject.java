package com.platforge.data.field;

import com.platforge.data.field.FieldData.ParseDataException;

public abstract class StrictDataObject implements DataObject {

	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		addFields((StrictFieldData) fields);
	}
	
	public abstract void addFields(StrictFieldData fields) throws ParseDataException, NumberFormatException;
}
