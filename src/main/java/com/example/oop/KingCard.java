package com.example.oop;

import java.util.ArrayList;
import java.util.List;

public class KingCard extends Card {
    public KingCard() {
        super("King", 6);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " plays King and swaps hands with " + target.getName() + "...", 30);
        Display.pause(300);

        if (!target.isAlive() || target.isProtected()) {
            Display.print("Target is protected or out. Swap canceled.", 30);
            return;
        }

        List<Card> currentHand = new ArrayList<>(current.getCards());
        List<Card> targetHand = new ArrayList<>(target.getCards());

        current.getCards().clear();
        target.getCards().clear();

        current.getCards().addAll(targetHand);
        target.getCards().addAll(currentHand);

        Display.print(current.getName() + " now has: " + current.getCards(), 30);
        Display.print(target.getName() + " now has: " + target.getCards(), 30);
        Display.pause(600);
    }
}