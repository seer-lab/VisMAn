import java.util.List;

import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.AvailiblePinsException;
import net.heinke.cbingutter.game.exception.NegativeScoreException;
import net.heinke.cbingutter.game.exception.TooManyShotsException;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.frame.RegularFrame;
import net.heinke.cbingutter.game.frame.SpareBonusDummyFrame;
import net.heinke.cbingutter.game.player.Player;
import net.heinke.cbingutter.game.scoreboard.Validator;

import org.junit.Assert;
import org.junit.Test;


public class CombinedTests {
	
	@Test(expected = AvailiblePinsException.class)
	public void testNotEnoughPinsDummy() throws Exception{
		SpareBonusDummyFrame f = new SpareBonusDummyFrame();
		f.playShot(11);
	}
	
	@Test(expected = NegativeScoreException.class)
	public void testNegativeScoreDummy() throws Exception{
		SpareBonusDummyFrame f = new SpareBonusDummyFrame();
		f.playShot(-5);
	}
	
	@Test
	public void testStrikeDummy() throws Exception{
		SpareBonusDummyFrame f = new SpareBonusDummyFrame();
		f.playShot(10);
		Assert.assertTrue(f.isStrike());
		Assert.assertTrue(f.isSpareOrStrike());
		Assert.assertTrue(!f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 0);
		Assert.assertTrue(f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 1);
	}

	@Test
	public void testSingleShotDummy() throws Exception{
		SpareBonusDummyFrame f = new SpareBonusDummyFrame();
		f.playShot(4);
		Assert.assertTrue(!f.isStrike());
		Assert.assertTrue(!f.isSpareOrStrike());
		Assert.assertTrue(!f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 6);
		Assert.assertTrue(f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 1);
	}
	
	@Test(expected = AvailiblePinsException.class)
	public void testNotEnoughPinsFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(5);
		f.playShot(10);
	}
	
	@Test(expected = NegativeScoreException.class)
	public void testNegativeScoreFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(-5);
	}
	
	@Test(expected = TooManyShotsException.class)
	public void testTooManyBowlsFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(5);
		f.playShot(1);
		f.playShot(1);
	}
	
	@Test
	public void testStrikeFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(10);
		Assert.assertTrue(f.isStrike());
		Assert.assertTrue(f.isSpareOrStrike());
		Assert.assertTrue(!f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 0);
		Assert.assertTrue(f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 1);
	}
	
	@Test
	public void testSpareFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(6);
		f.playShot(4);
		Assert.assertTrue(!f.isStrike());
		Assert.assertTrue(f.isSpareOrStrike());
		Assert.assertTrue(f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 0);
		Assert.assertTrue(f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 2);
	}
	
	@Test
	public void testNonStrikeOrSpareFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(4);
		f.playShot(2);
		Assert.assertTrue(!f.isStrike());
		Assert.assertTrue(!f.isSpareOrStrike());
		Assert.assertTrue(!f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 4);
		Assert.assertTrue(f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 2);
	}
	
	@Test
	public void testSingleShotFrame() throws Exception{
		RegularFrame f = new RegularFrame();
		f.playShot(4);
		Assert.assertTrue(!f.isStrike());
		Assert.assertTrue(!f.isSpareOrStrike());
		Assert.assertTrue(!f.isSpare());
		Assert.assertEquals(f.pinsRemaining(), 6);
		Assert.assertTrue(!f.isCompleted());
		Assert.assertEquals(f.getShotsTaken(), 1);
	}
	
	@Test
	public void testFinalFrameAllStrikePlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(10);
		Assert.assertEquals(a.getRunningTotal(), 30);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalFrameStrikeSparePlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertEquals(a.getRunningTotal(), 20);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalFrameStrikePinsRemainPlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(4);
		Assert.assertEquals(a.getRunningTotal(), 19);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalFrameStrikeStrikeNormalPlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(10);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertEquals(a.getRunningTotal(), 25);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalFrameSpareStrikePlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(10);
		Assert.assertEquals(a.getRunningTotal(), 20);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalFrameSpareNormalPlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(5);
		Assert.assertEquals(a.getRunningTotal(), 15);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	@Test
	public void testFinalNormalPlayer() throws Exception{
		Player a = new Player("steve", 1);
		a.playShot(5);
		Assert.assertTrue(!a.hasConcludedTurn());
		a.playShot(4);
		Assert.assertEquals(a.getRunningTotal(), 9);
		Assert.assertTrue(a.hasConcludedTurn());
	}
	
	@Test
	public void testLookbackStrikeSparePlayer() throws Exception{
		Player a = new Player("steve", 4);
		a.playShot(10);
		a.playShot(10);
		a.playShot(5);
		a.playShot(5);
		List<Frame> frames =  a.getFrames();
		Assert.assertEquals(frames.get(0).getAbsoluteScore(), 25);
		Assert.assertEquals(frames.get(1).getAbsoluteScore(), 20);
		Assert.assertEquals(frames.get(2).getAbsoluteScore(), Constants.DUMMY_ABS_SCORE);
	}
	@Test
	public void testLookbackSpareNormalPlayer() throws Exception{
		Player a = new Player("steve", 4);
		a.playShot(10);
		a.playShot(5);
		a.playShot(5);
		a.playShot(5);
		a.playShot(4);
		List<Frame> frames =  a.getFrames();
		Assert.assertEquals(frames.get(0).getAbsoluteScore(), 20);
		Assert.assertEquals(frames.get(1).getAbsoluteScore(), 15);
		Assert.assertEquals(frames.get(2).getAbsoluteScore(), 9);
	}
	
	@Test
	public void testNegativeScoreValidator(){
		Player a = new Player("Tim");
		boolean result = Validator.validateScore("-1", a);
		Assert.assertTrue(!result);
	}
	
	@Test
	public void testRandomStringValidator(){
		Player a = new Player("Tim");
		boolean result = Validator.validateScore("bazinga", a);
		Assert.assertTrue(!result);
	}
	@Test
	public void testTooManyPinsValidator(){
		Player a = new Player("Tim");
		boolean result = Validator.validateScore("11", a);
		Assert.assertTrue(!result);
	}
	@Test
	public void testNotEnoughRemainingPinsValidator() throws Exception{
		Player a = new Player("Tim");
		a.playShot(4);
		boolean result = Validator.validateScore("7", a);
		Assert.assertTrue(!result);
	}
	
	@Test
	public void testStrikeSecondShotValidator() throws Exception{
		Player a = new Player("Tim");
		a.playShot(4);
		boolean result = Validator.validateScore("X", a);
		Assert.assertTrue(!result);
	}
	@Test
	public void testSpareFirstShotValidator() throws Exception{
		Player a = new Player("Tim");
		boolean result = Validator.validateScore("/", a);
		Assert.assertTrue(!result);
	}
	

}
