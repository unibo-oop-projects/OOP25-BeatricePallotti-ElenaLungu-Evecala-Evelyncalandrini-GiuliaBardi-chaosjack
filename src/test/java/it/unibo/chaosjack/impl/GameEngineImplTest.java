package it.unibo.chaosjack.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.impl.StandardDeck;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.Suit;
import it.unibo.chaosjack.model.impl.TableImpl;

import java.util.List;
import java.util.ArrayList;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.impl.GameEngineImpl;
import it.unibo.chaosjack.model.impl.NPCimpl;
import it.unibo.chaosjack.model.impl.PlayerImpl;
import it.unibo.chaosjack.model.api.Dealer;
import it.unibo.chaosjack.model.api.Partecipant;
import it.unibo.chaosjack.model.impl.DealerImpl;
import it.unibo.chaosjack.model.api.NPC;
import it.unibo.chaosjack.model.api.SpecialRound;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;

/**
 * Test for GameEngineImpl class.
 */
public class GameEngineImplTest {
    
    private Deck deck;
    private Dealer dealer;
    private Player human1;
    private NPC bot1;
    private GameEngine engine;
    private SpecialRound specialRound;
    private Table table;
    
    @BeforeEach
    public void setUp() {
       deck = new StandardDeck();
       table = new TableImpl(null, null, engine);
       human1 = new PlayerImpl("topolino",  1000);
       bot1 = new NPCimpl("pippo",  1000);
       dealer = new DealerImpl();
        List<Partecipant> players = new ArrayList<>();
        players.add(human1);
        players.add(bot1);

        engine = new GameEngineImpl(deck, players, dealer,table);
        specialRound = new DoubleHeartsRule();
        
    }

    @Test
    public void nextTurnTest() {
       
        assertEquals(table.getCurrentState(), Table.State.FIRST_BET);
        assertThrows(IllegalStateException.class, () -> {
            engine.nextTurn();
        });

        this.table.stepPassage();
        engine.nextTurn();
        assertEquals("topolino", engine.getCurrentPlayer().getName());
        engine.nextTurn();
        assertEquals("pippo", engine.getCurrentPlayer().getName());

    }

    @Test
    public void dealerTurnTest() {
        this.table.stepPassage();
        this.table.stepPassage();
        this.table.stepPassage();
        engine.dealerTurn();
        assertEquals(dealer, engine.getCurrentPlayer());
        assertTrue (dealer.getHand().getCards().size() != 0);
        assertThrows(IllegalStateException.class, () -> {
            engine.dealerTurn();
        });
        
    }
        
    @Test
    public void setSpecialRoundAndCurrentScoreTest() {
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.TWO, Suit.HEARTS));
        engine.setSpecialRound(specialRound);
        int score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(4, score); 

        specialRound = null;
        engine.setSpecialRound(specialRound);
        score = engine.currentScore(engine.getPlayers().get(0).getHand());
        assertEquals(2, score);
    }

    @Test
    public void getPlayerScoreTest() {
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.THREE, Suit.SPADES));
        int score = engine.getPlayerScore(engine.getPlayers().get(0).getName());
        assertEquals(3, score);
    }

    @Test
    public void turnsBeyondLimitsTest() {
        engine.nextTurn();
        engine.nextTurn();
        assertEquals(dealer, engine.getCurrentPlayer());

    }

    @Test
    public void unknownPlayer(){
         int score = engine.getPlayerScore("paperino");
        assertEquals(0,score);
    }

    @Test
    public void standTest(){
        this.table.stepPassage();
        this.table.stepPassage();
        this.table.stepPassage();

        engine.stand();

        assertEquals(Table.State.RESULTS, this.table.getCurrentState());
    }

    

    

    
  
}
