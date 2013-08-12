package net.heinke.cbingutter.game.frame;

import net.heinke.cbingutter.game.Constants;


public class SpareBonusDummyFrame extends RegularFrame{
	public SpareBonusDummyFrame() {
		super(Constants.BONUS_SHOTS_FINAL_FRAME_SPARE);
	}


	@Override
	public boolean isCompleted() {
		return super.bowls.size() == Constants.BONUS_SHOTS_FINAL_FRAME_SPARE;
	}
	
	@Override
	public int setAbsoluteScore(int score){
		return 0;
	}
	
	@Override
	public int setAbsoluteScoreNoBonus(){
		return 0;
	}

}