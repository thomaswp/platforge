package edu.elon.honors.price.data.types;

import edu.elon.honors.price.data.Behavior;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.game.Formatter;

public class Variable extends ScopedData<Variable> {
	private static final long serialVersionUID = 1L;

	public Variable(int id, DataScope scope) {
		super(id, scope);
	}
	
	public Variable() {
		this(0, DataScope.Global);
	}
	
	public String getName(PlatformGame game, Behavior behavior) {
		if (scope == DataScope.Global) {
			if (game != null) {
				return Formatter.format("%03d: %s", id, 
						game.variableNames[id]);
			}
		} else if (scope == DataScope.Local) {
			if (behavior != null) {
				return "[" + behavior.variableNames.get(id) + "]";
			}
		} else if (scope == DataScope.Param){
			if (behavior != null) {
				return "{" + behavior.parameters.get(id).name + "}";
			}
		}
		return "<None>";
	}
	
	@Override 
	public String toString() {
		return Formatter.format("[V[%d,%d]", id, scope.toInt());
	}

	@Override
	public Variable copy() {
		return new Variable(id, scope);
	}
}
