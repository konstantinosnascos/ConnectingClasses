package com.example.oop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Player> players = new ArrayList<>();
        players.add(new Player("Kostas"));           // Människa
        players.add(new Player("Bot-Bob", false));  // AI
        players.add(new Player("Bot-Chloe", false)); // AI

        Game game = new Game(players);
        game.startGame();



    }
}