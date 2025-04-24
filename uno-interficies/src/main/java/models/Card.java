package models;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;

import models.enums.Colors;
import models.enums.Type;

public class Card extends JPanel implements Serializable {
    private final Colors color;
    private final Type power;
    private final int number;

    private String imagePath = "src\\main\\java\\img\\reverso\\carta_detras.png";
    private InterfaceEventDraw interfaceEventDraw;
    private Point clickOffset;
    private boolean isDetached = false;

    public Card() {
        this(0, Colors.RED, Type.NUM); // Default values
    }

    public Card(int number, Colors color, Type power) {
        this.color = color;
        this.power = power;
        this.number = number;
        setOpaque(true);
        setPreferredSize(new Dimension(40, 65));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (interfaceEventDraw != null) {
                    interfaceEventDraw.cardTurn(); // Will handle image + spawning
                    
                    Point screenPoint = e.getLocationOnScreen();
                    interfaceEventDraw.cardDropped(Card.this, screenPoint);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (interfaceEventDraw != null) {
                    interfaceEventDraw.cardDrag();
                }

                // Para mover la carta
                setLocation(getX() + e.getX() - clickOffset.x, getY() + e.getY() - clickOffset.y);
            }
        });
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(imgFile.getAbsolutePath());
            g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawString("Imagen no encontrada", 10, 20);
        }
    }

    public Colors getColor() {
        return color;
    }

    public Type getPower() {
        return power;
    }

    public int getNum() {
        return number;
    }
    public void addInterfaceEventDraw(InterfaceEventDraw ied) {
        this.interfaceEventDraw = ied;
    }

    public void setIsDetached(boolean isDetached) {
        this.isDetached = isDetached;
    }
    public boolean isDetached() {
        return isDetached;
    }
}