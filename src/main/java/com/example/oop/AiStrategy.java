package com.example.oop;

import java.util.*;
import java.util.stream.Collectors;


public class AiStrategy {
    private Player ai;
    private GameKnowledge knowledge;
    private Random random = new Random();

    public AiStrategy(Player ai, GameKnowledge knowledge) {
        this.ai = ai;
        this.knowledge = knowledge;
    }

    /**
     * Välj bästa kort att spela
     */
    public Card chooseCardToPlay(List<Card> cards, List<Player> allPlayers) {
        // Regel 1: Tvingad Countess
        if (ai.hasCard(CountessCard.class) &&
                (ai.hasCard(KingCard.class) || ai.hasCard(PrinceCard.class))) {
            return findCard(cards, CountessCard.class);
        }

        // Regel 2: Har Priest och finns osedda spelare?
        boolean hasUnseen = allPlayers.stream()
                .anyMatch(p -> p != ai && p.isAlive() && !knowledge.hasSeenPlayer(p));
        if (ai.hasCard(PriestCard.class) && hasUnseen) {
            return findCard(cards, PriestCard.class);
        }

        // Regel 3: Finns en spelare med lågt kort (som vi sett)?
        Optional<Player> weakTarget = allPlayers.stream()
                .filter(p -> knowledge.hasSeenPlayer(p))
                .min(Comparator.comparing(p -> knowledge.getSeenCard(p).getValue()));

        if (weakTarget.isPresent()) {
            if (ai.hasCard(BaronCard.class)) return findCard(cards, BaronCard.class);
            if (ai.hasCard(PrinceCard.class)) return findCard(cards, PrinceCard.class);
        }

        // Regel 4: Har Guard? Spela den (så AI kan gissa)
        if (ai.hasCard(GuardCard.class)) {
            return findCard(cards, GuardCard.class);
        }

        // Standard: Spela ett säkert kort (ej Countess)
        List<Card> safeCards = cards.stream()
                .filter(c -> !(c instanceof CountessCard))
                .filter(c -> !(c instanceof PrincessCard))
                .toList();

// If we have safe cards, use one
        if (!safeCards.isEmpty()) {
            return safeCards.get(random.nextInt(safeCards.size()));
        }

// If only Countess left → play it (should only happen if Prince/King not held)
        if (ai.hasCard(CountessCard.class)) {
            return findCard(cards, CountessCard.class);
        }

// Last resort: Must be Princess (should not happen!)
        Card princess = findCard(cards, PrincessCard.class);
        if (princess != null) {
            // Log warning — this means AI has ONLY Princess → bad design
            return princess;
        }

// Fallback
        return cards.get(0);
    }

    /**
     * Välj mål för ett kort
     */
    public Player chooseTargetForCard(Card card, List<Player> candidates, List<Player> allPlayers) {
        if (candidates.isEmpty()) return null;

        // Försök undvika skyddade
        List<Player> unprotected = candidates.stream()
                .filter(p -> !p.isProtected())
                .collect(Collectors.toList());
        if (unprotected.isEmpty()) unprotected = candidates;
        if (unprotected.isEmpty()) return null;

        // Specialfall: Priest – försök titta på någon vi inte sett
        if (card instanceof PriestCard) {
            return unprotected.stream()
                    .filter(p -> !knowledge.hasSeenPlayer(p))
                    .findFirst()
                    .orElse(unprotected.get(random.nextInt(unprotected.size())));
        }

        // Baron, Guard, Prince – försök välja en svag spelare (lågt känt kort)
        if (card instanceof GuardCard || card instanceof BaronCard || card instanceof PrinceCard) {
            return unprotected.stream()
                    .filter(p -> knowledge.hasSeenPlayer(p))
                    .min(Comparator.comparing(p -> knowledge.getSeenCard(p).getValue()))
                    .orElse(unprotected.get(random.nextInt(unprotected.size())));
        }

        // Default: slumpmässig spelare
        return unprotected.get(random.nextInt(unprotected.size()));
    }

    /**
     * Gissning för Guard – slumpmässigt från kvarvarande kort
     */
    public String guessBestCard() {
        List<String> possible = knowledge.getPossibleGuesses();
        return possible.get(random.nextInt(possible.size()));
    }

    // --- Hjälpmetod: Hitta ett kort i en lista ---
    private Card findCard(List<Card> cards, Class<? extends Card> type) {
        return cards.stream()
                .filter(type::isInstance)
                .findFirst()
                .orElseGet(() ->
                        cards.stream()
                                .filter(c -> !(c instanceof PrincessCard))  // Avoid suicide
                                .filter(c -> !(c instanceof CountessCard)) // Unless forced
                                .findFirst()
                                .orElse(cards.get(0)) // Desperate fallback
                );
    }
}