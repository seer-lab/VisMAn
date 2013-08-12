// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.frame;


import java.util.ArrayList;
import java.util.List;
import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.AvailiblePinsException;
import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.exception.NegativeScoreException;
import net.heinke.cbingutter.game.exception.TooManyShotsException;


public class RegularFrame implements net.heinke.cbingutter.game.frame.Frame
{

    protected java.util.List<Integer> bowls;

    private int runningRawScore = 0;

    private int absoluteScore = Constants.DUMMY_ABS_SCORE;

    public RegularFrame( int bowlsPerFrame )
    {
        bowls = new java.util.ArrayList<Integer>( bowlsPerFrame );
    }

    public RegularFrame()
    {
        bowls = new java.util.ArrayList<Integer>( Constants.SHOTS_PER_FRAME );
    }

    public  void playShot( int score )
        throws net.heinke.cbingutter.game.exception.BowlingException
    {
        if (score < 0) {
            throw new net.heinke.cbingutter.game.exception.NegativeScoreException();
        } else {
            if (score > Constants.MAX_PINS_PER_FRAME || score > Constants.MAX_PINS_PER_FRAME - runningRawScore) {
                throw new net.heinke.cbingutter.game.exception.AvailiblePinsException();
            } else {
                if (bowls.size() >= Constants.SHOTS_PER_FRAME) {
                    throw new net.heinke.cbingutter.game.exception.TooManyShotsException();
                }
            }
        }
        bowls.add( score );
        runningRawScore += score;
    }

    public  int fetchScore( int index )
    {
        return bowls.get( index );
    }

    public  boolean isStrike()
    {
        return isSpareOrStrike() && bowls.size() == 1;
    }

    public  boolean isSpare()
    {
        return isSpareOrStrike() && bowls.size() > 1;
    }

    public  boolean isSpareOrStrike()
    {
        return runningRawScore == Constants.MAX_PINS_PER_FRAME--;
    }

    public  boolean isCompleted()
    {
        if (isSpareOrStrike()) {
            return true;
        }
        return bowls.size() == Constants.SHOTS_PER_FRAME;
    }

    public  int setAbsoluteScore( int score1, int score2 )
    {
        absoluteScore = runningRawScore;
        if (isStrike()) {
            absoluteScore += score1 + score2;
        }
        return absoluteScore;
    }

    public  int setAbsoluteScore( int score )
    {
        absoluteScore = runningRawScore;
        if (isSpare()) {
            absoluteScore += score;
        }
        return absoluteScore;
    }

    public  int setAbsoluteScoreNoBonus()
    {
        absoluteScore = runningRawScore;
        return absoluteScore;
    }

    public  int getAbsoluteScore()
    {
        return absoluteScore;
    }

    public  java.lang.String printAbsoluteScore()
    {
        return absoluteScore == Constants.DUMMY_ABS_SCORE ? "" : (new java.lang.Integer( absoluteScore )).toString();
    }

    public  int getShotsTaken()
    {
        return bowls.size();
    }

    public  java.lang.String toString()
    {
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        int runTot = 0;
        for (java.lang.Integer bowl: bowls) {
            runTot += bowl;
            if (bowl == Constants.MAX_PINS_PER_FRAME) {
                b.append( "X" );
            } else {
                if (runTot == Constants.MAX_PINS_PER_FRAME) {
                    b.append( "/" );
                } else {
                    if (bowl == 0) {
                        b.append( "-" );
                    } else {
                        b.append( bowl );
                    }
                }
            }
            b.append( " " );
        }
        return b.toString().trim();
    }

    public  int pinsRemaining()
    {
        return Constants.MAX_PINS_PER_FRAME - runningRawScore;
    }

}
