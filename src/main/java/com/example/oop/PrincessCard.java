package com.example.oop;

public class PrincessCard extends Card {
    public PrincessCard() {
        super("Princess", 8);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " discards the PRINCESS...", 30);
        Display.pause(300);
        Display.print(current.getName() + " is ELIMINATED!", 30);
        current.eliminate();
        Display.pause(600);
    }

    @Override
    public boolean needsTarget() {
        return false;
    }
}