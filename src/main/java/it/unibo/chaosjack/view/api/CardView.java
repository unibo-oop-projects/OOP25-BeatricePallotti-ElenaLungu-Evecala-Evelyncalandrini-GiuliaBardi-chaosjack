package it.unibo.chaosjack.view.api;

import javafx.scene.Parent;

/**
 * Visual interface for a playing card.
 */
public interface CardView {

    /**
     * Returns the root node of the card visualization.
     * 
     * @return the root node as a Parent
     */
    Parent getRootNode();
}
