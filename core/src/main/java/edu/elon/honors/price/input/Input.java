package edu.elon.honors.price.input;

import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.Touch;
import edu.elon.honors.price.graphics.Graphics;

public abstract class Input {
	public interface Impl extends Touch.Listener, Pointer.Listener, Keyboard.Listener {
		void update(int delta);
		boolean isTapped();
		boolean isPressed();
		void vibrate(int ms);
		int getTappedPointer();
		float getLastTouchX(int pointer);
		float getLastTouchY(int pointer);
		boolean isTouchDown(int pointer);
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
	
	private static class DefaultImpl implements Impl {

		boolean touchDown;
		int tapped;
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
		public boolean isPressed() {
			return touchDown;
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
		public void onPointerCancel(Event event) {
			
		}

		@Override
		public void onKeyDown(playn.core.Keyboard.Event event) {
			
		}

		@Override
		public void onKeyTyped(TypedEvent event) {
			
		}

		@Override
		public void onKeyUp(playn.core.Keyboard.Event event) {
			
		}
	}
}
