package models;

import java.util.Random;
import models.enums.Colors;
import models.enums.Names;
import random.Logger;

public class Npc extends iPlayer {

    public Npc(Game game) {
        super(game);
        name = Names.values()[new Random().nextInt(Names.values().length)].toString();
    }

    @Override
    public Card playCard(Card actual) {
        // Va comprobando todo su mazo en busca de una carta que tirar
        for (Card card : deck) {
            if (card.getColor().equals(actual.getColor()) || card.getColor().equals(Colors.BLACK) || card.getNum() == actual.getNum()) {
                waitTime(1000);
                deck.remove(card);
                Logger.npcPlays(this, card);
                return card;
            }
        }

        // Si no ha encontrado una carta roba y la intenta tirar
        Card drawCard = game.draw();
        if (drawCard.getColor().equals(actual.getColor()) || drawCard.getColor().equals(Colors.BLACK) || drawCard.getNum() == actual.getNum()) {
            waitTime(1000);
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

}
