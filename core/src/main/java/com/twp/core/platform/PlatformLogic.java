package com.twp.core.platform;

import java.util.LinkedList;
import java.util.List;

import org.jbox2d.common.Vec2;

import com.twp.core.game.Debug;
import com.twp.core.game.Logic;
import com.twp.core.game.RectF;
import com.twp.core.graphics.Graphics;
import com.twp.core.graphics.ImageSprite;
import com.twp.core.graphics.Sprite;
import com.twp.core.graphics.Viewport;
import com.twp.core.input.Button;
import com.twp.core.input.Input;
import com.twp.core.input.JoyStick;

import playn.core.CanvasImage;
import playn.core.PlayN;
import edu.elon.honors.price.data.ActorClass;
import edu.elon.honors.price.data.Map;
import edu.elon.honors.price.data.ObjectClass;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.UILayout;
import edu.elon.honors.price.physics.Vector;

public class PlatformLogic implements Logic {

	private static final int BORDER_Y = 130;
	private static final int BORDER_X = 350;
	public static final float SCALE = PhysicsHandler.SCALE;
	public static final int MAX_WIDTH = 1024;

	private Map map;
	private PlatformGame game;

	private PhysicsHandler physics;
	private TriggerHandler triggerHandler;
	private BackgroundHandler backgroundHandler; 

	private LinkedList<JoyStick> joysticks = new LinkedList<JoyStick>();
	private LinkedList<Button> buttons = new LinkedList<Button>();
	private Sprite hero;
	private Vector cameraOffset = new Vector(), joystickPull = new Vector();
	private Vector offset;

	private LinkedList<ImageSprite> drawScreenSprites = new LinkedList<ImageSprite>();
	private LinkedList<ImageSprite> drawWorldSprites = new LinkedList<ImageSprite>();
	private Viewport drawViewport;
	
	Vec2 tempVector = new Vec2();

	private Interpreter interpreter;

	private long gameTime = 0, lastGameTime = -1;
	
	private boolean paused;

	public PlatformGame getGame() {
		return game;
	}
	
	public long getGameTime() {
		return gameTime;
	}
	
	public long getLastGameTime() {
		return lastGameTime;
	}

	public Map getMap() {
		return map;
	}

	public PhysicsHandler getPhysics() {
		return physics;
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}

	public Vector getOffset() {
		return offset;
	}

	public List<JoyStick> getJoysticks() {
		return joysticks;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	@Override
	public void setPaused(boolean paused) {
		paused = true;
	}

	public JoyStick getJoystick(int index) {
		if (joysticks.size() <= index) 
			return null;
		return joysticks.get(index);
	}

	public Button getButton(int index) {
		if (buttons.size() <= index) 
			return null;
		return buttons.get(index);
	}

	public PlatformLogic(PlatformGame game) {
		paused = true;
		this.game = game;
	}
	
	protected boolean hasKeyboard() {
		return PlayN.keyboard().hasHardwareKeyboard();
	}

	@Override
	public void initialize() {

		if (Graphics.getWidth() > MAX_WIDTH) {
			Graphics.setWidth(MAX_WIDTH);
		}
		Globals.setInstance(new Globals());

		map = game.maps.get(game.selectedMapId);

		backgroundHandler = new BackgroundHandler(game);

		drawViewport = new Viewport();
		drawViewport.setZ(5);

		for (int i = 0; i < game.uiLayout.buttons.size(); i++) {
			UILayout.Button button = game.uiLayout.buttons.get(i);
			if (hasKeyboard() && button.defaultAction) continue;
			int x = button.x >= 0 ? button.x : Graphics.getWidth() + button.x;
			int y = button.y >= 0 ? button.y : Graphics.getHeight() + button.y;
			buttons.add(new Button(x, y, 10, 
					button.radius, button.color));
			buttons.get(i).setActive(button.defaultAction);
		}
		for (int i = 0; i < game.uiLayout.joysticks.size(); i++) {
			UILayout.JoyStick joystick = game.uiLayout.joysticks.get(i);
			if (hasKeyboard() && joystick.defaultAction) continue;
			int x = joystick.x >= 0 ? joystick.x : Graphics.getWidth() + joystick.x;
			int y = joystick.y >= 0 ? joystick.y : Graphics.getHeight() + joystick.y;
			joysticks.add(new JoyStick(x, y, 10, 
					joystick.radius, joystick.color));
			joysticks.get(i).setActive(joystick.defaultAction);
		}

		offset = new Vector();

		physics = new PhysicsHandler(this);
		hero = physics.getHero().getSprite();
		this.interpreter = new Interpreter(this);
		triggerHandler = new TriggerHandler(this);

		Viewport.DefaultViewport.setZ(3);

		update(1);
	}



	@Override
	public void update(long timeElapsed) {

		if (Input.isTapped() || Input.getWalkDir() != 0 || Input.getJump()) paused = false;

		if (paused) {
			updateScroll(1);
			return;
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).update();
		}
		for (int i = 0; i < joysticks.size(); i++) {
			joysticks.get(i).update();
		}

		ActorBody heroBody = physics.getHero();
		if (!heroBody.isStunned()) {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).isActive() &&
						buttons.get(i).isTapped()) {
					heroBody.jump(true);
				}
			}
			if (Input.getJump()) {
				heroBody.jump(true);
				Input.clearJump();
			}

			boolean joyFound = false;
			joystickPull.set(0, 0);
			for (int i = 0; i < joysticks.size(); i++) {
				if (joysticks.get(i).isActive()) {
					joystickPull.set(joysticks.get(i).getPull());
					joyFound = true;
				}
			}

			if (heroBody.isOnLadder()) {
//				heroBody.setVelocityY(joystickPull.getY() * heroBody.getActor().speed);
//				antiGravity.set(0, -PhysicsHandler.GRAVITY * heroBody.getBody().getMass());
//				heroBody.getBody().applyForce(antiGravity, zeroVector);
			}
			else {
				if (joyFound) {
					float velocity = joystickPull.getX();
					if (physics.getWorld().getGravity().length() != 0) {
						Vec2 horizontal = VectorUtils.rotateDeg(tempVector.set(physics.getWorld().getGravity()), -90);
						horizontal.normalize();
						float dot = horizontal.x * joystickPull.getX() + horizontal.y * joystickPull.getY();
						velocity = dot * heroBody.getActor().speed;
					}
					
					heroBody.setHorizontalVelocity(velocity);
				} else {
					heroBody.setHorizontalVelocity(Input.getWalkDir() * heroBody.getActor().speed);
				}
			}
		}

		physics.update(timeElapsed, offset);
		updateWorldSprites();

		triggerHandler.checkTriggers();

		physics.checkBehaviors();
		physics.addAndRemove();

		interpreter.update();

		updateScroll(0.2f);
	
		lastGameTime = gameTime;
		gameTime += timeElapsed;
	}

	private void updateWorldSprites() {
		for (int i = 0; i < drawWorldSprites.size(); i++) {
			drawWorldSprites.get(i).setOriginX(offset.getX());
			drawWorldSprites.get(i).setOriginY(offset.getY());
		}
	}

	public void clearDrawScreen() {
		for (int i = 0; i < drawScreenSprites.size(); i++) {
			drawScreenSprites.get(i).dispose();
		}
		drawScreenSprites.clear();
		for (int i = 9; i < drawWorldSprites.size(); i++) {
			drawWorldSprites.get(i).dispose();
		}
		drawWorldSprites.clear();
	}

	public void drawLine(int x1, int y1, int x2, int y2, boolean world, int color) {
		int left = Math.min(x1, x2), top = Math.min(y1, y2);
		int right = Math.max(x1, x2), bot = Math.max(y1, y2);
		int width = right - left, height = bot - top;
		CanvasImage bmp = PlayN.graphics().createImage(width, height);
		ImageSprite s = new ImageSprite(drawViewport, bmp);
		s.getBitmapCanvas().setStrokeColor(color);
		s.getBitmapCanvas().setStrokeWidth(3);
		s.getBitmapCanvas().drawLine(x1 - left, y1 - top, x2 - left, y2 - top);
		s.setX(left);
		s.setY(top);
		addDrawSprite(s, world);
	}

	public void drawCircle(int x, int y, int rad, boolean world, int color, boolean filled) {
		Debug.write("Draw: %d %d %d", x, y, rad);
		CanvasImage bmp = PlayN.graphics().createImage(rad * 2, rad * 2);
		ImageSprite s = new ImageSprite(drawViewport, bmp);
		if (filled) {
			s.getBitmapCanvas().setFillColor(color);
			s.getBitmapCanvas().fillCircle(rad, rad, rad);
		} else {
			s.getBitmapCanvas().setStrokeColor(color);
			s.getBitmapCanvas().setStrokeWidth(3);
			s.getBitmapCanvas().strokeCircle(rad, rad, rad);
		}
		s.setX(x - rad);
		s.setY(y - rad);
		addDrawSprite(s, world);
	}

	public void drawBox(int x1, int y1, int x2, int y2, boolean world, int color, boolean filled) {
		int left = Math.min(x1, x2), top = Math.min(y1, y2);
		int right = Math.max(x1, x2), bot = Math.max(y1, y2);
		int width = right - left, height = bot - top;
		CanvasImage bmp = PlayN.graphics().createImage(width, height);
		ImageSprite s = new ImageSprite(drawViewport, bmp);
		if (filled) {
			s.getBitmapCanvas().setFillColor(color);
			s.getBitmapCanvas().fillRect(0, 0, width, height);
		} else {
			s.getBitmapCanvas().setStrokeColor(color);
			s.getBitmapCanvas().setStrokeWidth(3);
			s.getBitmapCanvas().strokeRect(0, 0, width, height);
		}
		s.setX(left);
		s.setY(top);
		addDrawSprite(s, world);
	}

	private void addDrawSprite(ImageSprite s, boolean world) {
		if (world) {
			drawWorldSprites.add(s);
		} else {
			drawScreenSprites.add(s);
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void load() {
	}

	private void updateScroll(float snap) {
		cameraOffset.clear();

		if (!hero.isDisposed()) {
			RectF heroRect = hero.getRect();
			boolean offLeft = heroRect.x < BORDER_X,
					offRight = heroRect.maxX() > Graphics.getWidth() - BORDER_X,
					offUp = heroRect.y < BORDER_Y,
					offDown = heroRect.maxY() > Graphics.getHeight() - BORDER_Y;
			
			if (offLeft && offRight) {
				cameraOffset.setX(Math.round((Graphics.getWidth() - heroRect.x - heroRect.maxX()) / 2));
			} else {
				if (offLeft) {
					cameraOffset.setX(BORDER_X - heroRect.x);
				}
				if (offRight) {
					cameraOffset.setX((Graphics.getWidth() - BORDER_X) - heroRect.maxX());
				}
			}
			if (offUp && offDown) {
				cameraOffset.setY(Math.round((Graphics.getHeight() - heroRect.y - heroRect.maxY()) / 2));
			} else {
				if (offUp) {
					cameraOffset.setY(BORDER_Y - heroRect.y);
				}
				if (offDown) {
					cameraOffset.setY((Graphics.getHeight() - BORDER_Y) - heroRect.maxY());
				}
			}
		}
		
		cameraOffset.multiply(snap);
		offset.add(cameraOffset);
		
		//bound camera to map borders
		int mapHeight = game.getSelectedMap().height(game);
		//The bottom doesn't show no matter what
		if (-offset.getY() + Graphics.getHeight() > physics.getMapFloorPx()) {
			offset.setY(Graphics.getHeight() - physics.getMapFloorPx());
		}
		if (mapHeight > Graphics.getHeight()) {
			//If we have more map height than screen height, bound the roof
			if (-offset.getY() < 0) {
				offset.setY(0);
			}
		}
		
		int mapWidth = game.getSelectedMap().width(game); 
		if (mapWidth < Graphics.getWidth()) {
			//If our map is smaller than our screen, center it
			offset.setX((Graphics.getWidth() - mapWidth) / 2);
		} else {
			if (-offset.getX() < physics.getMapLeftPx()) {
				offset.setX(physics.getMapLeftPx());
			}
			if (-offset.getX() + Graphics.getWidth() > physics.getMapRightPx()) {
				offset.setX(Graphics.getWidth() - physics.getMapRightPx());
			}
		}
		


		physics.updateScroll(offset);
		for (int i = 0; i < drawScreenSprites.size(); i++) {
			ImageSprite sprite = drawScreenSprites.get(i);
			sprite.setX(offset.getX());
			sprite.setY(offset.getY());
		}

		backgroundHandler.updateScroll(offset);
	}

	public static class ActorAddable {
		public ActorClass actor;
		public float startX, startY;
		public int startDir;

		public ActorAddable(ActorClass actor, float startX, float startY) {
			this(actor, startX, startY, 1);
		}

		public ActorAddable(ActorClass actor, float startX, float startY, int startDir) {
			this.actor = actor;
			this.startX = startX;
			this.startY = startY;
			this.startDir = startDir;
		}
	}

	public static class ObjectAddable {
		public ObjectClass object;
		public float startX, startY;

		public ObjectAddable(ObjectClass object, float startX, float startY) {
			this.object = object;
			this.startX = startX;
			this.startY = startY;
		}
	}
}
