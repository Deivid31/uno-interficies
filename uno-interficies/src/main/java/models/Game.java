package models;

import java.awt.Image;
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
    private ArrayList<iPlayer> players;
    private Card actualCard;
    private int turn;
    private Image img;
    private int direction;

    public Game() {
        random = new Random();
        drawDeck = new ArrayList<>();
        players = new ArrayList<>();
        gameStarted = false;
        turn = 0;
        direction = 1;
        fillDeck();

        players.add(new Npc(this));
        players.add(new Npc(this));
        players.add(new Npc(this));
        players.add(new Human(this));

        startCard();
    }

    public void startGame() {
        if (gameStarted) {
            Logger.error("Ya hay una partida empezada");
            return;
        }

        gameStarted = true;
        nextTurn();
    }

    public void fillDeck() {
        if (!drawDeck.isEmpty()) {
            Logger.warn("Se ha rellenado el mazo sin estar vacío");
        }
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
                    for (int k = 0; k < 4; k++) {
                        Card card = new Card(-j - 4, Color.values()[i], Type.values()[j + 3]);
                        drawDeck.add(card);
                    }
                }
            }
        }

        Logger.info("La pila de robo ahora tiene " + drawDeck.size() + " cartas");
    }

    public Card draw() {
        return drawDeck.get(random.nextInt(drawDeck.size()));
    }

    public void playerDraws(iPlayer player, int qty) {
        for (int i = 0; i < qty; i++) {
            player.getDeck().add(drawDeck.get(random.nextInt(drawDeck.size())));
        }
    }

    public void startCard() {
        Card card = drawDeck.get(random.nextInt(drawDeck.size()));
        actualCard = card;
        drawDeck.remove(card);
    }

    public List<Card> stratingCards() {
        ArrayList<Card> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Card card = drawDeck.get(random.nextInt(drawDeck.size()));
            result.add(card);
            drawDeck.remove(card);
        }
        return result;
    }

    public void nextTurn() {
        if (!gameStarted) {
            return;
        }

        iPlayer current = players.get(turn);

        if (current instanceof Human) {
            Logger.info("Esperando acción del jugador humano...");
            ((Human) current).setTurn(true);
            return;
        }

        Card card = current.playCard(actualCard);
        if (card != null) {
            actualCard = card;
        }

        handleSpecialCard(actualCard);
        checkEndGame();
        advanceTurn();
    }

    public void playHumanCard(Card card) {
        iPlayer current = players.get(turn);
        if (!(current instanceof Human)) {
            return;
        }

        if (((Human) current).canPlay(card, actualCard)) {
            current.getDeck().remove(card);
            actualCard = card;
            Logger.humanPlays((Human) current, card);
            handleSpecialCard(actualCard);
            checkEndGame();
            advanceTurn();
        } else {
            Logger.error("Carta inválida para jugar");
        }
    }

    private void advanceTurn() {
        turn = (turn + direction + players.size()) % players.size();
        nextTurn();
    }

    private void handleSpecialCard(Card card) {
        boolean skipAdvance = false;
        switch (card.getPower()) {
            case CHANGE_COLOR:
                actualCard = new Card(-6, Color.values()[random.nextInt(4)], Type.NUM);
                break;
            case SWAP:
                direction *= -1;
                break;
            case BLOCK:
                turn = (turn + direction + players.size()) % players.size();
                break;
            case DRAW2:
                int draw2Target = (turn + direction + players.size()) % players.size();
                playerDraws(players.get(draw2Target), 2);
                turn = (turn + 2 * direction + players.size()) % players.size();
                skipAdvance = true;
                break;
            case DRAW4:
                int draw4Target = (turn + direction + players.size()) % players.size();
                playerDraws(players.get(draw4Target), 4);
                actualCard = new Card(-6, Color.values()[random.nextInt(4)], Type.NUM);
                turn = (turn + 2 * direction + players.size()) % players.size();
                skipAdvance = true;
                break;
        }
        if (!skipAdvance) {
            turn = (turn + direction + players.size()) % players.size();
        }
    }

    private void checkEndGame() {
        if (players.get(turn).getDeckSize() == 0) {
            gameStarted = false;
            System.out.println("¡Gana el jugador " + players.get(turn).getName() + "!");
        }
    }
}
