package com.example.oop;

public class HandmaidCard extends Card {
    public HandmaidCard() {
        super("Handmaid", 4);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        current.setProtected(true);
        Display.print(current.getName() + " hides behind the Handmaid!", 30);
        Display.print("Protected until next turn.", 30);
        Display.pause(500);
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}