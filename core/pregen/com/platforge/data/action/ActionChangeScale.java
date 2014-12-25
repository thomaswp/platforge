package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.physics.*;
import com.platforge.player.core.action.ParameterException;
import com.platforge.player.core.action.ScriptableFragment;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class ActionChangeScale extends ScriptableInstance {
	public static final String NAME = "Change Scale";
	public static final int ID = 26;
	public static final String CATEGORY = "Actor|Object";
	
	public boolean ofTheActor;
	public OfTheActorData ofTheActorData;
	public class OfTheActorData extends ScriptableFragment {
		/** Type: <b>&lt;actorInstance&gt;</b> */
		public Parameters actorInstance;
		public ActorBody readActorInstance(GameState gameState) throws ParameterException {
			return gameState.readActorInstance(actorInstance);
		}
		
		@Override
		public void readParams(Iterator iterator) {
			actorInstance = iterator.getParameters();
		}
		/**
		 * <ul>
		 * <li><b>&lt;actorInstance&gt;</b> actorInstance</li>
		 * </ul>
		 */
		public static final String JAVADOC = "";
	}
	
	public boolean ofTheObject;
	public OfTheObjectData ofTheObjectData;
	public class OfTheObjectData extends ScriptableFragment {
		/** Type: <b>&lt;objectInstance&gt;</b> */
		public Parameters objectInstance;
		public ObjectBody readObjectInstance(GameState gameState) throws ParameterException {
			return gameState.readObjectInstance(objectInstance);
		}
		
		@Override
		public void readParams(Iterator iterator) {
			objectInstance = iterator.getParameters();
		}
		/**
		 * <ul>
		 * <li><b>&lt;objectInstance&gt;</b> objectInstance</li>
		 * </ul>
		 */
		public static final String JAVADOC = "";
	}
	
	/** Type: <b>&lt;seekBar&gt;</b> */
	public float scale;
	
	public ActionChangeScale() {
		ofTheActorData = new OfTheActorData();
		ofTheObjectData = new OfTheObjectData();
	}
	
	@Override
	public void readParams(Iterator iterator) {
		int of = iterator.getInt();
		ofTheActor = of == 0;
		if (ofTheActor) ofTheActorData.readParams(iterator);
		ofTheObject = of == 1;
		if (ofTheObject) ofTheObjectData.readParams(iterator);
		
		scale = iterator.getFloat();
	}
	/**
	 * 026 <b><i>Change Scale</i></b> (Actor|Object)<br />
	 * <ul>
	 * <li><b>&lt;radio&gt;</b> of</i>:</li><ul>
	 * <li>ofTheActor:</li>
	 * <ul>
	 * <li><b>&lt;actorInstance&gt;</b> actorInstance</li>
	 * </ul>
	 * <li>ofTheObject:</li>
	 * <ul>
	 * <li><b>&lt;objectInstance&gt;</b> objectInstance</li>
	 * </ul>
	 * </ul>
	 * <li><b>&lt;seekBar&gt;</b> scale</li>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
