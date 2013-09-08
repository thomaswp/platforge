package edu.elon.honors.price.input;

import playn.core.Color;
import edu.elon.honors.price.game.Debug;
import edu.elon.honors.price.graphics.ImageSprite;
import edu.elon.honors.price.graphics.Viewport;
import edu.elon.honors.price.physics.Vector;

public class Button extends UIControl {
	private Viewport viewport;
	private float radius;
	private Vector center, touch, dragStart, pull, temp;
	private ImageSprite outer, top;
	private int pid = -1;
	private boolean tapped;
	private boolean released;


	public boolean isPressed() {
		return !top.isVisible();
	}
	
	public boolean isTapped() {
		return tapped;
	}
	
	public boolean isReleased() {
		return released;
	}
	
	public int getPID() {
		return pid;
	}
	
	public Button(int x, int y, int z, int radius, int color) {
		viewport = new Viewport();
		viewport.setZ(z);
		center = new Vector(x, y);
		pull = new Vector();
		touch = new Vector();
		temp = new Vector();
		dragStart = new Vector();
		this.radius = radius;

		outer = new ImageSprite(viewport, x, y, radius * 2, radius * 2);
		outer.centerOrigin();
		outer.getBitmapCanvas().setFillColor(color);
		outer.getBitmapCanvas().fillCircle(radius, radius, radius);
				
		top = new ImageSprite(viewport, x, y, radius * 2, radius * 2);
		top.centerOrigin();
		top.getBitmapCanvas().setFillColor(Color.argb(100, 0, 0, 0));
		top.getBitmapCanvas().fillCircle(radius, radius, radius);
	}

	public void update() {
		this.tapped = false;
		this.released = false;
		if (Input.isTapped() && pid < 0) {
			int tapped = Input.getTappedPointer();
			touch.set(Input.getLastTouchX(tapped), Input.getLastTouchY(tapped));
			temp.set(touch);
			temp.subtract(center);
			if (temp.magnitude() <= radius) {
				dragStart.set(touch);
				pid = tapped;
				this.tapped = true;
				Input.vibrate(40);
			}
		}
		if (pid >= 0 && Input.isTouchDown(pid)) {
			touch.set(Input.getLastTouchX(pid), Input.getLastTouchY(pid));
			pull = touch.subtract(dragStart);
			if (pull.magnitude() > radius) {
				top.setVisible(false);
			} else {
				top.setVisible(true);
			}
			//Debug.write(pull);
		} else {
			if (pid >= 0) {
				released = true;
			}
			top.setVisible(false);
			pull.set(0, 0);
			pid = -1;
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		outer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return outer.isVisible();
	}
}
