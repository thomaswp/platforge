package com.platforge.player.core.graphics;

import com.platforge.player.core.game.Cache;
import com.platforge.player.core.game.Rect;

import playn.core.Image;

public class Tilemap {
	private Image[] tiles;
	private int[][] map;
	private Rect displayRect;
	private Viewport viewport;
	private ImageSprite[][] sprites;
	private int rows, columns, tileWidth, tileHeight;
	private float scrollX, scrollY;
	
	public float getScrollX() {
		return scrollX;
	}

	public void setScrollX(float scrollX) {
		scroll(scrollX - this.scrollX, 0);
	}

	public float getScrollY() {
		return scrollY;
	}

	public void setScrollY(float scrollY) {
		scroll(0, scrollY - this.scrollY);
	}

	public Rect getDisplayRect() {
		return displayRect;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
	
	public int getColor() {
		return viewport.getColor();
	}

	public void setColor(int color) {
		viewport.setColor(color);
	}

	public float getOpacity() {
		return viewport.getOpacity();
	}

	public void setOpacity(float opacity) {
		viewport.setOpacity(opacity);
	}

	public ImageSprite[][] getSprites() {
		return sprites;
	}
	
	public boolean isVisible() {
		return viewport.isVisible();
	}
	
	public void setVisible(boolean visible) {
		viewport.setVisible(visible);
	}
	
	public Viewport getViewport() {
		return viewport;
	}
	
	public int getWidth() {
		return tileWidth * columns;
	}
	
	public int getHeight() {
		return tileHeight * rows;
	}
	
	public void setMap(int[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (this.map[i][j] != map[i][j]) {
					this.map[i][j] = map[i][j];
					if (map[i][j] > 0) {
						if (sprites[i][j] == null) {
							int id = map[i][j];
							ImageSprite s = new ImageSprite(viewport, tiles[id]);
							s.setX(j * tileWidth);
							s.setY(i * tileHeight);
							s.setOriginX(scrollX);
							s.setOriginY(scrollY);
							sprites[i][j] = s;
						} else {
							sprites[i][j].setBitmap(tiles[map[i][j]]);
						}
					} else {
						if (sprites[i][j] != null) {
							sprites[i][j].dispose();
							sprites[i][j] = null;
						}
					}
				}
			}
		}
	}

	public Tilemap(Image tilesBitmap, int tileWidth, int tileHeight, int tileSpacing, 
			int[][] map, Rect displayRect, int z) {
		this.tiles = createTiles(tilesBitmap, tileWidth, tileHeight, tileSpacing);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.displayRect = displayRect;
		this.viewport = new Viewport(displayRect.minX(), displayRect.minY(), displayRect.width(), displayRect.height());
		this.viewport.setZ(z);
		this.rows = map.length;
		this.columns = map[0].length;
		this.map = new int[rows][columns];
		this.sprites = new ImageSprite[rows][columns];
		setMap(map);
	}
	
	public void scroll(float x, float y) {
		boolean updateVisible = (int)(scrollX / tileWidth) != (int)((scrollX + x) / tileWidth) ||
			(int)(scrollY / tileHeight) != (int)((scrollY + y) / tileHeight) ||
			(int)((scrollX + viewport.getWidth()) / tileWidth) != (int)((scrollX + viewport.getWidth() + x) / tileWidth) ||
			(int)((scrollY + viewport.getHeight()) / tileHeight) != (int)((scrollY + viewport.getHeight() + y) / tileHeight);
		scrollX += x;
		scrollY += y;
		//if (grid != null) grid.scroll(x, y);
		updateScroll(updateVisible);
	}
	
	public static int[][] readMap(String csv) {
		String[] lines = csv.split("\n");
		int[][] map = new int[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.endsWith(",")) line += "0";
			String[] values = line.split(",");
			map[i] = new int[values.length];
			for (int j = 0; j < values.length; j++) {
				if (values[j].length() > 0)
					map[i][j] = Integer.parseInt(values[j]);
				else
					map[i][j] = 0;
			}
		}
		return map;
	}
	
	private void updateScroll(boolean updateVisible) {
		for (int i = 0; i < sprites.length; i++) {
			for (int j = 0; j < sprites[i].length; j++) {
				ImageSprite sprite = sprites[i][j];
				if (sprite != null) {
					sprite.setOriginX(scrollX);
					sprite.setOriginY(scrollY);
					if (updateVisible)
						sprite.setVisible(viewport.isSpriteInBounds(sprite));
				}
			}
		}
	}
	
	public static Image[] createTiles(Image tilesBitmap, int tileWidth, int tileHeight, int tileSpacing) {
		if (((int)tilesBitmap.width() + tileSpacing) % (tileWidth + tileSpacing) != 0) {
			throw new RuntimeException("Impropper tile width: " + tileWidth + "x + " + tileSpacing + " != " + (int)tilesBitmap.width());
		}
		if (((int)tilesBitmap.height() + tileSpacing) % (tileHeight + tileSpacing) != 0) {
			throw new RuntimeException("Impropper tile height" + tileHeight + "x + " + tileSpacing + " != " + (int)tilesBitmap.height());
		}
		
		int rowTiles = ((int)tilesBitmap.width() + tileSpacing) / (tileWidth + tileSpacing);
		int columnTiles = ((int)tilesBitmap.height() + tileSpacing) / (tileHeight + tileSpacing);
		
		Image[] tiles = new Image[rowTiles * columnTiles];
		
		int index = 0;
		for (int j = 0; j < tilesBitmap.height(); j += tileHeight + tileSpacing) {
			for (int i = 0; i < tilesBitmap.width(); i += tileWidth + tileSpacing) {
				String cacheName = tilesBitmap.hashCode() + ":" + i + "x" + j;
				if (Cache.isBitmapRegistered(cacheName)) {
					tiles[index++] = Cache.getRegisteredBitmap(cacheName);
				}
				else {
					Image bmp = tilesBitmap.subImage(i, j, tileWidth, tileHeight);
					Cache.RegisterBitmap(cacheName, bmp);
					tiles[index++] = bmp;
				}
			}
		}
		return tiles;
	}
	
	
//	private void createGrid() {
//		if (Cache.isBitmapRegistered(getGridId())) {
//			gridBitmap = Cache.getRegisteredBitmap(getGridId());
//		} else {
//			gridBitmap = Bitmap.createBitmap(tileWidth, tileHeight, Sprite.defaultConfig);
//			Canvas c = new Canvas();
//			Paint p = new Paint();
//			p.setColor(Color.argb(200, 200, 200, 200));
//			p.setStyle(Style.STROKE);
//			c.setBitmap(gridBitmap);
//			c.drawRect(0, 0, tileWidth, tileHeight, p);
//			c.drawRect(1, 1, tileWidth - 1, tileHeight - 1, p);
//			Cache.RegisterBitmap(getGridId(), gridBitmap);
//		}
//		
//		//grid = new BackgroundSprite(gridBitmap, viewport);
//		grid = new BackgroundSprite(gridBitmap, viewport.getRect(), viewport.getZ());
//		grid.setZ(10);
//	}
	
//	private int getGridId() {
//		return this.getClass().hashCode() + tileWidth * (tileHeight + 5);
//	}
}
