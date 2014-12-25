package com.platforge.player.core.game;

import pythagoras.f.Rectangle;

public class RectF extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public float left() {
		return x;
	}
	
	public float top() {
		return y;
	}
	
	public float right() {
		return maxX();
	}
	
	public float bottom() {
		return maxY();
	}
	
	public RectF() {
		
	}
	
	public RectF(float left, float top, float right, float bottom) {
		super();
		set(left, top, right, bottom);
	}
	
	public RectF set(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right - left, bottom - top);
		return this;
	}
	
	public RectF set(Rectangle rect) {
		super.setBounds(rect);
		return this;
	}
	
	public RectF set(Rect rect) {
		super.setBounds(rect.x, rect.y, rect.width, rect.height);
		return this;
	}
	
	public RectF offset(float dx, float dy) {
		x += dx;
		y += dy;
		return this;
	}
	
	@Deprecated
	@Override
	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x, y, width, height);
	}

}
