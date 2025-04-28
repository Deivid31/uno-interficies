package models;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import models.enums.Colors;
import models.enums.Types;
import static models.enums.Types.CHANGE_COLOR;
import static models.enums.Types.DRAW2;
import static models.enums.Types.DRAW4;
import random.Logger;
import view.EndGUI;
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
    private ArrayList<Integer> pos = new ArrayList<>();
    private ArrayList<iPlayer> npcs = new ArrayList<>();

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

    }

    private void showOrder() {
        int humanIndex = -1;

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals("Human")) {
                humanIndex = i;
                break;
            }
        }

        if (humanIndex == -1) {
            Logger.error("No se encontró al jugador humano.");
            return;
        }

        pos.clear();
        npcs.clear();

        for (int j = 1; j <= 3; j++) {
            int npcIndex = (humanIndex + j) % players.size();
            pos.add(npcIndex);
            npcs.add(players.get(npcIndex));
        }
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void startGame() {

        if (gameStarted) {
            Logger.error("Ya hay una partida empezada");
            return;
        }
        Collections.shuffle(players);
        showOrder();

        gui.playerLabel1.setText(npcs.get(0).getName());
        gui.playerLabel2.setText(npcs.get(1).getName());
        gui.playerLabel3.setText(npcs.get(2).getName());

        gameStarted = true;
        Logger.gameStart(this);
        firstCard();
    }

    public void fillDeck() {
        if (!drawDeck.isEmpty()) {
            Logger.warn("Se ha rellenado el mazo sin estar vacío");
        }
        drawDeck.clear();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                Boolean enUso = false;
                if (i != 0) {
                    Card card = new Card(i, Colors.values()[j], Types.NUM);
                    Card card2 = new Card(i, Colors.values()[j], Types.NUM);
                    if (actualCard != null) {
                        if (card.toString().equals(actualCard.toString())) {
                            enUso = true;
                        }
                    }
                    for (iPlayer player : players) {
                        enUso = player.deck.stream().anyMatch(o -> card.toString().equals(o.toString()));
                    }
                    if (!enUso) {
                        drawDeck.add(card);
                        drawDeck.add(card2);
                    }
                } else {
                    Card card = new Card(i, Colors.values()[j], Types.NUM);
                    if (actualCard != null) {
                        if (card.toString().equals(actualCard.toString())) {
                            enUso = true;
                        }
                    }
                    for (iPlayer player : players) {
                        enUso = player.deck.stream().anyMatch(o -> card.toString().equals(o.toString()));
                    }
                    if (!enUso) {
                        drawDeck.add(card);
                    }
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
        Card drawed = drawDeck.get(random.nextInt(drawDeck.size()));
        gui.drawDeckLabel.setText("Mazo: " + drawDeck.size());
        drawDeck.remove(drawed);
        if (drawDeck.isEmpty()) {
            fillDeck();
        }
        return drawed;
    }

    public void playerDraws(iPlayer player, int qty) {
        for (int i = 0; i < qty; i++) {
            player.getDeck().add(draw());
        }
        if (player instanceof Human) {
            gui.displayHumanDeck();
        }
    }

    public void firstCard() {
        Card card = draw();
        actualCard = card;

        turn = (turn - direction + players.size()) % players.size();

        if (card != null) {
            handleSpecialCard(actualCard);
        }

        if (listener != null) {
            listener.onCardPlayed();
        }
        advanceTurn();
    }

    public List<Card> startingCards() {
        ArrayList<Card> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            result.add(draw());
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
            Card card = null;

            @Override
            protected Void doInBackground() throws Exception {
                card = current.playCard(actualCard);
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
                if (card != null) {
                    handleSpecialCard(actualCard);
                }
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
        for (int i = 0; i < 3; i++) {
            gui.colorLabel.setText(actualCard.actColorStr());
            gui.colorLabel.setForeground(actualCard.actColor());
            iPlayer player = players.get(pos.get(i));
            switch (i) {
                case 0:
                    gui.deckLabel1.setText("Cartas: " + player.deck.size());
                    break;
                case 1:
                    gui.deckLabel2.setText("Cartas: " + player.deck.size());
                    break;
                default:
                    gui.deckLabel3.setText("Cartas: " + player.deck.size());
                    break;
            }
        }
        turn = (turn + direction + players.size()) % players.size();
        int j1 = pos.get(0);
        int j2 = pos.get(1);
        int j3 = pos.get(2);
        if (turn == j1) {
            gui.playerLabel1.setForeground(Color.green);
            gui.playerLabel2.setForeground(Color.white);
            gui.playerLabel3.setForeground(Color.white);
        } else if (turn == j2) {
            gui.playerLabel1.setForeground(Color.white);
            gui.playerLabel2.setForeground(Color.green);
            gui.playerLabel3.setForeground(Color.white);
        } else if (turn == j3) {
            gui.playerLabel1.setForeground(Color.white);
            gui.playerLabel2.setForeground(Color.white);
            gui.playerLabel3.setForeground(Color.green);
        } else {
            gui.playerLabel1.setForeground(Color.white);
            gui.playerLabel2.setForeground(Color.white);
            gui.playerLabel3.setForeground(Color.white);
        }
        nextTurn();
    }

    private void handleSpecialCard(Card card) {
        boolean skipAdvance = false;
        switch (card.getType()) {
            case CHANGE_COLOR:
                actualCard.setColor(Colors.values()[random.nextInt(4)]);
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
                actualCard.setColor(Colors.values()[random.nextInt(4)]);
                turn = (turn + direction + players.size()) % players.size();
                skipAdvance = true;
                break;
        }
    }

    private void checkEndGame() {
        if (players.get(turn).getDeckSize() == 0) {
            gameStarted = false;
            System.out.println("¡Gana el jugador " + players.get(turn).getName() + "!");
            EndGUI fin = new EndGUI(players.get(turn).getName(), gui);
            fin.setVisible(true);
        }
    }

    public List<iPlayer> getPlayers() {
        return players;
    }

    public Card getActualCard() {
        return actualCard;
    }

    public ArrayList<Card> getDrawDeck() {
        return drawDeck;
    }
}
