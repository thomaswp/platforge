package com.platforge.data;

import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.field.StrictFieldData;

public class Tileset extends GameData {
	private static final long serialVersionUID = 2L;

	public int tileWidth, tileHeight, tileSpacing, rows, columns;
	public String name, bitmapName;
	
	@Override
	public void addFields(StrictFieldData fields) throws ParseDataException,
			NumberFormatException {
		tileWidth = fields.add(tileWidth, "tileWidth");
		tileHeight = fields.add(tileHeight, "tileHeight");
		tileSpacing = fields.add(tileSpacing, "tileSpacing");
		rows = fields.add(rows, "rows");
		columns = fields.add(columns, "columns");
		name = fields.add(name, "name");
		bitmapName = fields.add(bitmapName, "bitmapName");
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new Tileset();
			}
		};
	}
	
	private Tileset() { }
	
	public Tileset(String name, String bitmapName, 
			int tileWidth, int tileHeight, int rows, int columns) {
		this.name = name;
		this.bitmapName = bitmapName;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.rows = rows;
		this.columns = columns;
	}
	
	public int width() {
		return tileWidth * columns;
	}
	
	public int height() {
		return tileHeight * rows;
	}
}
