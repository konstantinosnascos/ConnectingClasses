package com.example.oop.card;


import com.example.oop.ui.Display;
import com.example.oop.core.Game;
import com.example.oop.core.Player;

public class BaronCard extends Card {
    public BaronCard() {
        super("Baron", 3);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " plays Baron against " + target.getName() + "...", 30);
        Display.print("Comparing hands...", 30);
        Display.pause(400);

        if (!current.isAlive() || !target.isAlive() || target.isProtected()) {
            Display.print("Target is protected or out. Nothing happens.", 30);
            return;
        }

        Card myCard = current.getCards().get(0);
        Card theirCard = target.getCards().get(0);

        if (myCard.getValue() > theirCard.getValue()) {
            Display.print("Baron wins! " + target.getName() + " is eliminated!", 30);
            target.eliminate();
        } else if (myCard.getValue() < theirCard.getValue()) {
            Display.print("Baron loses! " + current.getName() + " is eliminated!", 30);
            current.eliminate();
        } else {
            Display.print("Tie! No one is eliminated.", 30);
        }
        Display.pause(600);
    }
}