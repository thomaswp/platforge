package com.platforge.player.core.game;

import pythagoras.i.Rectangle;

public class Rect extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public int left() {
		return x;
	}
	
	public int top() {
		return y;
	}
	
	public int right() {
		return maxX();
	}
	
	public int bottom() {
		return maxY();
	}
	
	public Rect() {
		
	}
	
	public Rect(int left, int top, int right, int bottom) {
		super();
		set(left, top, right, bottom);
	}
	
	public Rect set(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right - left, bottom - top);
		return this;
	}
	
	public Rect set(Rectangle rect) {
		super.setBounds(rect);
		return this;
	}
	
	public Rect offset(int dx, int dy) {
		x += dx;
		y += dy;
		return this;
	}
	
	@Deprecated
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
}
