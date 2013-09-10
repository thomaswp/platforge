package edu.elon.honors.price.data.field;

import edu.elon.honors.price.data.field.FieldData.ParseDataException;

abstract class HashData {

	private final static int prime = 31;
	protected final DataObject dataObject;
	
	private int result = 1;

	protected boolean inHash;
	protected boolean inLeftEquals;
	protected boolean inRightEquals;

	// the last ___ added by the object
	private Object lastObject;
	private int lastInt;
	private long lastLong;
	private short lastShort;
	private double lastDouble;
	private float lastFloat;
	private byte lastByte;
	private char lastChar;
	private boolean lastBoolean;

	// used during equality comparison
	private boolean equalSoFar;
	// the index of the field we're comparing now
	private int fieldIndex;
	// the other HashCode we're comparing to
	private HashData compareTo;	
	// the field we are attempting to read from a hash code
	private int desiredFieldIndex;

	protected abstract void addFields(DataObject dataObject) 
			throws NumberFormatException, ParseDataException;
	
	public HashData(DataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	protected void reset() {
		lastObject = null;
		compareTo = null;
		inHash = false;
		inLeftEquals = false;
		inRightEquals = false;
	}

	@Override
	public int hashCode() {
		inHash = true;
		result = 1;
		try {
			addFields(dataObject);
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		inHash = false;
		return result;
	}
	
	protected interface ArrayHash {
		boolean areEqual(Object x1, Object x2, int index);
		int hash(Object x, int index);
		int length(Object x);
	}
	
	protected interface ArrayIO extends ArrayHash {
		String read(Object x, int index);
		void set(Object x, int index, String value);
		Object create(int length);
	}
	
	protected static ArrayIO intIO = new ArrayIO() {
		
		private int[] cast(Object x) {
			return (int[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = Integer.parseInt(value);
		}
		
		@Override
		public String read(Object x, int index) {
			return String.valueOf(cast(x)[index]);
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new int[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index] == cast(x2)[index];
		}

		@Override
		public int hash(Object x, int index) {
			return cast(x)[index];
		}
	};
	
	protected static ArrayIO booleanIO = new ArrayIO() {
		
		private boolean[] cast(Object x) {
			return (boolean[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = Boolean.parseBoolean(value);
		}
		
		@Override
		public String read(Object x, int index) {
			return String.valueOf(cast(x)[index]);
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new boolean[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index] == cast(x2)[index];
		}
		
		@Override
		public int hash(Object x, int index) {
			return cast(x)[index] ? 1231 : 1237;
		}
	};
	
	protected static ArrayIO stringIO = new ArrayIO() {
		
		private String[] cast(Object x) {
			return (String[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = value;
		}
		
		@Override
		public String read(Object x, int index) {
			return cast(x)[index];
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new String[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index].equals(cast(x2)[index]);
		}
		
		@Override
		public int hash(Object x, int index) {
			return cast(x)[index].hashCode();
		}
	};
	
	protected ArrayHash int2dIO = new ArrayHash() {
		
		private int[][] cast(Object x) {
			return (int[][]) x;
		}

		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return areArraysEqual(cast(x1)[index], cast(x2)[index], intIO);
		}
		
		@Override
		public int hash(Object x, int index) {
			return hashArray(cast(x)[index], intIO);
		}
	};

	// methods for adding a field for hashcode generation

	protected void addHash(Object o) {
		result = prime * result;
		if (o == null) return;
		
		int hash;
		if (o instanceof int[]) {
			hash = hashArray(o, intIO);
		} else if (o instanceof boolean[]) {
			hash = hashArray(o, booleanIO);
		} else if (o instanceof String[]) {
			hash = hashArray(o, stringIO);
		} else if (o instanceof int[][]) {
			hash = hashArray(o, int2dIO);
		} else {
			hash = o.hashCode();
		}
		result += hash;
	}
	
	protected <T extends DataObject> void addHash(T[] o) {
		result = prime * result;
		if (o == null) return;
		result += hashArray(o);
	}
	
	private static int hashArray(Object a, ArrayHash io) {
		int length = io.length(a);
		int result = 1;
		for (int i = 0; i < length; i++) {
			result = prime * result + io.hash(a, i);
		}
		return result;
	}
	
	private static <T extends DataObject> int hashArray(T[] a) {
		int length = a.length;
		int result = 1;
		for (int i = 0; i < length; i++) {
			result = prime * result + a[i].hashCode();
		}
		return result;
	}

	protected void addHash(int i) {
		result = prime * result + i;
	}

	protected void addHash(long l) {
		result = prime * result + (int) (l ^ (l >>> 32));
	}

	protected void addHash(short s) {
		result = prime * result + s;
	}

	protected void addHash(float f) {
		result = prime * result + Float.floatToIntBits(f);
	}

	protected void addHash(double d) {
		long temp;
		temp = Double.doubleToLongBits(d);
		result = prime * result + (int) (temp ^ (temp >>> 32));
	}

	protected void addHash(byte b) {
		result = prime * result + b;
	}

	protected void addHash(char c) {
		result = prime * result + c;
	}

	protected void addHash(boolean b) {
		result = prime * result + (b ? 1231 : 1237);
	}

	// methods for reading a field from this HashCode's object
	// for equality comparison

	protected void addLeftEquals(Object o) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		boolean eq;
		Object o2 = compareTo.lastObject;
		if (o == null) {
			eq = o2 == null;
		} else {
			eq = o.equals(o2);
			if (!eq && o.getClass() == o2.getClass()) {
				if (o instanceof int[]) {
					eq = areArraysEqual(o, o2, intIO);
				} else if (o instanceof boolean[]) {
					eq = areArraysEqual(o, o2, booleanIO);
				} else if (o instanceof String[]) {
					eq = areArraysEqual(o, o2, stringIO);
				} else if (o instanceof int[][]) {
					eq = areArraysEqual(o, o2, int2dIO);
				}
			}
		}
		if (!eq) equalSoFar = false;
		fieldIndex++;
	}

	@SuppressWarnings("unchecked")
	protected <T extends DataObject> void addLeftEquals(T[] o) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		boolean eq;
		T[] o2 = (T[]) compareTo.lastObject;
		if (o == null) {
			eq = o2 == null;
		} else {
			eq = areArraysEqual(o, o2);
		}
		if (!eq) equalSoFar = false;
		fieldIndex++;
	}
	
	private static boolean areArraysEqual(Object a1, Object a2, ArrayHash io) {
		int length1 = io.length(a1);
		int length2 = io.length(a2);
		if (length1 != length2) return false;
		for (int i = 0; i < length1; i++) {
			if (!io.areEqual(a1, a2, i)) return false;
		}
		return true;
	}
	
	private static <T extends DataObject>  boolean areArraysEqual(T[] a1, T[] a2) {
		if (a1.length != a2.length) return false;
		for (int i = 0; i < a1.length; i++) {
			if (!a1[i].equals(a2[i])) return false;
		}
		return true;
	}

	protected void addLeftEquals(int i) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (i != compareTo.lastInt) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(long l) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (l != compareTo.lastLong) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(short s) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (s != compareTo.lastShort) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(float f) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (f != compareTo.lastFloat) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(double d) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (d != compareTo.lastDouble) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(byte b) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (b != compareTo.lastByte) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(char c) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (c != compareTo.lastChar) equalSoFar = false;
		fieldIndex++;
	}

	protected void addLeftEquals(boolean b) {
		if (!equalSoFar) return;
		compareTo.populateField(fieldIndex);
		if (b != compareTo.lastBoolean) equalSoFar = false;
		fieldIndex++;
	}

	// methods for reading a field from another HashCode

	protected void addRightEquals(Object o) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastObject = o;
	}

	protected void addRightEquals(int i) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastInt = i;
	}

	protected void addRightEquals(long l) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastLong = l;
	}

	protected void addRightEquals(short s) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastShort = s;
	}

	protected void addRightEquals(float f) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastFloat = f;
	}

	protected void addRightEquals(double d) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastDouble = d;
	}

	protected void addRightEquals(byte b) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastByte = b;
	}

	protected void addRightEquals(char c) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastChar = c;
	}

	protected void addRightEquals(boolean b) {
		if (desiredFieldIndex >= 0 && fieldIndex++ != desiredFieldIndex) return;
		lastBoolean = b;
	}
	
	/** Registers this value for hashing and equality checks */
	public void addHashField(Object o) {
		if (inHash) addHash(o);
		if (inLeftEquals) addLeftEquals(o);
		if (inRightEquals) addRightEquals(o);
	}
	
	/** Registers this value for hashing and equality checks */
	public <T extends DataObject> void addHashField(T[] o) {
		if (inHash) addHash(o);
		if (inLeftEquals) addLeftEquals(o);
		if (inRightEquals) addRightEquals(o);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(int i) {
		if (inHash) addHash(i);
		if (inLeftEquals) addLeftEquals(i);
		if (inRightEquals) addRightEquals(i);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(long l) {
		if (inHash) addHash(l);
		if (inLeftEquals) addLeftEquals(l);
		if (inRightEquals) addRightEquals(l);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(short s) {
		if (inHash) addHash(s);
		if (inLeftEquals) addLeftEquals(s);
		if (inRightEquals) addRightEquals(s);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(float f) {
		if (inHash) addHash(f);
		if (inLeftEquals) addLeftEquals(f);
		if (inRightEquals) addRightEquals(f);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(double d) {
		if (inHash) addHash(d);
		if (inLeftEquals) addLeftEquals(d);
		if (inRightEquals) addRightEquals(d);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(byte b) {
		if (inHash) addHash(b);
		if (inLeftEquals) addLeftEquals(b);
		if (inRightEquals) addRightEquals(b);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(char c) {
		if (inHash) addHash(c);
		if (inLeftEquals) addLeftEquals(c);
		if (inRightEquals) addRightEquals(c);
	}

	/** Registers this value for hashing and equality checks */
	public void addHashField(boolean b) {
		if (inHash) addHash(b);
		if (inLeftEquals) addLeftEquals(b);
		if (inRightEquals) addRightEquals(b);
	}
	
	
	// Iterates through the other hashable's fields
	// until it reads the field with the desired index.
	// The desired field, therefore, will be stored in one
	// of the "lastXXX" fields of this class
	protected void populateField(int index) {
		inRightEquals = true;
		desiredFieldIndex = index;
		fieldIndex = 0;
		try {
			addFields(dataObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inRightEquals = false;
	}

	/** 
	 * Compares this HashCode to another, based on the fields added by
	 * its Hashable
	 */
	public boolean equals(FieldData hash) {
		//TODO: support inheritance.. maybe?

		// check for obvious incompatibility
		if (hash == null) return false;
		DataObject hashable = hash.dataObject;
		if (this.dataObject == hashable) return true;
		if (this.dataObject == null || hashable == null) return false;
		if (this.dataObject.getClass() != hashable.getClass()) return false;

		// by definition, if the hashcodes aren't equal, nor are the fields 
		if (hash.hashCode() != hashCode()) return false;

		// The process works by having our Hashable
		// add each of its fields. After every one,
		// we have the other Hashable add its fields
		// but we only store the one that matches the field
		// out Hashable just added. Then we compare them and repeat
		// until we find an inequality or all the fields have been added.

		equalSoFar = true;
		inLeftEquals = true;
		compareTo = hash;
		desiredFieldIndex = -1;
		fieldIndex = 0;
		try {
			addFields(dataObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inLeftEquals = false;
		return equalSoFar;
	}
}
