package com.platforge.data.field;

import java.util.List;

public class JsonSerializer implements FieldData {

	private String nextField() {
		return null;
	}
	
	@Override
	public boolean writeMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int add(int x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public int add(int x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long add(long x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public long add(long x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short add(short x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public short add(short x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float add(float x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public float add(float x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double add(double x) throws ParseDataException,
			NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public double add(double x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte add(byte x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public byte add(byte x, String field) throws ParseDataException,
			NumberFormatException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char add(char x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public char add(char x, String field) throws ParseDataException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean add(boolean x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public boolean add(boolean x, String field) throws ParseDataException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String add(String x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public String add(String x, String field) throws ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DataObject> T add(T x, Class<? extends T> clazz)
			throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, Class<? extends T> clazz,
			String field) throws ParseDataException, NumberFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DataObject> T add(T x) throws ParseDataException,
			NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz)
			throws ParseDataException, NumberFormatException {
		return add(x, clazz, nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz, String field)
			throws ParseDataException, NumberFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] addArray(boolean[] x) throws NumberFormatException,
			ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public boolean[] addArray(boolean[] x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] addArray(int[] x) throws NumberFormatException,
			ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public int[] addArray(int[] x, String field) throws NumberFormatException,
			ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] addArray(String[] x) throws NumberFormatException,
			ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public String[] addArray(String[] x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[][] add2DArray(int[][] x) throws NumberFormatException,
			ParseDataException {
		return add2DArray(x, nextField());
	}

	@Override
	public int[][] add2DArray(int[][] x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DataObject> T[] addArray(T[] x)
			throws NumberFormatException, ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public <T extends DataObject> T[] addArray(T[] x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x)
			throws NumberFormatException, ParseDataException {
		return addList(x, nextField());
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x,
			Class<? extends T> clazz) throws NumberFormatException,
			ParseDataException {
		return addList(x, nextField());
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x,
			Class<? extends T> clazz, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> addIntList(List<Integer> x)
			throws NumberFormatException, ParseDataException {
		return addIntList(x, nextField());
	}

	@Override
	public List<Integer> addIntList(List<Integer> x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> addStringList(List<String> x)
			throws NumberFormatException, ParseDataException {
		return addStringList(x, nextField());
	}

	@Override
	public List<String> addStringList(List<String> x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x)
			throws NumberFormatException, ParseDataException {
		return addBooleanList(x, nextField());
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x, String field)
			throws NumberFormatException, ParseDataException {
		// TODO Auto-generated method stub
		return null;
	}

}
