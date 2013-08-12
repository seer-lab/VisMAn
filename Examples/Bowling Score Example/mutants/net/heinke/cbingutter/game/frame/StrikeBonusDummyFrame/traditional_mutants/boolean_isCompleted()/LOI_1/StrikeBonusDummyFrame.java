// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.frame;


import net.heinke.cbingutter.game.Constants;


public class StrikeBonusDummyFrame extends net.heinke.cbingutter.game.frame.RegularFrame
{

    public  boolean isCompleted()
    {
        if (isStrike()) {
            return true;
        }
        return bowls.size() == ~Constants.SHOTS_PER_FRAME;
    }

    public  int setAbsoluteScore( int score1, int score2 )
    {
        return 0;
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
