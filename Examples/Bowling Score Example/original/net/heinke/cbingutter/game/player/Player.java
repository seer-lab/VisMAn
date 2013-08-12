package net.heinke.cbingutter.game.player;

import java.util.ArrayList;
import java.util.List;

import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.frame.RegularFrame;
import net.heinke.cbingutter.game.frame.SpareBonusDummyFrame;
import net.heinke.cbingutter.game.frame.StrikeBonusDummyFrame;


public class Player{
	private String playerName;
	private List<Frame> frames;
	private Frame currentFrame;
	private int extraFrameToPlay = 0;
	private int runningTotal = 0;
	private int noOfFrames = Constants.FRAMES_PER_MATCH;
	
	public Player(String name){
		this.playerName = name;
		this.frames = new ArrayList<Frame>(noOfFrames);
	}
	
	public Player(String name, int noOfFrames){
		this.playerName = name;
		this.frames = new ArrayList<Frame>(noOfFrames);
		this.noOfFrames = noOfFrames;
	}
	
	public Frame playShot(int score) throws BowlingException{
		if(currentFrame == null || currentFrame.isCompleted()){
			currentFrame = extraFrameToPlay > 0  ? handleBonusFrame(frames.get(frames.size() - 1)) : new RegularFrame();
		}
		currentFrame.playShot(score);
		if(currentFrame.isCompleted()){
			frames.add(currentFrame);
			if(frames.size() == noOfFrames
					&& currentFrame.isSpareOrStrike()){
				++extraFrameToPlay;
			}
			else if(frames.size() == noOfFrames +1
					&& currentFrame.isStrike() && frames.get(frames.size() - 2).isStrike()){
				++extraFrameToPlay;
			}else{
				extraFrameToPlay=0;
			}
			updateAbsoluteScore();
			return frames.get(frames.size() - 1);
		}
		return currentFrame;
	}
	
	public boolean hasConcludedTurn(){
		return currentFrame.isCompleted() && extraFrameToPlay == 0;
	}
	
	public int getRunningTotal(){
		return runningTotal;
	}
	
	public Frame getCurrentFrame(){
		return currentFrame;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public List<Frame> getFrames() {
		return frames;
	}

	private Frame handleBonusFrame(Frame frame){
		
		if(frame.isStrike() && extraFrameToPlay == 1)
			return new StrikeBonusDummyFrame();
		else{
			return new SpareBonusDummyFrame();
		}
			
	}
	
	private void updateAbsoluteScore(){
		int currentIndex = frames.size() - 1;
		
		if(!currentFrame.isSpareOrStrike()){
			runningTotal += currentFrame.setAbsoluteScoreNoBonus();
		}
		if(currentIndex - 2 >= 0){
			lookBack(currentIndex, true);
		}
		else if(currentIndex - 1 == 0){
			lookBack(currentIndex, false);
		}
			
	}
	
	private void lookBack(int currentIndex, boolean doubleLb){
		Frame singleLookback = frames.get(currentIndex - 1);
		if(singleLookback.getAbsoluteScore() == Constants.DUMMY_ABS_SCORE){
			if(singleLookback.isSpare()){
				runningTotal += singleLookback.setAbsoluteScore(currentFrame.fetchScore(0));
			}
			else if(singleLookback.isStrike()){
				if(!currentFrame.isStrike()){
					if(currentFrame.getShotsTaken() > 1)
						runningTotal += singleLookback.setAbsoluteScore(currentFrame.fetchScore(0), currentFrame.fetchScore(1));
					else
						runningTotal += singleLookback.setAbsoluteScore(currentFrame.fetchScore(0));
				}
				if(doubleLb){
					Frame doubleLookback = frames.get(currentIndex - 2);
					if(doubleLookback.isStrike()){
						runningTotal += doubleLookback.setAbsoluteScore(singleLookback.fetchScore(0), currentFrame.fetchScore(0));
					}
				}
			}
		}
	}


	
	
}
