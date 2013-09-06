package edu.elon.honors.price.data;

public class Tileset extends GameData {
	private static final long serialVersionUID = 2L;

	public int tileWidth, tileHeight, tileSpacing, rows, columns;
	public String name, bitmapName;
	
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
