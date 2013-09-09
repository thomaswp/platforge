package edu.elon.honors.price.data;

public interface DataObject {

	public abstract class Constructor {
		public abstract DataObject construct();
		
		static DataObject construct(String type) {
			return null;
		}
	}
	
	void addFields(FieldData fields);
}
