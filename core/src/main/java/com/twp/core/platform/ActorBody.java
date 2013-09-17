package com.twp.core.platform;

import java.util.List;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.twp.core.graphics.AnimatedSprite;
import com.twp.core.graphics.Viewport;
import com.twp.core.input.Input;

import playn.core.Image;
import edu.elon.honors.price.data.ActorAnimator;
import edu.elon.honors.price.data.ActorAnimator.Action;
import edu.elon.honors.price.data.ActorClass;
import edu.elon.honors.price.data.BehaviorInstance;
import edu.elon.honors.price.data.MapClass;
import edu.elon.honors.price.data.MapClass.CollidesWith;
import edu.elon.honors.price.game.Color;
import edu.elon.honors.price.physics.Vector;

public class ActorBody extends PlatformBody {
	
	private AnimatedSprite sprite;
	private ActorClass actor;
	private boolean isHero;
	private int directionX;
	private boolean stopped;
	private int stun;
	private boolean onLadder;
	private World world;
	private Vec2 respawnLocation;
	private Vec2 tempVector = new Vec2();
	private ActorAnimator animator;
	private boolean airJumped;
	private boolean collidedEdge;
	
	public boolean isCollidedEdge() {
		return collidedEdge;
	}
	
	@Override
	public MapClass getMapClass() {
		return actor;
	}
	
	@Override
	public List<BehaviorInstance> getBehaviorInstances() {
		return actor.behaviors;
	}

	public boolean isOnLadder() {
		return onLadder;
	}

	public void setOnLadder(boolean onLadder) {
		if (onLadder) {
			setVelocityX(0);
		}
		this.onLadder = onLadder;
	}

	public int getDirectionX() {
		return directionX;
	}

	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}
	
	public boolean isStunned() {
		return stun > 0;
	}
	
	public boolean isHero() {
		return isHero;
	}
	
	public ActorClass getActor() {
		return actor;
	}

//	public int getFacingDirectionX() {
//		int set = getCurrentAnimSet();
//		if (set == SET_WALK_LEFT ||
//				set == SET_JUMP_LEFT ||
//				set == SET_ATTACK_LEFT) {
//			return -1;
//		} else if (set == SET_WALK_RIGHT ||
//				set == SET_JUMP_RIGHT ||
//				set == SET_ATTACK_RIGHT) {
//			return 1;
//		}
//		return 0;
//	}

	public ActorBody(Viewport viewport, PhysicsHandler physics, ActorClass actor, int id, 
			float startX, float startY, int startDir, boolean isHero) {
		super(viewport, physics, id, startX, startY);
		
		this.actor = actor;
		this.directionX = startDir;
		this.animator = ActorAnimator.create(actor.imageName);
		
		stopped = true;
		
		Image bitmap = Data.loadActor(actor.imageName);
		Action[] actions = Action.values();
		Vector[][] frames = new Vector[actions.length][];
		int frameWidth = (int)bitmap.width() / animator.getTotalCols();
		int frameHeight = (int)bitmap.height() / animator.getTotalRows();
		for (int i = 0; i < actions.length; i++) {
			Action action = actions[i];
			ActorAnimator.ActionParams actionParams =
					animator.getActionParams(action);
			frames[i] = new Vector[actionParams.frames];
			for (int j = 0; j < actionParams.frames; j++) {
				frames[i][j] = new Vector(
						(actionParams.column + j) * frameWidth, 
						actionParams.row * frameHeight);
			}
		}
		this.sprite = new AnimatedSprite(viewport, bitmap, frames, 
				startX, startY, frameWidth, frameHeight);
		this.sprite.centerOrigin();
		this.sprite.setZoom(actor.zoom);
		this.sprite.setBaseColor(actor.color);
		this.sprite.setFrame(Action.WalkingRight.ordinal(), 0);
		super.sprite = sprite;
		this.isHero = isHero;
		world = physics.getWorld();
		
		behaviorRuntimes = new BehaviorRuntime[actor.behaviors.size()];
		for (int i = 0; i < behaviorRuntimes.length; i++) {
			behaviorRuntimes[i] = new BehaviorRuntime(
					actor.behaviors.get(i), physics.getGame());
		}

		BodyDef actorDef = new BodyDef();
		actorDef.position.set(spriteToVect(sprite, null));
		actorDef.type = BodyType.DYNAMIC;
		actorDef.fixedRotation = true;
		body = world.createBody(actorDef);
		for (int i = -1; i < 2; i += 2) {
			CircleShape actorShape = new CircleShape();
			FixtureDef actorFix = new FixtureDef();
			if (sprite.getWidth() > sprite.getHeight()) {
				actorShape.m_radius = sprite.getHeight() / SCALE / 2;
				actorShape.m_p.set(i * (sprite.getHeight() - sprite.getWidth()) / 2 / SCALE, 0);
			} else {
				actorShape.m_radius = sprite.getWidth() / SCALE / 2;
				actorShape.m_p.set(0, i * (sprite.getHeight() - sprite.getWidth()) / 2 / SCALE);
			}
			actorFix.shape = actorShape;
			actorFix.friction = 0;
			actorFix.restitution = 0;
			actorFix.density = 1;
			if (isHero) {
				actorFix.filter.categoryBits = getCategoryBits(CollidesWith.Hero);
			} else {
				actorFix.filter.categoryBits = getCategoryBits(CollidesWith.Actors);
			}
			actorFix.filter.maskBits = getMaskBits();
			body.createFixture(actorFix);
	
		}

		if (isHero) {
			respawnLocation = new Vec2(body.getPosition());
		}
	}
	
	@Override
	public void destroy() {
		if (isHero) {
			respawnHero();
		} else {
			super.destroy();
		}
	}
	
	Vec2 temp = new Vec2();
	@Override
	public void update(long timeElapsed, Vector offset) {
		super.update(timeElapsed, offset);
		
//		lastBehaviorTime += timeElapsed;
		stun -= timeElapsed;
		collidedBodies.clear();
		collidedWall = false;
		
		checkDeath();
		
		if (world.getGravity().length() > 0) {
			float pi = (float)Math.PI;
			float gAngle = VectorUtils.angleRad(world.getGravity()) + 3 * pi / 2;
			float bAngle = body.getAngle();
			if (Math.abs(bAngle - gAngle) > 0) {
				if (bAngle - gAngle > pi) {
					gAngle += 2 * pi;
				}
				if (gAngle - bAngle > pi) {
					bAngle += 2 * pi;
				}
				bAngle = bAngle * 0.9f + gAngle * 0.1f;
			}
			body.setTransform(body.getPosition(), bAngle);
		}
		
		if (isHero) {
			stopped = true;
			Vec2 horizontal = temp.set(world.getGravity());
			VectorUtils.rotateDeg(horizontal, -90);
			float hVelocity = VectorUtils.dot(getVelocity(), horizontal);
			if (Math.abs(hVelocity) > 0.0001) {
				directionX = (int)Math.signum(hVelocity);
				stopped = false;
				setOnLadder(false);
			} else {
				directionX = 0;
			}
		}

		animator.update(timeElapsed, directionX, isGrounded(), isOnLadder());
		sprite.setFrame(animator.getAction(), animator.getFrame());
		sprite.setFlipped(animator.isFlipped());
		
		if (!isHero && actor.speed > 0) {
			setHorizontalVelocity(stopped ? 0 : directionX * actor.speed);
		}

		updateSprite(offset);
		lastVelocity.set(getVelocity());
	}
	
	private void checkDeath() {
		if (body.getPosition().y - sprite.getHeight() / SCALE > 
			physics.getMapFloorMeters()) {
			destroy();
		}
	}
	
	private void respawnHero() {
		body.setTransform(respawnLocation, body.getAngle());
		body.setLinearVelocity(body.getLinearVelocity().mul(0));
		stun(null);
	}
	
	public void doBehaviorWall() {
		//doBehavior(actor.wallBehavior, null);
	}
	
	public void doBehaviorEdge() {
		//doBehavior(actor.edgeBehavior, null);
	}
	
	public void doBehavoirCollideActor(int dir, ActorBody cause) {
		//doBehavior(actor.actorContactBehaviors[dir], cause);
	}
	
	public void doBehaviorCollideHero(int dir, ActorBody cause) {
		//doBehavior(actor.heroContactBehaviors[dir], cause);
	}
	
	public void doBehavior(int behavior, ActorBody cause) {
//		if (lastBehaviorTime < BEHAVIOR_REST)
//			return;
//		lastBehaviorTime = 0;

		switch (behavior) {
		case ActorClass.BEHAVIOR_STOP:
			stopped = true;
			break;
		case ActorClass.BEHAVIOR_JUMP_TURN:
			jump(false);
		case ActorClass.BEHAVIOR_TURN:
			directionX *= -1;
			break;
		case ActorClass.BEHAVIOR_JUMP:
			jump(false);
			break;
		case ActorClass.BEHAVIOR_START_STOP:
			stopped = !stopped;
			break;
		case ActorClass.BEHAVIOR_STUN:
			stun(cause);
			break;
		case ActorClass.BEHAVIOR_DIE:
			destroy();
			break;
		}	
	}
	
	public void stun(ActorBody cause) {
		stun = actor.stunDuration;
		sprite.flash(Color.RED, actor.stunDuration);
		if (cause != null) {
			setVelocityY(-actor.jumpVelocity / 1.5f);
			//if (cause == null)
			//	directionX *= -1;
			//else {
				directionX = (int)Math.signum(this.getSprite().getRect().x - cause.getSprite().getRect().x);
			//}
			stopped = false;
			if (isHero) {
				Input.vibrate(actor.stunDuration / 2);
			}
			onLadder = false;
		}
	}
	
	public void triggerAction() { 
		animator.action();
	}
	
	public void setHorizontalVelocity(float hv) {
		if (world.getGravity().length() == 0) {
			setVelocityX(hv);
			return;
		}
		
		Vec2 velocity = getVelocity();
		Vec2 gHat = temp.set(world.getGravity());
		gHat.normalize();
		//Project b onto gravity to erase horizontal velocity
		velocity.set(gHat.mulLocal(VectorUtils.dot(gHat, velocity)));
		Vec2 horizontal = VectorUtils.rotateDeg(temp.set(world.getGravity()), -90);
		horizontal.mulLocal(hv / horizontal.length());
		velocity.addLocal(horizontal);
		
		setVelocity(velocity);
	}
	
	public void setVerticalVelocity(float hv) {
		if (world.getGravity().length() == 0) {
			setVelocityY(-hv);
			return;
		}
		
		Vec2 velocity = getVelocity();
		Vec2 gHat = VectorUtils.rotateDeg(temp.set(world.getGravity()), -90);
		//Project b onto gravity to erase horizontal velocity
		velocity.set(gHat.mulLocal(VectorUtils.dot(gHat, velocity)));
		//Debug.write("%f, %f", newVelocity.x, newVelocity.y);
		Vec2 vertical = VectorUtils.rotateDeg(temp.set(world.getGravity()), 180);
		vertical.mulLocal(hv / vertical.length());
		velocity.addLocal(vertical);
		
		setVelocity(velocity);
	}
	
	public void jump(boolean checkGrounded) {
		boolean canJump;
		if (isGrounded()) {
			canJump = true;
		} else {
			canJump = actor.doubleJump && !airJumped;
		}
		
		if (!checkGrounded || canJump || isOnLadder()) {
			setVerticalVelocity(actor.jumpVelocity);	
			onLadder = false;
			animator.jump();
			if (!isGrounded()) airJumped = true;
		}
	}

	@Override
	public void onTouchGround() {
		this.onLadder = false;
		airJumped = false;
	}
	
	private FixtureCallback fixtureCallback = new FixtureCallback(physics);
	private AABB aabb = new AABB();
	public void checkBehavior() {
		for (int j = 0; j < collidedBodies.size(); j++) {
			if (collidedBodies.get(j) instanceof ActorBody)
			{
				ActorBody bodyB = (ActorBody)collidedBodies.get(j);
				
				int dir = getCollisionDirection(this, bodyB);
				if (bodyB.isHero())
					doBehaviorCollideHero(dir, bodyB);
				else
					doBehavoirCollideActor(dir, bodyB);
			}
		}
		if (collidedWall) {
			doBehaviorWall();
		}
		collidedEdge = false;
		if (getDirectionX() != 0) {
			float x = getPosition().x;
			float y = getPosition().y;
			tempVector.set((sprite.getWidth() / 2f + 5) * getDirectionX() / SCALE, 
					(sprite.getHeight() / 2f + 10) / SCALE);
			VectorUtils.rotateDeg(tempVector, physics.getGravityRotation());
			x += tempVector.x;
			y += tempVector.y;
			
			fixtureCallback.reset();
			float r = 1.5f / SCALE;
			
			aabb.lowerBound.set(x - r, y - r);
			aabb.upperBound.set(x + r, y + r);
			world.queryAABB(fixtureCallback, aabb);
			boolean contact = fixtureCallback.contact;
			if (!contact && isGrounded()) {
				collidedEdge = true;
			}
		}
	}
	
	private static class FixtureCallback implements QueryCallback {
		public boolean contact = false;
		public PhysicsHandler physics;

		private FixtureCallback(PhysicsHandler physics) {
			this.physics = physics;
		}
		
		public void reset() {
			contact = false;
		}

		@Override
		public boolean reportFixture(Fixture fixture) {
			if (physics.getFixtureTile(fixture) != null) {
				contact = true;
				return false;
			}
			PlatformBody body = physics.getFixtureBody(fixture); 
			if (body != null && body instanceof ObjectBody) {
				if (((ObjectBody)body).isPlatform()) {
					contact = true;
					return false;
				}
			}
			return true;
		}
	}
}
