package com.twp.platform;

import org.jbox2d.common.Vec2;

public class VectorUtils {
	
	public static final float RADIANS_TO_DEGREES = 180 / (float) Math.PI;
	
	public static float angleRad(Vec2 vector) {
		float angle = (float)Math.atan2(vector.y, vector.x);
		if(angle < 0)
			angle += 360;
		return angle;
	}
	
	public static float angleDeg(Vec2 vector) {
		return angleRad(vector) * RADIANS_TO_DEGREES;
	}

	public static Vec2 rotateRad(Vec2 vector, float radians) {
		float angle = angleRad(vector);
		angle += radians;
		float len = vector.length();
		vector.x = (float) Math.cos(angle) * len;
		vector.y = (float) Math.sin(angle) * len;
		return vector;
	}
	
	public static Vec2 rotateDeg(Vec2 vector, float degrees) {
		return rotateRad(vector, degrees / RADIANS_TO_DEGREES);
	}
	
	public static float dot(Vec2 a, Vec2 b) {
		return a.x * b.x + a.y * b.y;
	}
}
