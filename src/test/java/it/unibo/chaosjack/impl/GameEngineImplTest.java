package it.unibo.chaosjack.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unibo.chaosjack.model.api.Deck;
import it.unibo.chaosjack.model.impl.StandardDeck;
import it.unibo.chaosjack.model.impl.StandardCard;
import it.unibo.chaosjack.model.api.Player;
import it.unibo.chaosjack.model.impl.Rank;
import it.unibo.chaosjack.model.impl.Suit;

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
import it.unibo.chaosjack.model.impl.DoubleHeartsRule;


public class GameEngineImplTest {
    
    private Deck deck;
    private Dealer dealer;
    private Player human1;
    private NPC bot1;
    private GameEngine engine;
    private SpecialRound specialRound;
    

    @BeforeEach
    public void setUp() {
       deck = new StandardDeck();
       human1 = new PlayerImpl("topolino",  1000);
       bot1 = new NPCimpl("pippo",  1000);
       dealer = new DealerImpl();
        List<Partecipant> players = new ArrayList<>();
        players.add(human1);
        players.add(bot1);

        engine = new GameEngineImpl(deck, players, dealer);
        specialRound = new DoubleHeartsRule();
        
    }

    @Test
    public void nextTurnTest() {
        Partecipant currentPlayer = engine.getCurrentPlayer();
        assertEquals(currentPlayer, human1);

        engine.nextTurn();
        currentPlayer = engine.getCurrentPlayer();
        assertEquals(currentPlayer, bot1);

        engine.nextTurn();
        currentPlayer = engine.getCurrentPlayer();
        assertEquals(currentPlayer, dealer);
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
    public void getPlayerScoreTest(){
        engine.getPlayers().get(0).getHand().addCard(new StandardCard(Rank.THREE, Suit.SPADES));
        int score = engine.getPlayerScore(engine.getPlayers().get(0).getName());
        assertEquals(3, score);
    }
        
}
