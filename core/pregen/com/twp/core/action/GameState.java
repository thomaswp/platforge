package com.twp.core.action;

import edu.elon.honors.price.data.*;
import edu.elon.honors.price.data.types.*;
import edu.elon.honors.price.data.Event.Parameters.Iterator;
import edu.elon.honors.price.data.Event.Parameters;
import edu.elon.honors.price.physics.*;
import com.twp.core.input.*;
import com.twp.core.platform.*;
@SuppressWarnings("unused")
public interface GameState {
	public UIControl readUi(Parameters params) throws ParameterException;
	public com.twp.core.game.Rect readRegion(Parameters params) throws ParameterException;
	public JoyStick readJoystick(int params) throws ParameterException;
	public boolean readSwitch(Switch params) throws ParameterException;
	public ObjectClass readObjectClass(ObjectClassPointer params) throws ParameterException;
	public int readNumber(Parameters params) throws ParameterException;
	public Point readPoint(Parameters params) throws ParameterException;
	public Button readButton(int params) throws ParameterException;
	public Event readEvent(Object params) throws ParameterException;
	public Vector readVector(Parameters params) throws ParameterException;
	public ActorClass readActorClass(ActorClassPointer params) throws ParameterException;
	public ObjectBody readObjectInstance(Parameters params) throws ParameterException;
	public boolean readBoolean(Parameters params) throws ParameterException;
	public ActorBody readActorInstance(Parameters params) throws ParameterException;
	public int readVariable(Variable params) throws ParameterException;
}
