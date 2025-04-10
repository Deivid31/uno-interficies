package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.enums.Color;
import models.enums.Type;
import static models.enums.Type.CHANGE_COLOR;
import static models.enums.Type.DRAW2;
import static models.enums.Type.DRAW4;
import random.Logger;

public class Game {

    private Random random;

    private boolean gameStarted;
    private ArrayList<Card> drawDeck;
    private ArrayList<Npc> npcs;
    private Card actualCard;
    private int turn;
    private int direction;

    public Game() {
        random = new Random();
        drawDeck = new ArrayList<Card>();
        npcs = new ArrayList<Npc>();
        gameStarted = false;
        turn = 0;
        direction = 1;
        this.fillDeck();

        npcs.add(new Npc(this));
        npcs.add(new Npc(this));
        npcs.add(new Npc(this));

        startCard();

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
        gameCicle();

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

        Logger.info("La pila de robo ahora tiene " + drawDeck.size() + "cartas");

    }

    public Card draw() {
        return drawDeck.get(random.nextInt(drawDeck.size()));
    }

    public void playerDraws(Npc npc, int qty) {
        for (int i = 0; i < qty; i++) {
            npc.getDeck().add(drawDeck.get(random.nextInt(drawDeck.size())));
        }
    }

    public void startCard() {
        Card card = drawDeck.get(random.nextInt(drawDeck.size()));

        actualCard = card;

        drawDeck.remove(card);
    }

    public List<Card> stratingCards() {
        ArrayList<Card> result = new ArrayList<Card>();

        for (int i = 0; i < 7; i++) {
            Card card = drawDeck.get(random.nextInt(drawDeck.size()));
            result.add(card);
            drawDeck.remove(card);
        }

        return result;

    }

    public void gameCicle() {

        while (gameStarted) {

            Card card = npcs.get(turn).playCard(actualCard);

            if (card != null) {
                actualCard = card;
            }

            boolean skipAdvance = false;

            switch (actualCard.getPower()) {
                case CHANGE_COLOR:
                    actualCard = new Card(-6, Color.values()[random.nextInt(4)], Type.NUM);
                    break;

                case SWAP:
                    direction *= -1;
                    break;

                case BLOCK:
                    // Saltar el siguiente jugador
                    turn = (turn + direction + 3) % 3;
                    break;

                case DRAW2:
                    int draw2Target = (turn + direction + 3) % 3;
                    playerDraws(npcs.get(draw2Target), 2);
                    // Saltar al siguiente del que roba
                    turn = (turn + 2 * direction + 3) % 3;
                    skipAdvance = true;
                    break;

                case DRAW4:
                    int draw4Target = (turn + direction + 3) % 3;
                    playerDraws(npcs.get(draw4Target), 4);
                    actualCard = new Card(-6, Color.values()[random.nextInt(4)], Type.NUM);
                    // Saltar al siguiente del que roba
                    turn = (turn + 2 * direction + 3) % 3;
                    skipAdvance = true;
                    break;
            }

            if (npcs.get(turn).getDeckSize() == 0) {
                gameStarted = false;
                System.out.println("¡Gana el jugador " + npcs.get(turn).getName() + "!");
                break;
            }

            if (!skipAdvance) {
                // Avanzar turno normalmente
                turn = (turn + direction + 3) % 3;
            }
        }
    }

}
