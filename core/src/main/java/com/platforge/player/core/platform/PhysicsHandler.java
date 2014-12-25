package com.platforge.player.core.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import com.platforge.data.ActorClass;
import com.platforge.data.ActorInstance;
import com.platforge.data.Map;
import com.platforge.data.MapLayer;
import com.platforge.data.ObjectClass;
import com.platforge.data.ObjectInstance;
import com.platforge.data.PlatformGame;
import com.platforge.data.Tileset;
import com.platforge.data.Vector;
import com.platforge.data.MapClass.CollidesWith;
import com.platforge.player.core.graphics.Graphics;
import com.platforge.player.core.graphics.ImageSprite;
import com.platforge.player.core.graphics.Sprite;
import com.platforge.player.core.graphics.Tilemap;
import com.platforge.player.core.graphics.Viewport;
import com.platforge.player.core.platform.PlatformLogic.ActorAddable;
import com.platforge.player.core.platform.PlatformLogic.ObjectAddable;

public class PhysicsHandler {


	public static final float SCALE = 50;

	private ArrayList<ActorBody> actorBodies = new ArrayList<ActorBody>();
	private ArrayList<ObjectBody> objectBodies = new ArrayList<ObjectBody>();
	private ArrayList<PlatformBody> platformBodies = new ArrayList<PlatformBody>();
	private ArrayList<PlatformBody> destroyedBodies = new ArrayList<PlatformBody>();
	private ArrayList<PlatformBody> createdBodies = new ArrayList<PlatformBody>();
	private World world;
	private ActorBody heroBody;

	private ArrayList<ActorBody> toRemove = new ArrayList<ActorBody>();
	private ArrayList<ActorAddable> toAdd = new ArrayList<ActorAddable>();
	private ArrayList<QueuedContact> contacts = new ArrayList<QueuedContact>();
	
	
	private Fixture rightWallFixture, leftWallFixture, topWallFixture;
	//private Body rightWallBody, leftWallBody;

	private PlatformGame game;
	private Map map;
	private Tilemap[] layers;
	
	private int nextActorId = 0, nextObjectId = 0;

	private Vec2 gravity = new Vec2();

	private HashMap<Fixture, PlatformBody> bodyMap = new HashMap<Fixture, PlatformBody>();
	private HashMap<Fixture, ImageSprite> levelMap = new HashMap<Fixture, ImageSprite>();

	private float mapFloor, mapRight, mapLeft;

	public World getWorld() {
		return world;
	}

	public PlatformGame getGame() {
		return game;
	}

	public float getMapFloorMeters() {
		return mapFloor / SCALE;
	}

	public float getMapFloorPx() {
		return mapFloor;
	}
	
	public float getMapRightPx() {
		return mapRight;
	}
	
	public float getMapLeftPx() {
		return mapLeft;
	}

	public ActorBody getHero() {
		return heroBody;
	}

	public List<PlatformBody> getPlatformBodies() {
		return platformBodies;
	}

	public PlatformBody getFixtureBody(Fixture fixture) {
		return bodyMap.get(fixture);
	}

	public ImageSprite getFixtureTile(Fixture fixture) {
		return levelMap.get(fixture);
	}

	public ObjectBody getLastCreatedObject() {
		if (objectBodies.size() == 0) return null;
		return objectBodies.get(objectBodies.size() - 1);
	}

	public ActorBody getLastCreatedActor() {
		if (actorBodies.size() == 0) return null;
		return actorBodies.get(actorBodies.size() - 1);
	}

	public void setGravity(Vector vector) {
		gravity.set(vector.getX(), vector.getY());
		world.setGravity(gravity);
		for (PlatformBody body : platformBodies) {
			if (body != null) {
				body.getBody().setAwake(true);
			}
		}
	}

	public ActorBody getActorBodyFromId(int id) {
		for (int i = 0; i < actorBodies.size(); i++) {
			ActorBody body = actorBodies.get(i);
			if (body.id == id) return body;
		}
		return null;
	}

	public ObjectBody getObjectBodyFromId(int id) {
		for (int i = 0; i < objectBodies.size(); i++) {
			ObjectBody body = objectBodies.get(i);
			if (body.id == id) return body;
		}
		return null;
	}
	
	public List<PlatformBody> getDestroyedBodies() {
		return destroyedBodies;
	}
	
	public List<PlatformBody> getCreatedBodies() {
		return createdBodies;
	}
	
	public PhysicsHandler(PlatformLogic logic) {
		this.game = logic.getGame();
		this.map = game.getSelectedMap();
		initPhysics();
	}
	
	public boolean isBodyAlive(PlatformBody body) {
		return platformBodies.contains(body);
	}

	public void queueActor(ActorAddable actor) {
		toAdd.add(actor);
	}

	public void postDisposeBody(PlatformBody body) {
		world.destroyBody(body.getBody());
		if (body instanceof ObjectBody) {
			objectBodies.remove(getObjectBodyFromId(body.id));
		} else if (body instanceof ActorBody) {
			actorBodies.remove(getActorBodyFromId(body.id));
		}
		Fixture fixture = body.getBody().getFixtureList();
		while (fixture != null) {
			bodyMap.remove(fixture);
			fixture = fixture.m_next;
		}
		platformBodies.remove(body);
		destroyedBodies.add(body);
	}

	private void initPhysics() {
		mapFloor = map.height(game);
		mapLeft = 0;
		mapRight = map.width(game);

		world = new World(new Vec2(map.gravity.getX(), map.gravity.getY()));
		
		createWalls();

		ContactFilter filter = new ContactFilter() {

			@Override
			public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
				//Debug.write("Should Collide");
				
				if (!bodyMap.containsKey(fixtureA) && !bodyMap.containsKey(fixtureB))
					return false;

				PlatformBody bodyA = bodyMap.get(fixtureA);
				PlatformBody bodyB = bodyMap.get(fixtureB);
				
				if (isBoundaryFixture(fixtureA) || isBoundaryFixture(fixtureB)) {
					return true;
				}
				

				boolean collides = PlatformBody.collides(bodyA, bodyB);
//				Debug.write("%s collides %s: %s", bodyA, bodyB, collides);
				return collides;
			}
		};
		world.setContactFilter(filter);

		ContactListener listener = new ContactListener() {

			@Override
			public void endContact(Contact contact) {
				doEndContact(contact.getFixtureA(), contact.getFixtureB());
			}

			@Override
			public void beginContact(Contact contact) {
				if (contact.isTouching()) {
					contacts.add(QueuedContact.create(contact.getFixtureA(),
							contact.getFixtureB(), contact.m_manifold.localNormal));
//							contact.getFixtureB(), contact.getWorldManifold().getNormal()));
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) { }

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) { }
		};
		world.setContactListener(listener);

		layers = new Tilemap[map.layers.length];
		Tileset tileset = game.getMapTileset(map);
		for (int i = 0; i < layers.length; i++) {
			MapLayer layer = map.layers[i];
			layers[i] = new Tilemap(Data.loadTileset(tileset.bitmapName), 
					tileset.tileWidth, tileset.tileHeight, tileset.tileSpacing, 
					layer.tiles, Graphics.getRect(), i * 2);

		}

		//Coordinates from the center of the box, using Cartesian (not texture) coordinates, going clockwise
		HashMap<Integer, double[]> shapeMap = new HashMap<Integer, double[]>(12);

		//Ground Up 30 Left
		double[] groundUp30Left = new double[] {
				-0.5, -0.5,
				0.5, 0,
				0.5, -0.5
		}; 
		shapeMap.put(11, groundUp30Left);
		shapeMap.put(60, groundUp30Left);

		//Ground Up 30 Right
		double[] groundUp30Right = new double[] {
				-0.5, -0.5,
				-0.5, 0,
				0.5, 0.5,
				0.5, -0.5
		}; 
		shapeMap.put(12, groundUp30Right);
		shapeMap.put(61, groundUp30Right);

		//Ground Down 30 Left
		double[] groundDown30Left = new double[] {
				-0.5, -0.5,
				-0.5, 0.5,
				0.5, 0,
				0.5, -0.5
		}; 
		shapeMap.put(13, groundDown30Left);
		shapeMap.put(62, groundDown30Left);

		//Ground Down 30 Right
		double[] groundDown30Right = new double[] {
				-0.5, -0.5,
				-0.5, 0,
				0.5, -0.5
		}; 
		shapeMap.put(14, groundDown30Right);
		shapeMap.put(63, groundDown30Right);

		//Ground Up 45
		double[] groundUp45 = new double[] {
				-0.5, -0.5,
				0.5, 0.5,
				0.5, -0.5

		}; 
		shapeMap.put(36, groundUp45);
		shapeMap.put(54, groundUp45);
		
		//Ground Down 45
		double[] groundDown45 = new double[] {
				-0.5, 0.5,
				0.5, -0.5,
				-0.5, -0.5
		};
		shapeMap.put(37, groundDown45);
		shapeMap.put(55, groundDown45);

		//Roof Up 30 Left
		double[] roofUp30Left = new double[] {
				-0.5, -0.5,
				-0.5, 0.5,
				0.5, 0.5,
				0.5, 0
		}; 
		shapeMap.put(29, roofUp30Left);

		//Roof Up 30 Right
		double[] roofUp30Right = new double[] {
				-0.5, 0,
				-0.5, 0.5,
				0.5, 0.5
		}; 
		shapeMap.put(30, roofUp30Right);

		//Roof Down 30 Left
		double[] roofDown30Left = new double[] {
				-0.5, 0.5,
				0.5, 0.5,
				0.5, 0
		}; 
		shapeMap.put(27, roofDown30Left);

		//Roof Down 30 Right
		double[] roofDown30Right = new double[] {
				-0.5, 0,
				-0.5, 0.5,
				0.5, 0.5,
				0.5, -0.5
		}; 
		shapeMap.put(28, roofDown30Right);

		//Roof Up 45
		double[] roofUp45 = new double[] {
				-0.5, -0.5,
				-0.5, 0.5,
				0.5, 0.5

		};
		shapeMap.put(53, roofUp45);
		shapeMap.put(46, roofUp45);
		
		//Roof Down 45
		double[] roofDown45 = new double[] {
				-0.5, 0.5,
				0.5, 0.5,
				0.5, -0.5
		}; 
		shapeMap.put(52, roofDown45);
		shapeMap.put(43, roofDown45);

		for (int k = 0; k < layers.length; k++) {
			if (!map.layers[k].active)
				continue;

			ImageSprite[][] sprites = layers[k].getSprites();

			for (int i = 0; i < sprites.length; i++) {
				for (int j = 0; j < sprites[i].length; j++) {
					ImageSprite s = sprites[i][j];
					if (s != null) {
						BodyDef tileDef = new BodyDef();
						float x = (s.getX() + s.getWidth() / 2) / SCALE;
						float y = (s.getY() + s.getHeight() / 2) / SCALE;
						float width = s.getWidth() / SCALE;
						float height = s.getHeight() / SCALE;
						tileDef.position.set(x, y);
						tileDef.type = BodyType.STATIC;
						Body tileBody = world.createBody(tileDef);
						PolygonShape tileShape = new PolygonShape();
						int tileId = map.layers[k].tiles[i][j];
						if (shapeMap.get(tileId) != null) {
							double[] set = shapeMap.get(tileId);
							Vec2[] vertices = new Vec2[set.length / 2];
							for (int v = 0; v < vertices.length; v++) {
								vertices[v] = new Vec2(
										(float)set[v * 2] * width,
										(float)set[v * 2 + 1] * -height
										);
							}
							tileShape.set(vertices, vertices.length);
						} else {
							tileShape.setAsBox(width / 2, height / 2);
						}
						tileBody.createFixture(tileShape, 1);
						Fixture fixture = tileBody.getFixtureList();
						while (fixture != null) {
							fixture.getFilterData().categoryBits = 
									PlatformBody.getCategoryBits(CollidesWith.Terrain);
							levelMap.put(fixture, s);
							fixture = fixture.m_next;
						}
					}
				}
			}
		}

		for (int i = 0; i < map.actors.size(); i++) {
			ActorInstance instance = map.actors.get(i);
			float x = (instance.column + 0.5f) * game.getMapTileset(map).tileWidth;
			float y = (instance.row + 0.5f) * game.getMapTileset(map).tileHeight;
			int instanceId = instance.id;
			int actorId = instance.classIndex;
			if (actorId > 0) {
				addActorBody(game.actors[actorId], instanceId, x, y);
			} else {
				heroBody = addActorBody(game.getHero(), 
						instanceId,
						x,
						y, 1, true);
			}


		}

		for (int i = 0; i < map.objects.size(); i++) {
			ObjectInstance instance = map.objects.get(i);
			if (instance != null) {
				float x = instance.startX;
				float y = instance.startY;
				int instanceId = instance.id;
				int objectId = instance.classIndex;
				addObjectBody(game.objects[objectId], instanceId, x, y);
			}
		}
	}
	
	private boolean isBoundaryFixture(Fixture fixture) {
		return fixture == rightWallFixture || 
				fixture == leftWallFixture || 
				fixture == topWallFixture;
	}
	
	private void createWalls() {
		float thickness = 60 / SCALE;
		float width = map.width(game) / SCALE;
		float height = map.height(game) / SCALE;
		
		PolygonShape horizontalWallShape = new PolygonShape();
		horizontalWallShape.setAsBox(width / 2, thickness / 2);
		
		PolygonShape verticalWallShape = new PolygonShape();
		verticalWallShape.setAsBox(thickness / 2, height / 2);
		
		BodyDef wallDef = new BodyDef();
		wallDef.position.set(-thickness / 2, height / 2);
		wallDef.type = BodyType.STATIC;
		Body leftWallBody = world.createBody(wallDef);
		leftWallFixture = leftWallBody.createFixture(verticalWallShape, 1);
	
		
		wallDef = new BodyDef();
		wallDef.position.set(width + thickness / 2, height / 2);
		wallDef.type = BodyType.STATIC;
		Body rightWallBody = world.createBody(wallDef);
		rightWallFixture = rightWallBody.createFixture(verticalWallShape, 1);
		
		wallDef = new BodyDef();
		wallDef.position.set(width / 2, -thickness / 2);
		wallDef.type = BodyType.STATIC;
		Body topWallBody = world.createBody(wallDef);
		topWallFixture = topWallBody.createFixture(horizontalWallShape, 1);
		
	}
	
//	private void updateWallSprites(Vector offset) {
//	Sprite rightSprite = new Sprite(Viewport.DebugViewport, 
//			0, 0, widthPx, heightPx);
//	rightSprite.setOriginX(widthPx / 2);
//	rightSprite.setOriginY(heightPx / 2);
//	leftSprite.getBitmap().eraseColor(Color.RED);
//	levelMap.put(rightWallFixture, rightSprite);
	
//		Sprite leftSprite = levelMap.get(leftWallFixture);
//		Sprite rightSprite = levelMap.get(rightWallFixture);
//		
//		
//		
//		float y = Graphics.getHeight() / 2;
//		leftSprite.setY(y);
//		rightSprite.setY(y);
//		leftSprite.setX(offset.getX() - leftSprite.getWidth() / 2);
//		rightSprite.setX(offset.getX() + map.width(game) + rightSprite.getWidth() / 2);
//		
//		sprite.setX(body.getPosition().x * SCALE + offset.getX());
//		sprite.setY(body.getPosition().y * SCALE + offset.getY());
//	}

	public void update(long timeElapsed, Vector offset) {
		
		for (int i = 0; i < platformBodies.size(); i++) {
			if (platformBodies.get(i) != null) {
				platformBodies.get(i).update(timeElapsed, offset);
			}
		}

		world.step(timeElapsed / 1000.0f, 6, 6);


		for (int i = 0; i < contacts.size(); i++) {
			doContact(contacts.get(i));
		}
		contacts.clear();
	}

	public void addAndRemove() {
		for (int i = 0; i < toRemove.size(); i++) {
			toRemove.get(i).destroy();
		}
		toRemove.clear();
		for (int i = 0; i < toAdd.size(); i++) {
			addActorBody(toAdd.get(i));
		}
		toAdd.clear();
	}

	public void checkBehaviors() {
		for (int i = 0; i < actorBodies.size(); i++) {
			ActorBody body = actorBodies.get(i);
			if (body != null) {
				body.checkBehavior();
			}
		}
	}

	public void updateScroll(Vector offset) {
		for (int i = 0; i < platformBodies.size(); i++) {
			if (platformBodies.get(i) != null) {
				platformBodies.get(i).updateSprite(offset);
			}
		}
		for (int i = 0; i < layers.length; i++) {
			layers[i].setScrollX(-offset.getX());
			layers[i].setScrollY(-offset.getY());
		}
	}

	public ObjectBody addObjectBody(ObjectAddable object) {
		return addObjectBody(object.object, -1, object.startX, object.startY);
	}

	private ObjectBody addObjectBody(ObjectClass object, int id, float startX, float startY) {
		if (id < 0) {
			id = nextObjectId;
		}
		ObjectBody body = new ObjectBody(Viewport.DefaultViewport, this, 
				object, id, startX, startY);
		objectBodies.add(body);
		platformBodies.add(body);
		nextObjectId = Math.max(id, nextObjectId) + 1;

		Fixture fixture = body.getBody().getFixtureList();
		while (fixture != null) {
			bodyMap.put(fixture, body);
			fixture = fixture.m_next;
		}

		createdBodies.add(body);
		return body;
	}

	public  ActorBody addActorBody(ActorAddable actor) {
		return addActorBody(actor.actor, -1, actor.startX, actor.startY, actor.startDir);
	}

	private ActorBody addActorBody(ActorClass actor, int id, float startX, float startY) {
		return addActorBody(actor, id, startX, startY, -1, false);
	}

	private ActorBody addActorBody(ActorClass actor, int id, float startX, float startY, int startDir) {
		return addActorBody(actor, id, startX, startY, startDir, false);
	}

	private ActorBody addActorBody(ActorClass actor, int id, float startX, float startY, 
			int startDir, boolean isHero) {
		
		if (id < 0) {
			id = nextActorId;
		}
		
		ActorBody body = new ActorBody(Viewport.DefaultViewport, this, actor, id,
				startX, startY, startDir, isHero);
		actorBodies.add(body);
		platformBodies.add(body);
		
		nextActorId = Math.max(id, nextActorId) + 1;

		Fixture fixture = body.getBody().getFixtureList();
		while (fixture != null) {
			bodyMap.put(fixture, body);
			fixture = fixture.m_next;
		}

		createdBodies.add(body);
		return body;
	}

	private void doEndContact(Fixture fixtureA, Fixture fixtureB) {
		doEndContact(fixtureA, fixtureB, false);
	}

	private void doEndContact(Fixture fixtureA, Fixture fixtureB, boolean anti) {
		if (bodyMap.containsKey(fixtureA)) {
			PlatformBody bodyA = bodyMap.get(fixtureA);
			if (bodyMap.containsKey(fixtureB)) {
				PlatformBody bodyB = bodyMap.get(fixtureB);
				bodyA.getTouchingBodies().remove(bodyB);
				
				if (bodyB instanceof ObjectBody && 
						((ObjectBody) bodyB).getObject().isPlatform) {
				bodyA.getTouchingFloors().remove(fixtureB);
				bodyA.getTouchingWalls().remove(fixtureB);
				}
			} else if (levelMap.containsKey(fixtureB)) {
				bodyA.getTouchingWalls().remove(fixtureB);
				bodyA.getTouchingFloors().remove(fixtureB);
			}
		}
		if (!anti) {
			doEndContact(fixtureB, fixtureA, true);
		}
	}
	
	private void doContact(QueuedContact contact) {
		doContact(contact, false);
	}

	private void doContact(QueuedContact contact, boolean anti) {
		Fixture fixtureA = anti ? contact.fixtureB : contact.fixtureA;
		Fixture fixtureB = anti ? contact.fixtureA : contact.fixtureB;

		if (bodyMap.containsKey(fixtureA)) {

			PlatformBody bodyA = bodyMap.get(fixtureA);

			if (bodyMap.containsKey(fixtureB)) {
				PlatformBody bodyB = bodyMap.get(fixtureB);
				if (!bodyA.getTouchingBodies().contains(bodyB)) {
					bodyA.getCollidedBodies().add(bodyB);
					bodyA.getTouchingBodies().add(bodyB);
				}
				if (bodyB instanceof ObjectBody && 
						((ObjectBody) bodyB).getObject().isPlatform) {
					doPlatformContact(bodyA, fixtureB, bodyB.getSprite(), contact.normal);
				}
			} else if (levelMap.containsKey(fixtureB) || isBoundaryFixture(fixtureB)) {

				if (isBoundaryFixture(fixtureB)) {
					if	(fixtureB == rightWallFixture || fixtureB == leftWallFixture) {
						bodyA.doWallContact(fixtureB);
					}
				} else {
					ImageSprite spriteB = levelMap.get(fixtureB);
					doPlatformContact(bodyA, fixtureB, spriteB, contact.normal);
				}
			}

		}

		if (!anti) {
			doContact(contact, true);
		} else {
			contact.recycle();
		}
	}
	
	/**
	 * Returns the direction of gravity (in degrees)
	 */
	public float getGravityRotation() {
		if (world.getGravity().length() == 0) {
			return 0;
		}
		return VectorUtils.angleDeg(world.getGravity()) - 90;
	}
	
	Vec2 tempVector = new Vec2();
	private void doPlatformContact(PlatformBody bodyA, Fixture fixtureB, Sprite spriteB,
			Vec2 normal) {
		
		if (world.getGravity().length() == 0) {
			bodyA.doFloorContact(fixtureB);
			return;
		}
		
		//adjust for different gravities
		normal = VectorUtils.rotateDeg(tempVector.set(normal), -getGravityRotation());
		
		float nx = normal.x;
		float ny = -normal.y;
		
		if (Math.abs(nx) > Math.abs(ny)) {
			bodyA.doWallContact(fixtureB);
		} else if (ny > 0) {
			bodyA.doFloorContact(fixtureB);
		}
	}

	private AABB aabb = new AABB();
	public void regionCallback(final BodyCallback callback, float left, 
			float top, float right, float bottom) {
		aabb.lowerBound.set(left, top);
		aabb.upperBound.set(right, bottom);
		world.queryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				if (bodyMap.containsKey(fixture)) {
					return callback.doCallback(bodyMap.get(fixture));
				}
				return true;
			}
		}, aabb);
	}

	public static abstract class BodyCallback {
		public abstract boolean doCallback(PlatformBody body);
	}

	private static class QueuedContact {
		public Fixture fixtureA, fixtureB;
		public Vec2 normal = new Vec2();
		
		private static ArrayList<QueuedContact> toReuse =
				new ArrayList<QueuedContact>();

		public static QueuedContact create(Fixture fixtureA, Fixture fixtureB,
				Vec2 normal) {
			if (toReuse.size() == 0) return new QueuedContact(fixtureA, fixtureB, normal);
			QueuedContact qc = toReuse.remove(toReuse.size() - 1);
			qc.set(fixtureA, fixtureB, normal);
			return qc;
		}
		
		public void recycle() {
			toReuse.add(this);
		}
		
		private QueuedContact(Fixture fixtureA, Fixture fixtureB, Vec2 normal) {
			set(fixtureA, fixtureB, normal);
		}
		
		public void set(Fixture fixtureA, Fixture fixtureB, Vec2 normal) {
			this.fixtureA = fixtureA;
			this.fixtureB = fixtureB;
			this.normal.set(normal);
		}
	}
}

