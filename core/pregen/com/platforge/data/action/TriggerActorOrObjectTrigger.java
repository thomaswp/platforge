package com.platforge.data.action;

import com.platforge.data.*;
import com.platforge.data.Event.Parameters;
import com.platforge.data.Event.Parameters.Iterator;
import com.platforge.data.types.*;
import com.platforge.physics.*;
import com.platforge.player.core.action.ScriptableFragment;
import com.platforge.player.core.action.ScriptableInstance;
import com.platforge.player.core.input.*;
import com.platforge.player.core.platform.*;

@SuppressWarnings("unused")
public class TriggerActorOrObjectTrigger extends ScriptableInstance {
	public static final String NAME = "Actor or Object Trigger";
	public static final int ID = 2;
	public static final String CATEGORY = null;
	
	/** Type: <b>&lt;body&gt;</b> */
	public Parameters body;
	public boolean whenCollidesWith;
	public WhenCollidesWithData whenCollidesWithData;
	public class WhenCollidesWithData extends ScriptableFragment {
		public boolean collidesWithHero;
		public boolean collidesWithActor;
		public boolean collidesWithObject;
		public boolean collidesWithWall;
		public boolean collidesWithLedge;
		
		@Override
		public void readParams(Iterator iterator) {
			int collidesWith = iterator.getInt();
			collidesWithHero = collidesWith == 0;
			collidesWithActor = collidesWith == 1;
			collidesWithObject = collidesWith == 2;
			collidesWithWall = collidesWith == 3;
			collidesWithLedge = collidesWith == 4;
			
		}
		/**
		 * <ul>
		 * <li><b>&lt;radio&gt;</b> collidesWith</i>:</li><ul>
		 * <li>collidesWithHero:</li>
		 * <li>collidesWithActor:</li>
		 * <li>collidesWithObject:</li>
		 * <li>collidesWithWall:</li>
		 * <li>collidesWithLedge:</li>
		 * </ul>
		 * </ul>
		 */
		public static final String JAVADOC = "";
	}
	
	public boolean whenIsCreated;
	public boolean whenIsDestroyed;
	
	public TriggerActorOrObjectTrigger() {
		whenCollidesWithData = new WhenCollidesWithData();
	}
	
	@Override
	public void readParams(Iterator iterator) {
		body = iterator.getParameters();
		int when = iterator.getInt();
		whenCollidesWith = when == 0;
		if (whenCollidesWith) whenCollidesWithData.readParams(iterator);
		whenIsCreated = when == 1;
		whenIsDestroyed = when == 2;
		
	}
	/**
	 * 002 <b><i>Actor or Object Trigger</i></b> (null)<br />
	 * <ul>
	 * <li><b>&lt;body&gt;</b> body</li>
	 * <li><b>&lt;radio&gt;</b> when</i>:</li><ul>
	 * <li>whenCollidesWith:</li>
	 * <ul>
	 * <li><b>&lt;radio&gt;</b> collidesWith</i>:</li><ul>
	 * <li>collidesWithHero:</li>
	 * <li>collidesWithActor:</li>
	 * <li>collidesWithObject:</li>
	 * <li>collidesWithWall:</li>
	 * <li>collidesWithLedge:</li>
	 * </ul>
	 * </ul>
	 * <li>whenIsCreated:</li>
	 * <li>whenIsDestroyed:</li>
	 * </ul>
	 * </ul>
	 */
	public static final String JAVADOC = "";
}
