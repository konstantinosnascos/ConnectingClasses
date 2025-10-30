package com.example.oop.card;

import com.example.oop.core.Game;
import com.example.oop.core.Player;

public abstract class Card {
    protected String name;
    protected int value;

    public Card(String name, int value)
    {
        this.name = name;
        this.value = value;
    }

    public boolean needsTarget()
    {
        return true;
    }

    public String getName()
    {
        return name;
    }

    public int getValue()
    {
        return value;
    }

    public static boolean canPlayCountess(Player player) {
        return player.hasCard(CountessCard.class) &&
                (player.hasCard(KingCard.class) || player.hasCard(PrinceCard.class));
    }

    public abstract void effect(Game game, Player current, Player target);


    @Override
    public String toString()
    {
        return name + "(Value: " + value + ")";
    }
}
