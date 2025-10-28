package com.example.oop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private List<Player> players;
    private Deck deck;
    private List<Card> discardPile;
    private int roundNumber;
    public boolean isActive;
    private InputHelper inputHelper;
    private GameKnowledge knowledge;

    public Game(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.discardPile = new ArrayList<>();
        this.roundNumber = 1;
        this.isActive = true;
        this.inputHelper = new InputHelper();
        this.knowledge = new GameKnowledge(this.discardPile);
    }

    public void startGame() {
        deck.shuffle();
        burnCard();
        drawStartingHands();
        playRound();
    }

    private void burnCard() {
        Card burned = deck.drawCard();
        if (burned != null) {
            discardPile.add(burned);
            slowPrint("A card was burned face down.", 30);
        }
    }

    private void drawStartingHands() {
        for (Player p : players) {
            p.drawCard(deck);
        }
        pause(600);
    }

    public void playRound() {
        while (!isRoundOver()) {
            for (Player p : players) {
                if (p.isAlive() && isActive) {
                    nextTurn(p);
                }
            }
            pause(1000); // Paus mellan varje runda
        }
        endRound();
    }

    private void nextTurn(Player current) {
        pause(700);
        slowPrint("\n" + "=".repeat(40), 10);
        slowPrint("--- " + current.getName() + "'s turn ---", 30);
        pause(400);

        current.drawCard(deck);
        if (current.isProtected()) {
            current.setProtected(false);
            slowPrint(current.getName() + " is no longer protected.", 30);
            pause(500);
        }

        if (mustPlayCountess(current)) {
            Card countess = current.getCards().stream()
                    .filter(c -> c instanceof CountessCard)
                    .findFirst().orElse(null);
            if (countess != null) {
                slowPrint(current.getName() + " must play the Countess.", 30);
                pause(300);
                current.playCard(countess, null, this);
            }
            return;
        }

        if (current.isHuman()) {
            inputHelper.handleTurn(current, this);
        } else {
            handleAiTurn(current);
        }
    }

    private void handleAiTurn(Player ai) {
        try {
            Thread.sleep(800); // Vänta innan AI:s tur viskas
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        slowPrint(ai.getName() + " (AI) is playing...", 30);
        pause(600);

        AiStrategy aiStrategy = new AiStrategy(ai, knowledge);
        Card cardToPlay = aiStrategy.chooseCardToPlay(ai.getCards(), players);

        Player target = null;
        if (cardToPlay.needsTarget()) {
            List<Player> candidates = players.stream()
                    .filter(p -> p != ai && p.isAlive() && !p.isProtected())
                    .collect(Collectors.toList());

            if (!candidates.isEmpty()) {
                target = aiStrategy.chooseTargetForCard(cardToPlay, candidates, players);
            } else {
                target = ai;
            }
        }

        ai.playCard(cardToPlay, target, this);
        pause(800); // Paus efter AI drag
    }

    // --- Offentliga metoder ---

    public void addToDiscardPile(Card card) {
        discardPile.add(card);
        slowPrint(card.getName() + " was discarded.", 30);
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public Deck getDeck() {
        return deck;
    }

    private boolean mustPlayCountess(Player player) {
        return player.hasCard(CountessCard.class) &&
                (player.hasCard(KingCard.class) || player.hasCard(PrinceCard.class));
    }

    private boolean isRoundOver() {
        long alive = players.stream().filter(Player::isAlive).count();
        return alive <= 1 || deck.isEmpty();
    }

    public void endRound() {
        List<Player> alive = players.stream().filter(Player::isAlive).toList();
        pause(1000);

        // Fall 1: Endast en levande spelare → vinner
        if (alive.size() == 1) {
            Display.print(alive.get(0).getName() + " wins the round!", 30);
        }
        // Fall 2: Deck tomt → vinnare har högst kort
        else if (deck.isEmpty()) {
            // Filtrera spelare som har minst 1 kort
            List<Player> playersWithCards = alive.stream()
                    .filter(p -> !p.getCards().isEmpty())
                    .toList();

            if (!playersWithCards.isEmpty()) {
                Player winner = playersWithCards.stream()
                        .max(Comparator.comparingInt(p -> p.getCards().get(0).getValue()))
                        .orElse(null);

                if (winner != null) {
                    Display.print("Deck is empty! " + winner.getName() + " wins with the highest card!", 30);
                    return;
                }
            }

            // Ingen spelare har kort? → ingen vinner
            Display.print("No cards left — no winner!", 30);
        }

        isActive = false;
        roundNumber++;
        pause(800);
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public GameKnowledge getKnowledge() {
        return knowledge;
    }

    public InputHelper getInputHelper() {
        return inputHelper;
    }

    public List<Player> getPlayers() {
        return players;
    }

    // === DRAMATISKA HJÄLPMEtODER ===

    /**
     * Skriver ut text tecken för tecken – som en typskrivare
     */
    public void slowPrint(String text, long delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }

    /**
     * Vänta en stund – för pauser och spänning
     */
    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}