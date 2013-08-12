// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.exception;


public class TooManyShotsException extends net.heinke.cbingutter.game.exception.BowlingException
{

    public TooManyShotsException()
    {
        message = "Number of shot taken exceeds the per frame limit";
    }

}
