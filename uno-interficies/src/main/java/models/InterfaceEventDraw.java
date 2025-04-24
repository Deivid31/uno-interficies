package models;

import java.awt.Point;

public interface InterfaceEventDraw {
    public void cardDrag();
    public void cardTurn();
    public void cardDropped(Cards card, Point screenLocation);
}
