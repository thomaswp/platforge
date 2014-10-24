package edu.elon.honors.price.data;

import java.io.Serializable;

import edu.elon.honors.price.data.field.DataObject;
import edu.elon.honors.price.data.field.EqualsData;
import edu.elon.honors.price.data.field.HashData;

public abstract class GameData implements Serializable, DataObject {
	private static final long serialVersionUID = 1L;
	
//	private static Class<?>[] shallow = {Integer.class, Long.class, Short.class, Double.class, 
//		Float.class, Boolean.class, Byte.class, Character.class, String.class};

	public transient EqualsData equalsData = new EqualsData(this);
	public transient HashData hashData = new HashData(this);
	
	@Override
	public boolean equals(Object data) {
		if (this == data) return true;
		if (data == null) return false;
		if (getClass() != data.getClass()) return false;
		if (equalsData == null) equalsData = new EqualsData(this);
		return equalsData.equals(((GameData) data).equalsData);
	}
	
	@Override
	public int hashCode() {
		if (hashData == null) hashData = new HashData(this);
		return hashData.hashCode();
	}
	
	public static boolean areEqual(GameData o1, GameData o2) {	
		
		if (o1 == null || o2 == null)
			return (o1 == o2);

		if (o1.getClass() != o2.getClass())
			return false;
		
		return o1.equals(o2);
		
//		
//		//Debug.write("Comparing %s's", c.toString());
//		
//		for (int i = 0; i < shallow.length; i++) {
//			if (shallow[i].equals(c)) {
//				if (o1.equals(o2)) {
//					return true;
//				} else {
//					return false;
//				}
//			}
//		}
//
//		if (c.isEnum()) {
//			return o1.equals(o2);
//		}
//		
//		if (c.isArray()) {
//			if (Array.getLength(o1) != Array.getLength(o2))
//				return false;
//
//			String className = c.toString();
//			int index = className.lastIndexOf("[");
//			boolean shallowArray = className.length() > 1 && index == 0 && 
//			className.charAt(1) != 'L';
//			shallowArray = false;
//
//			for (int i = 0; i < Array.getLength(o1); i++) {
//				if (shallowArray) {
//					if (Array.get(o1, i).equals(Array.get(o2, i)))
//						return false;
//				} else {
//					if (!areEqual(Array.get(o1, i), Array.get(o2, i)))
//						return false;
//				}
//			}
//		}
//
//
//		if (o1 instanceof List) {
//			List<?> a1 = (List<?>)o1;
//			List<?> a2 = (List<?>)o2;
//			if (a1.size() != a2.size()) return false;
//			for (int i = 0; i < a1.size(); i++) {
//				if (!areEqual(a1.get(i), a2.get(i)))
//					return false;
//			}
//		}
//
//		while (c != null) {
//			for (Field field : c.getDeclaredFields()) {
//				//Debug.write("Field:" + field.getName());
//				int mods = field.getModifiers();
//				if (Modifier.isFinal(mods) || Modifier.isStatic(mods) ||
//						Modifier.isTransient(mods)) {
//					continue;
//				}
//				field.setAccessible(true);
//				try {
//					if (field.getType().isPrimitive()) {
//						if (!field.get(o1).equals(field.get(o2)))
//							return false;
//						else {
////							Debug.write("%s: %o vs %o", field.getName(),
////									field.get(o1), field.get(o2));
//						}
//					} else {
//						if (!areEqual(field.get(o1), field.get(o2)))
//							return false;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			c = c.getSuperclass();
//		}
//
//		return true;
	}
}
