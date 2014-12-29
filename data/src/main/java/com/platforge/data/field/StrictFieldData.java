package com.platforge.data.field;

import java.util.List;

import com.platforge.data.field.FieldData.ParseDataException;

public interface StrictFieldData {
	
	public boolean writeMode();
	
	public boolean readMode();
	
	public int add(int x, String field) throws ParseDataException, NumberFormatException;
	
	public long add(long x, String field) throws ParseDataException, NumberFormatException;
	
	public short add(short x, String field) throws ParseDataException, NumberFormatException;
	
	public float add(float x, String field) throws ParseDataException, NumberFormatException;
	
	public double add(double x, String field) throws ParseDataException, NumberFormatException;
	
	public byte add(byte x, String field) throws ParseDataException, NumberFormatException;
	
	public char add(char x, String field) throws ParseDataException;
	
	public boolean add(boolean x, String field) throws ParseDataException;
	
	public String add(String x, String field) throws ParseDataException;
	
	public <T extends DataObject> T add(T x, Class<? extends T> clazz, String field) throws ParseDataException, NumberFormatException;
	
	public <T extends DataObject> T add(T x, String clazz, String field) throws ParseDataException, NumberFormatException;
	
	public boolean[] addArray(boolean[] x, String field) throws NumberFormatException, ParseDataException;
	
	public int[] addArray(int[] x, String field) throws NumberFormatException, ParseDataException;
	
	public String[] addArray(String[] x, String field) throws NumberFormatException, ParseDataException;
	
	public int[][] add2DArray(int[][] x, String field) throws NumberFormatException, ParseDataException;
	
	public <T extends DataObject> T[] addArray(T[] x, String field) throws NumberFormatException, ParseDataException;
	
	public <T extends DataObject, L extends List<T>> L addList(L x, String field) 
			throws NumberFormatException, ParseDataException;
	
	public <T extends DataObject, L extends List<T>> L addList(L x, Class<? extends T> clazz, String field) 
			throws NumberFormatException, ParseDataException;
	
	public List<Integer> addIntList(List<Integer> x, String field) throws NumberFormatException, ParseDataException;

	public List<String> addStringList(List<String> x, String field) throws NumberFormatException, ParseDataException;
	
	public List<Boolean> addBooleanList(List<Boolean> x, String field) throws NumberFormatException, ParseDataException;
}