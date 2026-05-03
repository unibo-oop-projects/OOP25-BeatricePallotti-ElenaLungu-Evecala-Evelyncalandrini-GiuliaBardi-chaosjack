package it.unibo.chaosjack.model.api;

/**
 * This record models the result of a round
 * 
 * @param outcome rappresent the result of round  
 * @param playerPoints the players's points in his hand
 * @param dealerPoints the dealer's points
 * @param payOut the fishes that the player win in this round
 */
public record RoundResult(Outcome outcome, int playerPoints, int dealerPoints, int payOut) {

    /**
     * Possible round results
     */
    public enum Outcome {

        /**
         * player win
         */
        PLAYER_WON("You win!"),
        /**
         * player win with bonus
         */
        PLAYER_BONUS("Your winnings multiplied"),
        /**
         * dealer win
         */
        DEALER_WON("Dealer win!"),
        /**
         * push with dealer
         */
        PUSH("You push with Dealer"),
        /**
         * push with other players
         */
        PLAYERS_PUSH("You push with other player"),
        /**
         * player does BlackJack
         */
        PLAYER_BLACKJACK("BlackJack!!!"),
        /**
         * player doea BlackJack with bonus
         */
        BLACKJACK_BONUS("BlackJack with BONUS");

        private final String message;

        Outcome(final String message) {
            this.message = message;
        }

        /**
         * @return a description of the round result
         */
        public String getMessage() {
            return message;
        }

    }

    /**
     * @return valure of payOut
     */
    public int getPayOut() {
        return this.payOut;
    }
    
}
