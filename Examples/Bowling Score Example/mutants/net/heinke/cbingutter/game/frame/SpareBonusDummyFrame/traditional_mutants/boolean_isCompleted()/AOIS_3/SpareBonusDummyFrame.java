// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.frame;


import net.heinke.cbingutter.game.Constants;


public class SpareBonusDummyFrame extends net.heinke.cbingutter.game.frame.RegularFrame
{

    public SpareBonusDummyFrame()
    {
        super( Constants.BONUS_SHOTS_FINAL_FRAME_SPARE );
    }

    public  boolean isCompleted()
    {
        return super.bowls.size() == ++Constants.BONUS_SHOTS_FINAL_FRAME_SPARE;
    }

    public  int setAbsoluteScore( int score )
    {
        return 0;
    }

    public  int setAbsoluteScoreNoBonus()
    {
        return 0;
    }

}
