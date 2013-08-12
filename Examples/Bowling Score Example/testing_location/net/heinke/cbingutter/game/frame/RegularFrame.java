package net.heinke.cbingutter.game.frame;

import java.util.ArrayList;
import java.util.List;

import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.AvailiblePinsException;
import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.exception.NegativeScoreException;
import net.heinke.cbingutter.game.exception.TooManyShotsException;


public class RegularFrame implements Frame{

	protected List<Integer> bowls;
	private int runningRawScore = 0;
	private int absoluteScore = Constants.DUMMY_ABS_SCORE;
	
	public RegularFrame(int bowlsPerFrame){
		bowls = new ArrayList<Integer>(bowlsPerFrame);
	}
	
	public RegularFrame(){
		bowls = new ArrayList<Integer>(Constants.SHOTS_PER_FRAME);
	}
	
	@Override
	public void playShot(int score) throws BowlingException {
		if(score < 0)
			throw new NegativeScoreException();
		else if(score > Constants.MAX_PINS_PER_FRAME ||
				score > Constants.MAX_PINS_PER_FRAME - runningRawScore)
			throw new AvailiblePinsException();
		else if(bowls.size() >= Constants.SHOTS_PER_FRAME)
			throw new TooManyShotsException();
		
		bowls.add(score);
		runningRawScore += score;
	}

	@Override
	public int fetchScore(int index) {
		return bowls.get(index);
	}

	@Override
	public boolean isStrike() {
		return isSpareOrStrike() &&
				bowls.size() == 1;
	}

	@Override
	public boolean isSpare() {
		return isSpareOrStrike() &&
				bowls.size() > 1;
	}

	@Override
	public boolean isSpareOrStrike() {
		return runningRawScore == Constants.MAX_PINS_PER_FRAME;
	}

	@Override
	public boolean isCompleted() {
		if(isSpareOrStrike())
			return true;
		return bowls.size() == Constants.SHOTS_PER_FRAME;
	}

	@Override
	public int setAbsoluteScore(int score1, int score2) {
		absoluteScore = runningRawScore;
		if(isStrike())
			absoluteScore += score1 + score2;
		return absoluteScore;
	}

	@Override
	public int setAbsoluteScore(int score) {
		absoluteScore = runningRawScore;
		if(isSpare())
			absoluteScore += score;
		return absoluteScore;
	}

	@Override
	public int setAbsoluteScoreNoBonus() {
		absoluteScore = runningRawScore;
		return absoluteScore;
	}

	@Override
	public int getAbsoluteScore() {
		return absoluteScore;
	}
	
	@Override
	public String printAbsoluteScore() {
		
		return absoluteScore == Constants.DUMMY_ABS_SCORE ? "" : new Integer(absoluteScore).toString();
	}

	@Override
	public int getShotsTaken() {
		return bowls.size();
	}
	
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		int runTot = 0;
		for(Integer bowl : bowls){
			runTot += bowl;
			if(bowl == Constants.MAX_PINS_PER_FRAME){
				b.append("X");
			}
			else if(runTot == Constants.MAX_PINS_PER_FRAME){
				b.append("/");
			}
			else if(bowl == 0){
				b.append("-");
			}
			else{
				b.append(bowl);
			}
			b.append(" ");
		}
		return b.toString().trim();
	}
	
	@Override
	public int pinsRemaining(){
		return Constants.MAX_PINS_PER_FRAME - runningRawScore;
	}
}
