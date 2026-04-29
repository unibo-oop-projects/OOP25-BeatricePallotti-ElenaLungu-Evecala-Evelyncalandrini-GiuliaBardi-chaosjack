package it.unibo.chaoskjack.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.api.Table.State;
import it.unibo.chaosjack.model.api.Wallet;
import it.unibo.chaosjack.model.impl.TableImpl;

public class TableTest {
    private Table table;
    private Wallet wallet;
    private final List<String> players = List.of("Marameo", "Bob");
    
    @BeforeEach
    void setUp(){
        wallet = new Wallet() {
            private int balance = 2000;
            @Override
            public int getBalance() {return balance;}
            @Override
            public void addFunds(int amount){balance += amount;}
            @Override
            public boolean removeFunds(int amount){
                if (balance >= amount) {balance -= amount; return true;}
                return false;
            }
        };
        /* 
        gameEngine fakeEngine = new gameEngine() {
            @Override public deck getDeck() { return null; }
            @Override public hand getPlayerHand() { return () -> 20; }
            @Override public hand getDealerHand() { return () -> 17; }
            @Override public void changeState(turnState s) {}
        }

        */
        table = new TableImpl(wallet, players);
        // table = new TableImpl(wallet, players, fakeEngine);
    }

    @Test
    void testInitialState(){
        assertEquals(State.FIRST_BET, table.getCurrentState());
        assertEquals(0, table.getPot(), "the pot must be 0");
        assertEquals(1,table.getRoundCount());
    }

    @Test
    void testBettingLogic() {
        table.placeBet("Marameo", 200);
        assertEquals(200, table.getPot());
        assertEquals(1800, wallet.getBalance(), "the account holder balance must decrease");
        assertThrows(IllegalArgumentException.class, () ->table.placeBet("Marameo", -50));
    }

    @Test
    void testStepPassageValidation() {
        assertThrows(IllegalStateException.class, () -> table.stepPassage());

        table.placeBet("Bob", 100);
        table.stepPassage();
        assertEquals(State.PLAYING, table.getCurrentState());
    }

    @Test
    void testBettingInWrongState() {
        table.placeBet("Bob", 100);
        table.stepPassage();
        assertThrows(IllegalStateException.class, () -> table.placeBet("Marameo", 50));
    }

    @Test
    void testInsufficientFunds() {
        assertThrows(IllegalArgumentException.class, () -> table.placeBet("Bob", 5000));
        assertEquals(0, table.getPot(), "The plate must not increase if the funds are insufficient");
    }
    /* 
    @Test
    void testWinnerPlayerWins() {
        gameEngine fakeEngine = new gameEngine() {
            @Override public deck getDeck() { return null; }
            @Override public hand getPlayerHand() { return () -> 20; }
            @Override public hand getDealerHand() { return () -> 17; }
            @Override public void changeState(turnState s) {}
        }
        table = new TableImpl(wallet, players, fakeEngine);

        table.placeBet("Marameo", 100)
        table.stepPassege();
        RoundResult result = table.getWinner();
        assertEquals(Outcome.PLAYER_WON, result.getOutcome());
        assertEquals(200, result.getPayOut(), "the wins must be double value of the pot");
    }

    @Test
    void 
    */
    @Test
    void testOtherGame() {
        table.placeBet("Marameo", 100);
        table.otherGame();

        assertEquals(0, table.getPot(), "the pot must be empty");
        assertEquals(2, table.getRoundCount(), "the counter od the round must be incremented");
        assertEquals(State.FIRST_BET, table.getCurrentState());
    }

    @Test
    void testReset() {
        table.placeBet("Marameo", 100);
        table.reset();

        assertEquals(0, table.getPot());
        assertEquals(1, table.getRoundCount(), "The round must return a one round");
    }


}
