package com.platforge.data.field;

import java.util.ArrayList;
import java.util.List;

import playn.core.Json;
import playn.core.Json.Array;
import playn.core.Json.Writer;
import playn.core.PlayN;
import playn.core.json.JsonImpl;

import com.platforge.data.field.DataObject.Constructor;

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
		write(data, data == null ? null : data.getClass().getName());
	}
	
	private void write(DataObject data, String className) throws NumberFormatException, ParseDataException {
		writer.object();
		writer.value(key("class"), className(className));
		data.addFields(this);
		writer.end();
	}

	public static DataObject fromJson(String json) {
		return fromJson(json);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends DataObject> T fromJson(String json, Class<T> clazz) {
		JsonSerializer serializer = new JsonSerializer();
		serializer.write = false;
		try {
			return (T) serializer.read(JsonSerializer.json.parse(json));
		} catch (Exception e) {
			return null;
		}
	}
	
	private DataObject read(Json.Object obj) throws ParseDataException {
		String className = nameClass(obj.getString("class"));
		if (className == null) return null;
		DataObject data = Constructor.construct(className);
		Json.Object stackHead = this.obj;
		this.obj = obj;
		data.addFields(this);
		this.obj = stackHead;
		return data;
		
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
	
	private String nameClass(String name) {
		return name;
	}
	
	@Override
	public boolean writeMode() {
		return write;
	}

	@Override
	public boolean readMode() {
		return !write;
	}

	private void checkMissingKey(String key) throws ParseDataException {
		if (!obj.containsKey(key)) throw new ParseDataException("Missing key " + key);
	}

	private void invalidArrayLength()
			throws ParseDataException {
		throw new ParseDataException("Array length mismatch");
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
			checkMissingKey(key);
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
		return add(x, x == null ? null : x.getClass().getName(), nextField());
	}

	@Override
	public <T extends DataObject> T add(T x, String clazz)
			throws ParseDataException, NumberFormatException {
		return add(x, clazz, nextField());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataObject> T add(T x, String clazz, String field)
			throws ParseDataException, NumberFormatException {
		String key = key(field);
		if (write) {
			writer.object(key);
			write(x, clazz);
			writer.end();
			return x;
		} else {
			checkMissingKey(key);
			return (T) read(obj.getObject(key));
		}
	}
	
	// Arrays are stored int the format "length{\n}1|2|3|4"
	private Object addArray(Object x, String field, ArrayIO io) throws NumberFormatException, ParseDataException {
		String key = key(field);
		if (write) {
			if (x == null) {
				writer.nul(key);
			} else {
				int length = io.length(x);
				writer.array(key);
				for (int i = 0; i < length; i++) writer.value(io.readObject(x, i));
				writer.end();
			}
		} else {
			Array a = obj.getArray(key);
			if (a == null) return null;
			int length = a.length();
			if (x == null || io.length(x) != length) {
				x = io.create(length);
			}
			for (int i = 0; i < length; i++) io.set(x, i, a.getObject(i).toString());
		}
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
		return (boolean[]) addArray(x, field, ArrayIO.booleanIO);
	}

	@Override
	public int[] addArray(int[] x) throws NumberFormatException,
			ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public int[] addArray(int[] x, String field) throws NumberFormatException,
			ParseDataException {
		return (int[]) addArray(x, field, ArrayIO.intIO);
	}

	@Override
	public String[] addArray(String[] x) throws NumberFormatException,
			ParseDataException {
		return addArray(x, nextField());
	}

	@Override
	public String[] addArray(String[] x, String field)
			throws NumberFormatException, ParseDataException {
		return (String[]) addArray(x, field, ArrayIO.stringIO);
	}

	@Override
	public int[][] add2DArray(int[][] x) throws NumberFormatException,
			ParseDataException {
		return add2DArray(x, nextField());
	}

	@Override
	public int[][] add2DArray(int[][] x, String field)
			throws NumberFormatException, ParseDataException {
		String key = key(field);
		if (write) {
			writer.array(key);
			for (int[] v : x) {
				writer.array();
				for (int vv : v) writer.value(vv);
				writer.end();
			}
			writer.end();
			return x;
		} else {
			checkMissingKey(key);
			Array a = obj.getArray(key);
			if (x == null || x.length != a.length()) x = new int[a.length()][];
			for (int i = 0; i < x.length; i++) {
				Array aa = a.getArray(i);
				if (x[i] == null || x[i].length != aa.length()) x[i] = new int[aa.length()];
				int[] xx = x[i];
				for (int j = 0; j < xx.length; j++) xx[j] = aa.getInt(j);
			}
			return x;
		}
	}

	@Override
	public <T extends DataObject> T[] addArray(T[] x)
			throws NumberFormatException, ParseDataException {
		return addArray(x, nextField());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataObject> T[] addArray(T[] x, String field)
			throws NumberFormatException, ParseDataException {
		String key = key(field);
		if (write) {
			writer.array(key);
			for (T v : x) write(v);
			writer.end();
			return x;
		} else {
			checkMissingKey(key);
			Array a = obj.getArray(key);
			if (x == null) throw new ParseDataException("Array cannot be null");
			if (x.length != a.length()) invalidArrayLength();
			for (int i = 0; i < x.length; i++) x[i] = (T) read(obj);
			return x;
		}
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x)
			throws NumberFormatException, ParseDataException {
		return addList(x, nextField());
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
		return addList(x, null, field);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x, String field)
			throws NumberFormatException, ParseDataException {
		String key = key(field);
		if (write) {
			writer.array(key);
			for (T v : x) write(v);
			writer.end();
			return x;
		} else {
			checkMissingKey(key);
			Array a = obj.getArray(key);
			if (x == null) throw new ParseDataException("List cannot be null");
			x.clear();
			int size = a.length();
			for (int i = 0; i < size; i++) x.add((T) read(obj));
			return x;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> addPrimitiveList(List<T> x, String field, ArrayIO io) throws NumberFormatException, ParseDataException  {
		if (write) {
			int length = x == null ? 0 : x.size();
			Object array = io.create(length);
			for (int i = 0; i < length; i++) {
				io.set(array, i, String.valueOf(x.get(i)));
			}
			addArray(array, field, io);
		} else {
			Object array = addArray(null, field, io);
			if (array == null) return null;
			if (x == null) {
				x = new ArrayList<T>();
			} else {
				x.clear();
			}
			int length = io.length(array);
			for (int i = 0; i < length; i++) {
				x.add((T) io.readObject(array, i));
			}
		}
		return x;
	} 

	@Override
	public List<Integer> addIntList(List<Integer> x)
			throws NumberFormatException, ParseDataException {
		return addIntList(x, nextField());
	}
	
	@Override
	public List<Integer> addIntList(List<Integer> x, String field)
			throws NumberFormatException, ParseDataException {
		return addPrimitiveList(x, field, ArrayIO.intIO);
	}
	

	@Override
	public List<String> addStringList(List<String> x)
			throws NumberFormatException, ParseDataException {
		return addStringList(x, nextField());
	}

	@Override
	public List<String> addStringList(List<String> x, String field)
			throws NumberFormatException, ParseDataException {
		return addPrimitiveList(x, field, ArrayIO.stringIO);
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x)
			throws NumberFormatException, ParseDataException {
		return addBooleanList(x, nextField());
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x, String field)
			throws NumberFormatException, ParseDataException {
		return addPrimitiveList(x, field, ArrayIO.booleanIO);
	}

}
