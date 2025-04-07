package models;

import java.awt.CardLayout;
import java.io.Serializable;
import javax.swing.JPanel;
import models.enums.Color;
import models.enums.Type;

public class Card extends JPanel implements Serializable {
    private final CardLayout sides = new CardLayout();
    
    private int num;
    private Color color;
    private Type type;

    public Card(int num, Color color, Type type) {
        this.num = num;
        this.color = color;
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Type getPower() {
        return type;
    }

    public void setPower(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getColor().toString() + " " + this.getNum() + " (" + this.getPower().toString() + ")";
    }
}
