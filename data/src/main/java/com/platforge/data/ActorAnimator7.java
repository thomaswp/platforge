package com.platforge.data;

import com.platforge.data.ActorAnimator;

public class ActorAnimator7 extends ActorAnimator {

	@Override
	protected ActionParams createActionParams(Action action) {
		return new ActionParams(action.ordinal(), 0, 8);
	}

	@Override
	public int getTotalRows() {
		return 7;
	}

	@Override
	public int getTotalCols() {
		return 8;
	}

	@Override
	protected int getJumpHold() {
		return 3;
	}

}
