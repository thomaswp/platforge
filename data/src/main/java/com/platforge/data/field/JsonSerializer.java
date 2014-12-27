package com.platforge.data.field;

import java.util.List;

import com.platforge.data.field.DataObject.Constructor;

import playn.core.Json;
import playn.core.Json.Writer;
import playn.core.json.JsonImpl;
import playn.core.PlayN;

public class JsonSerializer implements FieldData {

	private static Json json;
	static {
		json = PlayN.platform() == null ? new JsonImpl() : PlayN.json();
	}
	
	private Writer writer;
	private Json.Object obj;
	private boolean write;
	
	public static String toJson(DataObject data) {
		JsonSerializer serializer = new JsonSerializer();
		serializer.writer = json.newWriter();
		serializer.write = true;
		try {
			serializer.write(data);
			return serializer.writer.write();
		} catch (Exception e) {
			return null;
		}
	}
	
	private void write(DataObject data) throws NumberFormatException, ParseDataException {
		write(data, data.getClass().getName());
	}
	
	private void write(DataObject data, String className) throws NumberFormatException, ParseDataException {
		writer.object();
		writer.value(key("class"), className(className));
		data.addFields(this);
		writer.end();
	}

	@SuppressWarnings("unchecked")
	public static <T extends DataObject> T fromJson(String json, Class<T> clazz) {
		JsonSerializer serializer = new JsonSerializer();
		serializer.write = false;
		try {
			DataObject obj = Constructor.construct(clazz.getName());
			serializer.obj = JsonSerializer.json.parse(json);
			if (serializer.obj == null) return null;
			obj.addFields(serializer);
			return (T) obj;
		} catch (Exception e) {
			return null;
		}
	}
	
	private String nextField() {
		return null;
	}
	
	private String key(String key) {
		return key;
	}
	
	private String className(String clazz) {
		return clazz;
	}
	
	@Override
	public boolean writeMode() {
		return write;
	}

	@Override
	public boolean readMode() {
		return !write;
	}

	private void missingKey(String key) throws ParseDataException {
		throw new ParseDataException("Missing key " + key);
	}

	@Override
	public int add(int x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public int add(int x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return obj.getInt(key);
		}
	}

	@Override
	public long add(long x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public long add(long x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return obj.getLong(key);
		}
	}

	@Override
	public short add(short x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public short add(short x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return (short) obj.getInt(key);
		}
	}

	@Override
	public float add(float x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public float add(float x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return (float) obj.getDouble(key);
		}
	}

	@Override
	public double add(double x) throws ParseDataException,
			NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public double add(double x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return obj.getDouble(key);
		}
	}

	@Override
	public byte add(byte x) throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public byte add(byte x, String field) throws ParseDataException,
			NumberFormatException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return (byte) obj.getInt(key);
		}
	}

	@Override
	public char add(char x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public char add(char x, String field) throws ParseDataException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			String s = obj.getString(key);
			if (s.length() != 1) throw new ParseDataException("Char must be saved as string of length 1");
			return s.charAt(0);
		}
	}

	@Override
	public boolean add(boolean x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public boolean add(boolean x, String field) throws ParseDataException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return obj.getBoolean(key);
		}
	}

	@Override
	public String add(String x) throws ParseDataException {
		return add(x, nextField());
	}

	@Override
	public String add(String x, String field) throws ParseDataException {
		String key = key(field);
		if (write) {
			writer.value(key, x);
			return x;
		} else {
			if (!obj.containsKey(key)) missingKey(key); 
			return obj.getString(key);
		}
	}

	@Override
	public <T extends DataObject> T add(T x, Class<? extends T> clazz)
			throws ParseDataException, NumberFormatException {
		return add(x, nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, Class<? extends T> clazz,
			String field) throws ParseDataException, NumberFormatException {
		return add(x, clazz.getName(), field);
	}

	@Override
	public <T extends DataObject> T add(T x) throws ParseDataException,
			NumberFormatException {
		return add(x, x.getClass().getName(), nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz)
			throws ParseDataException, NumberFormatException {
		return add(x, clazz, nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz, String field)
			throws ParseDataException, NumberFormatException {
		writer.object(key(field));
		write(x, clazz);
		writer.end();
		return x;
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
