package edu.elon.honors.price.data;

import java.util.List;

import edu.elon.honors.price.data.DataObject.Constructor;

public class FieldData extends PersistData {
	
	private static FieldData leftSide = new FieldData(), rightSide = new FieldData();

	public static class ParseDataException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public ParseDataException(String string) {
			super(string);
		}
		
		public ParseDataException() {
			super();
		}
	}
	
	public static int hashCode(DataObject object) {
		leftSide.dataObject = object;
		return leftSide.hashCode();
	}

	public static boolean equals(DataObject data1, DataObject data2) {
		if (data1 == data2) return true;
		if (data1 == null || data2 == null) return false;
		if (data1.getClass() != data2.getClass()) return false;
		leftSide.dataObject = data1;
		rightSide.dataObject = data2;
		return leftSide.equals(data2);
	}
	
	/** 
	 * Persists the given objects as a String in {@link Storage}, under
	 * the given identifier tag.
	 */
	public static void persist(DataObject persistable, String tag) {
		FieldData data = new FieldData();
		data.dataObject = persistable;
		data.writeMode = true;
		try {
			persistable.addFields(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Fetches the given object from {@link Storage}, using
	 * the given identifier tag. Returns null if fetching fails.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fetch(Class<T> clazz, String tag) {
		try {
			DataObject obj = Constructor.construct(clazz.getName());
			FieldData data = new FieldData();
			data.dataObject = obj;
			data.writeMode = false;
			obj.addFields(data);
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}


	@Override
	protected void addFields(DataObject dataObject) {
		dataObject.addFields(this);
	}
	
	public int addField(int x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public long addField(long x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public short addField(short x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public float addField(float x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public double addField(double x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public byte addField(byte x) throws ParseDataException, NumberFormatException {
		addHash(x);
		return persist(x);
	}
	
	public char addField(char x) throws ParseDataException { 
		addHash(x);
		return persist(x);
	}
	
	public boolean addField(boolean x) throws ParseDataException {
		addHash(x);
		return persist(x);
	}
	
	public String addField(String x) throws ParseDataException {
		addHash(x);
		return persist(x);
	}
	
	// Arrays are stored int the format "length \n 1|2|3|4"
	public int[] addArray(int[] x) throws NumberFormatException, ParseDataException {
		addHash(x);
		return persistArray(x);
	}
	
	public <T extends DataObject> List<T> addList(List<T> x) throws NumberFormatException, ParseDataException {
		addHash(x);
		return persistList(x);
	}
	
	public <T extends DataObject> List<T> addList(List<T> x, Class<? extends T> clazz) throws NumberFormatException, ParseDataException {
		addHash(x);
		return persistList(x, clazz);
	}
	
	public List<Integer> addIntList(List<Integer> x) throws NumberFormatException, ParseDataException  {
		addHash(x);
		return persistIntList(x);
	}
}
