package com.example.oop.ui;



import com.example.oop.core.Game;
import com.example.oop.core.Player;
import com.example.oop.card.Card;

import java.util.*;
import java.util.stream.Collectors;

public class InputHelper {
    private Scanner sc = new Scanner(System.in);

    /**
     * Huvudmeny: Spelarens tur
     */
    public void handleTurn(Player player, Game game) {
        Display.print("\n--- " + player.getName() + "'s turn ---", 20);
        Display.print("Your hand: " + player.getCards(), 10);
        Display.print("Choose an action:", 10);
        Display.print("1. Play card", 10);
        Display.print("2. View discard pile", 10);
        Display.print("99. End game", 10);

        int choice = getIntInput();
        switch (choice) {
            case 1 -> playCard(player, game);
            case 2 -> {
                Display.print("Discard pile: " + game.getDiscardPile(), 10);
                Display.pause(700);
                handleTurn(player, game); // Visa menyn igen
            }
            case 99 -> game.setActive(false);
            default -> {
                Display.print("Invalid choice. Try again.", 10);
                Display.pause(300);
                handleTurn(player, game);
            }
        }
    }

    /**
     * Välj ett spelbart kort (filtrera bort de som inte kan spelas)
     */
    public void playCard(Player player, Game game) {
        List<Card> hand = player.getCards();
        List<Card> playableCards = new ArrayList<>();

        // Hitta giltiga mål (för kort som behöver det)
        List<Player> validTargets = game.getPlayers().stream()
                .filter(p -> p != player && p.isAlive() && !p.isProtected())
                .collect(Collectors.toList());

        // Filtrera alla kort som faktiskt kan spelas
        for (Card card : hand) {
            if (card.needsTarget() && validTargets.isEmpty()) {
                Display.print("- " + card.getName() + " (no valid target)", 10);
            } else {
                playableCards.add(card);
            }
        }

        // Inga kort att spela?
        if (playableCards.isEmpty()) {
            Display.print("No playable cards. You must pass your turn.", 20);
            Display.pause(600);
            return;
        }

        // Visa spelbara kort
        Display.print("Choose a card to play:", 20);
        for (int i = 0; i < playableCards.size(); i++) {
            Display.print((i + 1) + ". " + playableCards.get(i).getName(), 10);
        }

        int choice = getIntInput() - 1;
        Card selectedCard = null;

        if (choice >= 0 && choice < playableCards.size()) {
            selectedCard = playableCards.get(choice);
        } else {
            Display.print("Invalid choice.", 10);
            Display.pause(200);
            playCard(player, game); // Försök igen
            return;
        }

        // Välj mål om det behövs
        Player target = null;
        if (selectedCard.needsTarget()) {
            target = chooseTarget(player, validTargets);
            if (target == null) {
                // Detta borde inte hända eftersom vi filtrerade, men trygghetsskydd
                Display.print("Could not select target. Canceling play.", 20);
                return;
            }
        }

        // Spela kortet
        player.playCard(selectedCard, target, game);
        Display.pause(500);
    }

    /**
     * Välj mål från fördefinierad lista (filtrerad)
     */
    public Player chooseTarget(Player current, List<Player> validTargets) {
        if (validTargets.isEmpty()) {
            return null;
        }

        Display.print("Choose a target:", 20);
        for (int i = 0; i < validTargets.size(); i++) {
            Display.print((i + 1) + ". " + validTargets.get(i).getName(), 10);
        }

        int choice = getIntInput() - 1;
        if (choice >= 0 && choice < validTargets.size()) {
            return validTargets.get(choice);
        } else {
            Display.print("Invalid target. Try again.", 20);
            Display.pause(300);
            return chooseTarget(current, validTargets);
        }
    }

    /**
     * Gissning för Guard – skiljer mellan AI och människa
     */
    public String askForCardGuess(Player guesser, GameKnowledge knowledge) {
        List<String> possibleGuesses = knowledge.getPossibleGuesses();

        if (guesser.isHuman()) {
            Display.print("Guess which card your opponent has (not Guard):", 20);
            for (int i = 0; i < possibleGuesses.size(); i++) {
                Display.print((i + 1) + ". " + possibleGuesses.get(i), 10);
            }

            int choice = getIntInput() - 1;
            if (choice >= 0 && choice < possibleGuesses.size()) {
                return possibleGuesses.get(choice);
            } else {
                Display.print("Invalid guess. Try again.", 20);
                Display.pause(300);
                return askForCardGuess(guesser, knowledge);
            }
        } else {
            // AI: slumpar bland kvarvarande möjligheter
            return possibleGuesses.get((int) (Math.random() * possibleGuesses.size()));
        }
    }

    // --- Hjälpmetod: Säker heltalsinput ---
    private int getIntInput() {
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // töm bufferten
            Display.print("Please enter a number.", 20);
            return getIntInput();
        }
    }
}