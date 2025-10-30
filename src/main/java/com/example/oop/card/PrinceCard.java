package com.example.oop.card;

import com.example.oop.ui.Display;
import com.example.oop.core.Game;
import com.example.oop.core.Player;

public class PrinceCard extends Card {
    public PrinceCard() {
        super("Prince", 5);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " plays Prince on " + target.getName() + "...", 30);
        Display.pause(300);

        if (!target.isAlive()) return;

        if (target.getCards().isEmpty()) {
            Display.print(target.getName() + " has no card to discard.", 30);
            return;
        }

        Card discarded = target.getCards().get(0);
        Display.print(target.getName() + " discards: " + discarded.getName(), 30);
        Display.pause(300);

        // Om det är Princess → eliminera via sitt eget effect
        if (discarded instanceof PrincessCard) {
            game.addToDiscardPile(discarded);
            discarded.effect(game, target, null);
            return;
        }

        game.addToDiscardPile(discarded);
        target.getCards().clear();

        Card newCard = game.getDeck().drawCard();
        if (newCard == null) {
            Display.print("No more cards. Nothing happens.", 30);
            return;
        }

        target.getCards().add(newCard);
        Display.print(target.getName() + " draws a new card.", 30);
        Display.pause(400);
    }
}