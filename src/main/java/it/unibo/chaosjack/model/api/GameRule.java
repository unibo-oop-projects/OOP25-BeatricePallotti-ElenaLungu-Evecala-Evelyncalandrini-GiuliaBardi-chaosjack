package it.unibo.chaosjack.model.api;
import java.util.List;

public interface GameRule {
    int calculateScore( List<Card> cards);
}
