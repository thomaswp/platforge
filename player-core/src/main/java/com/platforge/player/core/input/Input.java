package com.platforge.player.core.input;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.Touch;

import com.platforge.player.core.graphics.Graphics;

public abstract class Input {
	public interface Impl extends Touch.Listener, Pointer.Listener, Keyboard.Listener {
		void update(int delta);
		boolean isTapped();
		void vibrate(int ms);
		int getTappedPointer();
		float getLastTouchX(int pointer);
		float getLastTouchY(int pointer);
		boolean isTouchDown(int pointer);
		int getWalkDir();
		boolean getJump();
		void clearJump();
	}

	private static Impl instance = new DefaultImpl();

	public static Impl getInstance() {
		return instance;
	}
	
	public static void setInstance(Impl instance) {
		Input.instance = instance;
	}
	
	public static void update(int delta) {
		instance.update(delta);
	}
	
	public static void vibrate(int ms) {
		instance.vibrate(ms);
	}

	public static boolean isTapped() {
		return instance.isTapped();
	}

	public static int getTappedPointer() {
		return instance.getTappedPointer();
	}

	public static float getLastTouchX() {
		return instance.getLastTouchX(0);
	}
	
	public static float getLastTouchY() {
		return instance.getLastTouchY(0);
	}
	
	public static float getLastTouchX(int pointer) {
		return instance.getLastTouchX(pointer);
	}
	
	public static float getLastTouchY(int pointer) {
		return instance.getLastTouchY(pointer);
	}
	
	public static boolean isTouchDown(int pointer) {
		return instance.isTouchDown(pointer);
	}
	
	public static boolean isTouchDown() {
		return instance.isTouchDown(0);
	}
	
	public static int getWalkDir() {
		return instance.getWalkDir();
	}
	
	public static boolean getJump() {
		return instance.getJump();
	}
	
	public static void clearJump() {
		instance.clearJump();
	}
	
	private static class DefaultImpl implements Impl {

		boolean touchDown, jump;
		int tapped;
		boolean leftDown, rightDown, spaceDown;
		float lastTouchX, lastTouchY;
		
		@Override
		public void update(int delta) {
			if (touchDown) {
				if (tapped > 0) tapped--;
			}
		}

		@Override
		public boolean isTapped() {
			return tapped == 1;
		}

		@Override
		public void vibrate(int ms) { }

		@Override
		public int getTappedPointer() {
			return 0;
		}

		@Override
		public float getLastTouchX(int pointer) {
			return lastTouchX / Graphics.getGlobalScale();
		}

		@Override
		public float getLastTouchY(int pointer) {
			return lastTouchY / Graphics.getGlobalScale();
		}

		@Override
		public boolean isTouchDown(int pointer) {
			return touchDown;
		}

		@Override
		public void onTouchStart(playn.core.Touch.Event[] touches) { 
			
		}

		@Override
		public void onTouchMove(playn.core.Touch.Event[] touches) { 
			
		}

		@Override
		public void onTouchEnd(playn.core.Touch.Event[] touches) {
			
		}

		@Override
		public void onTouchCancel(playn.core.Touch.Event[] touches) { 
			
		}

		@Override
		public void onPointerStart(Event event) {
			touchDown = true;
			lastTouchX = event.x();
			lastTouchY = event.y();
			tapped = 2;
		}

		@Override
		public void onPointerEnd(Event event) {
			touchDown = false;
			lastTouchX = event.x();
			lastTouchY = event.y();
		}

		@Override
		public void onPointerDrag(Event event) {
			lastTouchX = event.x();
			lastTouchY = event.y();
		}

		@Override
		public void onPointerCancel(Event event) { }

		@Override
		public void onKeyDown(playn.core.Keyboard.Event event) {
			if (event.key() == Key.LEFT) {
				leftDown = true;
			} else if (event.key() == Key.RIGHT) {
				rightDown = true;
			} else if (event.key() == Key.SPACE) {
				if (!spaceDown) jump = true;
				spaceDown = true;
			}
		}

		@Override
		public void onKeyTyped(TypedEvent event) { }

		@Override
		public void onKeyUp(playn.core.Keyboard.Event event) {
			if (event.key() == Key.LEFT) {
				leftDown = false;
			} else if (event.key() == Key.RIGHT) {
				rightDown = false;
			} else if (event.key() == Key.SPACE) {
				spaceDown = false;
			}
		}

		@Override
		public int getWalkDir() {
			int dir = 0;
			if (leftDown) dir--;
			if (rightDown) dir++;
			return dir;
		}

		@Override
		public boolean getJump() {
			return jump;
		}

		@Override
		public void clearJump() {
			jump = false;
		}
	}

	public static void reset() {
		// TODO Auto-generated method stub
	}

	public static void setMultiTouch(boolean multiTouch) {
		// TODO Auto-generated method stub
	}
}
