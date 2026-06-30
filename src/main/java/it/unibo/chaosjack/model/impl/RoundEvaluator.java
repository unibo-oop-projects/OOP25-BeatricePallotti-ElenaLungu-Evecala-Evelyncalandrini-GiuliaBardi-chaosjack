package it.unibo.chaosjack.model.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Hand;
import it.unibo.chaosjack.model.api.RoundEvaluation;
import it.unibo.chaosjack.model.api.RoundResult;
import it.unibo.chaosjack.model.api.RoundResult.Outcome;

/**
 * Evaluates the winning conditions of a game round.
 */
public class RoundEvaluator {
    private static final int MAX_SCORE = 21;

    /**
     * Decide the winner o winners and the final result of the round based on the game rules.
     * 
     * @param engine the current game engine.
     * @param players list of players.
     * @param pot the current amount in the table pot.
     * @return evaluation with result and the winners.
     */
    public RoundEvaluation evaluate(final GameEngine engine, final List<String> players, final int pot) {
        final int dealerScore = engine.getDealerHand().getScore();
        final int dealerCardsCount = engine.getDealerHand().getCards().size();

        int max = -1;
        int minCards = 1;
        final List<String> bestPlayers = new ArrayList<>();

        for (final String name : players) {
            final int score = engine.getPlayerScore(name);
            if (score <= MAX_SCORE && score > 0) {
                final int cardsCount = getPlayerCardCount(engine, name);
                if (score > max || score == max && cardsCount < minCards) {
                    max = score;
                    minCards = cardsCount;
                    bestPlayers.clear();
                    bestPlayers.add(name);
                } else if (score == max && cardsCount == minCards) {
                    bestPlayers.add(name);
                }
            }
        }

        final RoundResult result = calculateRoundResult(engine, bestPlayers, max, minCards, dealerScore, dealerCardsCount, pot);
        return new RoundEvaluation(result, bestPlayers);
    }

    private int getPlayerCardCount(final GameEngine engine, final String playerName) {
        return engine.getPlayers().stream()
            .filter(p -> p.getName().equals(playerName))
            .findFirst()
            .map(p -> p.getHand().getCards().size())
            .orElse(2);
    }

    private RoundResult calculateRoundResult(
        final GameEngine engine,
        final List<String> bestPlayer,
        final int maxScore,
        final int minCards,
        final int dealerScore,
        final int dealerCardsCount,
        final int pot
    ) {
        if (bestPlayer.isEmpty()) {
            return new RoundResult(Outcome.DEALER_WON, 0, dealerScore, 0);
        }

        if (dealerScore <= MAX_SCORE) {
            if (dealerScore > maxScore) {
                return new RoundResult(Outcome.DEALER_WON, maxScore, dealerScore, 0);
            }

            if (dealerScore == maxScore) {
                if (dealerCardsCount < minCards) {
                    return new RoundResult(Outcome.DEALER_WON, maxScore, dealerScore, 0);
                }
                if (dealerCardsCount == minCards) {
                    return new RoundResult(Outcome.PUSH, maxScore, dealerScore, 0);
                }
            }
        }

        if (bestPlayer.size() > 1) {
            return new RoundResult(Outcome.PLAYERS_PUSH, maxScore, dealerScore, pot * 2);
        }

        return calculateSingleWinnerResult(engine, bestPlayer.get(0), maxScore, dealerScore, pot);
    }

    private RoundResult calculateSingleWinnerResult(
        final GameEngine engine,
        final String winner,
        final int playerScore,
        final int dealerScore,
        final int pot
    ) {
        final Hand winnerHand = engine.getPlayers().stream()
                .filter(p -> p.getName().equals(winner))
                .findFirst()
                .get()
                .getHand();

        final boolean isMonocolor = winnerHand.sameColor(winnerHand.getCards());
        final int bonus = isMonocolor ? 3 : 2;
        final boolean isBlackjack = playerScore == MAX_SCORE;

        if (isBlackjack && isMonocolor) {
            return new RoundResult(Outcome.BLACKJACK_BONUS, playerScore, dealerScore, pot * (bonus + 2));
        }

        if (isBlackjack) {
            return new RoundResult(Outcome.PLAYER_BLACKJACK, playerScore, dealerScore, pot * 3);
        }

        if (isMonocolor) {
            return new RoundResult(Outcome.PLAYER_BONUS, playerScore, dealerScore, pot * bonus);
        }

        return new RoundResult(Outcome.PLAYER_WON, playerScore, dealerScore, pot * bonus);
    }

}
