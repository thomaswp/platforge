package com.twp.core.platform;

import com.twp.core.action.Point;
import com.twp.core.input.UIControl;

import edu.elon.honors.price.physics.Vector;

public class TriggeringInfo {

	public ActorBody triggeringActor;
	public ObjectBody triggeringObject;
	public Vector triggeringVector;
	public UIControl triggeringControl;
	public Point triggeringPoint;
	public IBehaving behaving;
	public int behaviorIndex = -1;
	
	public void clearTriggerInfo() {
		triggeringActor = null;
		triggeringObject = null;
		triggeringVector = null;
		triggeringControl = null;
		triggeringPoint = null;
	}
	
	public void clearBehavingInfo() {
		behaving = null;
		behaviorIndex = -1;
	}
	
	public TriggeringInfo clone() {
		TriggeringInfo info = new TriggeringInfo();
		info.triggeringActor = this.triggeringActor;
		info.triggeringObject = this.triggeringObject;
		info.triggeringVector = this.triggeringVector;
		info.triggeringControl = this.triggeringControl;
		info.triggeringPoint = this.triggeringPoint;
		info.behaving = this.behaving;
		info.behaviorIndex = this.behaviorIndex;
		return info;
	}
}
