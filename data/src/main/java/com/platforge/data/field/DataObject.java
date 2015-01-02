package com.platforge.data.field;

import java.util.HashMap;

import com.platforge.data.field.FieldData.ParseDataException;

public interface DataObject {

	public static abstract class Constructor {
		private static HashMap<String, Constructor> constructorMap = 
				new HashMap<String, Constructor>();
		private static HashMap<String, String> classMap = 
				new HashMap<String, String>();
		
		
		public abstract DataObject construct();
		
		public static void register(Class<?> clazz, Constructor constructor) {
			register(clazz, clazz.getSimpleName(), constructor);
		}
		
		public static void register(Class<?> clazz, String key, Constructor constructor) {
			constructorMap.put(key, constructor);
			String className = clazz.getName();
			if (!constructorMap.containsKey(className)) {
				constructorMap.put(className, constructor);
			}
			classMap.put(className, key);
		}
		
		public static DataObject construct(String key) throws ParseDataException {
			Constructor constructor = constructorMap.get(key);
			if (constructor == null) throw new ParseDataException("No constructor for type: " + key);
			return constructor.construct();
		}
		
		public static String className(Class<?> clazz) throws ParseDataException {
			if (clazz == null) return "null";
			return className(clazz.getName());
		}
		
		public static String className(String clazz) throws ParseDataException {
			if (clazz == null) return "null";
			String name = classMap.get(clazz);
			if (name == null) return clazz;
			return name;
		}
	}
	
	void addFields(FieldData fields) throws ParseDataException, NumberFormatException;
}
