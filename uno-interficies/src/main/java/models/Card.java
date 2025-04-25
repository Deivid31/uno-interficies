package models;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import models.enums.Colors;
import models.enums.Types;

public class Card extends JPanel implements Serializable {

    private final Colors color;
    private final Types type;
    private final int number;

    private String imagePath = "src\\main\\java\\img\\reverso\\carta_detras.png";
    private InterfaceEventDraw interfaceEventDraw;
    private Point clickOffset;
    private boolean isDetached = false;
    private boolean isTurned = false;
    private boolean isDraw = false;
    private boolean isPlayed = false;

    public Card() {
        this(0, Colors.RED, Types.NUM);
    }

    public Card(int number, Colors color, Types type) {
        this.color = color;
        this.type = type;
        this.number = number;
        setOpaque(false);
        setPreferredSize(new Dimension(40, 65));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPlayed) {
                    clickOffset = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isPlayed) {
                    if (interfaceEventDraw != null) {
                        if (!isTurned) {
                            interfaceEventDraw.cardTurn();
                        }
                        Point screenPoint = e.getLocationOnScreen();
                        interfaceEventDraw.cardDropped(Card.this, screenPoint);
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isPlayed) {
                    if (interfaceEventDraw != null) {
                        if (!isDetached) {
                            interfaceEventDraw.cardDrag();
                        }
                    }
                    // Para mover la carta
                    setLocation(getX() + e.getX() - clickOffset.x, getY() + e.getY() - clickOffset.y);
                }
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

    public Types getType() {
        return type;
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

    public boolean isTurned() {
        return isTurned;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setIsTurned(boolean isTurned) {
        this.isTurned = isTurned;
    }

    public void setIsDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }

    public void setIsPlayed(boolean isPlayed) {
        this.isPlayed = isPlayed;
    }

    @Override
    public String toString() {
        return this.getColor().toString() + " " + this.getNum() + " (" + this.getType().toString() + ")";
    }
}
