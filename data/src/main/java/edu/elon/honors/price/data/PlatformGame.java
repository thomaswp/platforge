package edu.elon.honors.price.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.elon.honors.price.data.Behavior.BehaviorType;
import edu.elon.honors.price.data.field.DataObject;
import edu.elon.honors.price.data.field.FieldData;
import edu.elon.honors.price.data.field.FieldData.ParseDataException;
import edu.elon.honors.price.game.Formatter;

public class PlatformGame extends GameData {
	private static final long serialVersionUID = 3L;

	protected int _VERSION_ = Upgrader.LATEST_VERSION;

	@Deprecated
	public String name;

	@Deprecated
	public long lastEdited;


	public final ArrayList<Map> maps;
	public int selectedMapId;

	public UILayout uiLayout;

	public Tileset[] tilesets;
	public ActorClass[] actors;
	public ObjectClass[] objects;
	public Hero getHero() { return (Hero)actors[0]; }

	public String[] switchNames;
	public boolean[] switchValues;
	public String[] variableNames;
	public int[] variableValues;

	public final LinkedList<Behavior> mapBehaviors, 
	actorBehaviors, objectBehaviors;

	public transient Object tutorial;
	public transient Object copyData;

	public String ID;

	@Override
	public void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		_VERSION_ = fields.add(_VERSION_);
		fields.addList(maps);
		selectedMapId = fields.add(selectedMapId);
		uiLayout = fields.add(uiLayout);
		
		int length;
		length = fields.add(tilesets == null ? 0 : tilesets.length);
		if (fields.readMode() && (tilesets == null || tilesets.length != length)) {
			tilesets = new Tileset[length];
		}
		tilesets = fields.addArray(tilesets);
		
		length = fields.add(actors == null ? 0 : actors.length);
		if (fields.readMode() && (actors == null || actors.length != length)) {
			actors = new ActorClass[length];
		}
		actors = fields.addArray(actors);
		
		length = fields.add(objects == null ? 0 : objects.length);
		if (fields.readMode() && (objects == null || objects.length != length)) {
			objects = new ObjectClass[length];
		}
		objects = fields.addArray(objects);
		
		switchNames = fields.addArray(switchNames);
		switchValues = fields.addArray(switchValues);
		variableNames = fields.addArray(variableNames);
		variableValues = fields.addArray(variableValues);
		fields.addList(mapBehaviors);
		fields.addList(actorBehaviors);
		fields.addList(objectBehaviors);
		ID = fields.add(ID);
	}
	
	public static Constructor constructor() {
		return new Constructor() {
			@Override
			public DataObject construct() {
				return new PlatformGame();
			}
		};
	}

	public int getVersion() {
		return _VERSION_;
	}

	@Deprecated
	public String getName(String fallback) {
		return fallback;
	}

	public LinkedList<Behavior> getBehaviors(BehaviorType type) {
		switch (type) {
		case Map:  return mapBehaviors;
		case Actor: return actorBehaviors;
		case Object: return objectBehaviors;
		}
		return null;
	}

	public List<Event> getAllEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		for (Map map : maps) {
			for (Event event : map.events) {
				events.add(event);
			}
		}
		for (Behavior behavior : actorBehaviors) {
			events.addAll(behavior.events);
		}
		for (Behavior behavior : objectBehaviors) {
			events.addAll(behavior.events);
		}
		for (Behavior behavior : mapBehaviors) {
			events.addAll(behavior.events);
		}
		return events;
	}

	public void stripEditorData() {
		tutorial = null;
		copyData = null;
		for (Map map : maps) {
			if (map != null) map.editorData = null;
		}
	}

	public PlatformGame() {

		switchNames = new String[] { };
		switchValues = new boolean[] { };
		resizeSwitches(100);

		variableNames = new String[] { };
		variableValues = new int[] { };
		resizeVariables(100);

		tilesets = new Tileset[4];
		//tilesets[0] = new Tileset("Default", "StickTiles.png", 64, 64, 8, 8);
		//tilesets[1] = new Tileset("Ice", "ice.png", 48, 48, 8, 8);
		tilesets[0] = new Tileset("Grass", "grass.png", 64, 64, 10, 8);
		tilesets[1] = new Tileset("Castle", "castle.png", 64, 64, 10, 8);
		tilesets[2] = new Tileset("Night", "night.png", 64, 64, 10, 8);
		tilesets[3] = new Tileset("Snow", "snow.png", 64, 64, 10, 8);

		maps = new ArrayList<Map>();
		maps.add(new Map(this));
		selectedMapId = 0;


		actors = new ActorClass[3];
		actors[1] = new ActorClass();
		ActorClass ghost = actors[1];
		ghost.imageName = "a2/ghost.png";
		ghost.speed = 1.5f;
		ghost.zoom = 0.7f;
		ghost.jumpVelocity = 4f;
		ghost.edgeBehavior = ActorClass.BEHAVIOR_TURN;
		ghost.wallBehavior = ActorClass.BEHAVIOR_JUMP_TURN;
		ghost.actorContactBehaviors = new int[] {ActorClass.BEHAVIOR_NONE, 
				ActorClass.BEHAVIOR_NONE, ActorClass.BEHAVIOR_TURN, ActorClass.BEHAVIOR_TURN};
		ghost.heroContactBehaviors[ActorClass.ABOVE] = ActorClass.BEHAVIOR_DIE;
		ghost.name = "Ghost";

		ActorClass skeleton = new ActorClass();
		skeleton.imageName = "a2/skeleton.png";
		skeleton.speed = 1f;
		skeleton.edgeBehavior = ActorClass.BEHAVIOR_TURN;
		skeleton.wallBehavior = ActorClass.BEHAVIOR_TURN;
		skeleton.actorContactBehaviors = new int[] {ActorClass.BEHAVIOR_NONE, 
				ActorClass.BEHAVIOR_NONE, ActorClass.BEHAVIOR_TURN, ActorClass.BEHAVIOR_TURN};
		skeleton.heroContactBehaviors[ActorClass.ABOVE] = ActorClass.BEHAVIOR_DIE;
		skeleton.name = "Skeleton";
		actors[2] = skeleton;

		Hero hero = new Hero();
		hero.speed = 3.5f;
		hero.jumpVelocity = 6f;
		hero.stunDuration = 600;
		hero.imageName = "a5/robot.png";
		hero.name = "Hero";
		hero.actorContactBehaviors[ActorClass.BELOW] = ActorClass.BEHAVIOR_JUMP;
		hero.actorContactBehaviors[ActorClass.LEFT] = ActorClass.BEHAVIOR_STUN;
		hero.actorContactBehaviors[ActorClass.RIGHT] = ActorClass.BEHAVIOR_STUN;
		actors[0] = hero;

		objects = new ObjectClass[2];


		for (int i = 0; i < 1; i++) {
			objects[i * 2] = new ObjectClass();
			objects[i * 2].name = "Rock";
			objects[i * 2].imageName = "rock.png";
			objects[i * 2].zoom = 0.5f;

			objects[i * 2 + 1] = new ObjectClass();
			objects[i * 2 + 1].name = "Triangle";
			objects[i * 2 + 1].imageName = "triangle.png";
			objects[i * 2 + 1].zoom = 1;
		}

		uiLayout = new UILayout();

		mapBehaviors = new LinkedList<Behavior>();
		actorBehaviors = new LinkedList<Behavior>();
		objectBehaviors = new LinkedList<Behavior>();
	}

	public Map getSelectedMap() {
		return maps.get(selectedMapId);
	}

	public Tileset getMapTileset(Map map) {
		return tilesets[map.tilesetId];
	}

	public int getMapWidth(Map map) {
		return map.columns * getMapTileset(map).tileWidth;
	}

	public int getMapHeight(Map map) {
		return map.rows * getMapTileset(map).tileHeight;
	}

	public void resizeSwitches(int newSize) {
		switchNames = Copy.copyOf(switchNames, newSize);
		switchValues = Copy.copyOf(switchValues, newSize);
		for (int i = 0; i < switchNames.length; i++) {
			if (switchNames[i] == null) {
				switchNames[i] = Formatter.format("Switch%03d", i);
			}
		}

		//TODO: Important stuff for when variables no longer exist!
	}

	public void resizeVariables(int newSize) {
		variableNames = Copy.copyOf(variableNames, newSize);
		variableValues = Copy.copyOf(variableValues, newSize);
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] == null) {
				variableNames[i] = Formatter.format("Variable%03d", i);
			}
		}

		//TODO: Important stuff for when variables no longer exist!
	}
}
