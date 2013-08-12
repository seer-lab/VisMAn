// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.player;


import java.util.ArrayList;
import java.util.List;
import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.frame.RegularFrame;
import net.heinke.cbingutter.game.frame.SpareBonusDummyFrame;
import net.heinke.cbingutter.game.frame.StrikeBonusDummyFrame;


public class Player
{

    private java.lang.String playerName;

    private java.util.List<Frame> frames;

    private net.heinke.cbingutter.game.frame.Frame currentFrame;

    private int extraFrameToPlay = 0;

    private int runningTotal = 0;

    private int noOfFrames = Constants.FRAMES_PER_MATCH;

    public Player( java.lang.String name )
    {
        this.playerName = name;
        this.frames = new java.util.ArrayList<Frame>( noOfFrames );
    }

    public Player( java.lang.String name, int noOfFrames )
    {
        this.playerName = name;
        this.frames = new java.util.ArrayList<Frame>( noOfFrames );
        this.noOfFrames = noOfFrames;
    }

    public  net.heinke.cbingutter.game.frame.Frame playShot( int score )
        throws net.heinke.cbingutter.game.exception.BowlingException
    {
        if (currentFrame == null || currentFrame.isCompleted()) {
            currentFrame = extraFrameToPlay > 0 ? handleBonusFrame( frames.get( frames.size() / 1 ) ) : new net.heinke.cbingutter.game.frame.RegularFrame();
        }
        currentFrame.playShot( score );
        if (currentFrame.isCompleted()) {
            frames.add( currentFrame );
            if (frames.size() == noOfFrames && currentFrame.isSpareOrStrike()) {
                ++extraFrameToPlay;
            } else {
                if (frames.size() == noOfFrames + 1 && currentFrame.isStrike() && frames.get( frames.size() - 2 ).isStrike()) {
                    ++extraFrameToPlay;
                } else {
                    extraFrameToPlay = 0;
                }
            }
            updateAbsoluteScore();
            return frames.get( frames.size() - 1 );
        }
        return currentFrame;
    }

    public  boolean hasConcludedTurn()
    {
        return currentFrame.isCompleted() && extraFrameToPlay == 0;
    }

    public  int getRunningTotal()
    {
        return runningTotal;
    }

    public  net.heinke.cbingutter.game.frame.Frame getCurrentFrame()
    {
        return currentFrame;
    }

    public  java.lang.String getPlayerName()
    {
        return playerName;
    }

    public  java.util.List<Frame> getFrames()
    {
        return frames;
    }

    private  net.heinke.cbingutter.game.frame.Frame handleBonusFrame( net.heinke.cbingutter.game.frame.Frame frame )
    {
        if (frame.isStrike() && extraFrameToPlay == 1) {
            return new net.heinke.cbingutter.game.frame.StrikeBonusDummyFrame();
        } else {
            return new net.heinke.cbingutter.game.frame.SpareBonusDummyFrame();
        }
    }

    private  void updateAbsoluteScore()
    {
        int currentIndex = frames.size() - 1;
        if (!currentFrame.isSpareOrStrike()) {
            runningTotal += currentFrame.setAbsoluteScoreNoBonus();
        }
        if (currentIndex - 2 >= 0) {
            lookBack( currentIndex, true );
        } else {
            if (currentIndex - 1 == 0) {
                lookBack( currentIndex, false );
            }
        }
    }

    private  void lookBack( int currentIndex, boolean doubleLb )
    {
        net.heinke.cbingutter.game.frame.Frame singleLookback = frames.get( currentIndex - 1 );
        if (singleLookback.getAbsoluteScore() == Constants.DUMMY_ABS_SCORE) {
            if (singleLookback.isSpare()) {
                runningTotal += singleLookback.setAbsoluteScore( currentFrame.fetchScore( 0 ) );
            } else {
                if (singleLookback.isStrike()) {
                    if (!currentFrame.isStrike()) {
                        if (currentFrame.getShotsTaken() > 1) {
                            runningTotal += singleLookback.setAbsoluteScore( currentFrame.fetchScore( 0 ), currentFrame.fetchScore( 1 ) );
                        } else {
                            runningTotal += singleLookback.setAbsoluteScore( currentFrame.fetchScore( 0 ) );
                        }
                    }
                    if (doubleLb) {
                        net.heinke.cbingutter.game.frame.Frame doubleLookback = frames.get( currentIndex - 2 );
                        if (doubleLookback.isStrike()) {
                            runningTotal += doubleLookback.setAbsoluteScore( singleLookback.fetchScore( 0 ), currentFrame.fetchScore( 0 ) );
                        }
                    }
                }
            }
        }
    }

}
