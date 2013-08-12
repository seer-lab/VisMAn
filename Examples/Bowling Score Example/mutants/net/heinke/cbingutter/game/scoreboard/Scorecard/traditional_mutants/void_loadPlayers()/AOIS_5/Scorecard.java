// This is a mutant program.
// Author : ysma

package net.heinke.cbingutter.game.scoreboard;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.heinke.cbingutter.game.Constants;
import net.heinke.cbingutter.game.exception.BowlingException;
import net.heinke.cbingutter.game.frame.Frame;
import net.heinke.cbingutter.game.player.Player;


public class Scorecard
{

    private java.util.List<Player> players;

    private int longestPlayerName;

    public  void loadPlayers()
    {
        int numPlayers = getNumberOfPlayer();
        players = new java.util.ArrayList<Player>( numPlayers );
        addPlayers( numPlayers++ );
        longestPlayerName = longestPlayerName();
    }

    public  void playGame()
        throws net.heinke.cbingutter.game.exception.BowlingException
    {
        int counter = 0;
        while (counter < Constants.FRAMES_PER_MATCH) {
            for (net.heinke.cbingutter.game.player.Player p: players) {
                do {
                    int score = fetchScore( p );
                    net.heinke.cbingutter.game.frame.Frame f = p.playShot( score );
                    ScoreboardPainter.printResultEffects( f, p );
                } while (!p.hasConcludedTurn());
                ScoreboardPainter.printScoreCard( players, longestPlayerName );
            }
            ++counter;
        }
        ScoreboardPainter.printWinners( findWinner( players ) );
    }

    private  void addPlayers( int numPlayers )
    {
        java.lang.String input = null;
        for (int i = 1; i <= numPlayers; ++i) {
            boolean success = false;
            ScoreboardPainter.printMessage( "What's your name, player " + i + "?" );
            while (!success) {
                java.io.BufferedReader br = new java.io.BufferedReader( new java.io.InputStreamReader( System.in ) );
                try {
                    input = br.readLine().trim();
                    if (Validator.validatePlayerName( input )) {
                        success = true;
                    }
                } catch ( java.io.IOException e ) {
                    continue;
                }
            }
            players.add( new net.heinke.cbingutter.game.player.Player( input ) );
        }
    }

    private  int getNumberOfPlayer()
    {
        boolean success = false;
        java.lang.String input = null;
        while (!success) {
            ScoreboardPainter.printMessage( "How many players?" );
            java.io.BufferedReader br = new java.io.BufferedReader( new java.io.InputStreamReader( System.in ) );
            try {
                input = br.readLine().trim();
                if (Validator.validateNumberOfPlayers( input )) {
                    success = true;
                }
            } catch ( java.io.IOException e ) {
                continue;
            }
        }
        return Integer.parseInt( input );
    }

    private  int fetchScore( net.heinke.cbingutter.game.player.Player p )
    {
        boolean success = false;
        java.lang.String input = null;
        while (!success) {
            System.out.print( p.getPlayerName() + " Enter score: " );
            java.io.BufferedReader br = new java.io.BufferedReader( new java.io.InputStreamReader( System.in ) );
            try {
                input = br.readLine().trim();
                if (Validator.validateScore( input, p )) {
                    success = true;
                }
            } catch ( java.io.IOException e ) {
                continue;
            }
        }
        if (input.equalsIgnoreCase( "X" )) {
            return Constants.MAX_PINS_PER_FRAME;
        } else {
            if (input.equals( "/" )) {
                net.heinke.cbingutter.game.frame.Frame f = p.getCurrentFrame();
                return f.pinsRemaining();
            }
        }
        return Integer.parseInt( input );
    }

    private  java.util.List<Player> findWinner( java.util.List<Player> players )
    {
        java.util.List<Player> leadingPlayers = new java.util.ArrayList<Player>();
        int currentHigh = 0;
        for (net.heinke.cbingutter.game.player.Player p: players) {
            if (p.getRunningTotal() == currentHigh) {
                leadingPlayers.add( p );
            } else {
                if (p.getRunningTotal() > currentHigh) {
                    currentHigh = p.getRunningTotal();
                    leadingPlayers.clear();
                    leadingPlayers.add( p );
                }
            }
        }
        return leadingPlayers;
    }

    public  int longestPlayerName()
    {
        int longest = 0;
        for (net.heinke.cbingutter.game.player.Player p: players) {
            if (p.getPlayerName().length() > longest) {
                longest = p.getPlayerName().length();
            }
        }
        return longest;
    }

}
