package com.twp.core.action;

public class ActionFactory {
	public final static int[] ACTION_IDS = new int[] {
		0,
		1,
		2,
		3,
		4,
		5,
		7,
		8,
		9,
		10,
		11,
		12,
		13,
		14,
		15,
		16,
		17,
		18,
		19,
		20,
		21,
		22,
		23,
		24,
		25,
		26,
	};
	public final static String[] ACTION_NAMES = new String[] {
		"Set Switch",
		"Set Variable",
		"Create Actor",
		"Debug Box",
		"Move Actor",
		"Trigger Actor Behavior",
		"",
		"If...",
		"Create Object",
		"Move Object",
		"Set Velocity",
		"Debug Message",
		"Point Operation",
		"Change Gravity",
		"UI Action",
		"Destroy Object",
		"Draw to Screen",
		"Loop...",
		"Trigger Actor Animation",
		"Animate",
		"Wait",
		"Stop",
		"Else",
		"Trigger Event",
		"Change Color",
		"Set Behavior Parameters",
		"Change Scale",
	};
	public final static String[] ACTION_CATEGORIES = new String[] {
		"Variables",
		"Variables",
		"Actor",
		"Debug",
		"Actor",
		"Actor",
		"Misc",
		"Control",
		"Object",
		"Object",
		"Physics",
		"Debug",
		"Variables",
		"Physics",
		"UI",
		"Object",
		"UI",
		"Control",
		"Animate",
		"Animate",
		"Control",
		"Control",
		"Hidden",
		"Control",
		"Actor|Object",
		"Actor|Object",
		"Actor|Object",
	};
	public static ScriptableInstance getActionInstance(int id) {
		if (id == ActionSetSwitch.ID) return new ActionSetSwitch();
		if (id == ActionSetVariable.ID) return new ActionSetVariable();
		if (id == ActionCreateActor.ID) return new ActionCreateActor();
		if (id == ActionDebugBox.ID) return new ActionDebugBox();
		if (id == ActionMoveActor.ID) return new ActionMoveActor();
		if (id == ActionTriggerActorBehavior.ID) return new ActionTriggerActorBehavior();
		if (id == ActionIf.ID) return new ActionIf();
		if (id == ActionCreateObject.ID) return new ActionCreateObject();
		if (id == ActionMoveObject.ID) return new ActionMoveObject();
		if (id == ActionSetVelocity.ID) return new ActionSetVelocity();
		if (id == ActionDebugMessage.ID) return new ActionDebugMessage();
		if (id == ActionPointOperation.ID) return new ActionPointOperation();
		if (id == ActionChangeGravity.ID) return new ActionChangeGravity();
		if (id == ActionUIAction.ID) return new ActionUIAction();
		if (id == ActionDestroyObject.ID) return new ActionDestroyObject();
		if (id == ActionDrawToScreen.ID) return new ActionDrawToScreen();
		if (id == ActionLoop.ID) return new ActionLoop();
		if (id == ActionTriggerActorAnimation.ID) return new ActionTriggerActorAnimation();
		if (id == ActionAnimate.ID) return new ActionAnimate();
		if (id == ActionWait.ID) return new ActionWait();
		if (id == ActionStop.ID) return new ActionStop();
		if (id == ActionElse.ID) return new ActionElse();
		if (id == ActionTriggerEvent.ID) return new ActionTriggerEvent();
		if (id == ActionChangeColor.ID) return new ActionChangeColor();
		if (id == ActionSetBehaviorParameters.ID) return new ActionSetBehaviorParameters();
		if (id == ActionChangeScale.ID) return new ActionChangeScale();
		return null;
	}
	public static ActionInterpreter<?> getInterpreterInstance(int id) {
		if (id == ActionSetSwitch.ID) return new InterpreterSetSwitch();
		if (id == ActionSetVariable.ID) return new InterpreterSetVariable();
		if (id == ActionCreateActor.ID) return new InterpreterCreateActor();
		if (id == ActionDebugBox.ID) return new InterpreterDebugBox();
		if (id == ActionMoveActor.ID) return new InterpreterMoveActor();
		if (id == ActionTriggerActorBehavior.ID) return new InterpreterTriggerActorBehavior();
		if (id == ActionIf.ID) return new InterpreterIf();
		if (id == ActionCreateObject.ID) return new InterpreterCreateObject();
		if (id == ActionMoveObject.ID) return new InterpreterMoveObject();
		if (id == ActionSetVelocity.ID) return new InterpreterSetVelocity();
		if (id == ActionDebugMessage.ID) return new InterpreterDebugMessage();
		if (id == ActionPointOperation.ID) return new InterpreterPointOperation();
		if (id == ActionChangeGravity.ID) return new InterpreterChangeGravity();
		if (id == ActionUIAction.ID) return new InterpreterUIAction();
		if (id == ActionDestroyObject.ID) return new InterpreterDestroyObject();
		if (id == ActionDrawToScreen.ID) return new InterpreterDrawToScreen();
		if (id == ActionLoop.ID) return new InterpreterLoop();
		if (id == ActionTriggerActorAnimation.ID) return new InterpreterTriggerActorAnimation();
		if (id == ActionAnimate.ID) return new InterpreterAnimate();
		if (id == ActionWait.ID) return new InterpreterWait();
		if (id == ActionStop.ID) return new InterpreterStop();
		if (id == ActionElse.ID) return new InterpreterElse();
		if (id == ActionTriggerEvent.ID) return new InterpreterTriggerEvent();
		if (id == ActionChangeColor.ID) return new InterpreterChangeColor();
		if (id == ActionSetBehaviorParameters.ID) return new InterpreterSetBehaviorParameters();
		if (id == ActionChangeScale.ID) return new InterpreterChangeScale();
		return null;
	}
	public static boolean isParent(int id) {
		if (id == ActionIf.ID) return true;
		if (id == ActionLoop.ID) return true;
		if (id == ActionElse.ID) return true;
		return false;
	}
}
