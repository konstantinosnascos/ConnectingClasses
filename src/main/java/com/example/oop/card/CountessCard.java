package com.example.oop.card;

import com.example.oop.ui.Display;
import com.example.oop.core.Game;
import com.example.oop.core.Player;

public class CountessCard extends Card {
    public CountessCard() {
        super("Countess", 7);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " played the Countess.", 30);
        Display.pause(400);
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}