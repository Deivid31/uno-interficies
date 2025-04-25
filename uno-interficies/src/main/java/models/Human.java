package models;

import models.enums.Types;

public class Human extends iPlayer {

    private boolean turn;

    public Human(Game game) {
        super(game);
        name = "Human";
    }

    public boolean canPlay(Card card, Card actualCard) {
        if (card == null || actualCard == null) {
            return false;
        }

        return actualCard.getColor().equals(card.getColor())
                || actualCard.getNum() == card.getNum()
                || card.getType() == Types.CHANGE_COLOR
                || card.getType() == Types.DRAW4;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void addCard(Card card) {
        deck.add(card);
    }
}
