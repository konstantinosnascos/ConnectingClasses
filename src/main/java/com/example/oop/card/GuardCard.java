package com.example.oop.card;

import com.example.oop.ui.Display;
import com.example.oop.core.Game;
import com.example.oop.core.Player;

import java.util.List;

public class GuardCard extends Card {
    public GuardCard() {
        super("Guard", 1);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " played Guard against " + target.getName(), 30);
        Display.pause(400);

        String guess;
        if (current.isHuman()) {
            guess = game.getInputHelper().askForCardGuess(current, game.getKnowledge());
        } else {
            List<String> possible = game.getKnowledge().getPossibleGuesses();
            guess = possible.get((int) (Math.random() * possible.size()));
            Display.print(current.getName() + " (AI) guesses: " + guess, 30);
        }

        Card targetCard = target.getCards().get(0);
        Display.pause(300);
        if (guess.equals(targetCard.getName())) {
            Display.print("...checking...", 30);
            Display.pause(400);
            Display.print(target.getName() + " is ELIMINATED!", 30);
            target.eliminate();
        } else {
            Display.print("Wrong guess.", 30);
        }
    }

}