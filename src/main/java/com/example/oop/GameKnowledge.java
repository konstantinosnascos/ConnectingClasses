package com.example.oop;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameKnowledge {
    private List<String> allCardNames;
    private Map<String, Integer> removedCards = new HashMap<>();
    private Map<Player, Card> seenCards = new HashMap<>(); // Known cards (Priest, etc.)
    private Map<Player, Set<String>> impossibleCards = new HashMap<>(); // Cards a player CANNOT have
    private Map<Player, String> lastAction = new HashMap<>(); // Optional: remember actions

    public GameKnowledge(List<Card> initialDiscardPile) {
        initialDiscardPile.forEach(this::recordDiscarded);
    }

    public void recordDiscarded(Card card) {
        removedCards.merge(card.getName(), 1, Integer::sum);
    }

    // Called when Priest reveals a card
    public void recordSeen(Player player, Card card) {
        seenCards.put(player, card);
    }

    // Mark that a player cannot have a certain card
    public void recordImpossible(Player player, String cardName) {
        impossibleCards.computeIfAbsent(player, k -> new HashSet<>()).add(cardName);
    }

    // Did we see this player's card?
    public boolean hasSeenPlayer(Player player) {
        return seenCards.containsKey(player);
    }

    public Card getSeenCard(Player player) {
        return seenCards.get(player);
    }

    public boolean isCardStillPossible(String cardName) {
        return !removedCards.containsKey(cardName);
    }

    // What cards could a player still have?
    public List<String> getPossibleCardsFor(Player player) {
        Map<String, Integer> available = new HashMap<>();
        available.put("Guard", 5);
        available.put("Priest", 2);
        available.put("Baron", 2);
        available.put("Handmaid", 2);
        available.put("Prince", 2);
        available.put("King", 1);
        available.put("Countess", 1);
        available.put("Princess", 1);

        // Subtract discarded ones
        for (Map.Entry<String, Integer> e : removedCards.entrySet()) {
            available.computeIfPresent(e.getKey(), (k, v) -> v - e.getValue());
        }

        // Apply impossible cards
        Set<String> impossible = impossibleCards.getOrDefault(player, new HashSet<>());
        for (String card : impossible) {
            available.remove(card);
        }

        // If we know their current card, remove it from possibilities (they don't still have it)
        if (seenCards.containsKey(player)) {
            String currentCard = seenCards.get(player).getName();
            available.remove(currentCard);
        }

        // Expand to list (with duplicates)
        List<String> possible = new ArrayList<>();
        available.forEach((name, count) -> {
            for (int i = 0; i < Math.max(0, count); i++) {
                possible.add(name);
            }
        });

        return possible.isEmpty() ? Arrays.asList("Guard") : possible;
    }

    public List<String> getPossibleGuesses() {
        return Stream.of("Priest", "Baron", "Handmaid", "Prince", "King", "Countess", "Princess")
                .filter(this::isCardStillPossible)
                .collect(Collectors.toList());
    }

    // Optional: remember card actions for logic
    public void onBaronSurvival(Player target, int attackerCardValue) {
        // Target survived → their card > attackerCardValue
        // So they cannot have Guard (1), Priest (2), ..., up to attackerCardValue
        for (int i = 1; i <= attackerCardValue; i++) {
            String weakCard = cardNameByValue(i);
            if (weakCard != null) recordImpossible(target, weakCard);
        }
    }

    public void onPrincePlayedOn(Player target) {
        // Target survived → they did *not* discard Princess
        recordImpossible(target, "Princess");
    }

    public void onKingSwap(Player current, Player target, Card currentCard, Card targetCard) {
        // Now knowledge updates: target now has currentCard, current has targetCard
        // Optional: you could track this if AI was part of swap
    }

    private String cardNameByValue(int value) {
        return switch (value) {
            case 1 -> "Guard";
            case 2 -> "Priest";
            case 3 -> "Baron";
            case 4 -> "Handmaid";
            case 5 -> "Prince";
            case 6 -> "King";
            case 7 -> "Countess";
            case 8 -> "Princess";
            default -> null;
        };
    }
}