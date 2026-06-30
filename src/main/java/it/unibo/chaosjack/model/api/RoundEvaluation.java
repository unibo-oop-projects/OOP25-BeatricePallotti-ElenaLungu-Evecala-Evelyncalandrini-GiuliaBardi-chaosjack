package it.unibo.chaosjack.model.api;

import java.util.List;

/**
 * Rappresents the final outcome of a game round.
 * 
 * @param result of the round (outcome, scores, payouts).
 * @param winners the list of players who won or tied.
 */
public record RoundEvaluation(RoundResult result, List<String> winners) { }
