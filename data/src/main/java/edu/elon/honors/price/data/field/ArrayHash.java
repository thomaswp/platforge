package edu.elon.honors.price.data.field;

public abstract class ArrayHash {
	public abstract boolean areEqual(Object x1, Object x2, int index);
	public abstract int hash(Object x, int index);
	public abstract int length(Object x);

}
