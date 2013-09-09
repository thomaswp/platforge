package com.twp.android;

import java.util.ArrayList;

import playn.core.Keyboard.TypedEvent;
import playn.core.Touch.Event;
import android.os.Vibrator;
import android.view.MotionEvent;
import edu.elon.honors.price.graphics.Graphics;
import edu.elon.honors.price.input.Input;

public class AndroidInput implements Input.Impl {

	/**
	 * An enum listing the possible states of a Key.
	 * 
	 * Released: The key was pressed and has just become lifted
	 * Lifted: The key is currently lifted
	 * Triggered: The key has just been pressed
	 * Held: The key has been pressed and is being held
	 * 
	 * @author Thomas Price
	 */
	public enum KeyStates {
		Lifted,
		Held,
		Triggered,
		Released
	}

	private static final int POINTERS = 4;
	private static final int EVENTS = 30;

	private static boolean multiTouch = true;

	//A list of unprocessed TouchEvents
	private ArrayList<ArrayList<TouchEvent>> touchEvents = new ArrayList<ArrayList<TouchEvent>>(POINTERS);
	private ArrayList<TouchState> touchStates;

	private static Vibrator vibrator;

	public AndroidInput() {
		touchStates = new ArrayList<AndroidInput.TouchState>(POINTERS);
		for (int i = 0; i < POINTERS; i++) {
			touchStates.add(new TouchState());
		}
	}

	public static void setVibrator(Vibrator vibrator) {
		AndroidInput.vibrator = vibrator;
	}

	/**
	 * Returns whether or not the user is touching the screen
	 * @return
	 */
	public boolean isTouchDown() {
		return isTouchDown(0);
	}

	public boolean isTouchDown(int pid) {
		if (pid < 0 || pid >= touchStates.size()) return false;
		return touchStates.get(pid).touchDown;
	}

	/**
	 * Gets the last X Coordinate where the user touched the screen
	 * @return The X Coordinate
	 */
	public float getLastTouchX() {
		return getLastTouchX(0);
	}

	public float getLastTouchX(int pid) {
		return touchStates.get(pid).lastTouchX;
	}

	/**
	 * Gets the last Y Coordinate where the user touched the screen
	 * @return The Y Coordinate
	 */
	public float getLastTouchY() {
		return getLastTouchY(0);
	}

	public float getLastTouchY(int pid) {
		return touchStates.get(pid).lastTouchY;
	}

	/**
	 * Gets whether or not the user has just touched the screen 
	 * @return
	 */
	public boolean isTapped() {
		if (multiTouch) {
			for (int i = 0; i < touchStates.size(); i++) {
				if (touchStates.get(i).tapped)
					return true;
			}
		} else {
			return touchStates.get(0).tapped;
		}
		return false;
	}

	public boolean isSecondaryTapped() {
		for (int i = 1; i < touchStates.size(); i++) {
			if (touchStates.get(i).tapped)
				return true;
		}
		return false;
	}

	public int getTappedPointer() {
		for (int i = 0; i < touchStates.size(); i++) {
			if (touchStates.get(i).tapped)
				return i;
		}
		return -1;
	}

	/**
	 * Gets the X Coordinate of where the user first touched the
	 * screen during this touch event.
	 * @return The X Coordinate
	 */
	public float getStartTouchX() {
		return getStartTouchX(0);
	}

	public float getStartTouchX(int pid) {
		return touchStates.get(pid).startTouchX;
	}

	/**
	 * Gets the Y Coordinate of where the user first touched the
	 * screen during this touch event.
	 * @return The Y Coordinate
	 */
	public float getStartTouchY() {
		return getStartTouchY(0);
	}

	public float getStartTouchY(int pid) {
		return touchStates.get(pid).startTouchY;
	}

	/**
	 * Gets the X distance the user's touch has moved during this touch event.
	 * @return The X distance
	 */
	public float getDistanceTouchX() {
		return getDistanceTouchX(0);
	}

	public float getDistanceTouchX(int pid) {
		return touchStates.get(pid).lastTouchX - touchStates.get(pid).startTouchX;
	}

	/**
	 * Gets the Y distance the user's touch has moved during this touch event.
	 * @return The Y distance
	 */
	public float getDistanceTouchY() {
		return getDistanceTouchY(0);
	}

	public float getDistanceTouchY(int pid) {
		return touchStates.get(pid).lastTouchY - touchStates.get(pid).startTouchY;
	}

	public void reset() {
		touchEvents.clear();
		for (int i = 0; i < touchStates.size(); i++) {
			touchStates.set(i, new TouchState());
		}
	}

	private void handleTouchEvents() {
		//process the touch event
		@SuppressWarnings("unused")
		String out = "";


		for (int i = 0; i < touchEvents.size(); i++) {

			ArrayList<AndroidInput.TouchEvent> touchEventList = touchEvents.get(i);

			TouchState touchState = touchStates.get(i);

			if (touchEventList.size() == 0) {
				touchState.tapped = false;
				continue;
			}

			TouchEvent event = touchEventList.get(0);


			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
				//User just touched the screen
				touchState.touchDown = true;
				touchState.tapped = true;
				touchState.startTouchX = event.getX();
				touchState.startTouchY = event.getY();
			} else {
				//User is still touching the screen
				touchState.tapped = false;
			}

			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
				//User just lifted
				touchState.touchDown = false;
			}

			touchState.lastTouchX = event.getX();
			touchState.lastTouchY = event.getY();

			out += i + "-" + touchState + ";";
		}

		for (int i = 0; i < touchEvents.size(); i++) {
			ArrayList<AndroidInput.TouchEvent> touchEventList = touchEvents.get(i);
			if (touchEventList.size() == 0)
				continue;
			if (touchEventList.size() == 1 && touchEventList.get(0).action == MotionEvent.ACTION_MOVE)
				continue;
			touchEventList.remove(0);
		}

		//if (out.length() > 0) Debug.write(out);
	}

	public void update(int timeElapsed) {
		handleTouchEvents();
	}

	//Keeps track of the data in a TouchEvent
	private static class TouchEvent {
		float x, y;
		int action;

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public int getAction() {
			return action;
		}

		public TouchEvent(Event event, int action) {
			set(event, action);
		}

		public void set(Event event, int action) {
			x = event.x() / Graphics.getGlobalScale();
			y = event.y() / Graphics.getGlobalScale();
			this.action = action;
		}
	}

	private static class TouchState {
		public boolean touchDown, tapped;
		public float startTouchX, startTouchY, lastTouchX, lastTouchY;

		@Override
		public String toString() {
			return (touchDown ? (tapped ? "Tapped" : "Down") : "Up") + "{" + lastTouchX + ", " + lastTouchY + "}"; 
		}
	}

	private void handleTouch(Event[] touches, int action) {
		for (int i = 0; i < touches.length; i++) {
			Event event = touches[i];
			int pid = event.id();

			if (pid == touchEvents.size()) {
				touchEvents.add(new ArrayList<AndroidInput.TouchEvent>(EVENTS));
			}
			if (pid == touchStates.size()) {
				touchStates.add(new TouchState());
			}

			ArrayList<AndroidInput.TouchEvent> touchEventList = touchEvents.get(pid);

			boolean added = false;
			//We want to override the last event if it wasn't up or down
			if (touchEventList.size() > 0) {
				TouchEvent e = touchEventList.get(touchEventList.size() - 1);
				if (e.getAction() == MotionEvent.ACTION_MOVE && action == MotionEvent.ACTION_MOVE) {
					e.set(event, i);
					added = true;
				}
			}
			if (!added) {
				//record the TouchEvent, but don't process it until the next frame			
				touchEventList.add(new TouchEvent(event, i));
			}
		}
	}

	@Override
	public void onTouchStart(Event[] touches) {
		handleTouch(touches, MotionEvent.ACTION_DOWN);
	}

	@Override
	public void onTouchMove(Event[] touches) {
		handleTouch(touches, MotionEvent.ACTION_MOVE);
	}

	@Override
	public void onTouchEnd(Event[] touches) {
		handleTouch(touches, MotionEvent.ACTION_UP);
	}

	@Override
	public void onTouchCancel(Event[] touches) {
		handleTouch(touches, MotionEvent.ACTION_CANCEL);
	}

	@Override
	public boolean isPressed() {
		return isTouchDown();
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

}
