package com.platforge.data;

import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.data.field.StrictFieldData;

public class MapLayer extends GameData {
	private static final long serialVersionUID = 2L;
	
	public String name;
	public int rows, columns;
	public int[][] tiles;
	public boolean active;
	public int defaultValue;

	@Override
	public void addFields(StrictFieldData fields) throws ParseDataException,
			NumberFormatException {
		name = fields.add(name, "name");
		rows = fields.add(rows, "rows");
		columns = fields.add(columns, "columns");
		tiles = fields.add2DArray(tiles, "tiles");
		active = fields.add(active, "active");
		defaultValue = fields.add(defaultValue, "defaultValue");
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new MapLayer();
			}
		};
	}
	
	public MapLayer() { }
	
	public MapLayer(String name, int rows, int columns, boolean active,
			int defaultValue) {
		this.rows = rows;
		this.columns = columns;
		this.active = active;
		this.tiles = new int[rows][columns];
		this.defaultValue = defaultValue;
		
		if (defaultValue != 0) {
			setAll(defaultValue);
		}
	}

	public void setAll(int id) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tiles[i][j] = id;
			}
		}
	}
}
