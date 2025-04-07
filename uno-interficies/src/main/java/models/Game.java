package models;

import java.util.ArrayList;
import models.enums.Color;
import models.enums.Type;
import random.Logger;

public class Game {

    private boolean gameStarted;
    private ArrayList<Card> drawDeck;
    private int turn;

    public Game() {
        drawDeck = new ArrayList<Card>();
        gameStarted = false;
        turn = 0;
        this.fillDeck();
    }

    /**
     * Al llamarlo inicia una nueva partida, si la partida anterior aún está
     * activa no hará nada
     *
     */
    public void startGame() {
        // Comprobar que el juego no sea nul·lo
        if (this.gameStarted) {
            Logger.error("Ya hay una partida empezada");
            return;
        }

        gameStarted = true;

    }

    public void fillDeck() {
        // Por si acaso nos interesa saber
        if (!drawDeck.isEmpty()) {
            Logger.warn("Se ha rellenado el mazo sin estar vacío");
        }

        // Borra todas las cartas
        drawDeck.clear();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != 0) {
                    Card card = new Card(i, Color.values()[j], Type.NUM);
                    Card card2 = new Card(i, Color.values()[j], Type.NUM);
                    drawDeck.add(card);
                    drawDeck.add(card2);
                } else {
                    Card card = new Card(i, Color.values()[j], Type.NUM);
                    drawDeck.add(card);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!Color.values()[i].equals(Color.BLACK)) {
                for (int j = 0; j < 3; j++) {
                    Card card = new Card(-j - 1, Color.values()[i], Type.values()[j]);
                    Card card2 = new Card(-j - 1, Color.values()[i], Type.values()[j]);
                    drawDeck.add(card);
                    drawDeck.add(card2);
                }
            } else {
                for (int j = 0; j < 2; j++) {
                    Card card = new Card(-j - 4, Color.values()[i], Type.values()[j + 3]);
                    Card card2 = new Card(-j - 4, Color.values()[i], Type.values()[j + 3]);
                    Card card3 = new Card(-j - 4, Color.values()[i], Type.values()[j + 3]);
                    Card card4 = new Card(-j - 4, Color.values()[i], Type.values()[j + 3]);
                    drawDeck.add(card);
                    drawDeck.add(card2);
                    drawDeck.add(card3);
                    drawDeck.add(card4);
                }
            }

        }

        for (Card card : drawDeck) {
            System.out.println(card.toString());
        }

    }

}
