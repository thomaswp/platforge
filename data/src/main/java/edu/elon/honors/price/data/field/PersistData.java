package edu.elon.honors.price.data.field;

import java.util.ArrayList;
import java.util.List;

import edu.elon.honors.price.data.field.DataObject.Constructor;

public class PersistData implements FieldData {

	private final static String NULL = "<<null>>";
	private final static String DIV = "\0";
	private final static String LINE = "\3";
	
	protected boolean writeMode;
	protected boolean dataMode;
	
	public static String persistData(DataObject persistable) {
		PersistData data = new PersistData();
		data.writeMode = true;
		data.dataMode = true;
		try {
			persistable.addFields(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.buffer.toString();
	}

	/** 
	 * Fetches the given object from {@link Storage}, using
	 * the given identifier tag. Returns null if fetching fails.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DataObject> T readData(Class<T> clazz, String data) {
		try {
			DataObject obj = Constructor.construct(clazz.getName());
			PersistData fd = new PersistData();
			fd.writeMode = false;
			fd.dataMode = true;
			fd.load(data);
			obj.addFields(fd);
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean writeMode() {
		return writeMode;
	}

	public boolean readMode() {
		return !writeMode;
	}
	
	private PersistData() { }
	
	private void addFields(DataObject x) throws NumberFormatException, ParseDataException {
		x.addFields(this);
	}
	
	public int add(int x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			x = Integer.parseInt(read());
		}
		return x;
	}
	
	public long add(long x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			x = Integer.parseInt(read());
		}
		return x;
	}
	
	public short add(short x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));	
		} else {
			x = Short.parseShort(read());
		}
		return x;
	}
	
	public float add(float x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			x = Float.parseFloat(read());
		}
		return x;
	}
	
	public double add(double x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			x = Double.parseDouble(read());
		}
		return x;
	}
	
	public byte add(byte x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			x = Byte.parseByte(read());
		}
		return x;
	}
	
	public char add(char x) throws ParseDataException { 
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			String r = read();
			if (r == null || r.length() < 1) throw new ParseDataException();
			x = r.charAt(0);
		}
		return x;
	}
	
	public boolean add(boolean x) throws ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			write(String.valueOf(x));
		} else {
			Boolean b = Boolean.parseBoolean(read());
			if (b == null) throw new ParseDataException();
			x = b;
		}
		return x;
	}
	
	public String add(String x) throws ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			write(x == null ? NULL : x);
		} else {
			x = read();
			if (x.equals(NULL)) x = null;
		}
		return x;
	}
	
	public <T extends DataObject> T add(T x) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		return add(x, (String) null);
	}
	
	public <T extends DataObject> T add(T x, Class<? extends T> clazz) throws ParseDataException, NumberFormatException {
		return add(x, clazz == null ? null : clazz.getName());
	}
	
	/** 
	 * Persists the given object, using the provided class to reconstruct the object. 
	 * This is useful if the item's class is an anonymous or derived class that is not
	 * registered with {@link PersistUtils}, but which does not itself have any important
	 * persistent fields.
	 */
	@SuppressWarnings("unchecked")
	public <T extends DataObject> T add(T x, String clazz) throws ParseDataException, NumberFormatException {
		if (!dataMode) return x;
		String type;
		if (writeMode) {
			type = x == null ? NULL : x.getClass().getName();
			if (clazz != null) type = clazz;
			add(type);
			if (x != null) addFields(x);
		} else {
			type = add((String) null);
			if (type.equals(NULL)) {
				x = null;
			} else {
				if (x != null) {
					if (!x.getClass().getName().equals(type)) throw new ParseDataException();
				} else {
					x = (T) Constructor.construct(type);
				}
				addFields(x);
			}
		}
		return x;
	}
	
	// Arrays are stored int the format "length{\n}1|2|3|4"
	private Object addArray(Object x, ArrayIO io) throws NumberFormatException, ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			int length = add(x == null ? 0 : io.length(x));
			if (x != null) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < length; i++) {
					if (sb.length() != 0) sb.append(DIV);
					sb.append(io.read(x, i));
				}
				if (length > 0) write(sb.toString());
			}
		} else {
			int length = add(0);
			if (length != 0) {
				if (x == null || io.length(x) != length) {
					x = io.create(length);
				}
				String[] data = read().split("\\" + DIV);
				int i = 0;
				for (String part : data) {
					io.set(x, i++, part);
				}
			} else {
				x = null;
			}
		}
		return x;
	}
	
	
	public int[] addArray(int[] x) throws NumberFormatException, ParseDataException {
		return (int[]) addArray(x, ArrayIO.intIO);
	}
	
	public boolean[] addArray(boolean[] x) throws NumberFormatException, ParseDataException {
		return (boolean[]) addArray(x, ArrayIO.booleanIO);
	}
	
	public String[] addArray(String[] x) throws NumberFormatException, ParseDataException {
		return (String[]) addArray(x, ArrayIO.stringIO);
	}
	
	public int[][] add2DArray(int[][] x) throws NumberFormatException, ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			int length = add(x == null ? 0 : x.length);
			if (x != null) {
				for (int i = 0; i < length; i++) {
					addArray(x[i]);
				}
			}
		} else {
			int length = add(0);
			if (length != 0) {
				if (x == null || x.length != length){
					x = new int[length][];
				}
				for (int i = 0; i < length; i++) {
					x[i] = addArray((int[]) null);
				}
			} else {
				x = null;
			}
		}
		return x;
	}
	
	public <T extends DataObject> T[] addArray(T[] x) throws NumberFormatException, ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			add(x == null ? -1 : x.length);
			if (x != null) {
				for (T t : x) {
					add(t);
				}
			}
		} else {
			int length = add(0);
			if (length >= 0) {
				if (x != null){
					if (x.length != length) throw new ParseDataException("Cannot read into a different sized array");
				} else {
					throw new ParseDataException("Cannot read into a null array");
				}
				for (int i = 0; i < length; i++) {
					x[i] = add((T) null);
				}
			} else {
				x = null;
			}
		}
		return x;
	}
	
	public <T extends DataObject, L extends List<T>> L addList(L x) throws NumberFormatException, ParseDataException {
		if (!dataMode) return x;
		return addList(x, null);
	}
	
	public <T extends DataObject, L extends List<T>> L addList(L x, Class<? extends T> clazz) throws NumberFormatException, ParseDataException {
		if (!dataMode) return x;
		if (writeMode) {
			add(x == null ? -1 : x.size());
			if (x != null) {
				for (T t : x) {
					add(t);
				}
			}
		} else {
			int length = add(0);
			if (length >= 0) {
				if (x != null){
					x.clear();
				} else {
					throw new ParseDataException("Cannot read into a null array");
				}
				for (int i = 0; i < length; i++) {
					x.add(add((T) null, clazz));
				}
			} else {
				x = null;
			}
		}
		return x;
	}
	
	public List<Integer> addIntList(List<Integer> x) throws NumberFormatException, ParseDataException  {
		return addPrimitiveList(x, ArrayIO.intIO);
	}
	
	public List<String> addStringList(List<String> x) throws NumberFormatException, ParseDataException  {
		return addPrimitiveList(x, ArrayIO.stringIO);
	}
	
	public List<Boolean> addBooleanList(List<Boolean> x) throws NumberFormatException, ParseDataException  {
		return addPrimitiveList(x, ArrayIO.booleanIO);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> addPrimitiveList(List<T> x, ArrayIO io) throws NumberFormatException, ParseDataException  {
		if (!dataMode) return x;
		if (writeMode) {
			int length = x == null ? 0 : x.size();
			Object array = io.create(length);
			for (int i = 0; i < length; i++) {
				io.set(array, i, String.valueOf(x.get(i)));
			}
			addArray(array, io);
		} else {
			Object array = addArray(null, io);
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
	
	protected StringBuffer buffer = new StringBuffer();
	protected String[] lines;
	protected int index = 0;
	
	protected void load(String text) {
		lines = text.split(LINE);
	}
	
	private String read() throws ParseDataException {
		if (index >= lines.length) throw new ParseDataException("Not enough data!");
		return lines[index++];
	}
	
	private void write(String data) {
		buffer.append(data);
		buffer.append(LINE);
	}
}
