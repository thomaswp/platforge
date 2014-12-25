package com.twp.android;

import java.util.ArrayList;

import com.platforge.player.core.game.Debug;
import com.platforge.player.core.graphics.Graphics;
import com.platforge.player.core.input.Input;

import playn.core.Keyboard.TypedEvent;
import playn.core.Touch.Event;
import android.os.Vibrator;

public class AndroidInput implements Input.Impl {


	private static Vibrator vibrator;

	public AndroidInput() {
	}

	public static void setVibrator(Vibrator vibrator) {
		AndroidInput.vibrator = vibrator;
	}

	@Override
	public void vibrate(int ms) {
		vibrator.vibrate(ms);
	}

	@Override
	public void onPointerStart(playn.core.Pointer.Event event) { }

	@Override
	public void onPointerEnd(playn.core.Pointer.Event event) { }

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) { }

	@Override
	public void onPointerCancel(playn.core.Pointer.Event event) { }

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) { }

	@Override
	public void onKeyTyped(TypedEvent event) { }

	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) { }

	@Override
	public int getWalkDir() {
		return 0;
	}

	@Override
	public boolean getJump() {
		return false;
	}

	@Override
	public void clearJump() { }
	
	private static class TouchState {

		public int id;
		public float x, y;
		public int hold;

		public void onTouch(Event touch, int hold) {
			onTouch(touch);
			this.hold = hold;
		}
		
		public void onTouch(Event touch) {
			x = touch.x() / Graphics.getGlobalScale();
			y = touch.y() / Graphics.getGlobalScale();
		}
		
		public void update() {
			if (hold > 1) {
				hold--;
			}
		}
		
	}
	
	private TouchState missState = new TouchState();
	private ArrayList<TouchState> touchStates = new ArrayList<AndroidInput.TouchState>();
	private ArrayList<Integer> touchIds = new ArrayList<Integer>();
	
	private TouchState getState(int id) {
		return getState(id, false);
	}
	
	private TouchState getState(int id, boolean add) {
		if (touchIds.contains(id)) {
			return touchStates.get(touchIds.indexOf(id));
		} else if (add) {
			touchIds.add(id);
			while (touchStates.size() < touchIds.size()) {
				touchStates.add(new TouchState());
			}
			TouchState state = touchStates.get(touchIds.size() - 1);
			state.id = id;
			return state;
		}
		return missState;
	}
	
	private void removeState(int id) {
		int index = touchIds.indexOf(id);
		if (index >= 0) {
			touchIds.remove(index);
			TouchState reuse = touchStates.remove(index);
			//touchStates.add(reuse);
		}
	}
	
	@Override
	public void onTouchStart(Event[] touches) {
		for (Event touch : touches) {
			getState(touch.id(), true).onTouch(touch, 3);
		}
	}

	@Override
	public void onTouchMove(Event[] touches) {
		for (Event touch : touches) {
			getState(touch.id()).onTouch(touch);
		}
	}

	@Override
	public void onTouchEnd(Event[] touches) {
		for (Event touch : touches) {
			removeState(touch.id());
		}
	}

	@Override
	public void onTouchCancel(Event[] touches) {
		for (Event touch : touches) {
			getState(touch.id()).onTouch(touch, 0);
		}
	}

	@Override
	public void update(int delta) {
		for (TouchState state : touchStates) {
			state.update();
		}
	}

	@Override
	public boolean isTapped() {
		for (TouchState state : touchStates) {
			if (state.hold == 2) {
				Debug.write("tapped: %d", state.id);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getTappedPointer() {
		for (TouchState state : touchStates) {
			if (state.hold == 2) return state.id;
		}
		return -1;
	}

	@Override
	public float getLastTouchX(int pointer) {
		return getState(pointer).x;
	}

	@Override
	public float getLastTouchY(int pointer) {
		return getState(pointer).y;
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return getState(pointer).hold != 0;
	}
}
