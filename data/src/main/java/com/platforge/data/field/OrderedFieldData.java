package com.platforge.data.field;

import java.util.List;

public abstract class OrderedFieldData implements FieldData {

	@Override
	public int add(int x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public long add(long x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public short add(short x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public float add(float x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public double add(double x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public byte add(byte x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public char add(char x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public boolean add(boolean x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public String add(String x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public <T extends DataObject> T add(T x, Class<? extends T> clazz,
			String field) throws ParseDataException, NumberFormatException {
		return add(x, clazz);
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz, String field)
			throws ParseDataException, NumberFormatException {
		return add(x, clazz);
	}

	@Override
	public boolean[] addArray(boolean[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}

	@Override
	public int[] addArray(int[] x, String field) throws NumberFormatException,
			ParseDataException {
		return addArray(x);
	}

	@Override
	public String[] addArray(String[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}

	@Override
	public int[][] add2DArray(int[][] x, String field)
			throws NumberFormatException, ParseDataException {
		return add2DArray(x);
	}

	@Override
	public <T extends DataObject> T[] addArray(T[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x, String field)
			throws NumberFormatException, ParseDataException {
		return addList(x);
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x,
			Class<? extends T> clazz, String field)
			throws NumberFormatException, ParseDataException {
		return addList(x, clazz);
	}

	@Override
	public List<Integer> addIntList(List<Integer> x, String field)
			throws NumberFormatException, ParseDataException {
		return addIntList(x);
	}

	@Override
	public List<String> addStringList(List<String> x, String field)
			throws NumberFormatException, ParseDataException {
		return addStringList(x);
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x, String field)
			throws NumberFormatException, ParseDataException {
		return addBooleanList(x);
	}
}
