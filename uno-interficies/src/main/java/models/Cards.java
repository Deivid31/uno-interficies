package models;

import java.awt.CardLayout;
import java.io.Serializable;
import javax.swing.JPanel;
import models.enums.Color;
import models.enums.Type;

public class Cards extends JPanel implements Serializable {
    CardLayout sides;
    Color color;
    Type power;
    int number;

    public Cards() {
    }

    public Cards(Color color, Type power, int number, JPanel front, JPanel back) {
        this.color = color;
        this.power = power;
        this.number = number;
        this.sides = new CardLayout();
        this.sides.addLayoutComponent(front, "FRONT");
        this.sides.addLayoutComponent(back, "BACK");
    }
}