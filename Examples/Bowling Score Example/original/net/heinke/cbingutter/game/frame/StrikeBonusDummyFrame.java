package net.heinke.cbingutter.game.frame;

import net.heinke.cbingutter.game.Constants;


public class StrikeBonusDummyFrame extends RegularFrame {
	@Override
	public boolean isCompleted() {
		if(isStrike())
			return true;
		return bowls.size() == Constants.SHOTS_PER_FRAME;
	}

	@Override
	public int setAbsoluteScore(int score1, int score2) {
		return 0;
	}

	@Override
	public int setAbsoluteScore(int score) {
		return 0;
	}

	@Override
	public int setAbsoluteScoreNoBonus() {
		return 0;
	}
}
