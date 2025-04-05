package models;

import java.awt.CardLayout;
import java.io.Serializable;
import javax.swing.JPanel;
import models.enums.Type;
import models.enums.Power;

public class UnoCards extends JPanel implements Serializable {
    CardLayout sides;
    Type type;
    Power power;
    int number;

    public UnoCards() {
    }

    public UnoCards(Type type, Power power, int number, JPanel front, JPanel back) {
        this.type = type;
        this.power = power;
        this.number = number;
        this.sides = new CardLayout();
        this.sides.addLayoutComponent(front, "FRONT");
        this.sides.addLayoutComponent(back, "BACK");
    }
}
¡