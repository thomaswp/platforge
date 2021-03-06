package com.platforge.player.core.action;

//import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.platforge.data.Event.Action;
import com.platforge.data.action.ActionFactory;
import com.platforge.player.core.game.Debug;
import com.platforge.player.core.platform.PlatformLogic;

public abstract class ActionInterpreter<T extends ScriptableInstance> {

	Random rand = new Random();
	public static final float SCALE = PlatformLogic.SCALE;
	
//	private static final List<Class<?>> interpreters = 
//			ActionFactory.getInterpreters();

	private static HashMap<Action, ScriptableInstance> actionMap =
			new HashMap<Action, ScriptableInstance>();

	private static HashMap<Integer, ActionInterpreter<?>> interperaterMap =
			new HashMap<Integer, ActionInterpreter<?>>();

//	protected Class<?> getActionClass() {
//		return getActionClass(getClass());
//	}

//	private static Class<?> getActionClass(Class<?> cls) {
//		ParameterizedType parameterizedType =
//				(ParameterizedType) cls.getGenericSuperclass();
//		return (Class<?>) parameterizedType.getActualTypeArguments()[0];
//	}

	protected abstract void interperate(T action, PlatformGameState gameState) 
			throws ParameterException;

	protected void updateControl(ActionControl control, Action action) {
		control.actionIndex++;
	}
	
	public interface WaitChecker {
		public boolean isWaiting(PlatformGameState gameState);
	}
	
	protected static WaitChecker waitChecker;
		
	public static WaitChecker interperate(Action action, PlatformGameState gameState,
			ActionControl control) throws ParameterException {
		
		waitChecker = null;
		
		ScriptableInstance instance = actionMap.get(action);
		if (instance == null) {
			instance = ActionFactory.getActionInstance(action.id);
			if (instance == null) throw new RuntimeException(
					"Invalid action id: " + action.id);
			instance.setParameters(action.params);
			actionMap.put(action, instance);
		}

		ActionInterpreter<?> interp = interperaterMap.get(action.id);
		if (interp == null) {
			interp = ActionFactory.getInterpreterInstance(action.id);
			if (interp == null) throw new RuntimeException(
					"No interpreter for action #" + action.id);
			interperaterMap.put(action.id, interp);
		}


		invoke(instance.getClass(), interp, instance, gameState);
		interp.updateControl(control, action);
		
		return waitChecker;
	}

	@SuppressWarnings("unchecked")
	private static <S extends ScriptableInstance> void invoke(Class<S> cls, 
			ActionInterpreter<?> interp, ScriptableInstance instance,
			PlatformGameState gameState) throws ParameterException {
		ActionInterpreter<S> castInterp = (ActionInterpreter<S>)interp;
		S castAction = (S)instance;
		castInterp.interperate(castAction, gameState);
	}
}
