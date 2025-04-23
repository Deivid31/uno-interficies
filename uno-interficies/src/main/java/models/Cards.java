package models;

import java.awt.CardLayout;
import java.io.Serializable;
import javax.swing.JPanel;
import models.enums.Color;
import models.enums.Type;

public class Cards extends JPanel implements Serializable {
    private final CardLayout sides = new CardLayout();
    private final JPanel front;
    private final JPanel back;
    
    private final Color color;
    private final Type power;
    private final int number;
    
    public Cards() {    
        this.front = null;
        this.back = null;
        this.color = null;
        this.power = null;
        this.number = 0;
    }

    public Cards(Color color, Type power, int number, JPanel front, JPanel back) {
        this.color = color;
        this.power = power;
        this.number = number;
        this.setLayout(sides);

        this.front = front;
        this.back = back;

        this.add(this.front, "FRONT");
        this.add(this.back, "BACK");

        sides.show(this, "BACK");
    }
}