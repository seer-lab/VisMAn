// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.exception;


public class NegativeScoreException extends net.heinke.cbingutter.game.exception.BowlingException
{

    public NegativeScoreException()
    {
        message = "A negative score is not valid";
    }

}
