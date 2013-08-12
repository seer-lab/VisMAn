// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.exception;


public class AvailiblePinsException extends net.heinke.cbingutter.game.exception.BowlingException
{

    public AvailiblePinsException()
    {
        message = "Not enough availible pins for the entered score";
    }

}
