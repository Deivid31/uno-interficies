package models;

import java.util.ArrayList;
import java.util.List;
import random.Logger;

public class iPlayer {

    Game game;
    String name;
    List<Card> deck;

    public iPlayer(Game game) {
        deck = new ArrayList<Card>();
        this.game = game;

        deck = game.startingCards();
    }

    public Card playCard(Card actual) {
        Logger.error("No se ha implementado el método de jugar carta");
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

}
