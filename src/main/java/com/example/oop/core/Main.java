package com.example.oop.core;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Player> players = new ArrayList<>();
        players.add(new Player("Kostas"));           // Människa
        players.add(new Player("Bot-Bob", false));  // bot
        players.add(new Player("Bot-Chloe", false)); // bot

        Game game = new Game(players);
        game.startGame();



    }
}