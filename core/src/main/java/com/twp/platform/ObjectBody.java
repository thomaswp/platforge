package com.twp.platform;

import java.util.List;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import playn.core.Canvas;
import playn.core.Color;
import playn.core.Image;
import playn.core.Path;
import edu.elon.honors.price.data.BehaviorInstance;
import edu.elon.honors.price.data.Data;
import edu.elon.honors.price.data.MapClass;
import edu.elon.honors.price.data.MapClass.CollidesWith;
import edu.elon.honors.price.data.ObjectClass;
import edu.elon.honors.price.graphics.Sprite;
import edu.elon.honors.price.graphics.Viewport;
import edu.elon.honors.price.physics.Vector;

public class ObjectBody extends PlatformBody {

	private static final float SCALE = PlatformLogic.SCALE;

	private ObjectClass object;

	public boolean isPlatform() {
		return object.isPlatform;
	}
	
	@Override
	public MapClass getMapClass() {
		return object;
	}
	
	public ObjectClass getObject() {
		return object;
	}
	
	@Override
	public List<BehaviorInstance> getBehaviorInstances() {
		return object.behaviors;
	}

	public ObjectBody(Viewport viewport, PhysicsHandler physics, ObjectClass object, int id, 
			float startX, float startY) {
		super(viewport, physics, id, startX, startY);

		this.object = object;
		
		behaviorRuntimes = new BehaviorRuntime[object.behaviors.size()];
		for (int i = 0; i < behaviorRuntimes.length; i++) {
			behaviorRuntimes[i] = new BehaviorRuntime(
					object.behaviors.get(i), physics.getGame());
		}

		Image bitmap = Data.loadObject(object.imageName);
		this.sprite = new Sprite(viewport, bitmap);
		this.sprite.setX(startX); this.sprite.setY(startY);
		this.sprite.centerOrigin();
		this.sprite.setZoom(object.zoom);
		this.sprite.setBaseColor(object.color);

		BodyDef objectDef = new BodyDef();
		objectDef.position.set(spriteToVect(sprite, null));
		objectDef.type = object.moves ? BodyType.DYNAMIC : BodyType.KINEMATIC;
		objectDef.fixedRotation = !object.rotates;
		body = physics.getWorld().createBody(objectDef);

		float[] xs = new float[8]; float[] ys = new float[8];
		int pts = sprite.convexHull(xs, ys);
		sprite.centerOrigin();

		//drawDebugOutline(xs, ys);
		
		Vec2[] points = new Vec2[pts];
		int pi = points.length - 1;
		for (int i = 0; i < 8; i++) {
			if (xs[i] >= 0) {
				points[pi] = new Vec2(
						(xs[i] - bitmap.width() / 2) / SCALE * object.zoom, 
						(ys[i] - bitmap.height() / 2) / SCALE * object.zoom
				);
				//Debug.write(points[pi]);
				pi--;
			}
		}
		PolygonShape poly = new PolygonShape();
		poly.set(points, points.length);
		FixtureDef fixture = new FixtureDef();
		if (isPlatform()) {
			fixture.filter.categoryBits = getCategoryBits(CollidesWith.Terrain);
		} else {
			fixture.filter.categoryBits = getCategoryBits(CollidesWith.Objects);
		}
		fixture.filter.maskBits = getMaskBits();
		fixture.shape = poly;
		fixture.density = getDensity(object.density);
		fixture.friction = object.friction;
		fixture.restitution = object.restitution;
		body.createFixture(fixture);
	}
	
	@SuppressWarnings("unused")
	private void drawDebugOutline(float[] xs, float[] ys) {
		sprite.setMutable(true);
		Canvas canvas = sprite.getBitmapCanvas();
		
		Path path = canvas.createPath();
		boolean first = true;
		for (int i = 0; i < xs.length; i++) {
			if (xs[i] >= 0) {
				if (first)
					path.moveTo(xs[i], ys[i]);
				else
					path.lineTo(xs[i], ys[i]);
				first = false;
			}
			//Debug.write("(%f,%f)", xs[i], ys[i]);
		}
		path.close();
		canvas.setFillColor(Color.argb(150, 255, 255, 255));
		sprite.getBitmapCanvas().fillPath(path);
	}

	@Override
	public void update(long timeElapsed, Vector offset) {
		super.update(timeElapsed, offset);
		collidedBodies.clear();
		collidedWall = false;

		updateSprite(offset);
		lastVelocity.set(getVelocity());
	}

	@Override
	public void updateSprite(Vector offset) {
		setSpritePosition(sprite, body, offset);
		if (object.rotates) {
			sprite.setRotation(body.getAngle() * 180 / (float)Math.PI);
		}
	}
}
