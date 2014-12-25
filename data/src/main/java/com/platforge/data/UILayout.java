package com.platforge.data;

import java.io.Serializable;
import java.util.LinkedList;

import com.platforge.data.field.DataObject;
import com.platforge.data.field.FieldData;
import com.platforge.data.field.FieldData.ParseDataException;
import com.platforge.game.Color;

public class UILayout extends GameData {
	private static final long serialVersionUID = 1L;
	
	public final static int DEFAULT_RAD = 65; 
	public final static int DEFAULT_ALPHA = 150;
	
	public final LinkedList<Button> buttons = new LinkedList<Button>();
	public final LinkedList<JoyStick> joysticks = new LinkedList<JoyStick>();
	
	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		fields.addList(buttons);
		fields.addList(joysticks);
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new UILayout();
			}
		};
	}
	
	public UILayout() {
		buttons.add(new Button(-80, -80, DEFAULT_RAD, Color.argb(DEFAULT_ALPHA, 255, 0, 0), true));
		joysticks.add(new JoyStick(80, -80, DEFAULT_RAD, Color.argb(DEFAULT_ALPHA, 0, 0, 255), true));
	}
	
	public static abstract class CircleControl extends GameData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public int x, y;
		public int radius, color;
		public boolean defaultAction;
		public String name;
		
		@Override
		public void addFields(FieldData fields) throws ParseDataException,
				NumberFormatException {
			x = fields.add(x);
			y = fields.add(y);
			radius = fields.add(radius);
			color= fields.add(color);
			defaultAction = fields.add(defaultAction);
			name = fields.add(name);
		}
		
		public CircleControl(int x, int y, int radius, int color,
				boolean defaultAction) {
			super();
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.color = color;
			this.defaultAction = defaultAction;
		}
		
		public int getRealX(int width) {
			return x >= 0 ? x : width + x;
		}
		
		public int getRealY(int height) {
			return y >= 0 ? y : height+ y;
		}
		
		public void setRealX(int newX, int width) {
			if (newX < 0) newX = 0;
			if (newX > width - 1) newX = width - 1;
			
			x = newX > width / 2 ? newX - width : newX;
		}
		
		public void setRealY(int newY, int height) {
			if (newY < 0) newY = 0;
			if (newY > height - 1) newY = height - 1;
			
			y = newY > height / 2 ? newY - height : newY;
		}

		public void setRelX(int newX, int width) {
			if (newX < -width / 2) {
				x = width + newX;
			} else if (newX > width / 2) {
				x  = -width + newX;
			} else {
				x = newX;
			}
		}

		public void setRelY(int newY, int height) {
			if (newY < -height/ 2) {
				y = height + newY;
			} else if (newY > height / 2) {
				y  = -height + newY;
			} else {
				y = newY;
			}
		}
	}
	
	public static class Button extends CircleControl {
		private static final long serialVersionUID = 1L;

		public static Constructor constructor() {
			return new Constructor() {
				@Override
				public DataObject construct() {
					return new Button(0, 0, 0, 0, false);
				}
			};
		}
		
		public Button(int x, int y, int radius, int color,
				boolean defaultAction) {
			super(x, y, radius, color, defaultAction);
			name = "New Button";
		}
	}
	
	public static class JoyStick extends CircleControl {
		private static final long serialVersionUID = 1L;

		public static Constructor constructor() {
			return new Constructor() {
				@Override
				public DataObject construct() {
					return new JoyStick(0, 0, 0, 0, false);
				}
			};
		}
		
		public JoyStick(int x, int y, int radius, int color,
				boolean defaultAction) {
			super(x, y, radius, color, defaultAction);
			name = "New Joy Stick";
		}
	}

}
