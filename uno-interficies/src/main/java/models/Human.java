package models;

import models.enums.Type;

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

        return card.getColor().equals(card.getColor())
                || card.getNum() == card.getNum()
                || card.getPower() == Type.CHANGE_COLOR
                || card.getPower() == Type.DRAW4;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

}
