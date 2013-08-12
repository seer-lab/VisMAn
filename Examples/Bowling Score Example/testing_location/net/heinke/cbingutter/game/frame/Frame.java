package net.heinke.cbingutter.game.frame;

import net.heinke.cbingutter.game.exception.BowlingException;

public interface Frame {
	public void playShot(int score) throws BowlingException;
	public int fetchScore(int index);
	public boolean isStrike();
	public boolean isSpare();
	public boolean isSpareOrStrike();
	public boolean isCompleted();
	public int setAbsoluteScore(int score1, int score2);
	public int setAbsoluteScore(int score);
	public int setAbsoluteScoreNoBonus();
	public int getAbsoluteScore();
	public int getShotsTaken();
	public int pinsRemaining();
	public String printAbsoluteScore();
}
