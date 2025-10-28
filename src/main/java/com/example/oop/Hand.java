package com.example.oop;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand()
    {
        cards = new ArrayList<>();
    }

    public void addCard(Card c)
    {
        if (cards.size()<2)
        {
            cards.add(c);
        }
    }


    public void removeCard(Card c)
    {
        cards.remove(c);
    }

    public List<Card> getCards()
    {
        return cards;
    }
}
