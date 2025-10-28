package com.example.oop;

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