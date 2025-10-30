package com.example.oop.core;

import com.example.oop.card.*;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    private Stack<Card> cards;
    private Card burnedCard;


    public Deck()
    {
        cards = new Stack<>();
        initializeDeck();
        burnOneCard();
    }

    private void burnOneCard() {
        if (!cards.isEmpty()) {
            burnedCard = cards.pop();
        }
    }
    private void initializeDeck()
    {
        for (int i = 0; i < 5; i++)
        {
            cards.add(new GuardCard());
        }
        for (int i = 0; i < 2; i++)
        {
            cards.add(new PriestCard());
        }
        for (int i = 0; i < 2; i++)
        {
            cards.add(new BaronCard());
        }
        for (int i = 0; i < 2; i++)
        {
            cards.add(new HandmaidCard());
        }
        for (int i = 0; i < 2; i++)
        {
            cards.add(new PrinceCard());
        }
        cards.add(new KingCard());
        cards.add(new CountessCard());
        cards.add(new PrincessCard());
        shuffle();
    }


    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    public Card drawCard()
    {
        if (!cards.isEmpty())
        {
            return cards.pop();
        }
        return null;
    }


    public boolean isEmpty()
    {
        return cards.isEmpty();
    }
}
