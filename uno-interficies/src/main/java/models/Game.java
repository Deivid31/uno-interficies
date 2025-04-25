package models;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.enums.Colors;
import models.enums.Types;
import static models.enums.Types.CHANGE_COLOR;
import static models.enums.Types.DRAW2;
import static models.enums.Types.DRAW4;
import random.Logger;
import view.MainGUI;

public class Game {

    private Random random;
    private MainGUI gui;
    private boolean gameStarted;
    private ArrayList<Card> drawDeck;
    private ArrayList<iPlayer> players;
    private Card actualCard;
    private int turn;
    private Image img;
    private int direction;
    private GameListener listener;

    public Game(MainGUI gui) {
        this.gui = gui;

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

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void startGame() {

        if (gameStarted) {
            Logger.error("Ya hay una partida empezada");
            return;
        }

        gui.playerLabel1.setText(players.get(0).getName());
        gui.playerLabel2.setText(players.get(1).getName());
        gui.playerLabel3.setText(players.get(2).getName());

        gameStarted = true;
        Logger.gameStart(this);
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
                    Card card = new Card(i, Colors.values()[j], Types.NUM);
                    Card card2 = new Card(i, Colors.values()[j], Types.NUM);
                    drawDeck.add(card);
                    drawDeck.add(card2);
                } else {
                    Card card = new Card(i, Colors.values()[j], Types.NUM);
                    drawDeck.add(card);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!Colors.values()[i].equals(Colors.BLACK)) {
                for (int j = 0; j < 3; j++) {
                    Card card = new Card(-j - 1, Colors.values()[i], Types.values()[j]);
                    Card card2 = new Card(-j - 1, Colors.values()[i], Types.values()[j]);
                    drawDeck.add(card);
                    drawDeck.add(card2);
                }
            } else {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 4; k++) {
                        Card card = new Card(-j - 4, Colors.values()[i], Types.values()[j + 3]);
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
        if (listener != null) {
            listener.onCardPlayed();
        }
        drawDeck.remove(card);
    }

    public List<Card> startingCards() {
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

        // Ejecutar turno del NPC sin bloquear el hilo de la GUI para que se vean las
        // cartas que van tirando
        new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Card card = current.playCard(actualCard);
                if (card != null) {
                    actualCard = card;
                }
                return null;
            }

            @Override
            protected void done() {
                if (listener != null) {
                    listener.onCardPlayed();
                }

                handleSpecialCard(actualCard);
                checkEndGame();
                advanceTurn();
            }
        }.execute();
    }

    public void playHumanCard(Card card) {
        iPlayer current = players.get(turn);
        if (!(current instanceof Human)) {
            return;
        }

        if (((Human) current).canPlay(card, actualCard)) {
            current.getDeck().remove(card);
            actualCard = card;
            if (listener != null) {
                listener.onCardPlayed();
            }
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
        switch (card.getType()) {
            case CHANGE_COLOR:
                actualCard = new Card(-6, Colors.values()[random.nextInt(4)], Types.NUM);
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
                turn = (turn + direction + players.size()) % players.size();
                break;
            case DRAW4:
                int draw4Target = (turn + direction + players.size()) % players.size();
                playerDraws(players.get(draw4Target), 4);
                actualCard = new Card(-6, Colors.values()[random.nextInt(4)], Types.NUM);
                turn = (turn + direction + players.size()) % players.size();
                skipAdvance = true;
                break;
        }
    }

    private void checkEndGame() {
        if (players.get(turn).getDeckSize() == 0) {
            gameStarted = false;
            System.out.println("¡Gana el jugador " + players.get(turn).getName() + "!");
        }
    }

    public List<iPlayer> getPlayers() {
        return players;
    }

    public Card getActualCard() {
        return actualCard;
    }
}
