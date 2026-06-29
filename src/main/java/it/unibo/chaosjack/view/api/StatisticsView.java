package it.unibo.chaosjack.view.api;

import it.unibo.chaosjack.model.api.Statistics;
import javafx.scene.Parent;

/**
 * Interface of player's statistics view.
 */
public interface StatisticsView {

    /**
     * Generates the root node of the statistics interface.
     * 
     * @param stats the statistics data source.
     * @param onBack the callback action to return on the menu.
     * 
     * @return the parent layout node.
     */
    Parent createRoot(Statistics stats, Runnable onBack);
}
