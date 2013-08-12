// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.exception;


public class BowlingException extends java.lang.Exception
{

    protected java.lang.String message;

    public BowlingException( java.lang.String message )
    {
        this.message = message;
    }

    public BowlingException()
    {
    }

    public  java.lang.String getMessage()
    {
        return message;
    }

}
