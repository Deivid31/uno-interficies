package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.enums.Color;
import models.enums.Names;
import random.Logger;

public class Npc {

    private Game game;
    private String name;
    private List<Card> deck;

    public Npc(Game game) {
        name = Names.values()[new Random().nextInt(Names.values().length)].toString();
        deck = new ArrayList<Card>();
        this.game = game;

        deck = game.stratingCards();
    }

    public Card playCard(Card actual) {
        // Va comprobando todo su mazo en busca de una carta que tirar
        for (Card card : deck) {
            if (card.getColor().equals(actual.getColor()) || card.getColor().equals(Color.BLACK) || card.getNum() == actual.getNum()) {
                waitTime(1000);
                deck.remove(card);
                Logger.npcPlays(this, card);
                return card;
            }
        }

        // Si no ha encontrado una carta roba y la intenta tirar
        Card drawCard = game.draw();
        if (drawCard.getColor().equals(actual.getColor()) || drawCard.getColor().equals(Color.BLACK) || drawCard.getNum() == actual.getNum()) {
            waitTime(1000);
            deck.remove(drawCard);
            Logger.npcPlays(this, drawCard);
            return drawCard;
        }

        // Si no podía tirarla pasa su turno
        waitTime(1000);
        deck.add(drawCard);
        Logger.npcDraws(this, drawCard);
        return null;
    }

    public void waitTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.error(ex.getMessage());
        }
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
