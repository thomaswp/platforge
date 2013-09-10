package edu.elon.honors.price.data.field;

import java.util.List;

import edu.elon.honors.price.data.field.DataObject.Constructor;

public class FieldData extends PersistData {
	
	public FieldData(DataObject dataObject) {
		super(dataObject);
	}
	
	public static class ParseDataException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public ParseDataException(String string) {
			super(string);
		}
		
		public ParseDataException() {
			super();
		}
	}
	
	public static String persistData(DataObject persistable) {
		FieldData data = new FieldData(persistable);
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
			FieldData fd = new FieldData(obj);
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


	@Override
	protected void addFields(DataObject dataObject) throws NumberFormatException, ParseDataException {
		dataObject.addFields(this);
	}
	
	public int add(int x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public long add(long x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public short add(short x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public float add(float x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public double add(double x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public byte add(byte x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public char add(char x) throws ParseDataException { 
		addHashField(x);
		return persist(x);
	}
	
	public boolean add(boolean x) throws ParseDataException {
		addHashField(x);
		return persist(x);
	}
	
	public String add(String x) throws ParseDataException {
		addHashField(x);
		return persist(x);
	}
	
	public <T extends DataObject> T add(T x, Class<? extends T> clazz) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x, clazz);
	}
	
	public <T extends DataObject> T add(T x) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	public <T extends DataObject> T add(T x, String clazz) throws ParseDataException, NumberFormatException {
		addHashField(x);
		return persist(x);
	}
	
	// Arrays are stored int the format "length \n 1|2|3|4"	
	public boolean[] addArray(boolean[] x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistArray(x);
	}
	
	public int[] addArray(int[] x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistArray(x);
	}
	
	public String[] addArray(String[] x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistArray(x);
	}
	
	public int[][] add2DArray(int[][] x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persist2DArray(x);
	}
	
	public <T extends DataObject> T[] addArray(T[] x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistArray(x);
	}
	
	public <T extends DataObject, L extends List<T>> L addList(L x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistList(x);
	}
	
	/** Persists the list, using the provided class for reconstruction. See {@link Data#persist(Persistable, Class)} */
	public <T extends DataObject, L extends List<T>> L addList(L x, Class<? extends T> clazz) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistList(x);
	}
	
	public List<Integer> addIntList(List<Integer> x) throws NumberFormatException, ParseDataException  {
		addHashField(x);
		return persistIntList(x);
	}

	public List<String> addStringList(List<String> x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistStringList(x);
	}
	
	public List<Boolean> addBooleanList(List<Boolean> x) throws NumberFormatException, ParseDataException {
		addHashField(x);
		return persistBooleanList(x);
	}
}
