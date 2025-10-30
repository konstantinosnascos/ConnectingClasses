package com.example.oop.core;



import com.example.oop.card.Card;

import java.util.List;

public class Player {
    private String name;
    private Hand hand;
    private boolean isActive;
    private boolean isProtected;
    private boolean isHuman;

    // Konstruktor du kanske använde förr (behåll om du måste, men annars ta bort)
    // Ta bara bort denna om du inte använder den explicit i koden
    public Player(String name, Hand hand, boolean isActive) {
        this.name = name;
        this.hand = hand;
        this.isActive = isActive;
        this.isProtected = false;
        this.isHuman = true; // antar att denna är för mänsklig spelare
    }

    //  Huvudkonstruktor för spelare – avgör om det är AI eller människa
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.hand = new Hand();
        this.isActive = true;
        this.isProtected = false;
        this.isHuman = isHuman;
    }

    //  Default: människa
    public Player(String name) {
        this(name, true); // automatiskt en människa om man bara anger namn
    }

    // --- Getters / setters ---
    public boolean isHuman() {
        return isHuman;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public boolean isProtected() {
        return isProtected;
    }

    // Metod för att rita kort
    public void drawCard(Deck deck) {
        Card drawn = deck.drawCard();
        if (drawn != null) {
            hand.addCard(drawn);
        }
    }

    // Spela ett kort
    public void playCard(Card card, Player target, Game game) {
        hand.removeCard(card);
        game.addToDiscardPile(card);
        card.effect(game, this, target);
    }

    public boolean isAlive() {
        return isActive;
    }

    public void eliminate() {
        isActive = false;
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return hand.getCards();
    }

    public boolean hasCard(Class<? extends Card> cardType) {
        return getCards().stream().anyMatch(cardType::isInstance);
    }


}