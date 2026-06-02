package it.unibo.chaosjack.view.api;

/**
 * Component responsible for displying game scores.
 */
public interface GameScoreDisplay {

    /**
     * Show player cards score.
     * @param score of the hand.
     */
    void setPlayer1Score(int score);

    /**
     * Show NPC player cards score.
     * @param score of the hand.
     */
    void setPlayer2Score(int score);

    /**
     * Show dealer cards score.
     * @param score of the hand.
     */
    void setDealerScore(int score);
    
}
