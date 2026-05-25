package it.unibo.chaosjack.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.chaosjack.controller.api.ActionController;
import it.unibo.chaosjack.controller.impl.ActionControllerImpl;
import it.unibo.chaosjack.model.api.GameEngine;
import it.unibo.chaosjack.model.api.Table;
import it.unibo.chaosjack.model.impl.GameEngineImpl;
import it.unibo.chaosjack.model.impl.TableImpl;

/* tess for ActionController.
 */
class ActionControllerTest {
    
    private ActionController controller;
    private Table table;
    private GameEngine engine;

    @BeforeEach
    public void setUp() { //prima di ogni test devo fare  questo set up
        table = new TableImpl(null, null, engine);
        engine = new GameEngineImpl(null, null, null);
        controller = new ActionControllerImpl(table, engine);
    }


}
