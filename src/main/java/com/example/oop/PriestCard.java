package com.example.oop;

public class PriestCard extends Card {
    public PriestCard() {
        super("Priest", 2);
    }

    @Override
    public void effect(Game game, Player current, Player target) {
        Display.print(current.getName() + " plays Priest and looks at " + target.getName() + "'s card...", 30);
        Display.pause(300);

        if (!target.getCards().isEmpty()) {
            Card card = target.getCards().get(0);
            Display.print("[Secret] " + target.getName() + " has: " + card.getName() + " (Value: " + card.getValue() + ")", 30);
            game.getKnowledge().recordSeen(target, card); // AI minns
        } else {
            Display.print(target.getName() + " has no cards.", 30);
        }
        Display.pause(500);
    }
}