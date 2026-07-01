package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * Rappresents the final outcome of a game round.
 * 
 * @param result of the round (outcome, scores, payouts).
 * @param winners the list of players who won or tied.
 */
public record RoundEvaluation(RoundResult result, List<String> winners) {

    /**
     * Compact constructor to ensure the winners list.
     * 
     * @param result round result.
     * @param winners list of winners.
     */
    public RoundEvaluation {
        winners = List.copyOf(winners);
    }

    @Override
    public List<String> winners() {
        return List.copyOf(winners);
    }
 }
